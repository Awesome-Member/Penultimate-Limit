package MenuPck.ListTypes;

import Base.Com;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import Moduls.BattleInfo;
import battlepck.BattleManager;

public final class BattleMenuList extends MenuList {
   public int isJoinBattlePlayerId = -1;
   public BattleInfo isJoinBattleInfo = null;

   public BattleMenuList(int id, int backMenu, String list) {
      super(246, -1, (String)list);
   }

   protected final void onCloseMenuEvent(int backIndex) {
      switch (backIndex) {
         case 6:
            Com.GameTh.gameControllAnalogData.lastActivateOtherPlayers = Mystery.currentTimeMillis;
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      switch (backIndex) {
         case 246:
            if (res) {
               switch (Com.touchControlType) {
                  case 4:
                     Com.GameTh.gameControllDirectData.activateJoinToPlayer(this.isJoinBattlePlayerId);
                     break;
                  default:
                     BattleManager.createFightJoinToPlayer(this.isJoinBattlePlayerId);
               }
            }

            MenuManager.instance.guiHandler.hideAllWindows();
         default:
      }
   }

   protected final void onOKMenuEvent(int backIndex) {
      switch (backIndex) {
         case 6:
            try {
               this.onYesNoEnterEvent(backIndex, true);
               return;
            } catch (Exception exception) {
               Com.SendMistace("Error on OK to battle query", true, true, exception);
            }
         default:
      }
   }
}
