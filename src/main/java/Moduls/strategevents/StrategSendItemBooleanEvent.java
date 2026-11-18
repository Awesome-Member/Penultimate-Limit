package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendItemBooleanEvent extends StrategSendEvent {
   private short itemInd;
   private boolean content;

   protected StrategSendItemBooleanEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.itemInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.content = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case 112:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapHealth = this.content;
            return;
         case 113:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapHitDist = this.content;
            return;
         case 114:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapPoison = this.content;
            return;
         case 115:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapParalize = this.content;
            return;
         case 116:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapFriends = this.content;
            return;
         case 117:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapAnswer = this.content;
            return;
         case 118:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapPull = this.content;
            return;
         case 119:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).upgradeCheapSpeed = this.content;
            return;
         case 120:
         case 121:
         case 123:
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
         case 122:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).forgeCan = this.content;
            return;
         case 124:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).isCanUpgradeCheap = this.content;
            return;
         case 125:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).canUse = this.content;
      }
   }
}
