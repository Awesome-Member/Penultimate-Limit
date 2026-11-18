package Base;
import Moduls.PFter;

import Base.IndirectNetworkHandler;
import Base.Com;
import MenuPck.MenuManager;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import desktop.io.HttpConnectionAdapter;
import desktop.io.SocketConnection;
import desktop.io.StreamConnection;
import desktop.io.FileConnectionAdapter;
import desktop.graphics.GameGraphics;
import desktop.storage.RecordStore;
import desktop.storage.RecordStoreException;

public class Black implements PFter {
   private boolean lockReplace = false;

   public void Set() {
      MenuManager.instance.guiHandler.hideAllWindows();
      Com.mainTh.setStopPaint();
      Com.mainTh.setPFter1(this);
   }

   public void setLockReplace(boolean lockReplace2) {
      this.lockReplace = lockReplace2;
   }

   public void paint(GameGraphics gameGraphics2, boolean justPaint) {
      gameGraphics2.setColor(0);
      gameGraphics2.fillRect(0, 0, Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
      if (Com.CoverTh.imgCover != null) {
         gameGraphics2.drawImage(Com.CoverTh.imgCover, Com.cameraScreen.getCw() / 2 - Com.CoverTh.imgCover.getWidth() / 2, Com.cameraScreen.getCh() / 2 - Com.CoverTh.imgCover.getHeight() / 2, 0);
      }

   }

   public void onAfterHardLoad() {
      Com.showGameCursor();
   }

   public void functionate() {
      IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
   }

   public void functionateUnderDrawProgress() {
      this.functionate();
      Com.fontSmall.animate();
      if (Com.fontBig != null) {
         Com.fontBig.animate();
      }

   }

   public void wheelRot(int num) {
   }

   public void movePointer(int x, int y) {
   }

   public void pressPointer(int x, int y) {
   }

   public void dragPointer(int x, int y) {
   }

   public void releasePointer(int pointerX, int pointerY) {
   }

   public void pressLeftSoft() {
   }

   public void pressRightSoft() {
   }

   public void releaseLeftSoft() {
   }

   public void releaseRightSoft() {
   }

   public void pressStar() {
   }

   public void pressSharp() {
   }

   public void press1() {
   }

   public void press3() {
   }

   public void press7() {
   }

   public void press9() {
   }

   public void press0() {
   }

   public void release1() {
   }

   public void release3() {
   }

   public void release7() {
   }

   public void release9() {
   }

   public void pressLeft() {
   }

   public void pressRight() {
   }

   public void pressUp() {
   }

   public void pressDown() {
   }

   public void pressFire() {
   }

   public void releaseLeft() {
   }

   public void releaseRight() {
   }

   public void releaseUp() {
   }

   public void releaseDown() {
   }

   public void releaseFire() {
   }

   public void releaseAllEvent() {
   }

   public boolean isReplaceable() {
      return !this.lockReplace;
   }

   public static void deleteMapCache(String messageText, boolean unusedProgressMarker) {
      try {
         removeMapFromCacheQueue(messageText);
         RecordStore.deleteRecordStore(messageText);
      } catch (RecordStoreException exception) {
      }
   }

   public static String[] listRecordStoresWithPrefix(String messageText) {
      String[] recordStoreNames = RecordStore.listRecordStores();
      int stringIndex = 0;
      int storeIndexLimit;
      if (recordStoreNames != null) {
         storeIndexLimit = recordStoreNames.length;
      } else {
         storeIndexLimit = 0;
      }

      for(int recordStoreNameIndex = 0; recordStoreNameIndex < storeIndexLimit; ++recordStoreNameIndex) {
         if (recordStoreNames[recordStoreNameIndex].startsWith(messageText)) {
            ++stringIndex;
         }
      }

      String[] string = null;
      if (stringIndex > 0) {
         string = new String[stringIndex];
         stringIndex = 0;

         for(int recordStoreNameIndex2 = 0; recordStoreNameIndex2 < storeIndexLimit; ++recordStoreNameIndex2) {
            if (recordStoreNames[recordStoreNameIndex2].startsWith(messageText)) {
               string[stringIndex] = recordStoreNames[recordStoreNameIndex2];
               ++stringIndex;
            }
         }
      }

      return string;
   }

   public static boolean initializeMapCache(String messageText, int expectedRecordCount) {
      try {
         deleteMapCache(messageText, true);
         RecordStore recordStore = Com.innerOpenRecordStore(messageText, true);
         switch (Com.dumpWorkType) {
            case 1:
            default:
               moveMapToCacheQueueEnd(messageText);
               Com.closeRecordStore(recordStore);
               return true;
            case 3:
               Com.closeRecordStore(recordStore);
               return false;
         }
      } catch (RecordStoreException exception) {
         return false;
      }
   }

   public static void appendMapCacheRecord(String messageText, byte[] recordData, int expectedRecordCount) {
      RecordStore recordStore = null;

      try {
         if ((recordStore = Com.innerOpenRecordStore(messageText, false)).getNumRecords() == expectedRecordCount) {
            recordStore.addRecord(recordData, 0, recordData.length);
         } else {
            Com.closeRecordStore(recordStore);
            Com.showFatalScreen("Нарушен порядок сохранения карты");
         }

         Com.closeRecordStore(recordStore);
      } catch (RecordStoreException exception) {
         try {
         Black.mayThrowRecordStoreException();
            if (recordStore != null) {
               Com.closeRecordStore(recordStore);
               deleteMapCache(messageText, true);
            }

         } catch (RecordStoreException exception2) {
         }
      }
   }

   public static byte[] readMapCacheRecord(String messageText, int recordIndex) {
      byte[] recordData = null;
      RecordStore recordStore = null;

      try {
         if ((recordStore = Com.innerOpenRecordStore(messageText, true)).getNumRecords() <= recordIndex) {
            Com.closeRecordStore(recordStore);
            return null;
         }

         recordData = recordStore.getRecord(recordIndex + 1);
         Com.closeRecordStore(recordStore);
      } catch (RecordStoreException exception) {
         try {
         Black.mayThrowRecordStoreException();
            if (recordStore != null) {
               Com.closeRecordStore(recordStore);
            }
         } catch (RecordStoreException exception2) {
         }
      }

      return recordData;
   }

   public static String loadCacheQueue() {
      byte[] recordData = null;
      RecordStore recordStore = null;

      try {
         Black.mayThrowRecordStoreException();
         if ((recordStore = Com.innerOpenRecordStore("feastofwar_cache_queue", true)).getNumRecords() <= 0) {
            Com.closeRecordStore(recordStore);
            return null;
         }

         recordData = recordStore.getRecord(1);
         Com.closeRecordStore(recordStore);
      } catch (RecordStoreException exception) {
         try {
         Black.mayThrowRecordStoreException();
            if (recordStore != null) {
               Com.closeRecordStore(recordStore);
            }
         } catch (RecordStoreException exception2) {
         }
      }

      return recordData == null ? null : new String(recordData);
   }

   public static void saveCacheQueue(byte[] recordData) {
      RecordStore recordStore = null;

      try {
         Black.mayThrowRecordStoreException();
         if ((recordStore = Com.innerOpenRecordStore("feastofwar_cache_queue", true)).getNumRecords() > 0) {
            recordStore.setRecord(1, recordData, 0, recordData.length);
         } else {
            recordStore.addRecord(recordData, 0, recordData.length);
         }

         Com.closeRecordStore(recordStore);
      } catch (RecordStoreException exception) {
         try {
         Black.mayThrowRecordStoreException();
            if (recordStore != null) {
               Com.closeRecordStore(recordStore);
            }

         } catch (RecordStoreException exception2) {
         }
      }
   }

   public static String moveMapToCacheQueueEnd(String messageText) {
      String messageText2;
      if ((messageText2 = loadCacheQueue()) == null) {
         messageText2 = messageText;
      } else {
         if (messageText.length() > 0 && messageText2.indexOf(messageText) >= 0) {
            if ((messageText2 = messageText2.substring(0, messageText2.indexOf(messageText)) + messageText2.substring(Math.min(messageText2.indexOf(messageText) + messageText.length() + 1, messageText2.length()))).endsWith("*")) {
               messageText2 = messageText2.substring(0, messageText2.length() - 1);
            }

            messageText2 = messageText2;
         }

         messageText2 = messageText + "*" + messageText2;
      }

      saveCacheQueue(messageText2.getBytes());
      return messageText2;
   }

   public static void removeMapFromCacheQueue(String substring) {
      String cacheQueue;
      if ((cacheQueue = loadCacheQueue()) != null) {
         if (substring.length() > 0 && cacheQueue.indexOf(substring) >= 0) {
            if ((substring = cacheQueue.substring(0, cacheQueue.indexOf(substring)) + cacheQueue.substring(Math.min(cacheQueue.indexOf(substring) + substring.length() + 1, cacheQueue.length()))).endsWith("*")) {
               substring = substring.substring(0, substring.length() - 1);
            }

            saveCacheQueue(substring.getBytes());
         }
      }
   }

   public static StreamConnection openConnection(String messageText) {
      try {
      if (messageText.contains("sms://")) {
         throw new IOException("can't sand SMS");
      } else if (messageText.startsWith("file://")) {
         FileConnectionAdapter fileConnectionAdapter;
         (fileConnectionAdapter = new FileConnectionAdapter()).openFile(messageText);
         return fileConnectionAdapter;
      } else if (messageText.startsWith("http://")) {
         HttpConnectionAdapter httpConnectionAdapter;
         (httpConnectionAdapter = new HttpConnectionAdapter()).connect(messageText);
         return httpConnectionAdapter;
      } else {
         String messageText2;
         int replaceAll = (messageText2 = messageText.replaceAll("socket://", "")).indexOf(":");
         String substring = "";
         int port = 0;
         if (replaceAll != -1) {
            substring = messageText2.substring(0, replaceAll);
            port = Integer.parseInt(messageText2.substring(replaceAll + 1));
         }

         return new SocketConnection(substring, port);
      }
   
      } catch (IOException exception) {
         Black.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static Enumeration listFileRoots() {
      try {
         File[] files = File.listRoots();
         Vector vector3 = new Vector();

         for(int lengthIndex = 0; lengthIndex < files.length; ++lengthIndex) {
            String messageText = files[lengthIndex].getAbsolutePath().replace("\\", "/");
            vector3.add(messageText);
         }

         return vector3.elements();
      } catch (Exception exception) {
         return null;
      }
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }


   private static void mayThrowRecordStoreException() throws RecordStoreException {}
}
