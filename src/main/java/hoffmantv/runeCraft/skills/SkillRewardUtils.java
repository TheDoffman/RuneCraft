package hoffmantv.runeCraft.skills;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.time.Duration;
import java.util.Random;

public class SkillRewardUtils {

    /**
     * Triggers a generic skill rank-up effect that is the same for all skills.
     *
     * @param player    The player who ranked up.
     * @param skillName The name of the skill (e.g., "Woodcutting", "Combat").
     * @param newLevel  The new rank achieved.
     */
    public static void triggerSkillRankUpEffect(Player player, String skillName, int newLevel) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        Random random = new Random();

        // Launch 2 fireworks around the player.
        for (int i = 0; i < 2; i++) {
            Firework firework = world.spawn(loc.clone().add(random.nextDouble() * 2 - 1, 0, random.nextDouble() * 2 - 1), Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL)
                    .withFlicker()
                    .withTrail()
                    .withColor(Color.BLUE, Color.RED, Color.YELLOW)
                    .build();
            meta.addEffect(effect);
            meta.setPower(1);
            firework.setFireworkMeta(meta);
        }

        // Use a universal sound, text color, and particle for all rank-ups.
        Sound rankUpSound = Sound.ENTITY_PLAYER_LEVELUP;
        NamedTextColor titleColor = NamedTextColor.AQUA;
        Particle rankUpParticle = Particle.HAPPY_VILLAGER;

        // Play the rank-up sound.
        player.playSound(loc, rankUpSound, 1.0F, 1.0F);

        // Display a full-screen title.
        Title title = Title.title(
                Component.text("Rank Up!").color(NamedTextColor.GOLD),
                Component.text("Your " + skillName + " skill is now rank " + newLevel + "!")
                        .color(titleColor),
                Title.Times.times(Duration.ofMillis(20), Duration.ofMillis(3000), Duration.ofMillis(20))
        );
        player.showTitle(title);

        // Spawn the particle effect.
        world.spawnParticle(rankUpParticle, loc, 50, 1, 1, 1, 0.5);

        // Display an action bar message.
        ((Audience) player).sendActionBar(Component.text("Your " + skillName + " skill is now rank " + newLevel + "!")
                .color(NamedTextColor.LIGHT_PURPLE));
    }
}