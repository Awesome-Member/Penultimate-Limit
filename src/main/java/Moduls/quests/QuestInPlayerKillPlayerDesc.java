package Moduls.quests;

import Moduls.StrategGraphic;
import java.io.DataInputStream;

public final class QuestInPlayerKillPlayerDesc {
   public int needToKillPlayerId = -1;
   public int needToKillPlayerLevel = 1;
   public boolean needToKillPlayerOnline = false;
   public StrategGraphic needToKillPlayerGraphics = null;
   public String needToKillPlayerName = "";

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.needToKillPlayerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      if (this.needToKillPlayerId >= 0) {
         this.needToKillPlayerLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.needToKillPlayerName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
         this.needToKillPlayerOnline = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         this.needToKillPlayerGraphics = new StrategGraphic(dataInputStream);
      } else {
         this.needToKillPlayerGraphics = null;
         this.needToKillPlayerLevel = 1;
         this.needToKillPlayerName = null;
         this.needToKillPlayerOnline = false;
      }
   }
}
