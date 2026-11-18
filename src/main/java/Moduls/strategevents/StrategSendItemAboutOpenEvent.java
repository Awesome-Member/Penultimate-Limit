package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import Moduls.ItemSimple;
import java.io.DataInputStream;

public final class StrategSendItemAboutOpenEvent extends StrategSendEvent {
   private short itemInd;
   private byte byteContent;
   private ItemSimple itemContent = null;

   protected StrategSendItemAboutOpenEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.itemInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.byteContent = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.itemContent = ItemSimple.readInstance(dataInputStream);
   }

   public final void applyToStrategist() {
      InventoryItem inventoryItem2;
      (inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).openState = this.byteContent;
      inventoryItem2.openItem = this.itemContent;
   }
}
