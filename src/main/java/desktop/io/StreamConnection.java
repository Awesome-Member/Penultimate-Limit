package desktop.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StreamConnection {
   private Socket socket;

   public StreamConnection() {
   }

   public StreamConnection(String messageText, int mnemonicIndex) {
      try {
      this.socket = new Socket();
      this.socket.setKeepAlive(true);
      this.socket.setPerformancePreferences(0, 2, 1);
      this.socket.setTcpNoDelay(true);
      this.socket.setTrafficClass(8);
      this.socket.connect(new InetSocketAddress(messageText, mnemonicIndex));
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public void close() {
      try {
      this.socket.close();
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public DataOutputStream openDataOutputStream() {
      try {
      return new DataOutputStream(this.socket.getOutputStream());
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public DataInputStream openDataInputStream() {
      try {
      return new DataInputStream(this.socket.getInputStream());
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public OutputStream openOutputStream() {
      try {
      return this.socket.getOutputStream();
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public InputStream openInputStream() {
      try {
      return this.socket.getInputStream();
   
      } catch (IOException exception) {
         StreamConnection.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
}
