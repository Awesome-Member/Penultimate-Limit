package bridge.mixin;

import Base.Game;
import Base.Com;
import Base.NetRequest;
import Moduls.TileMap;
import Moduls.MapBuffer;
import Moduls.Tile;
import Moduls.TileMapCell;
import Moduls.MoveableUnitDiscretShort;
import Moduls.DrawOrderElementStrateg;
import Moduls.DrawOrderElementOtherStrateg;
import Moduls.PathfindingGridBlockable;
import Engine.Camera;
import applet.Main;
import bridge.ui.ClickGui;
import javax.swing.SwingUtilities;
import bridge.movement.Speed;
import bridge.movement.NoClip;
import bridge.movement.NoStuck;
import bridge.movement.QuestFollower;
import MenuPck.UIComponent;
import bridge.player.Invisibility;
import bridge.visuals.ESP;
import bridge.visuals.BlockText;
import bridge.visuals.ShowStats;
import bridge.visuals.HideStatues;
import bridge.visuals.Zoom;
import bridge.visuals.CellTrail;
import bridge.visuals.SkinChanger;
import bridge.visuals.MapBuilder;
import Moduls.StrategGraphic;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import bridge.player.Reach;
import bridge.player.AutoBattle;
import battlepck.client.BattleTh;
import bridge.other.ObjectInteract;
import bridge.other.AutoCollect;
import bridge.other.AutoDungeon;
import bridge.other.AutoJuice;
import bridge.other.AutoGatherRoute;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestsOnMap;
import bridge.other.MobGather;
import Base.GameControllDirectData;
import Base.GameMapObjectsController;
import MenuPck.MenuList;
import MenuPck.MenuSupport;
import Base.MainTh;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import desktop.graphics.GameGraphics;
import java.awt.Graphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Game.class, remap = false)
public abstract class GameMixin {

    @Inject(method = "functionateUnderDrawProgress()V",
            at = @At(value = "INVOKE", target = "LModuls/quests/QuestStrateg;processUpdateKillPlayers(LModuls/Strategist;)Z", shift = At.Shift.AFTER),
            remap = false, require = 1)
    private void entropy$followQuestMarker(CallbackInfo callback) {
        QuestFollower.INSTANCE.update((Game)(Object)this);
    }

    @Inject(method = "clickUIComponent(LMenuPck/UIComponent;II)V", at = @At("HEAD"), remap = false, require = 1)
    private void entropy$stopQuestOnManualClick(UIComponent component, int x, int y, CallbackInfo callback) {
        if (component.id != 10017) return;
        Game game = (Game)(Object)this;
        if (QuestFollower.INSTANCE.enabled) QuestFollower.INSTANCE.stop(game);
        if (AutoJuice.INSTANCE.enabled) AutoJuice.INSTANCE.stop(game, "Stopped by map click.");
        for (AutoGatherRoute route : AutoGatherRoute.routes()) {
            if (route.enabled) route.stop(game);
        }
    }

    @Inject(method = "paint(Ldesktop/graphics/GameGraphics;Z)V",
            at = @At(value = "INVOKE", target = "LBase/GameCommons;paintInScreenCoord(Ldesktop/graphics/GameGraphics;LEngine/Camera;LModuls/TileMap;)V"),
            remap = false, require = 1)
    private void entropy$prepareJuiceMapForPaint(GameGraphics graphics, boolean justPaint, CallbackInfo callback) {
        AutoJuice.INSTANCE.prepareMapForPaint();
    }

    @Inject(method = "paint(Ldesktop/graphics/GameGraphics;Z)V",
            at = @At(value = "INVOKE", target = "LBase/GameCommons;paintInScreenCoord(Ldesktop/graphics/GameGraphics;LEngine/Camera;LModuls/TileMap;)V", shift = At.Shift.AFTER),
            remap = false, require = 1)
    private void entropy$drawCellTrail(GameGraphics graphics, boolean justPaint, CallbackInfo callback) {
        CellTrail.INSTANCE.draw(graphics);
    }

    @Redirect(method = "paint(Ldesktop/graphics/GameGraphics;Z)V",
            at = @At(value = "INVOKE", target = "LEngine/Camera;isActiveLongStay()Z"),
            remap = false, require = 1)
    private boolean entropy$keepMapTextVisible(Camera camera) {
        if (!BlockText.INSTANCE.enabled)return camera.isActiveLongStay();
        TileMap map=Com.currentGameMap;
        if(map!=null && map.widthTl>0 && map.heightTl>0 && map.cells!=null)map.initVisibleTexts(camera);
        return true;
    }

