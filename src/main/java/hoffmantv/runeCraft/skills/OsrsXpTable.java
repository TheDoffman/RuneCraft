package hoffmantv.runeCraft.skills;

public final class OsrsXpTable {
    private static final int MAX_LEVEL = 99;
    private static final int[] XP_TABLE = new int[MAX_LEVEL + 1];

    static {
        int points = 0;
        XP_TABLE[1] = 0;
        for (int lvl = 1; lvl < MAX_LEVEL; lvl++) {
            points += (int) Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            XP_TABLE[lvl + 1] = points / 4;
        }
    }

    private OsrsXpTable() {
    }

    public static int xpForLevel(int level) {
        if (level <= 1) {
            return 0;
        }
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        return XP_TABLE[level];
    }

    public static int levelForXp(double xp) {
        int cappedXp = (int) Math.floor(xp);
        for (int lvl = 1; lvl < MAX_LEVEL; lvl++) {
            if (cappedXp < XP_TABLE[lvl + 1]) {
                return lvl;
            }
        }
        return MAX_LEVEL;
    }

    public static int getMaxLevel() {
        return MAX_LEVEL;
    }
}
