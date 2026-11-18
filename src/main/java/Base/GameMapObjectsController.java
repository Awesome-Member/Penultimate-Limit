package Base;

import Engine.Delegate;
import Engine.Delegateable;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuShowListDelegate;
import MenuPck.MenuSupport;
import MenuPck.ListTypes.OtherMenuList;
import MenuPck.ListTypes.ServicesMenuList;
import Moduls.StrategGraphic;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.InventoryItem;
import Moduls.ItemSimple;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestSingle;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestSupItemNeed;
import Moduls.quests.QuestSupTextIdsNeed;
import Moduls.quests.QuestsOnMap;
import java.util.Vector;

public final class GameMapObjectsController implements Delegateable {
   public static final GameMapObjectsController instance = new GameMapObjectsController();
   private int delegateRealoadQuesterTownInd;
   private int delegateAnalisMapObjectInd;
   private String curInfo = "";
   public String curFloatInfo = "";
   public int curNeedCrys = 0;
   public long curNeedGold = 0L;
   public ItemSimple curNeedItem = null;

   private GameMapObjectsController() {
   }

   public final boolean analisMapObject(int tInd, boolean fastUse) {
      if (MenuManager.instance.guiHandler.isActive() && !fastUse) {
         return false;
      } else {
         Com.currentGameMap.towns[tInd].activedAtTime = Mystery.currentTimeMillis;
         NetRequest netRequest;
         int integerCode;
         String messageText3;
         int rewardId;
         GameMapObjectsController gameMapObjectsController;
         int delegateAnalisMapObjectInd2;
         boolean useFastAction;
         int integerCode2;
         NetRequest netRequest2;
         String messageText;
         switch (Com.currentGameMap.towns[tInd].type) {
            case 0:
               return true;
            case 1:
               MenuSupport.instance.curActiveTownInd = tInd;
               if (!MenuManager.instance.guiHandler.isActive()) {
                  (new OtherMenuList()).showMenuMessage(174, Com.currentGameMap.towns[tInd].GPFile);
               }

               return true;
            case 401:
               MenuSupport.instance.curActiveTownInd = tInd;
               return actionMapObjectTreasure(tInd);
            case 402:
               MenuSupport.instance.curActiveTownInd = tInd;
               delegateAnalisMapObjectInd2 = tInd;
               gameMapObjectsController = this;
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140210);
                  netRequest.request.writeUTF(Com.currentGameMap.towns[delegateAnalisMapObjectInd2].GPFile);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 4140211) {
                     MenuSupport.instance.string5 = Com.currentGameMap.towns[delegateAnalisMapObjectInd2].GPFile;
                     MenuSupport.instance.string6 = netRequest.response.readUTF();
                     loadItemsSellerSoldData(netRequest);
                     loadItemsSellerItemsData(netRequest);
                     MenuManager.instance.showMenuById(190);
                  } else if (integerCode == 4140212) {
                     messageText3 = netRequest.response.readUTF();
                     rewardId = netRequest.response.readInt();
                     long timestamp = netRequest.response.readLong();
                     ItemSimple itemSimple = ItemSimple.readInstance(netRequest.response);
                     if (timestamp > 0L) {
                        gameMapObjectsController.delegateAnalisMapObjectInd = delegateAnalisMapObjectInd2;
                        ServicesMenuList.confirmPurchaseWithCurrencies(new Delegate(gameMapObjectsController, 3), new Delegate(gameMapObjectsController, 5), timestamp, timestamp, (String)null, messageText3, (String)null);
                     } else if (rewardId > 0) {
                        ServicesMenuList.suggestBuyCrystals((Delegate)null, new Delegate(gameMapObjectsController, 5), rewardId, (String)null, messageText3, (String)null);
                     } else if (itemSimple != null) {
                        gameMapObjectsController.delegateAnalisMapObjectInd = delegateAnalisMapObjectInd2;
                        ServicesMenuList.suggestBuyItem(new Delegate(gameMapObjectsController, 3), new Delegate(gameMapObjectsController, 5), itemSimple, (String)null, messageText3, (String)null);
                     } else {
                        (new OtherMenuList()).showMenuMessage(185, messageText3);
                     }
                  } else if (integerCode == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 403:
               MenuSupport.instance.curActiveTownInd = tInd;
               delegateAnalisMapObjectInd2 = tInd;
               gameMapObjectsController = this;
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140310);
                  netRequest.request.writeUTF(Com.currentGameMap.towns[delegateAnalisMapObjectInd2].GPFile);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 4140311) {
                     MenuSupport.instance.curActiveTownInd = delegateAnalisMapObjectInd2;
                     MenuSupport.instance.monsterSellerHead = netRequest.response.readInt();
                     MenuSupport.instance.monsterSellerBody = netRequest.response.readInt();
                     MenuSupport.instance.string7 = netRequest.response.readUTF();
                     gameMapObjectsController.executeDelegated(1);
                  } else if (integerCode == 4140312) {
                     MenuSupport.instance.monsterSellerHead = netRequest.response.readInt();
                     MenuSupport.instance.monsterSellerBody = netRequest.response.readInt();
                     MenuSupport.instance.string7 = netRequest.response.readUTF();
                     long timestamp2 = netRequest.response.readLong();
                     new Delegate(gameMapObjectsController, 1);
                     Delegate delegate = new Delegate(gameMapObjectsController, 5);
                     MenuSupport instance2 = MenuSupport.instance;
                     instance2 = MenuSupport.instance;
                     instance2 = MenuSupport.instance;
                     ServicesMenuList.checkGoldAndExecute(delegate, timestamp2);
                  } else if (integerCode == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception2) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 404:
               MenuSupport.instance.curActiveTownInd = tInd;
               useFastAction = fastUse;
               delegateAnalisMapObjectInd2 = tInd;
               gameMapObjectsController = this;
               boolean actionSucceeded = true;
               Com.PrBarTh.Set(true);
               netRequest2 = new NetRequest();

