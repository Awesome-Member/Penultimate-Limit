package MenuPck;

import Base.Com;
import Base.GameCommons;
import Engine.Delegateable;
import Engine.MathGame;
import Engine.Mystery;
import Moduls.StrategGraphic;
import java.util.Vector;

public class MenuList extends GUIHandlerEvents implements Delegateable {
   public static final Vector LIST_IGNORE = new Vector();
   private int curEnterState = 1;
   protected int curMenuId = -1;
   private int[] jointIds;
   private Vector _list;
   private Vector _skipList;
   private int _backId;
   protected Vector hints;
   private int curListArraySkipedCount = 0;
   protected String[] curListArray = new String[0];
   private String textEnterTitleText = null;
   private long clickHoldTimeTill = -1L;
   private boolean allowBackgroundClick = true;
   private String nextTitleText = null;
   private boolean nextTitleShowMob = false;
   private int nextTitleShowMobHead = -1;
   private int nextTitleShowMobBody = -1;
   private boolean nextTitleShowMobs = false;
   private int[][] nextTitleShowMobsUnits = null;
   private boolean nextTitleShowAvatar = false;
   private StrategGraphic nextTitleShowAvatarGraphic = null;
   private int curInnerInfoBackId = -2;

   public MenuList(int id, int backMenu, String[] list) {
      this.jointIds = new int[]{id};
      this._list = Mystery.vectorFromArray(list);
      this._backId = backMenu;
      this.showWorldMapMenu();
   }

   public MenuList(int id, int backMenu, String list) {
      this.jointIds = new int[]{id};
      Vector vector3 = Mystery.vectorFromString(list);
      this._backId = backMenu;
      this._list = vector3;
      this.showWorldMapMenu();
   }

   public MenuList(int[] ids, int backMenu, String list) {
      this.jointIds = ids;
      Vector vector3 = Mystery.vectorFromString(list);
      this._backId = -2;
      this._list = vector3;
      this.showWorldMapMenu();
   }

   private void showWorldMapMenu() {
      this._skipList = new Vector();
   }

   protected final int[] getJointIds() {
      return this.jointIds;
   }

   protected static void showFloatMessageS(String messageText) {
      GameCommons.instance.showFloatText((String)messageText, 3000);
   }

   protected static void showFloatMessageS2(String messageText) {
      GameCommons.instance.showFloatText((String)messageText, 3000);
   }

   protected final void clearSkips() {
      this._skipList.removeAllElements();
   }

   protected final void addSkip(int itemIndex) {
      for(int sizeIndex = 0; sizeIndex < this._skipList.size(); ++sizeIndex) {
         int skipList;
         if ((skipList = (Integer)this._skipList.elementAt(sizeIndex)) == itemIndex) {
            return;
         }

         if (skipList > itemIndex) {
            this._skipList.insertElementAt(itemIndex, sizeIndex);
            return;
         }
      }

      this._skipList.addElement(itemIndex);
   }

   public final void activateEl(int i) {
      switch (this.curEnterState) {
         case 1:
            this.onListClickEvent(this.curMenuId, i);
            return;
         case 2:
            this.onTextEnterEvent(this.curMenuId, true, String.valueOf(i));
            return;
         case 3:
            this.onNumberEnterEvent(this.curMenuId, i);
            return;
         case 4:
            this.onOKSelected();
            return;
         case 5:
            this.onYesNoEnterEvent(this.curMenuId, i == 0);
            return;
         case 6:
            this.handleMainMenuAction(this.curMenuId);
            return;
         case 7:
            this.onInventoryBackSelected2();
         default:
      }
   }

   public final int getCurrentMenuListsCount() {
      return this.curListArray.length + this.curListArraySkipedCount;
   }

   public static void hideMenuWindows() {
      MenuManager.instance.guiHandler.hideAllWindows();
   }

   public static void showUnitPortrait(int head, int body) {
      MenuManager.instance.guiHandler.showTopWindowDecorUnit(head, body);
   }

   public static void showUnitPortraits(int[][] units) {
      if (units != null) {
         MenuManager.instance.guiHandler.showTopWindowDecorUnits(units);
      }
   }

