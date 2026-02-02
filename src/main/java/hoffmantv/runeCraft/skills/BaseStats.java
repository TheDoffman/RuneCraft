package hoffmantv.runeCraft.skills;

import org.bukkit.entity.Player;

public abstract class BaseStats implements SkillStats {
    protected int level = 1;
    protected double xp = 0;

    protected abstract String getSkillKey();
    protected abstract String getSkillDisplayName();

    protected double transformIncomingXp(double amount) {
        return amount;
    }

    protected void onLevelUp(Player player) {
        SkillRewardUtils.triggerSkillRankUpEffect(player, getSkillDisplayName(), this.level);
    }

    protected void onMilestoneLevel(Player player) {
        SkillRewardUtils.triggerSkillRankUpEffect(player, getSkillDisplayName(), this.level);
    }

    public void loadFromPlayer(Player player) {
        String id = player.getUniqueId().toString();
        double storedXp = PlayerSkillDataManager.getPlayerSkillXP(id, getSkillKey());
        int storedLevel = PlayerSkillDataManager.getPlayerSkillRank(id, getSkillKey());
        if (storedLevel < 1) {
            storedLevel = 1;
        }

        double minXpForLevel = OsrsXpTable.xpForLevel(storedLevel);
        if (storedXp < minXpForLevel) {
            this.xp = minXpForLevel + storedXp;
        } else {
            this.xp = storedXp;
        }
        this.level = OsrsXpTable.levelForXp(this.xp);
        if (this.level < storedLevel) {
            this.level = storedLevel;
        }
    }

    public void save(Player player) {
        String id = player.getUniqueId().toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, getSkillKey(), this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, getSkillKey(), this.level);
        PlayerSkillDataManager.queueSave();
    }

    public void addExperience(double amount, Player player) {
        double xpReward = transformIncomingXp(amount);
        if (level >= OsrsXpTable.getMaxLevel()) {
            this.xp += xpReward;
            save(player);
            return;
        }
        this.xp += xpReward;
        int newLevel = OsrsXpTable.levelForXp(this.xp);
        if (newLevel > level) {
            for (int lvl = level + 1; lvl <= newLevel; lvl++) {
                level = lvl;
                onLevelUp(player);
                if (this.level % 5 == 0) {
                    onMilestoneLevel(player);
                }
            }
        }
        save(player);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getXp() {
        return xp;
    }
}
