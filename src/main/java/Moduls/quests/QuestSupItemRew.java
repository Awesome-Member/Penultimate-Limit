package Moduls.quests;

import Moduls.ItemSimple;
import java.io.DataInputStream;

public final class QuestSupItemRew extends ItemSimple {
   public boolean unweight = false;
   public int chance;
   public int maxCount;
   public int stackMaxCount;

   public QuestSupItemRew(DataInputStream dataInputStream) {
      this.read(dataInputStream);
      this.unweight = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.chance = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.maxCount = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.stackMaxCount = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final String getNameWithCount() {
      return this.count == this.maxCount && this.count == 1 ? this.nameVis : (this.count == this.maxCount ? String.valueOf(this.count) : this.count + "ə" + this.maxCount) + "x" + this.nameVis;
   }
}
