package Moduls.quests;

import Moduls.Strategist;
import Moduls.TileMap;
import java.io.DataInputStream;
import java.util.Vector;

public final class QuestsOnMap {
   private QuestSingle[] quests = null;
   public String nameId = "";
   public String startText = "";
   private String endText = "";
   private byte workState = 0;
   public short indicatorHeight = 54;
   public boolean noIndication = false;
   private boolean hightlightLikeTown = false;
   public String clientHeaderLabel;
   public byte drawSignType = 4;
   public byte questsStateCach = 4;
   public int hisTownInd = 0;

   public QuestsOnMap(DataInputStream dataInputStream, int hisTownInd2) {
      this.loadFromStream(dataInputStream, hisTownInd2);
   }

   private void loadFromStream(DataInputStream dataInputStream, int hisTownInd2) {
      this.nameId = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.startText = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.endText = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.workState = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.indicatorHeight = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.noIndication = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.hightlightLikeTown = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.clientHeaderLabel = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.quests = new QuestSingle[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(int i = 0; i < this.quests.length; ++i) {
         this.quests[i] = new QuestSingle(dataInputStream);
      }

      this.hisTownInd = hisTownInd2;
   }

   public static Vector getQuestsInProgress(TileMap tileMap, int questerInd, QuestStrateg questStrateg) {
      Vector vector3 = new Vector();
      Vector quests2 = questStrateg.quests;

      for(int sizeIndex = 0; sizeIndex < quests2.size(); ++sizeIndex) {
         QuestInPlayer questInPlayer;
         if ((questInPlayer = (QuestInPlayer)quests2.elementAt(sizeIndex)).questSingle.inMap.equals(tileMap.nameId) && questInPlayer.questSingle.inMapQuesterInd == questerInd) {
            vector3.addElement(questInPlayer);
         }
      }

      return vector3;
   }

   public final Vector getQuestsForTake(Strategist strategist2, QuestStrateg questStrateg) {
      Vector vector3 = new Vector();
      int i;
      QuestSingle questSingle2;
      if (this.workState == 0) {
         for(i = 0; i < this.quests.length; ++i) {
            if ((questSingle2 = this.quests[i]).isAvailableByDone(questStrateg)) {
               if (questSingle2.isAvailable(strategist2, questStrateg)) {
                  vector3.addElement(questSingle2);
               }
               break;
            }
         }
      } else {
         for(i = 0; i < this.quests.length; ++i) {
            if ((questSingle2 = this.quests[i]).isAvailable(strategist2, questStrateg)) {
               vector3.addElement(questSingle2);
            }
         }
      }

      return vector3;
   }

   public final int getQuestsWillCanTakeCount(Strategist strategist2, QuestStrateg questStrateg) {
      int questsWillCanTakeCount = 0;

      for(int questIndex = 0; questIndex < this.quests.length; ++questIndex) {
         QuestSingle questSingle2;
         if ((questSingle2 = this.quests[questIndex]).isCanTakeAver(strategist2, questStrateg) && questSingle2.isAvailableByDone(questStrateg)) {
            ++questsWillCanTakeCount;
         }
      }

      return questsWillCanTakeCount;
   }
}
