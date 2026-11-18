package desktop.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import desktop.io.StreamConnection;

public final class FileConnectionAdapter extends StreamConnection {
   private File file;

   public final void openFile(String messageText) {
      this.file = new File(messageText.replaceFirst("file://localhost/", ""));
   }

   public final boolean exists() {
      return this.file.exists();
   }

   public final Enumeration list() {
      File[] files = this.file.listFiles();
      Vector vector3 = new Vector();

      for(int lengthIndex = 0; lengthIndex < files.length; ++lengthIndex) {
         if (files[lengthIndex].isDirectory()) {
            vector3.add(files[lengthIndex].getName().replace("\\", "/") + "/");
         } else {
            vector3.add(files[lengthIndex].getName().replace("\\", "/"));
         }
      }

      return vector3.elements();
   }

   public final InputStream openInputStream() {
      return decompiledsupport.UncheckedDataInput.open(this.file);
   }

   public final long fileSize() {
      return this.file.length();
   }

   public final void close() {
   }
}
