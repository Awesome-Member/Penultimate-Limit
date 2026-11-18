package bridge.player;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Base.GameControllDirectData;
import Base.GameSelectionStructure;
import MenuPck.MenuManager;
import Moduls.Enemy;
import battlepck.BattleManager;
import bridge.ui.ClickGui;

public final class Reach extends ClickGui.Feature {
    public static final Reach INSTANCE = new Reach();

    private Reach() {
        super(ClickGui.Category.PLAYER, "Reach", "Allows you to attack from afar");
    }

    public void update(Game game) {
        if (!enabled || GameCommons.instance.loadMap || Com.dontFightAndAction || Com.mainTh.stopPaint || MenuManager.instance.guiHandler.isActive()) return;

        GameControllDirectData controls = game.gameControllDirectData;
        GameSelectionStructure target = controls.moveTarget;
        if (target != null && (target.selectedEnemyInd >= 0 || target.selectedOtherStrategId >= 0 && !controls.moveTargetPeacefull))
        {
            controls.pathFolower.disableMovePath();
            int enemyIndex = target.selectedEnemyInd;
            if (enemyIndex >= 0)
            {
                controls.disableControll();
                if (Com.EnsMap == null || Com.EnsMap.Enemys == null || enemyIndex >= Com.EnsMap.Enemys.length) return;
                Enemy enemy = Com.EnsMap.Enemys[enemyIndex];
                if (enemy != null && enemy.Enable && enemy.fightable)
                {
                    BattleManager.createFightWithMob(enemyIndex);
                }
            } else if (target.selectedOtherStrategObj != null && target.selectedOtherStrategObj.availAtack)
            {
                int playerId = target.selectedOtherStrategId;
                controls.disableControll();
                BattleManager.createFightWithPlayer(playerId);
            }
        }
    }
}
