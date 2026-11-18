
package Base;

import java.io.IOException;

import Base.NetRequest;
import Base.Com;
import desktop.PlatformRuntime;
import java.util.Hashtable;
import desktop.graphics.GameImage;

public final class ImagesCach {
    private static Hashtable imagesCach = new Hashtable();
    public static String lastName = null;

    public static int loadCach(String loadText, String loadText2, int incomeStore, int[] levels, boolean byDump, boolean dumping, int reqCode) {
        try {
        int[] result;
        int[] resultAlias = result = new int[1];
        int step = 0;
        int totalSteps = 1;
        NetRequest request = new NetRequest();
        do {
            int countOrLevelIndex;
            Com.PrBarTh.setText((byDump ? "[\u041a\u044d\u0448]" : "[\u0421\u0435\u0442\u044c]") + loadText + " " + (step + 1) + "/" + totalSteps);
            if (byDump) {
                if (dumping) {
                    request.response = Com.connectToRSForNetResources(incomeStore + step);
                }
            } else {
                request = new NetRequest(dumping);
                request.writeHeader(1000000);
                request.request.writeInt(reqCode);
                for (countOrLevelIndex = 0; countOrLevelIndex < levels.length; ++countOrLevelIndex) {
                    request.request.writeInt(levels[countOrLevelIndex]);
                }
                request.request.writeInt(step);
                Com.sendRequest(request);
                request.response.readInt();
                if (dumping) {
                    Com.dumpToRSForNetResources(incomeStore + step, request.responseBytes);
                    request.responseBytes = null;
                }
            }
            if (step == 0) {
                totalSteps = request.response.readInt();
            }
            countOrLevelIndex = request.response.readInt();
            for (int i = 0; i < countOrLevelIndex; ++i) {
                String key;
                Com.PrBarTh.setText(loadText2 + " " + i + "/" + countOrLevelIndex);
                lastName = key = request.response.readUTF();
                Object imageData = new byte[request.response.readInt()];
                request.response.readFully((byte[])imageData);
                imageData = GameImage.createImage((byte[])imageData, 0, ((byte[])imageData).length);
                imagesCach.put(key, imageData);
                PlatformRuntime.getInstance();
            }
            request.response.close();
        } while (++step < totalSteps);
        resultAlias[0] = incomeStore + totalSteps;
        return result[0];

        } catch (IOException exception) {
            ImagesCach.<RuntimeException>throwUnchecked(exception);
            throw new AssertionError(exception);
        }
    }

    public static GameImage getImage(String name) {
        return (GameImage)imagesCach.get(name);
    }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
   private ImagesCach() {
   }

}

