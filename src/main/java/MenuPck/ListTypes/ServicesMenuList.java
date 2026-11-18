package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Base.GameEffectSupport;
import Engine.Delegate;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import Moduls.ItemSimple;
import java.util.Vector;

public final class ServicesMenuList extends MenuList {
   private static GameEffectSupport[] gameEffectSupportEntries = null;
   private static int bagCountBuy = 0;
   private static int protectCountBuy = 0;
   private static int vipCountBuy = 0;
   private static int crysCountBuy = 0;
   private static byte curVipType = 0;
   private static Delegate sugestBuyItemSuccessDelegate = null;
   private static Delegate sugestBuyItemBackDelegate = null;
   private static ItemSimple sugestBuyItemSimple = null;

   public ServicesMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      switch (id) {
         case 37:
            this.hints = Mystery.vectorFromString("|||" + Mystery.stringFormat("Получи %ɼɽ за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_gold_add), "1"}) + "|" + Mystery.stringFormat("Заново расставь баллы по умениям за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_skills)}) + "|Получай больше опыта и золота от боев с монстрами!" + "|" + Mystery.stringFormat("Безопасность позволяет отказываться от любых нападений других игроков. Цена %ɾɿ за % мин", '%', new String[]{String.valueOf(Com.c_protection_cost), String.valueOf(Com.c_protection_time_for_one_pay / 60L / 1000L)}) + "|" + Mystery.stringFormat("Увеличь размер инвентаря +1 место за %ɾɿ", '%', new String[]{String.valueOf(Com.c_inventory_cost)}) + "|" + Mystery.stringFormat("Смени расу своего героя за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_class_cost)}) + "|" + Mystery.stringFormat("Смени пол своего героя за %ɾɿ", '%', new String[]{String.valueOf(Com.c_gender_choo_cost)}) + "|" + Mystery.stringFormat("Смени имя своему питомцу за %ɾɿ", '%', new String[]{String.valueOf(Com.c_pet_rename_cost)}));
            return;
         case 82:
            this.hints = Mystery.vectorFromString("Выбери пол|Выбери пол");
         default:
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      NetRequest netRequest;
      int integerCode;
      byte menuId2;
      NetRequest netRequest2;
      switch (backIndex) {
         case 154:
            if (res) {
               exchangeCrysToGold(crysCountBuy);
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 155:
            if (res) {
               if (Strategist.instance.GP_Crystals < Com.c_serv_skills) {
                  GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
               } else {
                  Com.PrBarTh.Set(true);
                  (netRequest = new NetRequest()).writeHeader(1457000);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, Com.newg_sprite);
                  Com.sendRequest(netRequest);

                  try {
                     switch (netRequest.response.readInt()) {
                        case 1457001:
                           IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                           break;
                        case 1457002:
                           GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 156:
            if (res) {
               if ((integerCode = vipCountBuy) * Com.c_vip_cost[curVipType] > Strategist.instance.GP_Crystals) {
                  GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
               } else {
                  Com.PrBarTh.Set(true);
                  (netRequest2 = new NetRequest()).writeHeader(6200000);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest2.request, integerCode);
                  decompiledsupport.UncheckedDataInput.writeByte(netRequest2.request, curVipType);
                  Com.sendRequest(netRequest2);

                  try {
                     if ((integerCode = netRequest2.response.readInt()) == 6200001) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest2.response);
                        GameCommons.instance.showFloatText((String)"Героизм\rдействует!", 3000);
                     } else if (integerCode == 6200002) {
                        GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                     }
                  } catch (Exception exception2) {
                     exception2.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 157:
            if (res) {
               if ((integerCode = protectCountBuy) * Com.c_protection_cost > Strategist.instance.GP_Crystals) {
                  GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
               } else {
                  Com.PrBarTh.Set(true);
                  (netRequest2 = new NetRequest()).writeHeader(6201000);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest2.request, integerCode);
                  Com.sendRequest(netRequest2);

                  try {
                     if ((integerCode = netRequest2.response.readInt()) == 6201001) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest2.response);
                        GameCommons.instance.showFloatText((String)"Ты теперь\rв безопасности!", 3000);
                     } else if (integerCode == 6201002) {
                        GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                     }
                  } catch (Exception exception3) {
                     exception3.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 158:
            if (res) {
               if ((integerCode = bagCountBuy) * Com.c_inventory_cost > Strategist.instance.GP_Crystals) {
                  GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
               } else {
                  Com.PrBarTh.Set(true);
                  (netRequest2 = new NetRequest()).writeHeader(6300000);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest2.request, integerCode);
                  Com.sendRequest(netRequest2);

                  try {
                     if ((integerCode = netRequest2.response.readInt()) == 6300001) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest2.response);
                        GameCommons.instance.showFloatText((String)"Инвентарь расширен!", 3000);
                     } else if (integerCode == 6300002) {
                        GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                     }
                  } catch (Exception exception4) {
                     exception4.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 159:
         case 160:
            if (res) {
               if (Com.c_gender_choo_cost > Strategist.instance.GP_Crystals) {
                  suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_gender_choo_cost);
                  return;
               }

               Com.PrBarTh.Set(true);
               (netRequest = new NetRequest()).writeHeader(4675000);
               decompiledsupport.UncheckedDataInput.writeByte(netRequest.request, backIndex == 159 ? 0 : 1);
               Com.sendRequest(netRequest);

               try {
                  if ((backIndex = netRequest.response.readInt()) == 4675001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     GameCommons.instance.showFloatText((String)"Пол сменен!", 3000);
                  } else if (backIndex == 4675002) {
                     GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                  }
               } catch (Exception exception5) {
                  exception5.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId2 = 37;
            MenuManager.instance.showMenuById(menuId2);
         default:
            return;
         case 225:
            if (res) {
               MarketMenuList.searchId = sugestBuyItemSimple.typeId;
               MarketMenuList.searchNeedCount = sugestBuyItemSimple.count;
               setLastSelectedListElForced(104, -1);
               MenuManager.instance.showMenuById(104);
            } else {
               suggestBuyItemResult(false);
            }
      }
   }

   private static boolean exchangeCrysToGold(int count) {
      boolean res = false;
      if (count > Strategist.instance.GP_Crystals) {
         GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
      } else {
         Com.PrBarTh.Set(true);
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1456000);
         decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, count);
         Com.sendRequest(netRequest);

         try {
            if ((count = netRequest.response.readInt()) == 1456001) {
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               res = true;
            } else if (count == 1456002) {
               GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         Com.PrBarTh.Stop();
      }

      return res;
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      label22:
      switch (menuId) {
         case 37:
            switch (itemIndex) {
               case 5:
               default:
                  break label22;
               case 3:
                  setAnimatedTitle(Mystery.stringFormat("Получи %ɼɽ за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_gold_add), "1"}));
                  return;
               case 4:
                  setAnimatedTitle(Mystery.stringFormat("Заново расставь баллы по умениям за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_skills)}));
                  return;
               case 6:
                  setAnimatedTitle(Mystery.stringFormat("Безопасность позволяет отказываться от любых нападений других игроков. Цена %ɾɿ за % мин", '%', new String[]{String.valueOf(Com.c_protection_cost), String.valueOf(Com.c_protection_time_for_one_pay / 60L / 1000L)}));
                  return;
               case 7:
                  setAnimatedTitle(Mystery.stringFormat("Увеличь размер инвентаря +1 место за %ɾɿ", '%', new String[]{String.valueOf(Com.c_inventory_cost)}));
                  return;
               case 8:
                  setAnimatedTitle(Mystery.stringFormat("Смени расу своего героя за %ɾɿ", '%', new String[]{String.valueOf(Com.c_serv_class_cost)}));
                  return;
               case 9:
                  setAnimatedTitle(Mystery.stringFormat("Смени пол своего героя за %ɾɿ", '%', new String[]{String.valueOf(Com.c_gender_choo_cost)}));
                  return;
               case 10:
                  setAnimatedTitle(Mystery.stringFormat("Смени имя своему питомцу за %ɾɿ", '%', new String[]{String.valueOf(Com.c_pet_rename_cost)}));
                  return;
            }
         case 92:
            setAnimatedTitle(Mystery.stringFormat("Получай в % раза больше опыта и в % раза больше золота от боев с монстрами.\nЦена %ɾɿ за % минут.", new String[]{Com.c_vip_exp_coof[itemIndex], Com.c_vip_money_coof[itemIndex], String.valueOf(Com.c_vip_cost[itemIndex]), String.valueOf(Com.c_vip_time_for_one_pay / 1000L / 60L)}));
            return;
      }

      super.onGenerateHintEvent(menuId, itemIndex);
   }

   public final void showCountChangeCrystals(boolean fromGame) {
      if (fromGame) {
         GameCommons.instance.isFromGameMenu = true;
      }

      this.showNumberEnter(161, 1, Strategist.instance.GP_Crystals, "ɾɿ");
   }

   public final void onNumberEnterEvent(int backIndex, int inputNumber) {
      switch (backIndex) {
         case 161:
            crysCountBuy = inputNumber;
            this.showConfirmationForMenu(154, "Получить золото?\nЦена " + inputNumber + "ɾɿ. +" + Com.c_serv_gold_add * inputNumber + " золотых в твой кошелек!");
            return;
         case 162:
            vipCountBuy = inputNumber;
            this.showConfirmationForMenu(156, "Стать героем?\nЦена " + inputNumber * Com.c_vip_cost[curVipType] + "ɾɿ. Стать героем на " + Com.c_vip_time_for_one_pay * (long)inputNumber / 60L / 60L / 1000L + " часов " + Com.c_vip_time_for_one_pay * (long)inputNumber / 60L / 1000L % 60L + " минут");
            return;
         case 163:
            protectCountBuy = inputNumber;
            this.showConfirmationForMenu(157, "Получить безопасность?\nЦена " + inputNumber * Com.c_protection_cost + "ɾɿ. Получить безопасность на " + Com.c_protection_time_for_one_pay * (long)inputNumber / 60L / 1000L + " минут");
            return;
         case 164:
            bagCountBuy = inputNumber;
            this.showConfirmationForMenu(158, "Расширить инвентарь?\nЦена " + inputNumber * Com.c_inventory_cost + "ɾɿ. +" + inputNumber + " мест в твой инвентарь!");
         default:
      }
   }

   protected final void onNumberCancelEvent(int backIndex) {
      byte menuId;
      switch (backIndex) {
         case 161:
            if (GameCommons.instance.isFromGameMenu) {
               MenuManager.instance.guiHandler.hideAllWindows();
               return;
            }

            menuId = 37;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 162:
            menuId = 92;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 163:
            menuId = 37;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 164:
            menuId = 37;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   public final void onNumberChangedEvent(int backIndex, int inputNumber) {
      switch (backIndex) {
         case 161:
            setAnimatedTitle("Сколько кристаллов потратить?\nТы получишь " + inputNumber * Com.c_serv_gold_add + "ɼɽ");
            return;
         case 162:
            setAnimatedTitle("Ты потратишь " + inputNumber * Com.c_vip_cost[curVipType] + "ɾɿ и станешь героем на " + (long)inputNumber * Com.c_vip_time_for_one_pay / 60L / 60L / 1000L + " часов " + (long)inputNumber * Com.c_vip_time_for_one_pay / 60L / 1000L % 60L + " минут");
            return;
         case 163:
            setAnimatedTitle("Ты потратишь " + inputNumber * Com.c_protection_cost + "ɾɿ и получишь ̎̏безопасность на " + (long)inputNumber * Com.c_protection_time_for_one_pay / 60L / 1000L + " минут!");
            return;
         case 164:
            setAnimatedTitle("Ты переносишь " + (Com.c_base_bag_size + Strategist.instance.GP_HeroLevel + Strategist.instance.buyedBagSize) + " мест. За " + inputNumber * Com.c_inventory_cost + "ɾɿ ты получишь +" + inputNumber + " мест");
         default:
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector items = super.onGenerateItemsAndSkipsEvent(menuId);
      if (menuId == 37) {
         this.clearSkips();
         this.addSkip(0);
         this.addSkip(1);
         this.addSkip(2);
      } else if (menuId == 92) {
         for (int i = 0; i < Com.c_vip_exp_coof.length; i++) {
            items.addElement(Mystery.stringFormat("Премиум x% за %ɾɿ",
                    new String[]{Com.c_vip_exp_coof[i], String.valueOf(Com.c_vip_cost[i])}));
         }
      }
      return items;
   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String messageText) {
      switch (backIndex) {
         case 182:
            byte menuId;
            if (res) {
               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4650000);
                  netRequest.request.writeUTF(messageText);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4650001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     menuId = 37;
                     MenuManager.instance.showMenuById(menuId);
                  } else {
                     menuId = 37;
                     MenuManager.instance.showMenuById(menuId);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               return;
            } else {
               menuId = 37;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 37:
            byte menuId2;
            switch (itemIndex) {
               case 3:
                  if (Strategist.instance.GP_Crystals <= 0) {
                     suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), 1);
                     return;
                  }

                  this.showCountChangeCrystals(false);
                  return;
               case 4:
                  if (Strategist.instance.GP_Crystals < Com.c_serv_skills) {
                     suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_serv_skills);
                     return;
                  }

                  this.showConfirmationForMenu(155, "Сбросить умения?\nЦена " + Integer.toString(Com.c_serv_skills) + "ɾɿ. Все баллы умений снимаются в запас, и затем ты сможешь расставить их заново, как угодно.");
                  return;
               case 5:
                  menuId2 = 92;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 6:
                  if (Strategist.instance.GP_Crystals < Com.c_protection_cost) {
                     suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_protection_cost);
                     return;
                  }

                  this.showNumberEnter(163, 1, Strategist.instance.GP_Crystals / Com.c_protection_cost, "");
                  return;
               case 7:
                  if (Strategist.instance.GP_Crystals < Com.c_inventory_cost) {
                     suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_inventory_cost);
                     return;
                  }

                  this.showNumberEnter(164, 1, Strategist.instance.GP_Crystals / Com.c_inventory_cost, "");
                  return;
               case 8:
                  MenuSupport.instance.isCreateStrategFromServiceMenu = true;
                  menuId2 = 2;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 9:
                  menuId2 = 82;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 10:
                  if (Strategist.instance.petB < 0 || Strategist.instance.petH < 0) {
                     showFloatMessageS("У вас нет питомца");
                     return;
                  } else {
                     if (Strategist.instance.GP_Crystals < Com.c_pet_rename_cost) {
                        suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_pet_rename_cost);
                        return;
                     }

                     this.showTextEnter(182, "Напиши имя питомца", "");
                     return;
                  }
               default:
                  return;
            }
         case 82:
            if (Strategist.instance.GP_Gender == itemIndex) {
               GameCommons.instance.showFloatText((String)"Это твой текущий пол!", 3000);
               return;
            } else {
               switch (itemIndex) {
                  case 0:
                     this.showConfirmationForMenu(159, "Сменить пол героя на мужской за " + Com.c_gender_choo_cost + "ɾɿ?");
                     return;
                  case 1:
                     this.showConfirmationForMenu(160, "Сменить пол героя на женский за " + Com.c_gender_choo_cost + "ɾɿ?");
                  default:
                     return;
               }
            }
         case 92:
            curVipType = (byte)itemIndex;
            if (Strategist.instance.GP_Crystals < Com.c_vip_cost[curVipType]) {
               suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_vip_cost[curVipType]);
               return;
            } else {
               if (Strategist.instance.vipStatus >= 0 && curVipType != Strategist.instance.vipStatus) {
                  GameCommons.instance.showFloatText((String)"Активен другой премиум!", 3000);
                  return;
               }

               this.showNumberEnter(162, 1, Strategist.instance.GP_Crystals / Com.c_vip_cost[curVipType], "");
               return;
            }
      }
   }

   public static void suggestBuyCrystals(Delegate delegate, int needCrystals) {
      suggestBuyCrystals(delegate, delegate, needCrystals, (String)null, (String)null, (String)null);
   }

   public static void suggestBuyCrystals(Delegate delegate, Delegate delegate2, int needCrystals) {
      suggestBuyCrystals((Delegate)null, delegate2, needCrystals, (String)null, (String)null, (String)null);
   }

   public static void suggestBuyCrystals(Delegate delegate, Delegate delegate2, int needCrystals, String infoText) {
      suggestBuyCrystals((Delegate)null, delegate2, needCrystals, infoText, (String)null, (String)null);
   }

   public static void suggestBuyCrystals(Delegate delegate, Delegate delegate2, int needCrystals, String infoText, String postInfo, String replaceInfo) {
      GameCommons.instance.showFloatText("Не хватает кристаллов.");
      if (delegate2 != null) {
         delegate2.execute();
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }
   }

   public static void confirmGoldPurchase(Delegate delegate, Delegate delegate2, long goldCost) {
      checkGoldAndExecute(delegate2, goldCost);
   }

   public static void confirmGoldPurchaseWithMessage(Delegate delegate, Delegate delegate2, long goldCost, String messageText) {
      checkGoldAndExecute(delegate2, goldCost);
   }

   public static void confirmPurchaseWithCurrencies(Delegate delegate, Delegate delegate2, long goldCost, long crystalCost, String messageText, String messageText2, String messageText3) {
      checkGoldAndExecute(delegate2, goldCost);
   }

   public static void checkGoldAndExecute(Delegate delegate, long goldCost) {
      String messageText = "Не хватает %needdeltaɼɽ";
      messageText = Mystery.stringReplace("Не хватает %needdeltaɼɽ", "%needdelta", String.valueOf(goldCost - (long)Strategist.instance.GP_Money));
      GameCommons.instance.showFloatText(messageText);
      if (delegate != null) {
         delegate.execute();
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }
   }

   public static void suggestBuyItem(Delegate delegate, Delegate delegate2, ItemSimple itemSimple) {
      suggestBuyItem(delegate, delegate2, itemSimple, (String)null, (String)null, (String)null);
   }

   public static void suggestBuyItem(Delegate delegate, Delegate delegate2, ItemSimple itemSimple, String infoText) {
      suggestBuyItem(delegate, delegate2, itemSimple, infoText, (String)null, (String)null);
   }

   public static void suggestBuyItem(Delegate delegate, Delegate delegate2, ItemSimple itemSimple, String infoText, String postInfo, String replaceInfo) {
      sugestBuyItemSuccessDelegate = delegate;
      sugestBuyItemBackDelegate = delegate2;
      sugestBuyItemSimple = itemSimple;
      String messageText = Mystery.stringReplace((infoText != null ? infoText + "\n" : "") + "Требуется %needval. Поискать эту вещь на рынке?" + (postInfo != null ? "\n\n" + postInfo : ""), "%needval", itemSimple.count + "x" + itemSimple.nameVis);
      (new ServicesMenuList(225, -1, "")).showConfirmationForMenu(225, messageText);
   }

   public static void suggestBuyItemResult(boolean success) {
      Delegate delegate;
      if (success) {
         delegate = sugestBuyItemSuccessDelegate;
      } else {
         delegate = sugestBuyItemBackDelegate;
      }

      if (delegate != null) {
         delegate.execute();
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }

      sugestBuyItemSimple = null;
      sugestBuyItemBackDelegate = null;
      sugestBuyItemSuccessDelegate = null;
   }

}