    @Inject(method = "paint(Ldesktop/graphics/GameGraphics;Z)V",
            at = @At(value = "INVOKE", target = "LMenuPck/UIConteiner;paint(Ldesktop/graphics/GameGraphics;)V"),
            remap = false, require = 1)
    private void entropy$drawESP(GameGraphics graphics, boolean justPaint, CallbackInfo callback) {
        ESP.INSTANCE.draw(graphics);
        ShowStats.INSTANCE.draw(graphics);
    }

    @Redirect(method = "functionateUnderDrawProgress()V",  at = @At(value = "INVOKE", target = "LBase/Com;sendWhatInBlock()V"),  remap = false, require = 1)
    private void entropy$skipStuckReport() {
        if (!NoStuck.INSTANCE.enabled)Com.sendWhatInBlock();
    }

    @Redirect(method = "functionate()V",
            at = @At(value = "INVOKE", target = "LBase/Com;startGameMap(Z)V"),
            remap = false, require = 1)
    private void entropy$measureJuiceMapLoad(boolean justLoading) {
        AutoJuice.INSTANCE.loadMap(justLoading);
    }

    @Inject(method = "functionate()V", at = @At("HEAD"), remap = false, require = 1)
    private void entropy$restoreStatuesBeforeMapUpdate(CallbackInfo callback) {
        HideStatues.INSTANCE.restore();
    }

    @Inject(method = "functionate()V", at = @At("RETURN"), remap = false, require = 1)
    private void entropy$updateModules(CallbackInfo callback) {
        MapBuilder.INSTANCE.update();
        NoClip.INSTANCE.update();
        Speed.INSTANCE.update();
        MobGather.INSTANCE.update((Game)(Object)this);
        AutoDungeon.INSTANCE.update((Game)(Object)this);
        Invisibility.INSTANCE.update();
        ShowStats.INSTANCE.update();
        Reach.INSTANCE.update((Game)(Object)this);
        ObjectInteract.INSTANCE.update((Game)(Object)this);
        AutoCollect.INSTANCE.update((Game)(Object)this);
        AutoJuice.INSTANCE.update((Game)(Object)this);
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.update((Game)(Object)this);
        HideStatues.INSTANCE.update();
    }
}

@Mixin(value = Com.class, remap = false)
abstract class JuiceMapBufferMixin {
    @Shadow
    private static NetRequest smallReqWithMap(int id, int part, boolean saveBytes) {
        throw new AssertionError();
    }

    @Redirect(method = {"loadMapStartData", "loadMapPart"},
            at = @At(value = "INVOKE", target = "LBase/Com;smallReqWithMap(IIZ)LBase/NetRequest;"),
            remap = false, require = 4)
    private static NetRequest entropy$measureMapRequest(int id, int part, boolean saveBytes) {
        AutoJuice juice = AutoJuice.INSTANCE;
        long started = juice.beginMapRequestTiming();
        try {
            return smallReqWithMap(id, part, saveBytes);
        } finally {
            juice.endMapRequestTiming(started);
        }
    }

    @Redirect(method = {"loadMapStartData", "loadMapPart"},
            at = @At(value = "INVOKE", target = "LBase/Black;readMapCacheRecord(Ljava/lang/String;I)[B"),
            remap = false, require = 6)
    private static byte[] entropy$reuseMapCacheRecord(String store, int index) {
        return AutoJuice.INSTANCE.readMapRecord(store, index);
    }

    @Redirect(method = "startGameMap(Z)V",
            at = @At(value = "INVOKE", target = "LModuls/MapBuffer;TryRedraw(LEngine/Camera;LModuls/TileMap;[LModuls/Tile;)V"),
            remap = false, require = 1)
    private static void entropy$deferJuiceMapBuffer(MapBuffer buffer, Camera camera, TileMap map, Tile[] tiles) {
        AutoJuice.INSTANCE.prepareMapBuffer(buffer, camera, map, tiles);
    }
}

@Mixin(value = StrategGraphic.class, remap = false)
abstract class SkinChangerMixin {
    @ModifyVariable(method = "draw(Ldesktop/graphics/GameGraphics;[SBIIBBJZSZ)V",
            at = @At("HEAD"), argsOnly = true, ordinal = 0, remap = false, require = 1)
    private static short[] entropy$replaceAppearance(short[] original) {
        return SkinChanger.INSTANCE.appearance(original);
    }
}

