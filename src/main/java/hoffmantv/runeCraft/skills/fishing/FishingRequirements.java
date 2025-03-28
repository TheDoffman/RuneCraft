package hoffmantv.runeCraft.skills.fishing;

import org.bukkit.Material;

public class FishingRequirements {

    /**
     * Returns a FishDrop based on the player's fishing level.
     * Lower levels catch "Raw Shrimp"; mid-level may catch "Raw Trout";
     * higher levels can catch "Raw Salmon".
     */
    public static FishDrop getFishDrop(int fishingLevel) {
        if (fishingLevel < 20) {
            return new FishDrop(Material.COD, "Raw Shrimp", 10, 1);
        } else if (fishingLevel < 40) {
            // 30% chance to catch Trout, otherwise Shrimp.
            if (Math.random() < 0.3) {
                return new FishDrop(Material.COD, "Raw Trout", 20, 1);
            } else {
                return new FishDrop(Material.COD, "Raw Shrimp", 10, 1);
            }
        } else {
            // 50% chance Salmon, 50% chance Trout.
            if (Math.random() < 0.5) {
                return new FishDrop(Material.SALMON, "Raw Salmon", 30, 1);
            } else {
                return new FishDrop(Material.COD, "Raw Trout", 20, 1);
            }
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