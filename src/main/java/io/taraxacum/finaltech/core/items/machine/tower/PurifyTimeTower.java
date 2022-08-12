package io.taraxacum.finaltech.core.items.machine.tower;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.api.interfaces.RecipeItem;
import io.taraxacum.finaltech.core.menu.AbstractMachineMenu;
import io.taraxacum.finaltech.core.menu.unit.StatusL2Menu;
import io.taraxacum.finaltech.util.ItemStackUtil;
import io.taraxacum.finaltech.util.LocationUtil;
import io.taraxacum.finaltech.util.MachineUtil;
import io.taraxacum.finaltech.util.SlimefunUtil;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Final_ROOT
 * @since 2.0
 */
public class PurifyTimeTower extends AbstractTower implements RecipeItem {
    private final double baseRange = FinalTech.getValueManager().getOrDefault(3.2, "items", SlimefunUtil.getIdFormatName(PurifyTimeTower.class), "range-base");
    private final double mulRange = FinalTech.getValueManager().getOrDefault(0.2, "items", SlimefunUtil.getIdFormatName(PurifyTimeTower.class), "range-mul");

    public PurifyTimeTower(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    protected BlockPlaceHandler onBlockPlace() {
        return MachineUtil.BLOCK_PLACE_HANDLER_PLACER_DENY;
    }

    @Nonnull
    @Override
    protected BlockBreakHandler onBlockBreak() {
        return MachineUtil.simpleBlockBreakerHandler(this);
    }

    @Nonnull
    @Override
    protected AbstractMachineMenu setMachineMenu() {
        return new StatusL2Menu(this);
    }

    @Override
    protected void tick(@Nonnull Block block, @Nonnull SlimefunItem slimefunItem, @Nonnull Config config) {
        Location location = block.getLocation();
        BlockMenu blockMenu = BlockStorage.getInventory(location);
        ItemStack item = blockMenu.getItemInSlot(this.getInputSlot()[0]);
        JavaPlugin javaPlugin = this.getAddon().getJavaPlugin();
        double range = this.baseRange;
        if(ItemStackUtil.isItemSimilar(item, this.getItem())) {
            range += item.getAmount() * this.mulRange;
        }

        final double finalRange = range;
        javaPlugin.getServer().getScheduler().runTask(javaPlugin, () -> {
            int count = 0;
            for(Entity entity : location.getWorld().getNearbyEntities(LocationUtil.getCenterLocation(block), finalRange, finalRange, finalRange, entity -> entity instanceof LivingEntity)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                for(PotionEffect potionEffect : livingEntity.getActivePotionEffects()) {
                    livingEntity.removePotionEffect(potionEffect.getType());
                    if(potionEffect.getDuration() > 1) {
                        livingEntity.addPotionEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration() / 2, potionEffect.getAmplifier()));
                    }
                }
                count++;
            }

            if(blockMenu.hasViewer()) {
                PurifyTimeTower.this.updateMenu(blockMenu, count, finalRange);
            }
        });
    }

    private void updateMenu(@Nonnull BlockMenu blockMenu, int amount, double range) {
        ItemStack item = blockMenu.getItemInSlot(StatusL2Menu.STATUS_SLOT);
        ItemStackUtil.setLore(item, SlimefunUtil.updateMenuLore(FinalTech.getLanguageManager(), this,
                String.valueOf(amount),
                String.valueOf(range)));
    }

    @Override
    public void registerDefaultRecipes() {
        SlimefunUtil.registerDescriptiveRecipe(FinalTech.getLanguageManager(), this,
                String.valueOf(this.baseRange),
                String.valueOf(this.mulRange));
    }
}
