package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.quests.QuestStrateg;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class StrategSendCurMapEvent extends StrategSendEvent {
   private String mapId;
   private int mapInd;
   private int strX;
   private int strY;
   private byte[] adding;

   protected StrategSendCurMapEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.mapInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.mapId = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.strX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.strY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.adding = new byte[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];
      decompiledsupport.UncheckedDataInput.readFully(dataInputStream, this.adding);
   }

   public final void applyToStrategist() {
      Strategist.instance.curMapInd = this.mapInd;
      Strategist.instance.curMapId = this.mapId;
      Strategist.instance.cx = (short)this.strX;
      Strategist.instance.cy = (short)this.strY;
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.adding);
      DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
      QuestStrateg.instance.loadJustWays(dataInputStream);
      QuestStrateg.instance.loadFromStreamJustDone(dataInputStream, true);
      decompiledsupport.UncheckedDataInput.close(dataInputStream);
      decompiledsupport.UncheckedDataInput.close(byteArrayInputStream);
      Strategist.instance.onChanged(true);
   }

   public final int getApplyOrder() {
      return 2;
   }
}
