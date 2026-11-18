package bridge.player;

import Base.Com;
import battlepck.Battle;
import battlepck.BattleBottle;
import battlepck.BattleManager;
import battlepck.BattleMember;
import battlepck.BattleMemberSmall;
import battlepck.BattleUnit;
import battlepck.BottleEffect;
import battlepck.client.BattleTh;
import bridge.ui.ClickGui;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

public final class AutoBattle extends ClickGui.Feature {
    public static final AutoBattle INSTANCE = new AutoBattle();
    public volatile boolean heal = true, bombs = true, rage = true, special = true;
    public volatile int healthPercent = 50, rageLevel = 3;
    public volatile int targeting;
    private Battle lastBattle;
    private long nextAction;
    private Battle manualBattle;
    private int manualTarget = -1;

    public interface Actions {
        void entropy$order(int target, byte type, int slot, int x, int y);
    }

    private AutoBattle() {
        super(ClickGui.Category.PLAYER, "Auto Battle", "Automatic consumable use");
        dropdown("Target", new String[]{"Random", "Lowest HP", "Highest HP", "Manual"}, () -> targeting, value -> { targeting = value; manualTarget = -1; });
        checkbox("Auto Heal", () -> heal, value -> heal = value);
        slider("Health %", 1, 100, () -> healthPercent, value -> healthPercent = value);
        checkbox("Auto Bombs", () -> bombs, value -> bombs = value);
        checkbox("Auto Special", () -> special, value -> special = value);
        checkbox("Auto Rage", () -> rage, value -> rage = value);
        slider("Rage Level", 1, 3, () -> rageLevel, value -> rageLevel = value);
    }

    public void update(BattleTh thread) {
        if (!enabled || !BattleTh.realBattle || Com.mainTh == null || Com.mainTh.getTopPFter() != thread) return;
        Battle battle = BattleManager.getCurrentBattle();
        if (battle == null || battle.isWasEnd()) return;
        if (battle != lastBattle) {
            lastBattle = battle;
            nextAction = 0;
        }
        if (battle.hasUnDoneAct || System.currentTimeMillis() < nextAction) return;
        BattleUnit player = BattleManager.getCurrentBattlePlayerAvatar();
        BattleMember member = battle.getPlayerMember();
        BattleMemberSmall small = battle.getPlayerMemberSmall();
        if (player == null || player.Dead || player.CurCount <= 0 || member == null || small == null) return;

        BattleUnit target = selectTarget(battle, player);
        boolean manual = manualTargeting();
        if (manual && target == null) return;
        BattleUnit healTarget = manual ? target : player;
        Actions actions = (Actions)(Object)thread;
        if (heal && healTarget.curHealthMaxCount > 0 && healTarget.CurCount / (double)healTarget.curHealthMaxCount * 100 <= healthPercent)
        {
            BattleBottle bottle = readyBottle(member, small, true);
            if (bottle != null) {
                useBottle(actions, bottle, healTarget);
                return;
            }
        }

        if (special)
        {
            BattleBottle skill = (BattleBottle)member.bottlesBySlot.get(Byte.valueOf((byte)2));
            if (skill != null && ready(skill, member, small)) {
                BottleEffect effect = skill.effect;
                BattleUnit recipient = null;
                if (manual) {
                    recipient = target;
                } else if (effect.appSelf) {
                    boolean healing = effect.hitPower < 0 || effect.regenPower > 0;
                    if (!healing || player.curHealthMaxCount > 0
                            && player.CurCount / (double)player.curHealthMaxCount * 100 <= healthPercent) {
                        recipient = player;
                    }
                } else if (effect.appEnemys) {
                    recipient = target;
                } else if (effect.appFriends && target != null && target.side == player.side) {
                    recipient = target;
                }
                if (recipient != null) {
                    useBottle(actions, skill, recipient);
                    return;
                }
            }
        }
        if (target == null) return;
        if (rage && small.stopRageTime <= 0 && small.getRageLevel() >= rageLevel) {
            actions.entropy$order(target.hisInd, (byte)1, 0, -1, -1);
            nextAction = System.currentTimeMillis() + 500;
            return;
        }
        if (bombs) {
            BattleBottle bottle = readyBottle(member, small, false);
            if (bottle != null) useBottle(actions, bottle, target);
        }
    }

    public boolean manualTargeting() {
        return enabled && targeting == 3 && BattleTh.realBattle;
    }

    public void rememberManualTarget(int target) {
        manualBattle = BattleManager.getCurrentBattle();
        manualTarget = target;
    }

    public int savedManualTarget() {
        Battle battle = BattleManager.getCurrentBattle();
        if (!manualTargeting() || battle == null || battle != manualBattle || battle.isWasEnd()) {
            manualTarget = -1;
            return -1;
        }
        BattleUnit unit = battle.getUnit(manualTarget);
        if (unit == null || unit.Dead || unit.CurCount <= 0) manualTarget = -1;
        return manualTarget;
    }

    private BattleUnit selectTarget(Battle battle, BattleUnit player) {
        int mode = targeting;
        if (mode == 3) {
            BattleUnit selected = battle.getUnit(savedManualTarget());
            return selected != null && !selected.Dead && selected.CurCount > 0 ? selected : null;
        }
        BattleUnit target = null;
        int candidates = 0;
        for (BattleUnit unit : BattleManager.getCurrentBattleUnits()) {
            if (unit == null || unit.Dead || unit.CurCount <= 0 || unit.side != Battle.ENEMY_SIDE_FAST[player.side]) continue;
            candidates++;
            if (target == null || mode == 0 && ThreadLocalRandom.current().nextInt(candidates) == 0
                    || mode == 1 && unit.CurCount < target.CurCount
                    || mode == 2 && unit.CurCount > target.CurCount) target = unit;
        }
        return target;
    }

    private BattleBottle readyBottle(BattleMember member, BattleMemberSmall small, boolean healing) {
        Enumeration bottles = member.bottlesBySlot.elements();
        while (bottles.hasMoreElements()) {
            BattleBottle bottle = (BattleBottle)bottles.nextElement();
            if (bottle.slotInd == 2 || !ready(bottle, member, small)) continue;
            BottleEffect effect = bottle.effect;
            if (healing ? effect.appSelf && effect.hitPower < 0 : effect.appEnemys && effect.hitPower > 0) return bottle;
        }
        return null;
    }

    private boolean ready(BattleBottle bottle, BattleMember member, BattleMemberSmall small) {
        if (member.leftUsesCount == 0 || bottle.count <= 0 || bottle.cooldownTimeCur > 0 || bottle.effect == null) return false;
        for (Object entry : small.stopedItems.toArray()) {
            int[] ids = (int[])entry;
            for (int i = 1; i < ids.length; i++) {
                if (ids[i] == bottle.itemTypeId) return false;
            }
        }
        return true;
    }

    private void useBottle(Actions actions, BattleBottle bottle, BattleUnit target) {
        boolean ground = bottle.effect.appGround;
        actions.entropy$order(ground ? -1 : target.hisInd, (byte)2, bottle.slotInd, ground ? target.cx : -1, ground ? target.cy : -1);
        nextAction = System.currentTimeMillis() + 500;
    }
}
