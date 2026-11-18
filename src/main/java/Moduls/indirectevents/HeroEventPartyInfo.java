package Moduls.indirectevents;

import Moduls.Strategist;
import java.io.DataInputStream;

public final class HeroEventPartyInfo extends HeroEvent {
   public final void loadFromStream(DataInputStream dataInputStream) {
      DataInputStream dataInputStream2 = dataInputStream;
      Strategist instance2 = Strategist.instance;
      byte byteCode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      synchronized(instance2.partyInfoLock) {
         instance2.partyInfo = new String[byteCode][];

         for(int partyInfoIndex = 0; partyInfoIndex < byteCode; ++partyInfoIndex) {
            instance2.partyInfo[partyInfoIndex] = new String[3];
            instance2.partyInfo[partyInfoIndex][0] = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream2);
            instance2.partyInfo[partyInfoIndex][1] = String.valueOf(decompiledsupport.UncheckedDataInput.readInt(dataInputStream2));
            instance2.partyInfo[partyInfoIndex][2] = String.valueOf(decompiledsupport.UncheckedDataInput.readInt(dataInputStream2));
         }

      }
   }

   public final void apply() {
   }

   public final void afterApply() {
   }
}
