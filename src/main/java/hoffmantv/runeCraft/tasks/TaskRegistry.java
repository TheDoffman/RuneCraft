package hoffmantv.runeCraft.tasks;

import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRegistry {
    private final Map<UUID, Set<BukkitTask>> playerTasks = new ConcurrentHashMap<>();
    private final Set<BukkitTask> globalTasks = ConcurrentHashMap.newKeySet();

    public void registerPlayerTask(UUID playerId, BukkitTask task) {
        if (task == null) return;
        playerTasks.computeIfAbsent(playerId, id -> ConcurrentHashMap.newKeySet()).add(task);
    }

    public void registerGlobalTask(BukkitTask task) {
        if (task == null) return;
        globalTasks.add(task);
    }

    public void cancelPlayerTasks(UUID playerId) {
        Set<BukkitTask> tasks = playerTasks.remove(playerId);
        if (tasks == null) return;
        for (BukkitTask task : tasks) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
    }

    public void cancelAll() {
        for (BukkitTask task : globalTasks) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        globalTasks.clear();
        for (UUID playerId : playerTasks.keySet()) {
            cancelPlayerTasks(playerId);
        }
        playerTasks.clear();
    }
}
