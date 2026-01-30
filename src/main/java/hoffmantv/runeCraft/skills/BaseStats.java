package hoffmantv.runeCraft.skills;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.entity.Player;

public abstract class BaseStats implements SkillStats {
    protected int level = 1;
    protected double xp = 0;

    protected abstract String getSkillKey();
    protected abstract String getSkillDisplayName();
    protected abstract double getXpMultiplier();

    protected double transformIncomingXp(double amount) {
        return amount;
    }

    protected double xpForNextLevel() {
        return Math.pow(level, 2) * getXpMultiplier();
    }

    protected void onLevelUp(Player player) {
        SkillRewardUtils.triggerSkillRankUpEffect(player, getSkillDisplayName(), this.level);
    }

    protected void onMilestoneLevel(Player player) {
        SkillRewardUtils.triggerSkillRankUpEffect(player, getSkillDisplayName(), this.level);
    }

    public void loadFromPlayer(Player player) {
        String id = player.getUniqueId().toString();
        this.xp = PlayerSkillDataManager.getPlayerSkillXP(id, getSkillKey());
        this.level = PlayerSkillDataManager.getPlayerSkillRank(id, getSkillKey());
        if (this.level < 1) {
            this.level = 1;
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
        if (level >= 99) {
            this.xp += xpReward;
            save(player);
            return;
        }
        this.xp += xpReward;
        while (this.xp >= xpForNextLevel() && level < 99) {
            this.xp -= xpForNextLevel();
            level++;
            onLevelUp(player);
            if (this.level % 5 == 0) {
                onMilestoneLevel(player);
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
