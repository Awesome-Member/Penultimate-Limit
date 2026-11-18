package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;

public final class StrategSendPartyContentEvent extends StrategSendEvent {
   private StrategEventSupportPartyElement[] content;

   protected StrategSendPartyContentEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = new StrategEventSupportPartyElement[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      for(int i = 0; i < this.content.length; ++i) {
         StrategEventSupportPartyElement strategEventSupportPartyElement = new StrategEventSupportPartyElement(decompiledsupport.UncheckedDataInput.readUTF(dataInputStream), decompiledsupport.UncheckedDataInput.readInt(dataInputStream), decompiledsupport.UncheckedDataInput.readInt(dataInputStream));
         this.content[i] = strategEventSupportPartyElement;
      }

   }

   public final void applyToStrategist() {
      Strategist instance2 = Strategist.instance;
      StrategEventSupportPartyElement[] content2 = this.content;
      Strategist strategist2 = instance2;
      synchronized(instance2.partyInfoLock) {
         strategist2.partyInfo = new String[content2.length][];

         for(int partyInfoIndex = 0; partyInfoIndex < content2.length; ++partyInfoIndex) {
            strategist2.partyInfo[partyInfoIndex] = new String[3];
            strategist2.partyInfo[partyInfoIndex][0] = content2[partyInfoIndex].name;
            strategist2.partyInfo[partyInfoIndex][1] = String.valueOf(content2[partyInfoIndex].life);
            strategist2.partyInfo[partyInfoIndex][2] = String.valueOf(content2[partyInfoIndex].maxLife);
         }

      }
   }
}
