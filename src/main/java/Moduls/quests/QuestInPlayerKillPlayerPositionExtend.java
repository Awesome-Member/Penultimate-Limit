package Moduls.quests;

import java.io.DataInputStream;

public final class QuestInPlayerKillPlayerPositionExtend {
   public int playerId;
   public QuestInPlayerKillPlayerPosition needToKillPlayerPos;

   public QuestInPlayerKillPlayerPositionExtend(DataInputStream dataInputStream) {
      this.playerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.needToKillPlayerPos = new QuestInPlayerKillPlayerPosition();
      this.needToKillPlayerPos.loadFromStream(dataInputStream);
   }
}
