package Moduls.indirectevents;

import Base.Com;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import desktop.storage.RecordStore;
import desktop.storage.RecordStoreException;

public final class HeroEventBanDevice extends HeroEvent {
   private long playerId;

   private static RecordStore openCacheStore(String name, boolean create) {
      try { return RecordStore.openRecordStore(name, create); }
      catch (RecordStoreException exception) { throw decompiledsupport.UncheckedDataInput.unchecked(exception); }
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.playerId = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
   }

   public final void apply() {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
      DataOutputStream dataOutputStream;
      decompiledsupport.UncheckedDataInput.writeLong((dataOutputStream = new DataOutputStream(byteArrayOutputStream)), this.playerId);
      decompiledsupport.UncheckedDataInput.flush(dataOutputStream);
      decompiledsupport.UncheckedDataInput.close(dataOutputStream);
      byte[] arr = byteArrayOutputStream.toByteArray();
      decompiledsupport.UncheckedDataInput.close(byteArrayOutputStream);

      try {
         RecordStore.deleteRecordStore("banned_player_id");
      } catch (Exception exception) {
      }

      try {
         RecordStore.deleteRecordStore("rqWPBnn_56");
      } catch (Exception exception2) {
      }

      openCacheStore("rqWPBnn_56", true).addRecord(arr, 0, arr.length);
      Com.CoverTh.Set(3);
   }

   public final void afterApply() {
   }
}
