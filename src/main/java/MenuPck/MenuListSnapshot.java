package MenuPck;

import Engine.Mystery;
import Moduls.StrategGraphic;
import java.util.Vector;

public final class MenuListSnapshot {
   public GUIHandlerSnapshot guiHandlerSnapshot;
   public MenuList menuList;
   public int curEnterState;
   public int curMenuId;
   public Vector _skipList;
   public int _backId;
   public Vector hints;
   public int curListArraySkipedCount = 0;
   public String[] curListArray = new String[0];
   public String textEnterTitleText = null;
   public long clickHoldTimeTill = -1L;
   public boolean allowBackgroundClick = true;
   public String nextTitleText = null;
   public boolean nextTitleShowMob = false;
   public int nextTitleShowMobHead = -1;
   public int nextTitleShowMobBody = -1;
   public boolean nextTitleShowMobs = false;
   public int[][] nextTitleShowMobsUnits = null;
   public boolean nextTitleShowAvatar = false;
   public StrategGraphic nextTitleShowAvatarGraphic = null;
   public int curInnerInfoBackId = -2;

   public MenuListSnapshot(GUIHandlerSnapshot gUIHandlerSnapshot, MenuList menuList2, int curEnterState2, int curMenuId2, Vector vector3, int _backId2, Vector vector4, String[] curListArray2, int curListArraySkipedCount2, String textEnterTitleText2, long clickHoldTimeTill2, boolean allowBackgroundClick2, String nextTitleText2, boolean nextTitleShowMob2, int nextTitleShowMobHead2, int nextTitleShowMobBody2, boolean nextTitleShowMobs2, int[][] nextTitleShowMobsUnits2, boolean nextTitleShowAvatar2, StrategGraphic strategGraphic2, int curInnerInfoBackId2) {
      this.guiHandlerSnapshot = gUIHandlerSnapshot;
      this.menuList = menuList2;
      this.curEnterState = curEnterState2;
      this.curMenuId = curMenuId2;
      this._skipList = Mystery.cloneVector(vector3);
      this._backId = _backId2;
      this.hints = Mystery.cloneVector(vector4);
      this.curListArray = Mystery.cloneArray(curListArray2);
      this.curListArraySkipedCount = curListArraySkipedCount2;
      this.textEnterTitleText = textEnterTitleText2;
      this.clickHoldTimeTill = clickHoldTimeTill2;
      this.allowBackgroundClick = allowBackgroundClick2;
      this.nextTitleText = nextTitleText2;
      this.nextTitleShowMob = nextTitleShowMob2;
      this.nextTitleShowMobHead = nextTitleShowMobHead2;
      this.nextTitleShowMobBody = nextTitleShowMobBody2;
      this.nextTitleShowMobs = nextTitleShowMobs2;
      this.nextTitleShowMobsUnits = nextTitleShowMobsUnits2;
      this.nextTitleShowAvatar = nextTitleShowAvatar2;
      this.nextTitleShowAvatarGraphic = strategGraphic2;
      this.curInnerInfoBackId = curInnerInfoBackId2;
   }

   public final void restore() {
      this.menuList.restoreSnapshot(this);
   }
}
