package hoffmantv.runeCraft.mobs;

public class MobLevelData {
    private final int level;
    private final double healthMultiplier;
    private final double damageMultiplier;
    private final double armorMultiplier;
    private final boolean elite;

    public MobLevelData(int level, double healthMultiplier, double damageMultiplier, double armorMultiplier, boolean elite) {
        // Clamp level between 1 and 99.
        int clampedLevel = Math.min(99, Math.max(1, level));
        this.level = clampedLevel;
        this.healthMultiplier = healthMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.armorMultiplier = armorMultiplier;
        this.elite = elite;
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

    public double getArmorMultiplier() {
        return armorMultiplier;
    }

    public boolean isElite() {
        return elite;
    }

    @Override
    public String toString() {
        return "Level " + level + " (Health x" + String.format("%.2f", healthMultiplier)
                + ", Damage x" + String.format("%.2f", damageMultiplier)
                + ", Armor x" + String.format("%.2f", armorMultiplier)
                + ", Elite " + elite + ")";
    }
}