   public static void showAvatarPortrait(StrategGraphic strategGraphic2) {
      if (strategGraphic2 != null) {
         MenuManager.instance.guiHandler.showTopWindowDecorAvatar(strategGraphic2);
      }
   }

   public final void showWorldMapMenu(int menuIndex, String title, int[][] questsMarks) {
      this.curEnterState = 6;
      this.curMenuId = 96;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = title;
      MenuManager.instance.guiHandler.showBottomWindowWorldMap(Com.c_world_map_cur_pos_x, Com.c_world_map_cur_pos_y, questsMarks, this, this.curMenuId);
   }

   public final void showOKMenuWithBackMenu(int backMenuIndex, String hintText) {
      this.curInnerInfoBackId = backMenuIndex;
      this.showMenuMessage(1000000, hintText);
   }

   public final void showMenuMessage(int menuId, String messageText) {
      this.showMessageWithPortrait(menuId, messageText, false, (StrategGraphic)null, false, -1, -1, false, (int[][])null);
   }

   private void showMessageWithPortrait(int curMenuId2, String nextTitleText2, boolean unusedProgressMarker, StrategGraphic strategGraphic2, boolean unusedProgressMarker2, int mobHeadIndex, int mobBodyIndex, boolean unusedProgressMarker3, int[][] integersGrid) {
      this.curEnterState = 4;
      this.curMenuId = curMenuId2;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = nextTitleText2;
      this.nextTitleShowAvatar = false;
      this.nextTitleShowAvatarGraphic = null;
      this.nextTitleShowMob = false;
      this.nextTitleShowMobHead = -1;
      this.nextTitleShowMobBody = -1;
      this.nextTitleShowMobs = false;
      this.nextTitleShowMobsUnits = null;
      MenuManager.instance.guiHandler.showBottomWindowOK((GUIHandlerEvents)this, (Object)this.curMenuId);
   }

   public final void showMessageForMenu(int menuId, String nextTitleText2) {
      this.curEnterState = 4;
      this.curMenuId = 172;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = nextTitleText2;
      MenuManager.instance.guiHandler.showBottomWindowOK(this, true, this.curMenuId);
   }

   public final void showNumberEnter(int menuIndex, int curValue, int minValue, int maxValue, String topHintText) {
      this.curEnterState = 3;
      this.curMenuId = menuIndex;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = topHintText;
      MenuManager.instance.guiHandler.showBottomWindowNumberEnter(curValue, minValue, maxValue, "", this, this.curMenuId);
   }

   public final void showNumberEnter(int menuIndex, int minValue, int maxValue, String hintText) {
      this.showNumberEnter(menuIndex, 0, minValue, maxValue, hintText);
   }

   public final void showNumberEnter(int menuIndex, int minValue, int maxValue, String hintText, String topHintText) {
      this.curEnterState = 3;
      this.curMenuId = menuIndex;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = topHintText;
      MenuManager.instance.guiHandler.showBottomWindowNumberEnter(0, minValue, maxValue, hintText, this, this.curMenuId);
   }

   public final void showTextEnter(int menuIndex, String titleText, String defaultText) {
      this.showTextEnter(menuIndex, titleText, defaultText, 0);
   }

   public final void showTextEnter(int menuIndex, String titleText, String defaultText, int startKeyboard) {
      this.curEnterState = 2;
      this.curMenuId = menuIndex;
      MenuManager.instance.curMenuList = this;
      this.textEnterTitleText = titleText;
      MenuManager.instance.guiHandler.showBottomWindowTextEnter(defaultText, startKeyboard, this, this.curMenuId);
   }

   public final void showTextEnterSystem(int menuIndex, String titleText, String defaultText, int maxLen) {
      MenuManager instance2 = MenuManager.instance;
      MenuManager.showTextInput(titleText, defaultText, this.curMenuId, menuIndex, maxLen);
   }

   public final void showYesNoMenu(int menuIndex, String title, String specYes, String specNo) {
      this.curEnterState = 5;
      this.curMenuId = 234;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = title;
      MenuManager.instance.guiHandler.showBottomWindowYesNo(this, specYes, specNo, this.curMenuId);
   }