@Mixin(value = GameControllDirectData.class, remap = false)
abstract class ObjectApproachMixin {
    @Inject(method = "buildPath(SS)V", at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void entropy$preventAttackApproach(short x, short y, CallbackInfo callback) {
        GameControllDirectData controls = (GameControllDirectData)(Object)this;
        if (ObjectInteract.INSTANCE.blocksApproach(controls)) {
            controls.pathFolower.disableMovePath();
            callback.cancel();
        }
    }
}

@Mixin(value = MenuList.class, remap = false)
abstract class QuestPortalMixin {
    @Shadow protected int curMenuId;

    @Inject(method = "functionate()V", at = @At("HEAD"), remap = false, require = 1)
    private void entropy$observeGatherMenu(CallbackInfo callback) {
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.observeMenu((MenuList)(Object)this, curMenuId);
    }
    @Inject(method = "showConfirmationForMenu(ILjava/lang/String;)V", at = @At("HEAD"),
            cancellable = true, remap = false, require = 1)
    private void entropy$confirmQuestPortal(int menuId, String text, CallbackInfo callback) {
        if (menuId != 186 && menuId != 187) return;
        int townIndex = MenuSupport.instance.curActiveTownInd;
        if (!QuestFollower.INSTANCE.shouldConfirmPortal(townIndex, menuId)
                && !AutoJuice.INSTANCE.shouldConfirmPortal(townIndex, menuId)) return;
        callback.cancel();
        if (menuId == 186) GameMapObjectsController.actionMapObjectMapPortalUse(townIndex);
        else GameMapObjectsController.actionMapObjectDirectPortalUse(townIndex);
    }
}

@Mixin(value = GameMapObjectsController.class, remap = false)
abstract class JuicePortalMixin {
    @Inject(method = "actionMapObjectDirectPortalUse(I)V", at = @At("HEAD"), remap = false, require = 1)
    private static void entropy$beforeJuiceJump(int townIndex, CallbackInfo callback) {
        AutoJuice.INSTANCE.beforePortal(townIndex);
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.beforePortal(townIndex);
    }

    @Inject(method = "actionMapObjectDirectPortalUse(I)V", at = @At("RETURN"), remap = false, require = 1)
    private static void entropy$afterJuiceJump(int townIndex, CallbackInfo callback) {
        AutoJuice.INSTANCE.afterPortal();
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.afterPortal();
    }

    @Inject(method = "actionMapObjectMapPortalUse(I)Z", at = @At("HEAD"), remap = false, require = 1)
    private static void entropy$beforeJuicePortal(int townIndex, CallbackInfoReturnable<Boolean> callback) {
        AutoJuice.INSTANCE.beforePortal(townIndex);
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.beforePortal(townIndex);
    }

    @Inject(method = "actionMapObjectMapPortalUse(I)Z", at = @At("RETURN"), remap = false, require = 1)
    private static void entropy$afterJuicePortal(int townIndex, CallbackInfoReturnable<Boolean> callback) {
        AutoJuice.INSTANCE.afterPortal();
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.afterPortal();
    }
}

@Mixin(value = QuestStrateg.class, remap = false)
abstract class JuiceRewardMixin {
    @Inject(method = "checkGetnQuestsForDone(LModuls/TileMap;ILModuls/quests/QuestsOnMap;LModuls/Strategist;Z)[Ljava/lang/String;",
            at = @At("RETURN"), remap = false, require = 1)
    private void entropy$juiceReward(TileMap map, int giver, QuestsOnMap quests, Moduls.Strategist player,
            boolean analyzeLast, CallbackInfoReturnable<String[]> callback) {
        for (AutoGatherRoute route : AutoGatherRoute.routes()) route.onReward(map, giver, callback.getReturnValue());
    }
}

@Mixin(value = Main.class, remap = false)
abstract class DesktopMixin {

    @Inject(method = "main([Ljava/lang/String;)V", at = @At("RETURN"), remap = false, require = 1)
    private static void entropy$installMenu(CallbackInfo callback) {
        SwingUtilities.invokeLater(ClickGui::install);
    }
}

@Mixin(value = BattleTh.class, remap = false)
abstract class AutoBattleMixin implements AutoBattle.Actions {
    @Shadow private int[] cursorWorldPosition;

