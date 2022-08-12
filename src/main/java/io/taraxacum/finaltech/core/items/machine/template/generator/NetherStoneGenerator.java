package io.taraxacum.finaltech.core.items.machine.template.generator;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

public class NetherStoneGenerator extends AbstractGeneratorMachine{
    public NetherStoneGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    void registerRandomOutputRecipes() {
        this.registerRecipe(Tag.BASE_STONE_NETHER);
    }
}
