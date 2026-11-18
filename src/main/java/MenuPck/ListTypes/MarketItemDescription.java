package MenuPck.ListTypes;

import Moduls.InventoryItem;

public class MarketItemDescription {
   public String string;
   public String string2;
   public int quantity;
   public int goldPrice;
   public int crystalPrice;
   public InventoryItem inventoryItem;

   public MarketItemDescription(String string3, String string22, int quantity2, int goldPrice2, int crystalPrice2, InventoryItem inventoryItem2) {
      this.string = string3;
      this.string2 = string22;
      this.quantity = quantity2;
      this.goldPrice = goldPrice2;
      this.crystalPrice = crystalPrice2;
      this.inventoryItem = inventoryItem2;
   }
}