    @Shadow
    private static int findUnitAtPixel(int x, int y) {
        throw new AssertionError();
    }

    @Inject(method = "analizeCursorStateByType()V", at = @At("RETURN"), remap = false, require = 1)
    private void entropy$selectAnyUnit(CallbackInfo callback) {
        if (AutoBattle.INSTANCE.manualTargeting() && cursorWorldPosition != null) {
            BattleTh.currentChoosedUnit = AutoBattle.INSTANCE.savedManualTarget();
        }
    }

    @Redirect(method = "functionateUnderDrawProgress()V",
            at = @At(value = "FIELD", target = "Lbattlepck/client/BattleTh;currentChoosedUnit:I", opcode = 179),
            remap = false, require = 1)
    private void entropy$restoreManualSelection(int value) {
        BattleTh.currentChoosedUnit = AutoBattle.INSTANCE.manualTargeting()
                ? AutoBattle.INSTANCE.savedManualTarget() : value;
    }

    @Inject(method = "cursorWorldActivate()V", at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void entropy$keepManualTarget(CallbackInfo callback) {
        if (AutoBattle.INSTANCE.manualTargeting() && cursorWorldPosition != null) {
            int target = findUnitAtPixel(cursorWorldPosition[0], cursorWorldPosition[1]);
            AutoBattle.INSTANCE.rememberManualTarget(target);
            BattleTh.currentChoosedUnit = target;
            if (target >= 0) callback.cancel();
        }
    }

    @Shadow
    private static void issuePlayerOrder(int target, byte type, int slot, int x, int y) {
        throw new AssertionError();
    }

    @Override
    public void entropy$order(int target, byte type, int slot, int x, int y) {
        issuePlayerOrder(target, type, slot, x, y);
    }

    @Inject(method = "functionateUnderDrawProgress()V", at = @At("RETURN"), remap = false, require = 1)
    private void entropy$autoBattle(CallbackInfo callback) {
        AutoBattle.INSTANCE.update((BattleTh)(Object)this);
    }
}

@Mixin(value = MainTh.class, remap = false)
abstract class ZoomWheelMixin {
    @Inject(method = "wheelRotate(I)V", at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void entropy$zoomWithWheel(int rotation, CallbackInfo callback) {
        if (Zoom.INSTANCE.scroll(rotation)) callback.cancel();
    }
}

@Mixin(targets = "desktop.ui.CanvasPanel", remap = false)
abstract class ZoomCanvasMixin {
    @Unique private float entropy$previousScale = 2.0f;

    @Inject(method = "paint(Ljava/awt/Graphics;)V", at = @At("HEAD"), remap = false, require = 1)
    private void entropy$resizeViewport(Graphics graphics, CallbackInfo callback) {
        float scale = Zoom.INSTANCE.scale();
        if (scale == entropy$previousScale) return;
        entropy$previousScale = scale;
        Component component = (Component)(Object)this;
        component.dispatchEvent(new ComponentEvent(component, ComponentEvent.COMPONENT_RESIZED));
    }

    @ModifyConstant(method = "paint(Ljava/awt/Graphics;)V", constant = @Constant(floatValue = 2.0f), remap = false, require = 2)
    private float entropy$renderScale(float original) {
        return Zoom.INSTANCE.scale();
    }
}

@Mixin(targets = "desktop.ui.CanvasMouseListener", remap = false)
abstract class ZoomMouseMixin {
    @ModifyConstant(method = {"mousePressed(Ljava/awt/event/MouseEvent;)V", "mouseReleased(Ljava/awt/event/MouseEvent;)V"}, constant = @Constant(floatValue = 2.0f), remap = false, require = 4)
    private float entropy$clickScale(float original) {
        return Zoom.INSTANCE.scale();
    }
}

@Mixin(targets = "desktop.ui.CanvasMotionListener", remap = false)
abstract class ZoomMotionMixin {
    @ModifyConstant(method = {"mouseDragged(Ljava/awt/event/MouseEvent;)V", "mouseMoved(Ljava/awt/event/MouseEvent;)V"}, constant = @Constant(floatValue = 2.0f), remap = false, require = 4)
    private float entropy$motionScale(float original) {
        return Zoom.INSTANCE.scale();
    }
}

@Mixin(targets = "desktop.ui.CanvasResizeListener", remap = false)
abstract class ZoomResizeMixin {
    @ModifyConstant(method = "componentResized(Ljava/awt/event/ComponentEvent;)V", constant = @Constant(floatValue = 2.0f), remap = false, require = 2)
    private float entropy$viewportScale(float original) {
        return Zoom.INSTANCE.scale();
    }
}

@Mixin(value = TileMap.class, remap = false)
abstract class TileMapMixin implements NoClip.CollisionMap {

