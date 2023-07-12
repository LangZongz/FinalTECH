package io.taraxacum.finaltech.core.item.usable.machine;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.core.interfaces.RecipeItem;
import io.taraxacum.finaltech.util.ConfigUtil;
import io.taraxacum.finaltech.util.RecipeUtil;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author Final_ROOT
 */
public class MachineAccelerateCardL3 extends AbstractMachineAccelerateCard implements RecipeItem {
    private final int times = ConfigUtil.getOrDefaultItemSetting(512, this, "times");

    public MachineAccelerateCardL3(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item) {
        super(itemGroup, item);
    }

    @Override
    protected int times() {
        return times;
    }

    @Override
    protected boolean consume() {
        return true;
    }

    @Override
    protected boolean conditionMatch(@Nonnull Player player) {
        if (player.getHealth() > player.getMaxHealth() * 0.1) {
            player.setHealth(player.getHealth() - player.getMaxHealth() * 0.1);
            return true;
        }
        return false;
    }

    @Override
    public void registerDefaultRecipes() {
        RecipeUtil.registerDescriptiveRecipe(FinalTech.getLanguageManager(), this,
                String.valueOf(this.times()));
    }
}
