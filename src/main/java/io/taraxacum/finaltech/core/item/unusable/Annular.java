package io.taraxacum.finaltech.core.item.unusable;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.core.interfaces.RecipeItem;
import io.taraxacum.finaltech.core.interfaces.UnCopiableItem;
import io.taraxacum.libs.plugin.dto.ItemWrapper;
import io.taraxacum.libs.plugin.util.ItemStackUtil;
import io.taraxacum.finaltech.util.RecipeUtil;
import io.taraxacum.libs.slimefun.interfaces.SimpleValidItem;
import io.taraxacum.libs.slimefun.util.SfItemUtil;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author Final_ROOT
 */
public class Annular extends UnusableSlimefunItem implements RecipeItem, SimpleValidItem, UnCopiableItem {
    private final ItemWrapper templateValidItem;

    public Annular(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType) {
        super(itemGroup, item, recipeType, new ItemStack[0]);
        ItemStack validItem = new ItemStack(this.getItem());
        SfItemUtil.setSpecialItemKey(validItem);
        this.templateValidItem = new ItemWrapper(validItem);
    }

    @Override
    public void registerDefaultRecipes() {
        RecipeUtil.registerDescriptiveRecipe(FinalTech.getLanguageManager(), this);
    }

    @Nonnull
    @Override
    public ItemStack getValidItem() {
        return ItemStackUtil.cloneItem(this.templateValidItem.getItemStack());
    }

    @Override
    public boolean verifyItem(@Nonnull ItemStack itemStack) {
        return ItemStackUtil.isItemSimilar(itemStack, this.templateValidItem);
    }
}