   public final void showConfirmationForMenu(int curMenuId2, String nextTitleText2) {
      this.curEnterState = 5;
      this.curMenuId = curMenuId2;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = nextTitleText2;
      MenuManager.instance.guiHandler.showBottomWindowYesNo((GUIHandlerEvents)this, (Object)this.curMenuId);
   }

   public final void showConfirmationWithPortrait(int curMenuId2, String nextTitleText2, boolean unusedProgressMarker, StrategGraphic strategGraphic2, boolean nextTitleShowMob2, int nextTitleShowMobHead2, int nextTitleShowMobBody2, boolean unusedProgressMarker2, int[][] integersGrid) {
      this.curEnterState = 5;
      this.curMenuId = curMenuId2;
      MenuManager.instance.curMenuList = this;
      this.showHeroMenu();
      this.nextTitleText = nextTitleText2;
      this.nextTitleShowAvatar = false;
      this.nextTitleShowAvatarGraphic = null;
      this.nextTitleShowMob = nextTitleShowMob2;
      this.nextTitleShowMobHead = nextTitleShowMobHead2;
      this.nextTitleShowMobBody = nextTitleShowMobBody2;
      this.nextTitleShowMobs = false;
      this.nextTitleShowMobsUnits = null;
      MenuManager.instance.guiHandler.showBottomWindowYesNo((GUIHandlerEvents)this, (Object)this.curMenuId);
   }

   public final void showInventoryMenu(int menuIndex, String[] slotsSymbols, int[] slotsNumbers, boolean justMid, StrategGraphic strategGraphic2) {
      Integer integer = (Integer)MenuSupport.instance.lastSelectedEls.get(menuIndex);
      int slot = 0;
      if (integer != null) {
         slot = integer;
      }

      this.curEnterState = 7;
      this.curMenuId = menuIndex;
      MenuManager.instance.curMenuList = this;
      MenuManager.instance.guiHandler.showBottomWindowInventory(slotsSymbols, slotsNumbers, slot, "ˊˋЧинить надетые", this.getRightSoftText(this.curMenuId), "ʒʓOK", justMid, strategGraphic2, this, this.curMenuId);
   }

   private void showHeroMenu() {
      this.nextTitleText = null;
      this.nextTitleShowAvatar = false;
      this.nextTitleShowMob = false;
      this.nextTitleShowMobs = false;
      this.nextTitleShowMobsUnits = null;
   }

   private void applyNextTitle() {
      if (this.nextTitleText != null) {
         MenuManager.instance.guiHandler.finishAnimationYUIComponent(this.nextTitleText);
      }

      if (this.nextTitleShowAvatar) {
         showAvatarPortrait(this.nextTitleShowAvatarGraphic);
      }

      if (this.nextTitleShowMob) {
         showUnitPortrait(this.nextTitleShowMobHead, this.nextTitleShowMobBody);
      }

      if (this.nextTitleShowMobs) {
         showUnitPortraits(this.nextTitleShowMobsUnits);
      }

   }

   public final MenuListSnapshot makeSnapshot() {
      return new MenuListSnapshot(MenuManager.instance.guiHandler.makeSnapshot(), this, this.curEnterState, this.curMenuId, this._skipList, this._backId, this.hints, this.curListArray, this.curListArraySkipedCount, this.textEnterTitleText, this.clickHoldTimeTill, this.allowBackgroundClick, this.nextTitleText, this.nextTitleShowMob, this.nextTitleShowMobHead, this.nextTitleShowMobBody, this.nextTitleShowMobs, this.nextTitleShowMobsUnits, this.nextTitleShowAvatar, this.nextTitleShowAvatarGraphic, this.curInnerInfoBackId);
   }

