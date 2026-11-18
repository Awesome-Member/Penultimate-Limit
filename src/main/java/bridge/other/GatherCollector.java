package bridge.other;

import Base.Com;
import Base.NetRequest;
import Base.IndirectNetworkHandler;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestSingle;
import Moduls.quests.QuestSingleIdSmall;
import Moduls.quests.QuestStrateg;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Vector;

final class GatherCollector {
    private static final class JuiceConnection {
        static final Field LOCK = networkField("sendLock"), INPUT = networkField("net_pdis"), OUTPUT = networkField("net_os");
    }

    private GatherCollector() { }

    static Vector<?> availableQuests(TileMap map, int giver) {
        if (map.questers == null || giver < 0 || giver >= map.questers.length || map.questers[giver] == null) return null;
        return map.questers[giver].getQuestsForTake(Strategist.instance, QuestStrateg.instance);
    }

    static int collectionChoice(Vector<?> available, String map, int giver, String questName) {
        if (available == null) return -1;
        for (int i = 0; i < available.size(); i++) {
            QuestSingle quest = (QuestSingle)available.elementAt(i);
            if (map.equals(quest.inMap) && quest.inMapQuesterInd == giver
                    && (questName == null || questName.equals(quest.name))) return i;
        }
        return -1;
    }

    static boolean rewardConfirmed(String[] result) {
        if (result == null || result.length == 0) return false;
        return "SUCCESS".equals(result[0]) || "SUCCESS_FLOAT".equals(result[0]) || "SUCCESS_SILENT".equals(result[0]);
    }

    public static void jump(String[] portals) throws IOException {
        NetRequest[] requests = new NetRequest[portals.length];
        for (int i = 0; i < portals.length; i++) {
            requests[i] = new NetRequest();
            requests[i].writeHeader(4140520);
            requests[i].request.writeUTF(portals[i]);
        }
        sendBatch(requests);
        int rejected = 0;
        for (NetRequest request : requests) {
            int response = request.response.readInt();
            if (response != 4140521) { rejected = response; continue; }
            Strategist.instance.cx = (short)request.response.readInt();
            Strategist.instance.cy = (short)request.response.readInt();
            request.response.readBoolean();
            IndirectNetworkHandler.instance.loadFromStreamAndApply(request.response);
        }
        Com.cameraWorld.resetSmooth();
        if (rejected != 0) throw new IOException("Server rejected the jump: " + rejected);
    }

    public static String[] collect(TileMap map, int giver, QuestSingle quest) throws IOException {
        NetRequest request = new NetRequest();
        request.writeHeader(4140920);
        quest.saveToStreamId(request.request);
        NetRequest reward = new NetRequest();
        reward.writeHeader(4141020);
        quest.saveToStreamId(reward.request);
        sendTogether(request, reward);
        int response = request.response.readInt();
        if (response == 4140923) throw new IOException("Free inventory slots required: " + request.response.readInt());
        if (response != 4140921) throw new IOException("Server rejected the attempt: " + response);
        QuestInPlayer accepted = new QuestInPlayer(request.response);
        if (request.response.readBoolean()) {
            request.response.readUTF();
            Com.applyRewardEvents(request);
        }
        QuestStrateg quests = QuestStrateg.instance;
        quests.quests.addElement(accepted);
        boolean refreshed = false;
        try {
            if (accepted.questSingle.switchCompass) quests.changeCompassedQuest(accepted);
            int result = reward.response.readInt();
            if (result != 4141021) throw new IOException("Server did not confirm the reward: " + result);
            String text = reward.response.readUTF();
            Com.applyRewardEvents(reward);
            quests.questsDone.put(quest.createStringIdSmall(), new QuestSingleIdSmall(
                    quest.inMap, quest.inMapQuesterInd, quest.inQuesterInd));
            int index = quests.quests.indexOf(accepted);
            if (index >= 0) {
                QuestStrateg.removeQuest(index, true);
                refreshed = true;
            }
            return new String[]{"SUCCESS_SILENT", text, "0"};
        } finally {
            if (!refreshed) Com.onQuestsChanged(true);
        }
    }

    private static Field networkField(String name) {
        try {
            Field field = Com.class.getDeclaredField(name); field.setAccessible(true); return field;
        } catch (ReflectiveOperationException exception) { throw new ExceptionInInitializerError(exception); }
    }

    private static byte[] frame(NetRequest request) {
        byte[] bytes = request.requestBuffer.toByteArray();
        int length = bytes.length - 4;
        for (int i = 0; i < 4; i++) bytes[i] = (byte)((length >>> (8 * i) & 255) - 128);
        return bytes;
    }

    private static void sendTogether(NetRequest first, NetRequest second) throws IOException {
        sendBatch(first, second);
    }

    private static void sendBatch(NetRequest... requests) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (NetRequest request : requests) buffer.write(frame(request));
        byte[] pair = buffer.toByteArray();
        try {
            synchronized (JuiceConnection.LOCK.get(null)) {
                InputStream input = (InputStream)JuiceConnection.INPUT.get(null);
                OutputStream output = (OutputStream)JuiceConnection.OUTPUT.get(null);
                if (input == null || output == null) throw new IOException("Server connection unavailable");
                Com.isActiveRequest = true; Com.lastSendRequestTime = System.currentTimeMillis();
                try {
                    Com.sentBytes += pair.length;
                    output.write(pair); output.flush();

                    for (NetRequest request : requests) readReply(input, request);
                } finally {
                    Com.isActiveRequest = false; Com.lastSendRequestTime = System.currentTimeMillis();
                }
            }
        } catch (IllegalAccessException exception) { throw new IOException("Cannot access the game connection", exception); }
    }

    private static void readReply(InputStream input, NetRequest request) throws IOException {
        int length = 0;
        for (int i = 0; i < 4; i++) {
            int value = input.read();
            if (value < 0) throw new EOFException("Connection closed");
            length |= ((value + 128) & 255) << (8 * i);
        }
        if (length <= 0 || length > 2097152) throw new IOException("Invalid response length: " + length);
        byte[] body = new byte[length];
        new DataInputStream(input).readFully(body);
        Com.getBytes += length + 4;
        request.responseBuffer = new ByteArrayInputStream(body);
        request.response = new DataInputStream(request.responseBuffer);
        request.success = true; request.responseReceived = true;
    }
}
