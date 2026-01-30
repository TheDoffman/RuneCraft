// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityCourseManager.java
package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public final class AgilityCourseManager {
    private static File file;
    private static FileConfiguration cfg;

    public static void init(RuneCraft plugin) {
        file = new File(plugin.getDataFolder(), "agility_courses.yml");
        if (!file.exists()) {
            try { file.getParentFile().mkdirs(); file.createNewFile(); } catch (Exception ignored) {}
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try { cfg.save(file); } catch (Exception ignored) {}
    }

    public static void setCourse(String name, List<CourseNode> nodes) {
        List<String> ser = new ArrayList<>();
        for (CourseNode n : nodes) {
            ser.add(n.type.name() + ";" + n.levelReq + ";" + n.xp + ";"
                    + n.loc.getWorld().getName() + ";" + n.loc.getBlockX() + ";" + n.loc.getBlockY() + ";" + n.loc.getBlockZ());
        }
        cfg.set("courses."+name, ser);
        save();
    }

    public static List<CourseNode> getCourse(String name) {
        List<String> ser = cfg.getStringList("courses."+name);
        List<CourseNode> out = new ArrayList<>();
        for (String s : ser) {
            String[] a = s.split(";");
            AgilityObstacleType t = AgilityObstacleType.valueOf(a[0]);
            int lvl = Integer.parseInt(a[1]);
            double xp = Double.parseDouble(a[2]);
            Location l = new Location(Bukkit.getWorld(a[3]), Integer.parseInt(a[4]), Integer.parseInt(a[5]), Integer.parseInt(a[6]));
            out.add(new CourseNode(t, lvl, xp, l));
        }
        return out;
    }

    public record CourseNode(AgilityObstacleType type, int levelReq, double xp, Location loc) {}
}