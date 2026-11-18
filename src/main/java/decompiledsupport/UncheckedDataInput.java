package decompiledsupport;

import java.io.DataInputStream;
import java.io.IOException;

/** Preserves checked I/O failures where the original bytecode exposed them unchecked. */
public final class UncheckedDataInput {
   private UncheckedDataInput() { }

   public static boolean readBoolean(DataInputStream input) { try { return input.readBoolean(); } catch (IOException e) { throwUnchecked(e); return false; } }
   public static byte readByte(DataInputStream input) { try { return input.readByte(); } catch (IOException e) { throwUnchecked(e); return 0; } }
   public static short readShort(DataInputStream input) { try { return input.readShort(); } catch (IOException e) { throwUnchecked(e); return 0; } }
   public static char readChar(DataInputStream input) { try { return input.readChar(); } catch (IOException e) { throwUnchecked(e); return 0; } }
   public static int readInt(DataInputStream input) { try { return input.readInt(); } catch (IOException e) { throwUnchecked(e); return 0; } }
   public static long readLong(DataInputStream input) { try { return input.readLong(); } catch (IOException e) { throwUnchecked(e); return 0L; } }
   public static float readFloat(DataInputStream input) { try { return input.readFloat(); } catch (IOException e) { throwUnchecked(e); return 0.0F; } }
   public static double readDouble(DataInputStream input) { try { return input.readDouble(); } catch (IOException e) { throwUnchecked(e); return 0.0D; } }
   public static String readUTF(DataInputStream input) { try { return input.readUTF(); } catch (IOException e) { throwUnchecked(e); return null; } }

   public static void writeUTF(java.io.DataOutputStream output, String messageText) { try { output.writeUTF(messageText); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeShort(java.io.DataOutputStream output, int data) { try { output.writeShort(data); } catch (IOException e) { throwUnchecked(e); } }   public static void writeBoolean(java.io.DataOutputStream output, boolean data) { try { output.writeBoolean(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeByte(java.io.DataOutputStream output, int data) { try { output.writeByte(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeChar(java.io.DataOutputStream output, int data) { try { output.writeChar(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeInt(java.io.DataOutputStream output, int data) { try { output.writeInt(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeLong(java.io.DataOutputStream output, long data) { try { output.writeLong(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeFloat(java.io.DataOutputStream output, float data) { try { output.writeFloat(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void writeDouble(java.io.DataOutputStream output, double data) { try { output.writeDouble(data); } catch (IOException e) { throwUnchecked(e); } }   public static int read(java.io.InputStream input, byte[] buffer, int offset, int length) { try { return input.read(buffer, offset, length); } catch (IOException e) { throwUnchecked(e); return -1; } }
   public static void write(java.io.DataOutputStream output, int data) { try { output.write(data); } catch (IOException e) { throwUnchecked(e); } }
   public static void write(java.io.DataOutputStream output, byte[] buffer, int offset, int length) { try { output.write(buffer, offset, length); } catch (IOException e) { throwUnchecked(e); } }
   public static void close(java.io.Closeable closeable) { try { closeable.close(); } catch (IOException e) { throwUnchecked(e); } }   public static java.io.FileInputStream open(java.io.File file) { try { return new java.io.FileInputStream(file); } catch (java.io.FileNotFoundException e) { throwUnchecked(e); return null; } }
   public static RuntimeException unchecked(Throwable throwable) { UncheckedDataInput.<RuntimeException>throwUnchecked(throwable); return null; }
   public static int getIntValue(org.jdom.Attribute attribute) { try { return attribute.getIntValue(); } catch (Exception e) { throw unchecked(e); } }
   public static void flush(java.io.Flushable flushable) { try { flushable.flush(); } catch (IOException e) { throwUnchecked(e); } }
   public static void readFully(DataInputStream input, byte[] buffer) { try { input.readFully(buffer); } catch (IOException e) { throwUnchecked(e); } }
   public static java.net.URL toURL(java.net.URI uri) { try { return uri.toURL(); } catch (java.net.MalformedURLException e) { throw unchecked(e); } }
   public static java.net.HttpURLConnection openHttpConnection(java.net.URL url) { try { return (java.net.HttpURLConnection)url.openConnection(); } catch (IOException e) { throw unchecked(e); } }
   public static void connect(java.net.HttpURLConnection connection) { try { connection.connect(); } catch (IOException e) { throw unchecked(e); } }
   public static int responseCode(java.net.HttpURLConnection connection) { try { return connection.getResponseCode(); } catch (IOException e) { throw unchecked(e); } }
   public static java.io.InputStream inputStream(java.net.HttpURLConnection connection) { try { return connection.getInputStream(); } catch (IOException e) { throw unchecked(e); } }
   public static java.io.OutputStream outputStream(java.net.HttpURLConnection connection) { try { return connection.getOutputStream(); } catch (IOException e) { throw unchecked(e); } }   @SuppressWarnings("unchecked")
   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
}