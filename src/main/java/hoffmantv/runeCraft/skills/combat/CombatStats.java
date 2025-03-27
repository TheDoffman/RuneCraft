package hoffmantv.runeCraft.skills.combat;


import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CombatStats {

    // Separate levels and XP for each stat.
    private int attackLevel, defenceLevel, strengthLevel;
    private double attackXP, defenceXP, strengthXP;

    public CombatStats() {
        // Initialize all stat levels to 1 and XP to 0.
        this.attackLevel = 1;
        this.defenceLevel = 1;
        this.strengthLevel = 1;
        this.attackXP = 0;
        this.defenceXP = 0;
        this.strengthXP = 0;
    }

    public static CombatStats load(Player player) {
        UUID uuid = player.getUniqueId();
        CombatStats stats = new CombatStats();
        String id = uuid.toString();
        // Load XP and level values from YAML; default to 0 XP and level 1 if not found.
        stats.attackXP = PlayerSkillDataManager.getPlayerSkillXP(id, "combat.attack");
        stats.defenceXP = PlayerSkillDataManager.getPlayerSkillXP(id, "combat.defence");
        stats.strengthXP = PlayerSkillDataManager.getPlayerSkillXP(id, "combat.strength");
        stats.attackLevel = PlayerSkillDataManager.getPlayerSkillRank(id, "combat.attack");
        stats.defenceLevel = PlayerSkillDataManager.getPlayerSkillRank(id, "combat.defence");
        stats.strengthLevel = PlayerSkillDataManager.getPlayerSkillRank(id, "combat.strength");
        if (stats.attackLevel < 1) stats.attackLevel = 1;
        if (stats.defenceLevel < 1) stats.defenceLevel = 1;
        if (stats.strengthLevel < 1) stats.strengthLevel = 1;
        return stats;
    }

    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "combat.attack", this.attackXP);
        PlayerSkillDataManager.setPlayerSkillXP(id, "combat.defence", this.defenceXP);
        PlayerSkillDataManager.setPlayerSkillXP(id, "combat.strength", this.strengthXP);
        PlayerSkillDataManager.setPlayerSkillRank(id, "combat.attack", this.attackLevel);
        PlayerSkillDataManager.setPlayerSkillRank(id, "combat.defence", this.defenceLevel);
        PlayerSkillDataManager.setPlayerSkillRank(id, "combat.strength", this.strengthLevel);
    }

    // Quadratic XP formula: next level requires (currentLevel^2 * 100) XP.
    private double xpForNextLevel(int currentLevel) {
        return Math.pow(currentLevel, 2) * 100;
    }

    // Methods to add XP for each stat and handle level-ups.
    private void addAttackExperience(double xp, Player player) {
        if (attackLevel >= 99) {
            this.attackXP += xp;
            return;
        }
        this.attackXP += xp;
        while (this.attackXP >= xpForNextLevel(attackLevel) && attackLevel < 99) {
            this.attackXP -= xpForNextLevel(attackLevel);
            attackLevel++;
            // Trigger individual level-up effect for Attack.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Attack", attackLevel);
            if (attackLevel % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Attack", attackLevel);
            }
        }
    }

    private void addDefenceExperience(double xp, Player player) {
        if (defenceLevel >= 99) {
            this.defenceXP += xp;
            return;
        }
        this.defenceXP += xp;
        while (this.defenceXP >= xpForNextLevel(defenceLevel) && defenceLevel < 99) {
            this.defenceXP -= xpForNextLevel(defenceLevel);
            defenceLevel++;
            // Trigger individual level-up effect for Defence.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Defence", defenceLevel);
            if (defenceLevel % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Defence", defenceLevel);
            }
        }
    }

    private void addStrengthExperience(double xp, Player player) {
        if (strengthLevel >= 99) {
            this.strengthXP += xp;
            return;
        }
        this.strengthXP += xp;
        while (this.strengthXP >= xpForNextLevel(strengthLevel) && strengthLevel < 99) {
            this.strengthXP -= xpForNextLevel(strengthLevel);
            strengthLevel++;
            // Trigger individual level-up effect for Strength.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Strength", strengthLevel);
            if (strengthLevel % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Strength", strengthLevel);
            }
        }
    }

    public void addKillExperience(double baseXP, Player player) {
        // Record the old levels.
        int oldAttack = attackLevel;
        int oldDefence = defenceLevel;
        int oldStrength = strengthLevel;

        // Add XP to each stat.
        addAttackExperience(baseXP * 1.0, player);
        addDefenceExperience(baseXP * 0.8, player);
        addStrengthExperience(baseXP * 1.2, player);

        // Build a combined rank-up message if any stat increased.
        StringBuilder combinedMessage = new StringBuilder();
        if (attackLevel > oldAttack) {
            combinedMessage.append("Attack: ").append(attackLevel).append(" ");
        }
        if (defenceLevel > oldDefence) {
            combinedMessage.append("Defence: ").append(defenceLevel).append(" ");
        }
        if (strengthLevel > oldStrength) {
            combinedMessage.append("Strength: ").append(strengthLevel).append(" ");
        }
    }

    // Getters for individual stat levels.
    public int getAttackLevel() {
        return attackLevel;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public int getCombatLevel() {
        return (int) Math.floor(defenceLevel * 0.25 + (attackLevel + strengthLevel) * 0.325);
    }
}