package io.taraxacum.finaltech.core.task.effect;

import io.taraxacum.finaltech.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 * @author Final_ROOT
 * @since 2.0
 */
public class UntreatableEffect extends AbstractEffect {
    private double health;

    public UntreatableEffect(int time, int level) {
        super(time, level);
    }

    @Override
    public String getId() {
        return "UNTREATABLE";
    }

    @Override
    public int maxLevel() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void tick(@Nonnull LivingEntity livingEntity) {
        double nowHealth = livingEntity.getHealth();
        if(this.health > 0 && this.health < livingEntity.getHealth()) {
            livingEntity.setHealth(this.health);
        }
        this.health = Math.min(this.health, Math.min(livingEntity.getHealth(), nowHealth));
        Location location = livingEntity.getLocation();
        location.getWorld().spawnParticle(Particle.FALLING_LAVA, location, 1);
    }

    @Override
    public void startTick(@Nonnull LivingEntity livingEntity) {
        this.health = livingEntity.getHealth();
    }

    @Override
    public void endTick(@Nonnull LivingEntity livingEntity) {
    }

    @Override
    public void addTick(@Nonnull LivingEntity livingEntity) {
    }

    @Override
    public boolean isSync() {
        return false;
    }
}
