package Moduls.quests;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class QuestSingleIdSmall {
   public String inMap = "";
   public short inMapQuesterInd = 0;
   public short inQuesterInd = 0;

   public QuestSingleIdSmall() {
   }

   public QuestSingleIdSmall(DataInputStream dataInputStream) {
      this.loadFromStream(dataInputStream);
   }

   public QuestSingleIdSmall(String inMap2, short inMapQuesterInd2, short inQuesterInd2) {
      this.inMap = inMap2;
      this.inMapQuesterInd = inMapQuesterInd2;
      this.inQuesterInd = inQuesterInd2;
   }

   public void loadFromStream(DataInputStream dataInputStream) {
      this.inMap = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.inMapQuesterInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.inQuesterInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
   }

   public final void saveToStreamId(DataOutputStream dataOutputStream) {
      decompiledsupport.UncheckedDataInput.writeUTF(dataOutputStream, this.inMap);
      decompiledsupport.UncheckedDataInput.writeShort(dataOutputStream, this.inMapQuesterInd);
      decompiledsupport.UncheckedDataInput.writeShort(dataOutputStream, this.inQuesterInd);
   }

   public final String createStringIdSmall() {
      return this.inMapQuesterInd + ":" + this.inQuesterInd;
   }

   public final boolean equalsById(QuestSingleIdSmall questSingleIdSmall) {
      return this.inMap.equals(questSingleIdSmall.inMap) && this.inMapQuesterInd == questSingleIdSmall.inMapQuesterInd && this.inQuesterInd == questSingleIdSmall.inQuesterInd;
   }
}
