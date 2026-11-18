package desktop.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public final class RecordStore {
   private File workDir;
   private Vector stores = new Vector();

   public RecordStore() {
   }

   private RecordStore(File file2) {
      this.workDir = file2;
   }

   public final int getSize() {
      int size = 0;

      for(int sizeIndex = 0; sizeIndex < this.stores.size(); ++sizeIndex) {
         File file2 = (File)this.stores.get(sizeIndex);
         size = (int)((long)size + file2.length());
      }

      return size;
   }

   public static RecordStore openRecordStore(String messageText, boolean createIfNecessary) throws RecordStoreException {
      File file2;
      File file3;
      try {
         file2 = (file3 = File.createTempFile("llll", ".tmp")).getParentFile();
         file3.delete();
      } catch (IOException exception) {
         throw new RecordStoreException("Couldn't create temp file");
      }

      file3 = new File(file2, "new_lastlimit_browser_cache_ll/" + messageText + "/");
      if (!createIfNecessary && !file3.exists()) {
         throw new RecordStoreException("no record store");
      } else {
         file3.mkdirs();
         RecordStore recordStore = new RecordStore(file3);
         int fileIndex = -1;
         boolean storeExists = false;

         do {
            ++fileIndex;
            File file4;
            if ((file4 = new File(file3, Integer.toString(fileIndex) + ".rs")).exists()) {
               recordStore.stores.add(file4);
            } else {
               storeExists = true;
            }
         } while(!storeExists);

         return recordStore;
      }
   }

   public final int getNumRecords() {
      return this.stores.size();
   }

   public final void setRecord(int recordId, byte[] bytes, int offset, int length) {
      --recordId;

      try {
         File file2;
         (file2 = (File)this.stores.elementAt(recordId)).createNewFile();
         FileOutputStream fileOutputStream;
         (fileOutputStream = new FileOutputStream(file2)).write(bytes, offset, length);
         fileOutputStream.close();
      } catch (IOException exception) {
         UnsupportedOperationException unsupportedOperationException;
         (unsupportedOperationException = new UnsupportedOperationException("can't setRecord")).setStackTrace(exception.getStackTrace());
         throw unsupportedOperationException;
      }
   }

   public static String[] listRecordStores() {
      File file2 = null;

      try {
         File file3;
         file2 = (file3 = File.createTempFile("llll", ".tmp")).getParentFile();
         file3.delete();
      } catch (IOException exception) {
         exception.printStackTrace();
      }

      return (new File(file2, "new_lastlimit_browser_cache_ll/")).list();
   }

   public static void deleteRecordStore(String messageText) throws RecordStoreException {
      File file2;
      try {
         File file3;
         file2 = (file3 = File.createTempFile("llll", ".tmp")).getParentFile();
         file3.delete();
      } catch (IOException exception) {
         throw new RecordStoreException("Couldn't create a temp file");
      }

      deleteDirectory(new File(file2, "new_lastlimit_browser_cache_ll/" + messageText + "/"));
   }

   private static boolean deleteDirectory(File file2) {
      if (file2.exists()) {
         File[] files = file2.listFiles();

         for(int lengthIndex = 0; lengthIndex < files.length; ++lengthIndex) {
            File file3;
            if ((file3 = files[lengthIndex]).isDirectory()) {
               deleteDirectory(file3);
            } else {
               file3.delete();
            }
         }
      }

      return file2.delete();
   }

   public final int addRecord(byte[] recordData, int offset, int length) {
      this.stores.add(new File(this.workDir, Integer.toString(this.stores.size()) + ".rs"));
      this.setRecord(this.stores.size(), recordData, 0, length);
      return this.stores.size();
   }

   public final void deleteRecord(int recordId) {
      --recordId;
      ((File)this.stores.remove(recordId)).delete();
   }

   public final byte[] getRecord(int read) {
      --read;

      try {
         File file2 = (File)this.stores.elementAt(read);
         FileInputStream fileInputStream = new FileInputStream(file2);
         int bytesRead = 0;

         byte[] recordData;
         for(recordData = new byte[(int)file2.length()]; recordData.length - bytesRead > 0 && (read = fileInputStream.read(recordData, bytesRead, recordData.length - bytesRead)) >= 0; bytesRead += read) {
         }

         fileInputStream.close();
         return recordData;
      } catch (IOException exception) {
         UnsupportedOperationException unsupportedOperationException;
         (unsupportedOperationException = new UnsupportedOperationException("can't setRecord")).setStackTrace(exception.getStackTrace());
         throw unsupportedOperationException;
      }
   }
}
