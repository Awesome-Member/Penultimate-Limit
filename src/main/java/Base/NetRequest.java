package Base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class NetRequest {
   public boolean success = false;
   public boolean responseReceived = false;
   public DataInputStream response;
   public DataOutputStream request;
   public ByteArrayOutputStream requestBuffer;
   public ByteArrayInputStream responseBuffer;
   public boolean retainResponseBytes = false;
   public byte[] responseBytes = null;
   private static final byte[] HEADER_PLACEHOLDER = new byte[4];

   public NetRequest() {
      this.requestBuffer = new ByteArrayOutputStream();
      this.request = new DataOutputStream(this.requestBuffer);

      try {
         this.request.write(HEADER_PLACEHOLDER);
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   public NetRequest(boolean saveBytes) {
      this.retainResponseBytes = saveBytes;
      this.requestBuffer = new ByteArrayOutputStream();
      this.request = new DataOutputStream(this.requestBuffer);

      try {
         this.request.write(HEADER_PLACEHOLDER);
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   public final void writeHeader(int val) {
      try {
         this.request.writeInt(val);
      } catch (IOException exception) {
         throw new RuntimeException(exception.toString());
      }
   }
}