   protected final void restoreSnapshot(MenuListSnapshot menuListSnapshot) {
      this.curEnterState = menuListSnapshot.curEnterState;
      this.curMenuId = menuListSnapshot.curMenuId;
      this._skipList = menuListSnapshot._skipList;
      this._backId = menuListSnapshot._backId;
      this.hints = menuListSnapshot.hints;
      this.curListArraySkipedCount = menuListSnapshot.curListArraySkipedCount;
      this.curListArray = menuListSnapshot.curListArray;
      this.textEnterTitleText = menuListSnapshot.textEnterTitleText;
      this.clickHoldTimeTill = menuListSnapshot.clickHoldTimeTill;
      this.allowBackgroundClick = menuListSnapshot.allowBackgroundClick;
      this.nextTitleText = menuListSnapshot.nextTitleText;
      this.nextTitleShowMob = menuListSnapshot.nextTitleShowMob;
      this.nextTitleShowMobHead = menuListSnapshot.nextTitleShowMobHead;
      this.nextTitleShowMobBody = menuListSnapshot.nextTitleShowMobBody;
      this.nextTitleShowMobs = menuListSnapshot.nextTitleShowMobs;
      this.nextTitleShowMobsUnits = menuListSnapshot.nextTitleShowMobsUnits;
      this.nextTitleShowAvatar = menuListSnapshot.nextTitleShowAvatar;
      this.nextTitleShowAvatarGraphic = menuListSnapshot.nextTitleShowAvatarGraphic;
      this.curInnerInfoBackId = menuListSnapshot.curInnerInfoBackId;
      MenuManager.instance.curMenuList = menuListSnapshot.menuList;
      MenuManager.instance.guiHandler.restoreSnapshot(menuListSnapshot.guiHandlerSnapshot);
   }

   protected final void showListMenuInner(int id, boolean withDataForce) {
      this.curEnterState = 1;
      this.curMenuId = id;
      MenuManager.instance.curMenuList = this;
      this._skipList.removeAllElements();
      Vector vector3;
      if ((vector3 = this.onGenerateItemsAndSkipsEvent(id)) != LIST_IGNORE) {
         if (vector3 != null && vector3.size() - this._skipList.size() > 0) {
            int curListArraySkipedCount2 = 0;

            int listIndex;
            for(listIndex = vector3.size() - 1; listIndex >= 0; --listIndex) {
               if (this._skipList.contains(listIndex)) {
                  vector3.removeElementAt(listIndex);
                  ++curListArraySkipedCount2;
               }
            }

            String[] list = new String[vector3.size()];
            this.curListArray = new String[vector3.size()];

            for(listIndex = 0; listIndex < vector3.size(); ++listIndex) {
               list[listIndex] = vector3.elementAt(listIndex).toString();
               this.curListArray[listIndex] = vector3.elementAt(listIndex).toString();
            }

            this.curListArraySkipedCount = curListArraySkipedCount2;
            int[] ids = new int[list.length];

            for(listIndex = 0; listIndex < ids.length; ids[listIndex] = listIndex++) {
            }

            String rightSoft = this.getRightSoftText(id);
            Integer integer = (Integer)MenuSupport.instance.lastSelectedEls.get(id);
            if (withDataForce) {
               if (!MenuManager.instance.guiHandler.forceDataBottomWindowTextList(list, ids, integer == null ? -1 : integer)) {
                  MenuManager.instance.guiHandler.showBottomWindowTextList(list, ids, rightSoft, integer == null ? -1 : integer, this, this.curMenuId);
                  return;
               }
            } else {
               MenuManager.instance.guiHandler.showBottomWindowTextList(list, ids, rightSoft, integer == null ? -1 : integer, this, this.curMenuId);
            }

         } else {
            this.curInnerInfoBackId = this.onGenerateBackIdEvent(id);
            this.showMenuMessage(1000000, "Список пуст");
         }
      }
   }

   protected final int getLastSelectedListEl() {
      Integer integer;
      return (integer = (Integer)MenuSupport.instance.lastSelectedEls.get(this.curMenuId)) == null ? -1 : integer;
   }

   protected final void setLastSelectedListElForced(int selectedIndex) {
      setLastSelectedListElForced(this.curMenuId, selectedIndex);
   }

   public static void setLastSelectedListElForced(int menuInd, int selectedIndex) {
      MenuSupport.instance.lastSelectedEls.put(menuInd, selectedIndex);
   }

   private String getRightSoftText(int id) {
      String rightSoft = "";
      if ((id = this.onGenerateBackIdEvent(id)) == -2) {
         rightSoft = "В игру";
      }

      if (id >= 0) {
         rightSoft = "Назад";
      }

      return rightSoft;
   }

