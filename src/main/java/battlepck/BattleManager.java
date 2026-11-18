package battlepck;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.ClientState;
import Base.Com;
import Base.GameCommons;
import MenuPck.MenuManager;
import MenuPck.ListTypes.BattleMenuList;
import Moduls.Strategist;
import Moduls.BattleInfo;
import battlepck.client.BattleTh;
import java.io.IOException;
import java.util.Vector;

public final class BattleManager {
   private static final Object globalLock = new Object();
   private static Battle currentBattle = null;
   private static Battle replaceBattle = null;

   public static void setReplaceBattle(Battle battle2) {
      synchronized(globalLock) {
         replaceBattle = battle2;
      }
   }

   public static void addEventsWithSpeedCorrectionToActualBattle(Vector vector3) {
      Battle battle2;
      synchronized(globalLock) {
         battle2 = replaceBattle != null ? replaceBattle : currentBattle;
      }

      battle2.addEventsWithSpeedCorrection(vector3);
   }

   public static void addEventsWithoutSpeedCorrectionToActualBattle(Vector vector3) {
      Battle battle2;
      synchronized(globalLock) {
         battle2 = replaceBattle != null ? replaceBattle : currentBattle;
      }

      battle2.addEventsWithoutSpeedCorrection(vector3);
   }

   public static void addBattleFinishEventToActualBattle() {
      Battle battle2;
      synchronized(globalLock) {
         battle2 = replaceBattle != null ? replaceBattle : currentBattle;
      }

      if (battle2 != null) {
         Vector vector3;
         (vector3 = new Vector()).addElement((Object)null);
         battle2.addEventsWithoutSpeedCorrection(vector3);
      }
   }

   public static void processReplaceBattle() {
      synchronized(globalLock) {
         if (replaceBattle != null) {
            long lastTickTime2 = currentBattle.lastTickTime;
            replaceBattle.lastTickTime = lastTickTime2;
            currentBattle = replaceBattle;
            replaceBattle = null;
         }
      }
   }

   public static Battle getCurrentBattle() {
      synchronized(globalLock) {
         return currentBattle;
      }
   }

   public static void setCurrentBattle(Battle battle2) {
      synchronized(globalLock) {
         currentBattle = battle2;
         replaceBattle = null;
      }
   }

   public static BattleUnit getCurrentBattlePlayerAvatar() {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? null : battle2.getPlayerAvatar();
   }

   public static BattleMember getCurrentBattlePlayerMember() {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? null : battle2.getPlayerMember();
   }

   public static BattleMemberSmall getCurrentBattlePlayerMemberSmall() {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? null : battle2.getPlayerMemberSmall();
   }

   public static BattleUnit getCurrentBattleUnit(int unitInd) {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? null : battle2.getUnit(unitInd);
   }

   public static BattleUnit[] getCurrentBattleUnits() {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? new BattleUnit[0] : battle2.getUnitsArray();
   }

   public static BattleUnit[] getPlayerUnits() {
      Battle battle2;
      return (battle2 = getCurrentBattle()) == null ? new BattleUnit[0] : battle2.getPlaterUnitsArray();
   }

   public static void startWatchBattle(String logId) {
      Com.PrBarTh.Set(true);
      ClientState.instance.set(10);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4620000);
         netRequest.request.writeUTF(logId);
         Com.sendRequest(netRequest);
         int PacketID;
         if ((PacketID = netRequest.response.readInt()) == 4620001) {
            netRequest.response.readInt();
            BattleTh.viewBattleDIS = netRequest.response;
            GameCommons.instance.stopTimer();
            MenuManager.instance.guiHandler.hideAllWindows();
            BattleTh.startBattle(netRequest, true);
            Com.PrBarTh.Stop();
            return;
         }

