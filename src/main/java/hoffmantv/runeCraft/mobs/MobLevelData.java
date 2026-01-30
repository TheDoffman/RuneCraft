package hoffmantv.runeCraft.mobs;

public class MobLevelData {
    private final int level;
    private final double healthMultiplier;
    private final double damageMultiplier;

    public MobLevelData(int level) {
        // Clamp level between 1 and 99.
        int clampedLevel = Math.min(99, Math.max(1, level));
        this.level = clampedLevel;
        // Example formulas:
        // Health multiplier: base 1.0 + (level / 50.0).
        // For level 1: 1.02, for level 99: 1.0 + 99/50 ≈ 2.98.
        this.healthMultiplier = 1.0 + (this.level / 50.0);
        // Damage multiplier: base 1.0 + (level / 100.0).
        // For level 1: 1.01, for level 99: 1.99.
        this.damageMultiplier = 1.0 + (this.level / 100.0);
    }

    public int getLevel() {
        return level;
    }

    public double getHealthMultiplier() {
        return healthMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public String toString() {
        return "Level " + level + " (Health x" + String.format("%.2f", healthMultiplier)
                + ", Damage x" + String.format("%.2f", damageMultiplier) + ")";
    }
}