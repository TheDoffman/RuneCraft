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
     *   20 - 29: "Raw Trout"
     *   30 - 39: "Raw Salmon"
     *   40 - 49: "Raw Lobster"
     *   50 - 61: "Raw Swordfish"
     *   62 - 75: "Raw Monkfish"
     *   76+: "Raw Shark"
     */
    public static FishDrop getFishDrop(int fishingLevel) {
        if (fishingLevel < 20) {
            // Use Material.COD as the base, but rename it "Raw Shrimp"
            return new FishDrop(Material.COD, "Raw Shrimp", 10, 1);
        } else if (fishingLevel < 30) {
            // Use Material.SALMON as the base, but rename it "Raw Trout"
            return new FishDrop(Material.SALMON, "Raw Trout", 50, 1);
        } else if (fishingLevel < 40) {
            // Use Material.SALMON as the base, but rename it "Raw Salmon"
            return new FishDrop(Material.SALMON, "Raw Salmon", 70, 1);
        } else if (fishingLevel < 50) {
            // Use Material.TROPICAL_FISH as the base, but rename it "Raw Lobster"
            return new FishDrop(Material.TROPICAL_FISH, "Raw Lobster", 90, 1);
        } else if (fishingLevel < 62) {
            // Use Material.TROPICAL_FISH as the base, but rename it "Raw Swordfish"
            return new FishDrop(Material.TROPICAL_FISH, "Raw Swordfish", 100, 1);
        } else if (fishingLevel < 76) {
            // Use Material.PUFFERFISH as the base, but rename it "Raw Monkfish"
            return new FishDrop(Material.PUFFERFISH, "Raw Monkfish", 120, 1);
        } else {
            // Use Material.PUFFERFISH as the base, but rename it "Raw Shark"
            return new FishDrop(Material.PUFFERFISH, "Raw Shark", 110, 1);
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