   private int processItemIndexBySkips(int itemIndex) {
      for(int i = 0; i < this._skipList.size(); ++i) {
         if (itemIndex >= (Integer)this._skipList.elementAt(i)) {
            ++itemIndex;
         }
      }

      return itemIndex;
   }

   public static void setAnimatedTitle(String messageText) {
      MenuManager.instance.guiHandler.finishAnimationYUIComponent(messageText);
   }

   public static void hideAvatarPortrait() {
      MenuManager.instance.guiHandler.hideTopWindowDecorAvatar();
   }

   protected void goBack() {
      int back;
      if ((back = this.onGenerateBackIdEvent(this.curMenuId)) == -2) {
         MenuManager.instance.guiHandler.hideAllWindows();
      } else {
         if (back >= 0) {
            MenuManager.instance.showMenuById(back);
         }

      }
   }

   protected Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = new Vector();

      for(int i = 0; i < this._list.size(); ++i) {
         vector3.addElement(this._list.elementAt(i));
      }

      return vector3;
   }

   protected void onGenerateHintEvent(int menuId, int itemIndex) {
      if (this.hints != null && itemIndex < this.hints.size()) {
         setAnimatedTitle(this.hints.elementAt(itemIndex).toString());
      }

   }

   protected int onGenerateBackIdEvent(int menuId) {
      return this._backId;
   }

   protected void onOKMenuEvent(int backIndex) {
   }

   protected void onNumberEnterEvent(int backIndex, int inputNumber) {
   }

   protected void onNumberCancelEvent(int backIndex) {
   }

   protected void onNumberChangedEvent(int backIndex, int inputNumber) {
   }

   protected void onListClickEvent(int menuId, int itemIndex) {
   }

   protected void onYesNoEnterEvent(int backIndex, boolean res) {
   }

   protected void onTextEnterEvent(int backIndex, boolean res, String messageText) {
   }

   protected void handleMainMenuAction(int actionIndex) {
   }

   protected void onInventorySelectEvent(int backIndex, int slotIndex) {
   }

   protected void onInventoryChangedEvent(int backIndex, int slotIndex) {
   }

   protected void onInventoryFixEvent(int backIndex) {
   }

   protected void onCloseMenuEvent(int backIndex) {
   }

   protected void onFunctionate(int menuId) {
   }

   public final void functionate() {
      this.onFunctionate(this.curMenuId);
   }

   public final void onDisableMenu(Object contextForCloseableMenu, Object contextForWillMenu) {
      super.onDisableMenu(contextForCloseableMenu, contextForWillMenu);
      int closingMenuId = contextForCloseableMenu != null && contextForCloseableMenu instanceof Integer ? (Integer)contextForCloseableMenu : -1;
      int nextMenuId = contextForWillMenu != null && contextForWillMenu instanceof Integer ? (Integer)contextForWillMenu : -1;
      if (closingMenuId == nextMenuId) {
         closingMenuId = -1;
      }

      this.onCloseMenuEvent(closingMenuId);
   }

   public final void onTextListChanged(int textId) {
      MenuSupport.instance.lastSelectedEls.put(this.curMenuId, textId);
      MenuManager.instance.guiHandler.hideTopWindow();
      this.onGenerateHintEvent(this.curMenuId, this.processItemIndexBySkips(textId));
   }

   public final void onTextListSelected(int textId) {
      if (!this.checkClickHold()) {
         try {
            this.onListClickEvent(this.curMenuId, this.processItemIndexBySkips(textId));
         } catch (Exception exception) {
            Com.SendMistace("Error in menu select list", true, true, exception);
            GameCommons.instance.showFloatText((String)"Произошла ошибка!", 3000);
         }
      }
   }

   public final void onInventoryBackSelected3() {
      if (!this.checkClickHold()) {
         this.goBack();
      }
   }

   public final void onNumberCancel() {
      if (!this.checkClickHold()) {
         this.onNumberCancelEvent(this.curMenuId);
      }
   }

   public final void onNumberChanged(int inputNumber) {
      this.applyNextTitle();
      this.onNumberChangedEvent(this.curMenuId, inputNumber);
   }

   public final void onNumberSelected(int inputNumber) {
      if (!this.checkClickHold()) {
         this.onNumberEnterEvent(this.curMenuId, inputNumber);
      }
   }

   public void onTextChanged(String messageText) {
      MenuManager.instance.guiHandler.finishAnimationYUIComponent(messageText.length() == 0 && this.textEnterTitleText != null ? this.textEnterTitleText : messageText);
   }

   public final void onTextSelected(String messageText) {
      if (!this.checkClickHold()) {
         try {
            this.onTextEnterEvent(this.curMenuId, true, messageText);
         } catch (Exception exception) {
            Com.SendMistace("Error in menu select text", true, false, exception);
            GameCommons.instance.showFloatText((String)"Произошла ошибка!", 3000);
         }
      }
   }

   public final void onTextDeclane() {
      if (!this.checkClickHold()) {
         try {
            this.onTextEnterEvent(this.curMenuId, false, "");
         } catch (Exception exception) {
            Com.SendMistace("Error in menu declane text", true, false, exception);
            GameCommons.instance.showFloatText((String)"Произошла ошибка!", 3000);
         }
      }
   }

   public final void onYesNoSelected(boolean confirmed, boolean byBackgroundClick) {
      if (!this.checkClickHold()) {
         if (!byBackgroundClick || this.allowBackgroundClick) {
            try {
               this.onYesNoEnterEvent(this.curMenuId, confirmed);
            } catch (Exception exception) {
               Com.SendMistace("Error in menu select yes no", true, false, exception);
               GameCommons.instance.showFloatText((String)"Произошла ошибка!", 3000);
            }
         }
      }
   }

   public final void onYesNoChanged() {
      this.applyNextTitle();
   }

   public final void onOKSelected() {
      if (!this.checkClickHold()) {
         switch (this.curMenuId) {
            case 1000000:
               if (this.curInnerInfoBackId >= 0) {
                  int curInnerInfoBackId2 = this.curInnerInfoBackId;
                  MenuManager.instance.showMenuById(curInnerInfoBackId2);
                  return;
               }

               hideMenuWindows();
               return;
            default:
               this.onOKMenuEvent(this.curMenuId);
         }
      }
   }

   public final void onOKChanged() {
      this.applyNextTitle();
   }

   public final void onMenuContentChanged() {
      this.applyNextTitle();
   }

   public final void onInventoryBackSelected() {
      if (!this.checkClickHold()) {
         this.handleMainMenuAction(this.curMenuId);
      }
   }

   public final void onInventoryChanged(int slotInd) {
      MenuSupport.instance.lastSelectedEls.put(this.curMenuId, slotInd);
      MenuManager.instance.guiHandler.hideTopWindow();
      this.onInventoryChangedEvent(this.curMenuId, slotInd);
   }

   public final void onInventorySelected(int slotInd) {
      if (!this.checkClickHold()) {
         this.onInventorySelectEvent(this.curMenuId, slotInd);
      }
   }

   public final void onInventoryBackSelected2() {
      if (!this.checkClickHold()) {
         this.goBack();
      }
   }

   public final void onInventoryFixSelected() {
      if (!this.checkClickHold()) {
         this.onInventoryFixEvent(this.curMenuId);
      }
   }

   public final void disableBackgroundClick() {
      this.allowBackgroundClick = false;
   }

   public final void ignoreClickDeltaTime(long time) {
      this.clickHoldTimeTill = System.currentTimeMillis() + time;
   }

   private boolean checkClickHold() {
      if (this.clickHoldTimeTill < 0L) {
         return false;
      } else if (System.currentTimeMillis() >= this.clickHoldTimeTill) {
         this.clickHoldTimeTill = -1L;
         return false;
      } else {
         return true;
      }
   }

   public final String getSoftPrefix() {
      this.checkClickHold();
      return this.clickHoldTimeTill <= 0L ? "" : "(" + MathGame.ceilDivide(this.clickHoldTimeTill - System.currentTimeMillis(), 1000L) + ")";
   }

   public void executeDelegated(int context) {
   }

   public final int getCurMenuId() {
      return this.curMenuId;
   }
}