         if (PacketID == 4620003) {
            GameCommons.instance.showFloatText(netRequest.response.readUTF());
         } else {
            GameCommons.instance.showFloatText((String)"Запись боя отсутствует", 5000);
         }
      } catch (IOException exception) {
         exception.printStackTrace();
      }

      ClientState.instance.set(4);
      Com.PrBarTh.Stop();
   }

   public static void createFightWithPlayer(int playerId) {
      Com.PrBarTh.Set(true);

      try {
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1392000);
         netRequest.request.writeInt(playerId);
         netRequest.request.writeInt(Strategist.instance.cx);
         netRequest.request.writeInt(Strategist.instance.cy);
         Com.sendRequest(netRequest);
         switch (netRequest.response.readInt()) {
            case 1392001:
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               return;
            case 1392002:
            case 1392007:
            default:
               return;
            case 1392003:
               GameCommons.instance.showFloatText((String)"Автоотказ от боя!", 3000);
               return;
            case 1392004:
               GameCommons.instance.showFloatText((String)"Мирная зона", 3000);
               return;
            case 1392005:
               GameCommons.instance.showFloatText((String)"Игрок занят", 3000);
               return;
            case 1392006:
               Com.PrBarTh.Stop();
               GameCommons.instance.showFloatText((String)"Нарушена целостность", 4000);
               Com.makeNewGameExit();
               ClientState.instance.set(2);
               return;
            case 1392008:
               GameCommons.instance.showFloatText("Отправлен запрос\nна дуэль");
               return;
            case 1392009:
               GameCommons.instance.showFloatText("Запрос на дуэль\nуже отправлен");
               return;
         }
      } catch (Exception exception) {
         Com.SendMistace("Error in createFightWithPlayer", true, true, exception);
      } finally {
         Com.PrBarTh.Stop();
      }

   }

   public static void createFightWithMob(int ind) {
      Com.PrBarTh.Set(true);

      try {
         short cx2 = Com.EnsMap.Enemys[ind].cx;
         short cy2 = Com.EnsMap.Enemys[ind].cy;
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1391000);
         netRequest.request.writeInt(ind);
         netRequest.request.writeInt(cx2);
         netRequest.request.writeInt(cy2);
         netRequest.request.writeInt(Strategist.instance.cx);
         netRequest.request.writeInt(Strategist.instance.cy);
         Com.sendRequest(netRequest);
         switch (netRequest.response.readInt()) {
            case 1391001:
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               break;
            case 1391006:
               Com.PrBarTh.Stop();
               GameCommons.instance.showFloatText((String)"Нарушена целостность", 4000);
               Com.makeNewGameExit();
               ClientState.instance.set(2);
               return;
         }
      } catch (Exception exception) {
         Com.SendMistace("Error in createFightWithMob", true, true, exception);
         return;
      } finally {
         Com.PrBarTh.Stop();
      }

   }

   public static void createFightJoinToPlayer(int otherPlayerId) {
      Com.PrBarTh.Set(true);

      try {
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1321000);
         netRequest.request.writeInt(otherPlayerId);
         netRequest.request.writeInt(Strategist.instance.cx);
         netRequest.request.writeInt(Strategist.instance.cy);
         Com.sendRequest(netRequest);
         switch (netRequest.response.readInt()) {
            case 1321001:
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               return;
            case 1321006:
               Com.PrBarTh.Stop();
               GameCommons.instance.showFloatText((String)"Нарушена целостность", 4000);
               Com.makeNewGameExit();
               ClientState.instance.set(2);
               return;
            default:
               return;
         }
      } catch (Exception exception) {
         Com.SendMistace("Error in createFightJoinToPlayer", true, true, exception);
      } finally {
         Com.PrBarTh.Stop();
      }

   }

   public static void appearBattle() {
      Com.PrBarTh.Set(true);
      ClientState.instance.set(7);
      NetRequest netRequest = new NetRequest();
      GameCommons.instance.stopTimer();

      try {
         netRequest.request.writeInt(1390000);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 1390001) {
            MenuManager.instance.guiHandler.hideAllWindows();
            Com.cameraWorld.resetSmooth();
            BattleTh.CurBattleInf.LoadFromStream(netRequest.response);
            BattleTh.startBattle(netRequest, false);
            Com.PrBarTh.Stop();
            return;
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      ClientState.instance.revert();
      Com.PrBarTh.Stop();
   }

   public static void showBattleInfoByPlayer(int otherPlayerId) {
      Com.PrBarTh.Set(true);

      try {
         NetRequest netRequest;
         (netRequest = new NetRequest()).writeHeader(1323000);
         netRequest.request.writeInt(otherPlayerId);
         Com.sendRequest(netRequest);
         if ((Com.PacketID = netRequest.response.readInt()) == 1323001) {
            BattleInfo battleInfo;
            (battleInfo = new BattleInfo()).LoadFromStream(netRequest.response);
            String hint = "Сражаться?";
            hint = hint + "\nПротивник:\n" + battleInfo.atackInfo;
            BattleMenuList battleMenuList;
            (battleMenuList = new BattleMenuList(246, -1, "")).isJoinBattlePlayerId = otherPlayerId;
            battleMenuList.isJoinBattleInfo = battleInfo;
            battleMenuList.showConfirmationForMenu(246, hint);
         } else {
            GameCommons.instance.showFloatText((String)"Состояние изменилось\nпопробуйте еще раз", 4000);
         }

         return;
      } catch (Exception exception) {
         exception.printStackTrace();
      } finally {
         Com.PrBarTh.Stop();
      }

   }
   private BattleManager() {
   }

}
