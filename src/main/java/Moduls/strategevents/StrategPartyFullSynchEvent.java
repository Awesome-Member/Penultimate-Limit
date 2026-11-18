package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.chat.Chat;
import java.io.DataInputStream;

public final class StrategPartyFullSynchEvent extends StrategSendEvent {
   private byte sentPartyState;
   private int sentPartyId;
   private StrategEventSupportPartyElement[] partyElements;

   protected StrategPartyFullSynchEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.sentPartyState = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.sentPartyId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      byte size = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.partyElements = new StrategEventSupportPartyElement[size];

      for(int i = 0; i < size; ++i) {
         this.partyElements[i] = new StrategEventSupportPartyElement(decompiledsupport.UncheckedDataInput.readUTF(dataInputStream), decompiledsupport.UncheckedDataInput.readInt(dataInputStream), decompiledsupport.UncheckedDataInput.readInt(dataInputStream));
      }

   }

   public final void applyToStrategist() {
      Strategist instance2 = Strategist.instance;
      StrategEventSupportPartyElement[] partyElements2 = this.partyElements;
      int sentPartyId2 = this.sentPartyId;
      int sentPartyState2 = this.sentPartyState;
      Strategist strategist2 = instance2;
      synchronized(instance2.partyInfoLock) {
         strategist2.partyState = (byte)sentPartyState2;
         strategist2.partyId = sentPartyId2;
         strategist2.partyInfo = new String[partyElements2.length][];
         sentPartyState2 = 0;

         while(true) {
            if (sentPartyState2 >= partyElements2.length) {
               break;
            }

            strategist2.partyInfo[sentPartyState2] = new String[3];
            strategist2.partyInfo[sentPartyState2][0] = partyElements2[sentPartyState2].name;
            strategist2.partyInfo[sentPartyState2][1] = String.valueOf(partyElements2[sentPartyState2].life);
            strategist2.partyInfo[sentPartyState2][2] = String.valueOf(partyElements2[sentPartyState2].maxLife);
            ++sentPartyState2;
         }
      }

      Chat instance3 = Chat.instance;
      Chat.refreshChatHeader();
   }
}
