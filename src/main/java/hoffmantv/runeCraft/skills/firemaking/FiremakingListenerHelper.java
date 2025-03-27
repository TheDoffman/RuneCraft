package hoffmantv.runeCraft.skills.firemaking;

import org.bukkit.block.Block;
import java.util.HashSet;
import java.util.Set;

public class FiremakingListenerHelper {
    private static final Set<Block> activeBlocks = new HashSet<>();

    public static boolean isActive(Block block) {
        return activeBlocks.contains(block);
    }

    public static void markActive(Block block) {
        activeBlocks.add(block);
    }

    public static void unmarkActive(Block block) {
        activeBlocks.remove(block);
    }
}