    @Inject(method = "loadFromStream(LBase/NetRequest;Ljava/io/DataInputStream;LEngine/MystFont;I)V",
            at = @At("HEAD"), remap = false, require = 1)
    private void entropy$clearOldMapEdits(CallbackInfo callback) {
        MapBuilder.INSTANCE.discardMap((TileMap)(Object)this);
    }

    @Shadow public TileMapCell[][] cells;
    @Shadow public int widthTl;
    @Shadow public int heightTl;

    @Shadow
    private boolean isBlockInCoordCalculate(int x, int y) {
        throw new AssertionError();
    }

    @Inject(method = "isBlockInCoordCalculate(II)Z", at = @At("HEAD"), cancellable = true,
            remap = false, require = 1)
    private void entropy$disableCollision(int x, int y, CallbackInfoReturnable<Boolean> callback) {
        if (NoClip.INSTANCE.enabled) callback.setReturnValue(false);
    }

    @Override
    public void entropy$rebuildCollisions() {
        for (int x = 0; x < widthTl; x++) {
            for (int y = 0; y < heightTl; y++) {
                cells[x][y].block = isBlockInCoordCalculate(x, y);
            }
        }
    }
}

@Mixin(value = MoveableUnitDiscretShort.class, remap = false)
abstract class MovementMixin {

    @Inject(method = "animateMovement(JJLModuls/PathfindingGridBlockable;IZII)I",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void entropy$moveWithoutInterval(long deltaTime, long interval, PathfindingGridBlockable map, int direction, boolean withTarget, int targetX, int targetY,
            CallbackInfoReturnable<Integer> callback) {
        if (interval > 0)return;
        MoveableUnitDiscretShort unit=(MoveableUnitDiscretShort)(Object)this;
        unit.moveLastDirection8D=direction;
        unit.moveAccumTime=0;
        if (direction==8) {
            callback.setReturnValue(withTarget && unit.cx==targetX && unit.cy==targetY ? 1 : 0);
            return;
        }
        if (!withTarget) {
            callback.setReturnValue(unit.animateMovement(deltaTime,1L,map,direction,false,targetX,targetY));
            return;
        }
        int steps=Math.max(Math.abs(targetX-unit.cx),Math.abs(targetY-unit.cy));
        steps=Math.min(steps,65535);
        for(int step=0;step<steps;step++) {
            boolean moved=unit.collidMoveStep(direction,map);
            if(unit.cx==targetX && unit.cy==targetY) {
                callback.setReturnValue(1);
                return;
            }
            if(!moved)break;
        }
        callback.setReturnValue(2);
    }
}

@Mixin(value = GameGraphics.class, remap = false)
abstract class GraphicsMixin implements ESP.DrawingSurface {
    @Shadow private Graphics graphics;

    @Override
    public Graphics entropy$createGraphics() {
        return graphics.create();
    }
}

@Mixin(value = DrawOrderElementStrateg.class, remap = false)
abstract class StrategTextMixin {

    @Redirect(method = "draw(Ldesktop/graphics/GameGraphics;)V",
            at = @At(value = "INVOKE", target = "LEngine/Camera;isActiveLongStay()Z"),
            remap = false, require = 1)
    private boolean entropy$keepOwnNameVisible(Camera camera) {
        return BlockText.INSTANCE.enabled || camera.isActiveLongStay();
    }
}

@Mixin(value = DrawOrderElementOtherStrateg.class, remap = false)
abstract class OtherStrategTextMixin {

    @Redirect(method = "draw(Ldesktop/graphics/GameGraphics;)V",
            at = @At(value = "INVOKE", target = "LEngine/Camera;isActiveLongStay()Z", ordinal = 1),
            remap = false, require = 1)
    private boolean entropy$keepPlayerNamesVisible(Camera camera) {
        return BlockText.INSTANCE.enabled || ShowStats.INSTANCE.enabled || camera.isActiveLongStay();
    }

}
