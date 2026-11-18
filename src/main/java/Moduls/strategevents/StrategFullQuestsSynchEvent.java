package Moduls.strategevents;

import Moduls.quests.QuestStrateg;
import java.io.DataInputStream;

public final class StrategFullQuestsSynchEvent extends StrategSendEvent {
   protected StrategFullQuestsSynchEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      QuestStrateg.instance.loadFromStreamJustQuests(dataInputStream, false);
   }

   public final void applyToStrategist() {
   }
}
