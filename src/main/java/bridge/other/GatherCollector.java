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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

final class GatherCollector {
    private static final Field SEND_LOCK = gameField(Com.class, "sendLock");
    private static final Field NET_INPUT = gameField(Com.class, "net_pdis");
    private static final Field NET_OUTPUT = gameField(Com.class, "net_os");
    private static final Field QUESTER_QUESTS = gameField(Moduls.quests.QuestsOnMap.class, "quests");
    static volatile boolean connectionBroken;
    private static Object brokenInput;
    private static int pendingPongs;

    static boolean connectionUsable() {
        if (!connectionBroken) return true;
        try {
            if (brokenInput != null && brokenInput != NET_INPUT.get(null)) connectionBroken = false;
        } catch (IllegalAccessException exception) {
            return false;
        }
        return !connectionBroken;
    }

    private static void killConnection() {
        try {
            OutputStream output = (OutputStream)NET_OUTPUT.get(null);
            if (output != null) try { output.close(); } catch (IOException ignored) { }
            InputStream input = (InputStream)NET_INPUT.get(null);
            if (input != null) try { input.close(); } catch (IOException ignored) { }
        } catch (IllegalAccessException ignored) { }
    }

    private static Field gameField(Class<?> type, String name) {
        try {
            Field field = type.getDeclaredField(name); field.setAccessible(true); return field;
        } catch (ReflectiveOperationException exception) { throw new ExceptionInInitializerError(exception); }
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

    static List<QuestSingle> questChain(TileMap map, int giver, String questName) {
        List<QuestSingle> chain = new ArrayList<QuestSingle>();
        if (map.questers == null || giver < 0 || giver >= map.questers.length || map.questers[giver] == null) return chain;
        try {
            QuestSingle[] quests = (QuestSingle[])QUESTER_QUESTS.get(map.questers[giver]);
            if (quests != null) for (QuestSingle quest : quests) if (questName.equals(quest.name)) chain.add(quest);
        } catch (IllegalAccessException ignored) { }
        return chain;
    }

    static boolean rewardConfirmed(String[] result) {
        if (result == null || result.length == 0) return false;
        return "SUCCESS".equals(result[0]) || "SUCCESS_FLOAT".equals(result[0]) || "SUCCESS_SILENT".equals(result[0]);
    }

    public static void jump(String[] portals) throws IOException {
        NetRequest[] requests = new NetRequest[portals.length];
        for (int i = 0; i < portals.length; i++) requests[i] = objectRequest(4140520, portals[i]);
        int replies = sendBatch(requests);
        for (int i = 0; i < replies; i++) applyJumpReply(requests[i]);
        Com.cameraWorld.resetSmooth();
        if (replies < requests.length) throw new IOException("jump interrupted after " + replies + "/" + requests.length);
    }

    public static String[] collect(TileMap map, int giver, QuestSingle quest) throws IOException {
        NetRequest request = new NetRequest();
        request.writeHeader(4140920);
        quest.saveToStreamId(request.request);
        NetRequest reward = new NetRequest();
        reward.writeHeader(4141020);
        quest.saveToStreamId(reward.request);
        int replies = sendBatch(request, reward);
        if (replies < 1) throw new IOException("collect interrupted before take");
        QuestInPlayer accepted = parseTakeReply(request);
        if (replies < 2) throw new IOException("collect interrupted before reward");
        boolean refreshed = false;
        try {
            String text = parseRewardReply(reward, quest, accepted);
            refreshed = true;
            return new String[]{"SUCCESS_SILENT", text, "0"};
        } finally {
            if (!refreshed) Com.onQuestsChanged(true);
        }
    }

    public static int batchRun(String entryKey, String[] forward, String[] back, String exitKey, List<QuestSingle> quests) throws IOException {
        int attempts = quests.size();
        NetRequest entryAnalyze = objectRequest(4140410, entryKey);
        NetRequest entryUse = objectRequest(4140420, entryKey);
        NetRequest exitAnalyze = objectRequest(4140410, exitKey);
        NetRequest exitUse = objectRequest(4140420, exitKey);
        List<NetRequest> takes = new ArrayList<NetRequest>();
        List<NetRequest> rewards = new ArrayList<NetRequest>();
        for (int attempt = 0; attempt < attempts; attempt++) {
            QuestSingle quest = quests.get(attempt);
            NetRequest take = new NetRequest();
            take.writeHeader(4140920);
            quest.saveToStreamId(take.request);
            NetRequest reward = new NetRequest();
            reward.writeHeader(4141020);
            quest.saveToStreamId(reward.request);
            takes.add(take);
            rewards.add(reward);
        }
        List<NetRequest> forwardRequests = new ArrayList<NetRequest>();
        for (String key : forward) forwardRequests.add(objectRequest(4140520, key));
        List<NetRequest> backRequests = new ArrayList<NetRequest>();
        for (String key : back) backRequests.add(objectRequest(4140520, key));
        List<NetRequest> all = new ArrayList<NetRequest>();
        List<Integer> ids = new ArrayList<Integer>();
        all.add(entryAnalyze); ids.add(4140410);
        all.add(entryUse); ids.add(4140420);
        for (NetRequest request : forwardRequests) { all.add(request); ids.add(4140520); }
        for (int attempt = 0; attempt < attempts; attempt++) {
            all.add(takes.get(attempt)); ids.add(4140920);
            all.add(rewards.get(attempt)); ids.add(4141020);
        }
        for (NetRequest request : backRequests) { all.add(request); ids.add(4140520); }
        all.add(exitAnalyze); ids.add(4140410);
        all.add(exitUse); ids.add(4140420);
        int total = all.size();
        int[] codes = new int[total];
        int read = 0;
        try {
            read = sendAndCheck(all, ids, codes, 0, 2);
            applyGateReply(all.get(0));
            if (read < 2) throw interrupted(read, total);
            applyPortalReply(all.get(1));
            read = sendAndCheck(all, ids, codes, read, read + forwardRequests.size());
            for (int i = 2; i < read; i++) applyJumpReply(all.get(i));
            if (read < 2 + forwardRequests.size()) throw interrupted(read, total);
            int questStart = read;
            int questEnd = questStart + attempts * 2;
            int done = 0;
            while (read < questEnd) {
                int attempt = (read - questStart) / 2;
                int pairFrom = read;
                read = sendAndCheck(all, ids, codes, pairFrom, pairFrom + 2);
                NetRequest take = all.get(pairFrom);
                int response = take.response.readInt();
                if (response == 4140923) throw new IOException("Free inventory slots required: " + take.response.readInt());
                if (response != 4140921) {
                    if (response != 4140922)
                        System.out.printf("attempt %d/%d rejected: %d%n", attempt + 1, attempts, response);
                    if (codes[pairFrom + 1] == 4141021 && read >= pairFrom + 2) {
                        QuestInPlayer inWork = QuestStrateg.instance.findGetnQuest(quests.get(attempt));
                        if (inWork != null) {
                            parseRewardReply(all.get(pairFrom + 1), quests.get(attempt), inWork);
                            done++;
                        }
                    }
                    continue;
                }
                QuestInPlayer accepted = parseTakeBody(take);
                if (read < pairFrom + 2) throw interrupted(read, total);
                parseRewardReply(all.get(pairFrom + 1), quests.get(attempt), accepted);
                done++;
            }
            if (done < attempts) {
                StringBuilder takeCodes = new StringBuilder("take codes:");
                for (int i = questStart; i < questEnd; i += 2) takeCodes.append(' ').append(codes[i]);
                System.out.println(takeCodes);
            }
            read = sendAndCheck(all, ids, codes, read, read + backRequests.size());
            for (int i = questEnd; i < read; i++) applyJumpReply(all.get(i));
            if (read < questEnd + backRequests.size()) throw interrupted(read, total);
            int exitFrom = read;
            read = sendAndCheck(all, ids, codes, exitFrom, exitFrom + 2);
            applyGateReply(all.get(exitFrom));
            if (read < exitFrom + 2) throw interrupted(read, total);
            applyPortalReply(all.get(exitFrom + 1));
            Com.cameraWorld.resetSmooth();
            return done;
        } catch (IOException exception) {
            StringBuilder dump = new StringBuilder("batch replies:");
            for (int i = 0; i < read; i++) dump.append(' ').append(ids.get(i)).append("->").append(codes[i]);
            System.out.println(dump);
            throw exception;
        }
    }

    private static int sendAndCheck(List<NetRequest> all, List<Integer> ids, int[] codes, int from, int to) throws IOException {
        int replies = sendBatch(ids.subList(from, to), all.subList(from, to).toArray(new NetRequest[0]));
        if (replies == 0) throw interrupted(from, all.size());
        for (int i = from; i < from + replies; i++) {
            all.get(i).response.mark(4);
            codes[i] = all.get(i).response.readInt();
            all.get(i).response.reset();
            if (codes[i] == 1101119) {
                connectionBroken = true;
                killConnection();
                throw new IOException("session invalid at reply " + (i + 1) + "/" + all.size() + " (" + codes[i] + "), relogin required");
            }
        }
        return from + replies;
    }

    private static IOException interrupted(int index, int total) {
        return new IOException("batch interrupted after reply " + index + "/" + total);
    }

    private static NetRequest objectRequest(int id, String key) throws IOException {
        NetRequest request = new NetRequest();
        request.writeHeader(id);
        request.request.writeUTF(key);
        return request;
    }

    private static void applyGateReply(NetRequest request) throws IOException {
        int response = request.response.readInt();
        if (response != 4140411) throw new IOException("Server rejected the gate: " + response);
        request.response.readBoolean();
    }

    private static void applyPortalReply(NetRequest request) throws IOException {
        int response = request.response.readInt();
        if (response != 4140421) throw new IOException("Server rejected the portal: " + response);
        IndirectNetworkHandler.instance.loadFromStreamAndApply(request.response);
    }

    private static void applyJumpReply(NetRequest request) throws IOException {
        int response = request.response.readInt();
        if (response != 4140521) throw new IOException("Server rejected the jump: " + response);
        Strategist.instance.cx = (short)request.response.readInt();
        Strategist.instance.cy = (short)request.response.readInt();
        request.response.readBoolean();
        IndirectNetworkHandler.instance.loadFromStreamAndApply(request.response);
    }

    private static QuestInPlayer parseTakeReply(NetRequest request) throws IOException {
        int response = request.response.readInt();
        if (response == 4140923) throw new IOException("Free inventory slots required: " + request.response.readInt());
        if (response != 4140921) throw new IOException("Server rejected the attempt: " + response);
        return parseTakeBody(request);
    }

    private static QuestInPlayer parseTakeBody(NetRequest request) throws IOException {
        QuestInPlayer accepted = new QuestInPlayer(request.response);
        if (request.response.readBoolean()) {
            request.response.readUTF();
            Com.applyRewardEvents(request);
        }
        QuestStrateg.instance.quests.addElement(accepted);
        return accepted;
    }

    private static String parseRewardReply(NetRequest reward, QuestSingle quest, QuestInPlayer accepted) throws IOException {
        int result = reward.response.readInt();
        if (result != 4141021) throw new IOException("Server did not confirm the reward: " + result);
        String text = reward.response.readUTF();
        Com.applyRewardEvents(reward);
        QuestStrateg quests = QuestStrateg.instance;
        quests.questsDone.put(quest.createStringIdSmall(), new QuestSingleIdSmall(
                quest.inMap, quest.inMapQuesterInd, quest.inQuesterInd));
        int index = quests.quests.indexOf(accepted);
        if (index >= 0) QuestStrateg.removeQuest(index, true);
        return text;
    }

    private static byte[] frame(NetRequest request) {
        byte[] bytes = request.requestBuffer.toByteArray();
        int length = bytes.length - 4;
        for (int i = 0; i < 4; i++) bytes[i] = (byte)((length >>> (8 * i) & 255) - 128);
        return bytes;
    }

    private static int sendBatch(NetRequest... requests) throws IOException {
        return sendBatch(null, requests);
    }

    private static int sendBatch(List<Integer> ids, NetRequest... requests) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (NetRequest request : requests) buffer.write(frame(request));
        byte[] data = buffer.toByteArray();
        int done = 0;
        try {
            synchronized (SEND_LOCK.get(null)) {
                InputStream input = (InputStream)NET_INPUT.get(null);
                OutputStream output = (OutputStream)NET_OUTPUT.get(null);
                if (input == null || output == null) { connectionBroken = true; return done; }
                Com.isActiveRequest = true; Com.lastSendRequestTime = System.currentTimeMillis();
                try {
                    Com.sentBytes += data.length;
                    output.write(data); output.flush();
                    long[] waits = {3000L, 8000L, 15000L};
                    while (done < requests.length) {
                        try {
                            readReply(input, requests[done], 2000);
                            done++;
                            continue;
                        } catch (IOException stall) { }
                        boolean recovered = false;
                        for (long wait : waits) {
                            System.out.println("batch reply " + (done + 1) + "/" + requests.length + " deferred by server, nudging with keepalive");
                            byte[] nudge = frame(keepaliveRequest());
                            Com.sentBytes += nudge.length;
                            output.write(nudge); output.flush();
                            pendingPongs++;
                            try {
                                readReply(input, requests[done], wait);
                                done++;
                                recovered = true;
                                break;
                            } catch (IOException ignored) { }
                        }
                        if (!recovered) break;
                    }
                    if (done < requests.length) {
                        connectionBroken = true; brokenInput = input;
                        System.out.println("server stopped replying after " + done + "/" + requests.length
                                + (ids == null ? "" : " replies, stalled on " + ids.get(done))
                                + " (burst " + data.length + " bytes), draining socket");
                        drainPongs(input, 500L, 64);
                        killConnection();
                        return done;
                    }
                    if (pendingPongs > 0) drainPongs(input, 2000L, pendingPongs);
                    return done;
                } finally {
                    Com.isActiveRequest = false; Com.lastSendRequestTime = System.currentTimeMillis();
                }
            }
        } catch (IllegalAccessException exception) {
            throw new IOException("Cannot access the game connection", exception);
        } catch (IOException exception) {
            connectionBroken = true;
            killConnection();
            return done;
        }
    }

