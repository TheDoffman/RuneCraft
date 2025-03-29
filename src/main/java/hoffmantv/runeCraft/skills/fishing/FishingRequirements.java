package hoffmantv.runeCraft.skills.fishing;

import org.bukkit.Material;

public class FishingRequirements {

    /**
     * Returns a FishDrop based solely on the player's fishing level.
     * Each fish type is locked behind a minimum fishing level and
     * uses a custom OSRS-style name.
     *
     * Levels:
     *   1 - 19: "Raw Shrimp"
     *   20 - 39: "Raw Trout"
     *   40 - 59: "Raw Salmon"
     *   60+: "Pufferfish"
     */
    public static FishDrop getFishDrop(int fishingLevel) {
        if (fishingLevel < 20) {
            // Use Material.COD as the base, but rename it "Raw Shrimp"
            return new FishDrop(Material.COD, "Raw Shrimp", 10, 1);
        } else if (fishingLevel < 40) {
            // Use Material.SALMON as the base, but rename it "Raw Trout"
            return new FishDrop(Material.SALMON, "Raw Trout", 20, 1);
        } else if (fishingLevel < 60) {
            // Use Material.SALMON as the base, but rename it "Raw Salmon"
            return new FishDrop(Material.SALMON, "Raw Salmon", 30, 1);
        } else {
            // Use Material.PUFFERFISH as the base, but rename it "Pufferfish"
            return new FishDrop(Material.PUFFERFISH, "Pufferfish", 40, 1);
        }
    }

    public static class FishDrop {
        public final Material dropMaterial;
        public final String displayName;
        public final double xpReward;
        public final int amount;

        public FishDrop(Material dropMaterial, String displayName, double xpReward, int amount) {
            this.dropMaterial = dropMaterial;
            this.displayName = displayName;
            this.xpReward = xpReward;
            this.amount = amount;
        }
    }
}