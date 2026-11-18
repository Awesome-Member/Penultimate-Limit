package Moduls.quests;

import java.io.DataInputStream;

public final class QuestInPlayerKillPlayerDescExtend {
   public QuestSingleIdSmall idSmall;
   public QuestInPlayerKillPlayerDesc needToKillPlayer;

   public QuestInPlayerKillPlayerDescExtend(DataInputStream dataInputStream) {
      this.idSmall = new QuestSingleIdSmall(dataInputStream);
      this.needToKillPlayer = new QuestInPlayerKillPlayerDesc();
      this.needToKillPlayer.loadFromStream(dataInputStream);
   }
}