    private static NetRequest keepaliveRequest() {
        NetRequest request = new NetRequest();
        request.writeHeader(6700000);
        return request;
    }

    private static int readReply(InputStream input, NetRequest request, long timeoutMillis) throws IOException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (true) {
            readFrame(input, request, deadline);
            request.response.mark(4);
            int code = request.response.readInt();
            request.response.reset();
            if (code != 6700001) return code;
            handlePong(request);
        }
    }

    private static void readFrame(InputStream input, NetRequest request, long deadline) throws IOException {
        int length = 0;
        for (int i = 0; i < 4; i++) length |= ((readByte(input, deadline) + 128) & 255) << (8 * i);
        if (length <= 0 || length > 2097152) throw new IOException("Invalid response length: " + length);
        byte[] body = new byte[length];
        for (int offset = 0; offset < length; ) {
            awaitInput(input, deadline);
            int read = input.read(body, offset, length - offset);
            if (read < 0) throw new EOFException("Connection closed");
            offset += read;
        }
        Com.getBytes += length + 4;
        request.responseBuffer = new ByteArrayInputStream(body);
        request.response = new DataInputStream(request.responseBuffer);
        request.success = true; request.responseReceived = true;
    }

    private static void handlePong(NetRequest pong) throws IOException {
        if (pendingPongs > 0) pendingPongs--;
        if (pong.response.available() < 5) return;
        pong.response.readInt();
        pong.response.mark(5);
        pong.response.readByte();
        int version = pong.response.readInt();
        pong.response.reset();
        int current = IndirectNetworkHandler.instance.getCurDataVersion();
        if (version == 1 || version == current + 1) IndirectNetworkHandler.instance.loadFromStreamAndApply(pong.response);
        else if (version > current + 1) System.out.println("keepalive events lost " + current + " -> " + version);
    }

    private static void drainPongs(InputStream input, long timeoutMillis, int limit) {
        int drained = 0;
        while (drained < limit) {
            NetRequest frame = new NetRequest();
            try {
                readFrame(input, frame, System.currentTimeMillis() + timeoutMillis);
            } catch (IOException stall) {
                if (pendingPongs > 0) System.out.println("keepalive reply pending: " + pendingPongs);
                return;
            }
            drained++;
            try {
                frame.response.mark(4);
                int code = frame.response.readInt();
                frame.response.reset();
                if (code == 6700001) handlePong(frame);
                else System.out.println("drained unexpected reply " + code);
            } catch (IOException ignored) { }
        }
    }

    private static int readByte(InputStream input, long deadline) throws IOException {
        awaitInput(input, deadline);
        int value = input.read();
        if (value < 0) throw new EOFException("Connection closed");
        return value;
    }

    private static void awaitInput(InputStream input, long deadline) throws IOException {
        while (input.available() == 0) {
            if (System.currentTimeMillis() > deadline) throw new IOException("Reply timeout");
            try { Thread.sleep(2); } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for a reply", interrupted);
            }
        }
    }
}
