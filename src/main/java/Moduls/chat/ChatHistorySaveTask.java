package Moduls.chat;

import desktop.PlatformRuntime;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import desktop.storage.RecordStore;

final class ChatHistorySaveTask implements Runnable {

   private Chat chat;

   ChatHistorySaveTask(Chat chat2) {
      this.chat = chat2;
   }

   public final void run() {
      PlatformRuntime.initializeThread();

      while(true) {
         try {
            Thread.sleep(10000L);
         } catch (InterruptedException exception) {
            exception.printStackTrace();
         }

         int[] integers;
         synchronized(Chat.getLastWritersLock(this.chat)) {
            integers = new int[Chat.getLastWritersIds(this.chat).size()];
            int integerIndex = 0;

            while(true) {
               if (integerIndex >= Chat.getLastWritersIds(this.chat).size()) {
                  break;
               }

               Integer integer = (Integer)Chat.getLastWritersIds(this.chat).elementAt(integerIndex);
               integers[integerIndex] = integer;
               ++integerIndex;
            }
         }

         try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream;
            (dataOutputStream = new DataOutputStream(byteArrayOutputStream)).writeInt(integers.length);

            for(int lengthIndex = 0; lengthIndex < integers.length; ++lengthIndex) {
               dataOutputStream.writeInt(integers[lengthIndex]);
            }

            dataOutputStream.flush();
            byte[] recordData = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
            RecordStore recordStore;
            if ((recordStore = RecordStore.openRecordStore("ll_last_writers", true)).getNumRecords() > 0) {
               recordStore.setRecord(1, recordData, 0, recordData.length);
            } else {
               recordStore.addRecord(recordData, 0, recordData.length);
            }
         } catch (Exception exception2) {
            exception2.printStackTrace();
         }

         synchronized(Chat.getLastWritersLock(this.chat)) {
            if (!Chat.isLastWritersSaveNeeded(this.chat)) {
               Chat.clearLastWritersSaveActive(this.chat, false);
               return;
            }

            Chat.clearLastWritersSaveNeeded(this.chat, false);
         }
      }
   }
}
