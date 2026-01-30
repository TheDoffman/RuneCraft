package hoffmantv.runeCraft.skills.mining;

import org.bukkit.block.Block;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MiningListenerHelper {
    private static final Map<Block, UUID> activeBlocks = new ConcurrentHashMap<>();

    public static boolean isActive(Block block) {
        return activeBlocks.containsKey(block);
    }

    public static void markActive(Block block, UUID playerId) {
        activeBlocks.put(block, playerId);
    }

    public static void unmarkActive(Block block) {
        activeBlocks.remove(block);
    }

    public static void clearForPlayer(UUID playerId) {
        activeBlocks.entrySet().removeIf(entry -> playerId.equals(entry.getValue()));
    }

    public static void clearAll() {
        activeBlocks.clear();
    }
}