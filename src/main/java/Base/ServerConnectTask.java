package Base;

import Base.Black;
import java.io.IOException;
import desktop.io.SocketConnection;

final class ServerConnectTask implements Runnable {
   private int port;
   public boolean connected;
   public boolean connecting;

   ServerConnectTask(int port2) {
      this.port = port2;
      this.connected = false;
      this.connecting = true;
   }

   private static void mayThrowIOException() throws IOException { }

   public final void run() {
      this.connecting = true;

      try {
         mayThrowIOException();
         Com.socketConnection = (SocketConnection)Black.openConnection("socket://win.lastlimit.ru:" + Integer.toString(this.port));
         SocketConnection socketConnection2 = Com.socketConnection;
         this.connected = true;
      } catch (IOException exception) {
         exception.printStackTrace();
      }

      this.connecting = false;
   }
}
