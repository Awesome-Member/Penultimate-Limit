package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;

public final class StrategSendDifInItemsEvent extends StrategSendEvent {
   private StrategEventSupportItemsDifElement[] content;

   protected StrategSendDifInItemsEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = new StrategEventSupportItemsDifElement[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(int i = 0; i < this.content.length; ++i) {
         StrategEventSupportItemsDifElement strategEventSupportItemsDifElement;
         if ((strategEventSupportItemsDifElement = new StrategEventSupportItemsDifElement(decompiledsupport.UncheckedDataInput.readByte(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream), (InventoryItem)null)).action == 1) {
            strategEventSupportItemsDifElement.item = Strategist.loadIndventoryItem(dataInputStream);
         }

         this.content[i] = strategEventSupportItemsDifElement;
      }

   }

   public final void applyToStrategist() {
      int indexOffset = 0;

      for(int contentIndex = 0; contentIndex < this.content.length; ++contentIndex) {
         StrategEventSupportItemsDifElement strategEventSupportItemsDifElement;
         if ((strategEventSupportItemsDifElement = this.content[contentIndex]).action == 0) {
            Strategist.instance.items.removeElementAt(strategEventSupportItemsDifElement.index + indexOffset);
            --indexOffset;
         }

         if (strategEventSupportItemsDifElement.action == 1) {
            Strategist.instance.items.insertElementAt(strategEventSupportItemsDifElement.item, strategEventSupportItemsDifElement.index);
            ++indexOffset;
         }
      }

   }
}
