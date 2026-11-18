package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;

public final class StrategSendItemAdditionalDataEvent extends StrategSendEvent {
   private short itemInd;
   private InventoryItem itemContent;

   protected StrategSendItemAdditionalDataEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.itemInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.itemContent = new InventoryItem();
      this.itemContent.readAdditional(dataInputStream);
   }

   public final void applyToStrategist() {
      InventoryItem inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(this.itemInd);
      InventoryItem itemContent2 = this.itemContent;
      InventoryItem inventoryItem3 = inventoryItem2;
      inventoryItem2.drawMob = itemContent2.drawMob;
      inventoryItem3.drawMobHead = itemContent2.drawMobHead;
      inventoryItem3.drawMobBody = itemContent2.drawMobBody;
      inventoryItem3.setSpritesSlot = itemContent2.setSpritesSlot;
      inventoryItem3.setSpritesSprite = itemContent2.setSpritesSprite;
      inventoryItem3.setSpritesOtherSlots = itemContent2.setSpritesOtherSlots;
   }
}