               try {
                  netRequest2.writeHeader(4140410);
                  netRequest2.request.writeUTF(Com.currentGameMap.towns[delegateAnalisMapObjectInd2].GPFile);
                  Com.sendRequest(netRequest2);
                  if ((rewardId = netRequest2.response.readInt()) == 4140411) {
                     if (!netRequest2.response.readBoolean() && !useFastAction) {
                        String messageText2 = netRequest2.response.readUTF();
                        MenuSupport.instance.curActiveTownInd = delegateAnalisMapObjectInd2;
                        (new OtherMenuList()).showConfirmationForMenu(186, messageText2);
                     } else {
                        actionSucceeded = actionMapObjectMapPortalUse(delegateAnalisMapObjectInd2);
                     }
                  } else if (rewardId == 4140412) {
                     gameMapObjectsController.showForCantUseObject(delegateAnalisMapObjectInd2, netRequest2.response.readUTF(), netRequest2.response.readUTF(), netRequest2.response.readInt(), netRequest2.response.readLong(), ItemSimple.readInstance(netRequest2.response));
                  } else if (rewardId == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception3) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               if (actionSucceeded) {
                  Com.PrBarTh.Stop();
               }

               return true;
            case 405:
               MenuSupport.instance.curActiveTownInd = tInd;
               useFastAction = fastUse;
               delegateAnalisMapObjectInd2 = tInd;
               gameMapObjectsController = this;
               Com.PrBarTh.Set(true);
               NetRequest netRequest3 = new NetRequest();

               try {
                  netRequest3.writeHeader(4140510);
                  netRequest3.request.writeUTF(Com.currentGameMap.towns[delegateAnalisMapObjectInd2].GPFile);
                  Com.sendRequest(netRequest3);
                  if ((integerCode2 = netRequest3.response.readInt()) == 4140511) {
                     if (!netRequest3.response.readBoolean() && !useFastAction) {
                        messageText = netRequest3.response.readUTF();
                        MenuSupport.instance.curActiveTownInd = delegateAnalisMapObjectInd2;
                        (new OtherMenuList()).showConfirmationForMenu(187, messageText);
                     } else {
                        actionMapObjectDirectPortalUse(delegateAnalisMapObjectInd2);
                     }
                  } else if (integerCode2 == 4140512) {
                     gameMapObjectsController.showForCantUseObject(delegateAnalisMapObjectInd2, netRequest3.response.readUTF(), netRequest3.response.readUTF(), netRequest3.response.readInt(), netRequest3.response.readLong(), ItemSimple.readInstance(netRequest3.response));
                  } else if (integerCode2 == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception4) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 406:
               MenuSupport.instance.curActiveTownInd = tInd;
               useFastAction = fastUse;
               delegateAnalisMapObjectInd2 = tInd;
               gameMapObjectsController = this;
               Town town = Com.currentGameMap.towns[tInd];
               if (!fastUse && town.typeIdName != null && town.typeIdName.length() > 0) {
                  String nameId2 = Com.currentGameMap.nameId;
                  String typeIdName2 = town.typeIdName;
                  QuestStrateg instance3 = QuestStrateg.instance;
                  integerCode2 = 0;
                  rewardId = instance3.quests.size();

                  boolean reopenObjectMenu;
                  label195:
                  while(true) {
                     if (integerCode2 >= rewardId) {
                        reopenObjectMenu = false;
                        break;
                     }

                     QuestInPlayer questInPlayer;
                     if (!(questInPlayer = (QuestInPlayer)instance3.quests.elementAt(integerCode2)).isDoneCach) {
                        QuestSupTextIdsNeed[] needDecorators2 = questInPlayer.questSingle.needDecorators;

                        for(int needDecorators2Index = 0; needDecorators2Index < needDecorators2.length; ++needDecorators2Index) {
                           QuestSupTextIdsNeed questSupTextIdsNeed;
                           if ((questSupTextIdsNeed = needDecorators2[needDecorators2Index]).name.equals(typeIdName2) && Mystery.isContainsStringInArray(questSupTextIdsNeed.maps, nameId2) && (QuestInPlayer.getFullCountForNameMapCountMap(typeIdName2, questInPlayer.usedDecorators) < questSupTextIdsNeed.count || questSupTextIdsNeed.count == -1)) {
                              reopenObjectMenu = true;
                              break label195;
                           }
                        }
                     }

                     ++integerCode2;
                  }

                  if (!reopenObjectMenu) {
                     return true;
                  }
               }

               Com.PrBarTh.Set(true);
               netRequest2 = new NetRequest();

               try {
                  netRequest2.writeHeader(4140610);
                  netRequest2.request.writeUTF(town.GPFile);
                  Com.sendRequest(netRequest2);
                  if ((rewardId = netRequest2.response.readInt()) == 4140611) {
                     messageText = netRequest2.response.readUTF();
                     MenuSupport.instance.curActiveTownInd = delegateAnalisMapObjectInd2;
                     if (useFastAction) {
                        destroyMapObject(delegateAnalisMapObjectInd2);
                     } else {
                        (new OtherMenuList()).showConfirmationForMenu(188, messageText);
                     }
                  } else if (rewardId == 4140612) {
                     gameMapObjectsController.showForCantUseObject(delegateAnalisMapObjectInd2, netRequest2.response.readUTF(), netRequest2.response.readUTF(), netRequest2.response.readInt(), netRequest2.response.readLong(), ItemSimple.readInstance(netRequest2.response));
                  } else if (rewardId == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception5) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 407:
               MenuSupport.instance.curActiveTownInd = tInd;
               tInd = tInd;
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140700);
                  netRequest.request.writeUTF(Com.currentGameMap.towns[tInd].GPFile);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) != 4140701 && integerCode != 4140705) {
                     if (integerCode == 4140004) {
                        brokeMapObjectsOrder();
                     } else {
                        GameCommons.instance.showFloatText("Произошла ошибка!");
                     }
                  } else {
                     messageText3 = netRequest.response.readUTF();
                     destroyMapObject2(tInd);
                     (new OtherMenuList()).showMenuMessage(185, messageText3);
                  }
               } catch (Exception exception6) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 408:
               MenuSupport.instance.curActiveTownInd = tInd;
               tInd = tInd;
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140800);
                  netRequest.request.writeUTF(Com.currentGameMap.towns[tInd].GPFile);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 4140801) {
                     messageText3 = netRequest.response.readUTF();
                     (new OtherMenuList()).showMenuMessage(185, messageText3);
                  } else if (integerCode == 4140004) {
                     brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception7) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               return true;
            case 409:
               MenuSupport.instance.curActiveTownInd = tInd;
               return this.actionMapObjectQuestGiver(tInd);
            default:
               return false;
         }
      }
   }

   private static boolean actionMapObjectTreasure(int townIndex) {
      Town town;
      QuestStrateg questStrateg;
      String messageText;
      if ((town = Com.currentGameMap.towns[townIndex]).typeIdName != null && town.typeIdName.length() > 0) {
         String nameId2 = Com.currentGameMap.nameId;
         messageText = town.typeIdName;
         questStrateg = QuestStrateg.instance;
         int questIndex = 0;
         int questsCount = questStrateg.quests.size();

         boolean questMatches;
         label72:
         while(true) {
            if (questIndex >= questsCount) {
               questMatches = false;
               break;
            }

            QuestInPlayer questInPlayer;
            if (!(questInPlayer = (QuestInPlayer)questStrateg.quests.elementAt(questIndex)).isDoneCach) {
               QuestSupTextIdsNeed[] needChests2 = questInPlayer.questSingle.needChests;

               for(int needChests2Index = 0; needChests2Index < needChests2.length; ++needChests2Index) {
                  QuestSupTextIdsNeed questSupTextIdsNeed;
                  if ((questSupTextIdsNeed = needChests2[needChests2Index]).name.equals(messageText) && Mystery.isContainsStringInArray(questSupTextIdsNeed.maps, nameId2) && (QuestInPlayer.getFullCountForNameMapCountMap(messageText, questInPlayer.usedChests) < questSupTextIdsNeed.count || questSupTextIdsNeed.count == -1)) {
                     questMatches = true;
                     break label72;
                  }
               }
            }

            ++questIndex;
         }

         if (!questMatches) {
            return false;
         }
      }

      boolean canFinishQuest = true;
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4140100);
         netRequest.request.writeUTF(Com.currentGameMap.towns[townIndex].GPFile);
         Com.sendRequest(netRequest);
         int integerCode;
         if ((integerCode = netRequest.response.readInt()) == 4140101) {
            String messageText2 = netRequest.response.readUTF();
            Com.applyRewardEvents(netRequest);
            destroyMapObject2(townIndex);
            short tInd2 = (short)townIndex;
            String typeIdName2 = Com.currentGameMap.towns[townIndex].typeIdName;
            short tInd22 = tInd2;
            messageText = Com.currentGameMap.nameId;
            questStrateg = QuestStrateg.instance;
            boolean hasAvailableQuest = false;

            for(int sizeIndex = 0; sizeIndex < questStrateg.quests.size(); ++sizeIndex) {
               if (((QuestInPlayer)questStrateg.quests.elementAt(sizeIndex)).tryAddChestUse(messageText, tInd22, typeIdName2)) {
                  hasAvailableQuest = true;
               }
            }

            if (hasAvailableQuest) {
               Com.onQuestsChanged(true);
            }

            GameCommons.instance.showFloatText((String)messageText2, 3000, 200, 1000, 0);
         } else if (integerCode == 4140004) {
            brokeMapObjectsOrder();
         } else {
            GameCommons.instance.showFloatText("Произошла ошибка!");
            canFinishQuest = false;
         }
      } catch (Exception exception) {
         GameCommons.instance.showFloatText("Произошла ошибка!");
         canFinishQuest = false;
      }

      Com.PrBarTh.Stop();
      return canFinishQuest;
   }

   private void showForCantUseObject(int tInd, String info, String floatInfo, int needCrys, long needGold, ItemSimple itemSimple) {
      this.curInfo = info;
      this.curFloatInfo = floatInfo;
      this.curNeedCrys = needCrys;
      this.curNeedGold = needGold;
      this.curNeedItem = itemSimple;
      this.delegateAnalisMapObjectInd = tInd;
      MenuSupport.instance.curActiveTownInd = tInd;
      (new OtherMenuList()).showConfirmationForMenu(247, this.curInfo);
   }

   public static void destroyMapObject(int tInd) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4140620);
         netRequest.request.writeUTF(Com.currentGameMap.towns[tInd].GPFile);
         Com.sendRequest(netRequest);
         int integerCode;
         if ((integerCode = netRequest.response.readInt()) == 4140621) {
            int[][] integersGrid = new int[netRequest.response.readInt()][];

            for(int addDIndex = 0; addDIndex < integersGrid.length; ++addDIndex) {
               integersGrid[addDIndex] = new int[3];
               integersGrid[addDIndex][0] = netRequest.response.readInt();
               integersGrid[addDIndex][1] = netRequest.response.readInt();
               integersGrid[addDIndex][2] = netRequest.response.readInt();
            }

            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
            Com.currentGameMap.addDecors(integersGrid, true);
            Com.currentGameMap.towns[tInd].state = 1;
            QuestStrateg.instance.recordDecoratorUse(Com.currentGameMap.nameId, (short)tInd, Com.currentGameMap.towns[tInd].typeIdName);
         } else if (integerCode == 4140004) {
            brokeMapObjectsOrder();
         } else {
            GameCommons.instance.showFloatText("Произошла ошибка!");
         }
      } catch (Exception exception) {
         GameCommons.instance.showFloatText("Произошла ошибка!");
      }

      Com.PrBarTh.Stop();
   }

   public static void actionMapObjectMonsterSellerUse(int integerCode) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4140320);
         netRequest.request.writeUTF(Com.currentGameMap.towns[integerCode].GPFile);
         Com.sendRequest(netRequest);
         if ((integerCode = netRequest.response.readInt()) == 4140321) {
            Com.applyRewardEvents(netRequest);
         } else if (integerCode == 4140004) {
            brokeMapObjectsOrder();
         } else {
            GameCommons.instance.showFloatText("Произошла ошибка!");
         }
      } catch (Exception exception) {
         GameCommons.instance.showFloatText("Произошла ошибка!");
      }

      Com.PrBarTh.Stop();
   }

   public final boolean actionMapObjectQuestGiver(int tInd) {
      this.delegateRealoadQuesterTownInd = tInd;
      MenuList.setLastSelectedListElForced(196, -1);
      return this.actionMapObjectQuestGiverForDelegate(false, false, false);
   }

   public final boolean actionMapObjectQuestGiverForDelegate(boolean skipNoSpace, boolean skipJustNeed, boolean analyzeSpaceAndNeedJustForLast) {
      try {
         Town town;
         int questGiverType;
         OtherMenuList.questGiverInMapInd = questGiverType = (town = Com.currentGameMap.towns[this.delegateRealoadQuesterTownInd]).typeInd;
         boolean hasQuestReward = false;
         short delegateRealoadQuesterTownInd2 = (short)this.delegateRealoadQuesterTownInd;
         String typeIdName2 = town.typeIdName;
         short delegateRealoadQuesterTownInd22 = delegateRealoadQuesterTownInd2;
         String nameId2 = Com.currentGameMap.nameId;
         QuestStrateg instance2 = QuestStrateg.instance;
         boolean hasNewQuests = false;

         for(int sizeIndex = 0; sizeIndex < instance2.quests.size(); ++sizeIndex) {
            if (((QuestInPlayer)instance2.quests.elementAt(sizeIndex)).tryAddQuesterUse(nameId2, delegateRealoadQuesterTownInd22, typeIdName2)) {
               hasNewQuests = true;
            }
         }

         if (hasNewQuests) {
            Com.onQuestsChanged(true);
         }

         if (hasNewQuests) {
            hasQuestReward = true;
            GlobalMapTh.gmTh.addUsedTown((short)this.delegateRealoadQuesterTownInd);
         }

         TileMap currentGameMap2 = Com.currentGameMap;
         String[] checkResult;
         if ((checkResult = QuestStrateg.instance.checkGetnQuestsForDone(currentGameMap2, questGiverType, currentGameMap2.questers[questGiverType], Strategist.instance, analyzeSpaceAndNeedJustForLast))[0] == "BROKE_ORDER") {
            return true;
         } else if (checkResult[0] != "SUCCESS" && checkResult[0] != "SUCCESS_ERROR") {
            if (checkResult[0] != "SUCCESS_FLOAT" && checkResult[0] != "SUCCESS_SILENT") {
               if (checkResult[0] == "CANT_GIVE_REWARD" && !skipNoSpace) {
                  MenuSupport.instance.curActiveTownInd = this.delegateRealoadQuesterTownInd;
                  (new OtherMenuList()).showYesNoMenu(234, checkResult[1], "Квесты", "В игру");
                  return true;
               } else {
                  if (!skipJustNeed || analyzeSpaceAndNeedJustForLast) {
                     int requiredCrystals = 0;
                     long sugestNeedGoldCount = 0L;
                     QuestSupItemNeed questSupItemNeed = null;
                     Vector quests2 = QuestStrateg.instance.quests;

                     for(int quests2Count = skipJustNeed ? quests2.size() - 1 : 0; quests2Count < quests2.size(); ++quests2Count) {
                        QuestInPlayer questInPlayer;
                        if ((questInPlayer = (QuestInPlayer)quests2.elementAt(quests2Count)).questSingle.inMap.equals(Com.currentGameMap.nameId) && questInPlayer.questSingle.inMapQuesterInd == questGiverType) {
                           QuestSingle questSingle2;
                           if ((questSingle2 = questInPlayer.questSingle).isNeedJustCrystals(Strategist.instance, questInPlayer)) {
                              requiredCrystals += questSingle2.needCrys;
                           }

                           if (questSingle2.isNeedJustGold(Strategist.instance, questInPlayer)) {
                              sugestNeedGoldCount += (long)questSingle2.needMoney;
                           }

                           QuestSupItemNeed questSupItemNeed2;
                           if ((questSupItemNeed2 = questSingle2.isNeedJustSelleableItem(Strategist.instance, questInPlayer)) != null) {
                              questSupItemNeed = questSupItemNeed2;
                           }
                        }
                     }

                     if (requiredCrystals > 0) {
                        ServicesMenuList.suggestBuyCrystals((Delegate)null, new Delegate(new MenuShowListDelegate(196), 0), requiredCrystals, "Для квеста не хватает кристаллов.");
                        return true;
                     }

                     if (sugestNeedGoldCount > 0L) {
                        ServicesMenuList.confirmGoldPurchaseWithMessage(new Delegate(this, skipNoSpace ? 4 : 2), new Delegate(new MenuShowListDelegate(196), 0), sugestNeedGoldCount, "Для квестов не хватает %needvalɼɽ.");
                        return true;
                     }

                     if (questSupItemNeed != null) {
                        ServicesMenuList.suggestBuyItem(new Delegate(this, skipNoSpace ? 4 : 2), new Delegate(new MenuShowListDelegate(196), 0), questSupItemNeed, "Для квеста не хватает вещей.");
                        return true;
                     }
                  }

                  QuestsOnMap questsOnMap;
                  if ((questsOnMap = Com.currentGameMap.questers[questGiverType]).getQuestsForTake(Strategist.instance, QuestStrateg.instance).size() <= 0 && QuestsOnMap.getQuestsInProgress(Com.currentGameMap, questGiverType, QuestStrateg.instance).size() <= 0) {
                     if (questsOnMap.getQuestsWillCanTakeCount(Strategist.instance, QuestStrateg.instance) > 0) {
                        (new OtherMenuList()).showMenuMessage(185, "Уровень слишком мал");
                        return true;
                     } else if (hasQuestReward) {
                        (new OtherMenuList()).showMenuMessage(185, "Успех!");
                        return true;
                     } else {
                        return false;
                     }
                  } else {
                     MenuManager.instance.showMenuById(196);
                     return true;
                  }
               }
            } else {
               label141: {
                  if (Integer.parseInt(checkResult[2]) == 1) {
                     MenuSupport.instance.curActiveTownInd = this.delegateRealoadQuesterTownInd;
                     if (MenuSupport.instance.curActiveTownInd < 0 || this.actionMapObjectQuestGiver(MenuSupport.instance.curActiveTownInd)) {
                        break label141;
                     }
                  }

                  MenuManager.instance.guiHandler.hideAllWindows();
               }

               if (checkResult[0] == "SUCCESS_FLOAT") {
                  GameCommons.instance.showFloatText(checkResult[1]);
               }

               return true;
            }
         } else {
            MenuSupport.instance.curActiveTownInd = this.delegateRealoadQuesterTownInd;
            MenuSupport.instance.questSuccessInfoMap = Com.currentGameMap.nameId;
            (new OtherMenuList()).showMenuMessage(195, checkResult[1]);
            return true;
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         GameCommons.instance.showFloatText("Произошла ошибка!");
         return false;
      }
   }

   public static void loadItemsSellerSoldData(NetRequest netRequest) {
      int integerCode = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
      MenuSupport.instance.itemsSellerSoldItems = new InventoryItem[integerCode];

      for(int itemsSellerSoldItemIndex = 0; itemsSellerSoldItemIndex < integerCode; ++itemsSellerSoldItemIndex) {
         InventoryItem inventoryItem2 = new InventoryItem();
         MenuSupport.instance.itemsSellerSoldItems[itemsSellerSoldItemIndex] = inventoryItem2;
         inventoryItem2.icon = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.name = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.description = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.readAdditional(netRequest.response);
         inventoryItem2.costGold = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.costCrys = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.count = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.unweight = decompiledsupport.UncheckedDataInput.readBoolean(netRequest.response);
      }

   }

   public static void loadItemsSellerItemsData(NetRequest netRequest) {
      int integerCode = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
      MenuSupport.instance.itemsSellerItems = new InventoryItem[integerCode];

      for(int itemsSellerItemIndex = 0; itemsSellerItemIndex < integerCode; ++itemsSellerItemIndex) {
         InventoryItem inventoryItem2 = new InventoryItem();
         MenuSupport.instance.itemsSellerItems[itemsSellerItemIndex] = inventoryItem2;
         inventoryItem2.icon = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.name = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.description = decompiledsupport.UncheckedDataInput.readUTF(netRequest.response);
         inventoryItem2.readAdditional(netRequest.response);
         inventoryItem2.count = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.costGold = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.costCrys = decompiledsupport.UncheckedDataInput.readInt(netRequest.response);
         inventoryItem2.unweight = decompiledsupport.UncheckedDataInput.readBoolean(netRequest.response);
      }

   }

   public static void actionMapObjectDirectPortalUse(int tInd) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4140520);
         netRequest.request.writeUTF(Com.currentGameMap.towns[tInd].GPFile);
         Com.sendRequest(netRequest);
         if ((tInd = netRequest.response.readInt()) == 4140521) {
            Strategist.instance.cx = (short)netRequest.response.readInt();
            Strategist.instance.cy = (short)netRequest.response.readInt();
            netRequest.response.readBoolean();
            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
            Com.cameraWorld.resetSmooth();
         } else if (tInd == 4140004) {
            brokeMapObjectsOrder();
         } else {
            GameCommons.instance.showFloatText("Произошла ошибка!");
         }
      } catch (Exception exception) {
         GameCommons.instance.showFloatText("Произошла ошибка!");
      }

      Com.PrBarTh.Stop();
   }

   public static boolean actionMapObjectMapPortalUse(int tInd) {
      boolean actionAllowed = true;
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4140420);
         netRequest.request.writeUTF(Com.currentGameMap.towns[tInd].GPFile);
         Com.sendRequest(netRequest);
         if ((tInd = netRequest.response.readInt()) == 4140421) {
            tInd = Strategist.instance.curMapInd;
            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
            if (tInd != Strategist.instance.curMapInd) {
               actionAllowed = false;
            }
         } else if (tInd == 4140004) {
            brokeMapObjectsOrder();
         } else {
            GameCommons.instance.showFloatText("Произошла ошибка!");
         }
      } catch (Exception exception) {
         GameCommons.instance.showFloatText("Произошла ошибка!");
      }

      if (actionAllowed) {
         Com.PrBarTh.Stop();
      }

      return actionAllowed;
   }

   public static void brokeMapObjectsOrder() {
      MenuManager.instance.guiHandler.hideAllWindows();
      ClientState.instance.set(2);
      GameCommons.instance.showFloatText((String)"Нарушена целостность", 4000);
      Com.makeNewGameExit();
   }

   public static void destroyMapObject2(int tInd) {
      Com.currentGameMap.towns[tInd].state = 1;
      TileMap currentGameMap2 = Com.currentGameMap;
      currentGameMap2.decorModifiNoBack(currentGameMap2.towns[tInd].cx, Com.currentGameMap.towns[tInd].cy, Com.currentGameMap.towns[tInd].RuinSpriteInd, Com.currentGameMap.towns[tInd].addingHeight);
      GameCommons.instance.redrawMapBuffer();
      GameEffects instance2 = GameEffects.instance;
      GameEffects.spawnClientEffect6(Com.currentGameMap.towns[tInd].dx, Com.currentGameMap.towns[tInd].dy + Com.currentGameMap.towns[tInd].addingHeight);
   }

   public final void executeDelegated(int context) {
      switch (context) {
         case 1:
            (new OtherMenuList()).showConfirmationWithPortrait(189, MenuSupport.instance.string7, false, (StrategGraphic)null, true, MenuSupport.instance.monsterSellerHead, MenuSupport.instance.monsterSellerBody, false, (int[][])null);
         default:
            return;
         case 2:
         case 4:
            this.actionMapObjectQuestGiverForDelegate(context == 4, false, false);
            return;
         case 3:
            this.analisMapObject(this.delegateAnalisMapObjectInd, true);
            return;
         case 5:
            if (MenuSupport.instance.curActiveTownInd >= 0 && MenuSupport.instance.curActiveTownInd < Com.currentGameMap.towns.length) {
               Com.currentGameMap.towns[MenuSupport.instance.curActiveTownInd].activedAtTime = Mystery.currentTimeMillis;
            }

            MenuManager.instance.guiHandler.hideAllWindows();
      }
   }
}
