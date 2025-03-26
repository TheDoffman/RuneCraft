// Updated CombatStats.java with YAML Integration
package hoffmantv.runeCraft.skilling;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CombatStats {
    private int level;
    private double experience;
    private double attack;
    private double defence;
    private double strength;

    public CombatStats() {
        this.level = 1;
        this.experience = 0;
        this.attack = 5.0;
        this.defence = 5.0;
        this.strength = 5.0;
    }

    // Loads CombatStats from the YAML file using PlayerSkillDataManager.
    // You can extend this to load individual stat values if you wish.
    public static CombatStats load(Player player) {
        UUID uuid = player.getUniqueId();
        CombatStats stats = new CombatStats();
        String id = uuid.toString();
        stats.experience = PlayerSkillDataManager.getPlayerSkillXP(id, "combat");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(id, "combat");
        // Additional fields (attack, defence, strength) could be loaded similarly if stored.
        return stats;
    }

    // Saves CombatStats to the YAML file using PlayerSkillDataManager.
    // You can extend this to save individual stat values if you choose.
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "combat", this.experience);
        PlayerSkillDataManager.setPlayerSkillRank(id, "combat", this.level);
        // Save individual stats (attack, defence, strength) here if desired.
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefence() {
        return defence;
    }

    public double getStrength() {
        return strength;
    }

    // Individual stat ranks (floor of the current stat value)
    public int getAttackRank() {
        return (int) Math.floor(attack);
    }

    public int getDefenceRank() {
        return (int) Math.floor(defence);
    }

    public int getStrengthRank() {
        return (int) Math.floor(strength);
    }

    // Combined combat level similar to RuneScape:
    public int getCombatLevel() {
        return (int) Math.floor(defence * 0.25 + (attack + strength) * 0.325);
    }

    // Adds experience and levels up when enough XP is reached.
    public void addExperience(double xp) {
        this.experience += xp;
        while (this.experience >= experienceForNextLevel()) {
            levelUp();
        }
    }

    // Simple formula: next level requires level * 100 XP.
    private double experienceForNextLevel() {
        return level * 100;
    }

    // Level up and improve individual stats.
    private void levelUp() {
        this.experience -= experienceForNextLevel();
        this.level++;
        this.attack += 1.0;
        this.defence += 1.0;
        this.strength += 1.0;
    }
}