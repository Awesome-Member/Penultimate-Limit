package desktop.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public final class HttpConnectionAdapter extends StreamConnection {
   private String url;
   private HttpURLConnection connection;

   public final void connect(String url2) {
      this.url = url2;
      URL uRL = decompiledsupport.UncheckedDataInput.toURL(URI.create(url2));
      this.connection = decompiledsupport.UncheckedDataInput.openHttpConnection(uRL);
      decompiledsupport.UncheckedDataInput.connect(this.connection);
   }

   public final int getResponseCode() {
      return decompiledsupport.UncheckedDataInput.responseCode(this.connection);
   }

   public final InputStream openInputStream() {
      return decompiledsupport.UncheckedDataInput.inputStream(this.connection);
   }

   public final DataInputStream openDataInputStream() {
      return new DataInputStream(decompiledsupport.UncheckedDataInput.inputStream(this.connection));
   }

   public final DataOutputStream openDataOutputStream() {
      return new DataOutputStream(decompiledsupport.UncheckedDataInput.outputStream(this.connection));
   }

   public final OutputStream openOutputStream() {
      return decompiledsupport.UncheckedDataInput.outputStream(this.connection);
   }

   public final void close() {
      this.connection.disconnect();
   }
}
