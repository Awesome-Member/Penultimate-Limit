package Moduls.strategevents;

import Moduls.InventoryItem;

public final class StrategEventSupportItemsDifElement {
   public byte action = 0;
   public short index = 0;
   public InventoryItem item = null;

   public StrategEventSupportItemsDifElement(byte action2, short itemIndex, InventoryItem inventoryItem2) {
      this.action = action2;
      this.index = itemIndex;
      this.item = null;
   }
}
