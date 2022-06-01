package io.taraxacum.finaltech.core.items.machine.standard;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.core.menu.standard.AbstractStandardMachineMenu;
import io.taraxacum.finaltech.api.operation.OrderedDustOperation;
import io.taraxacum.finaltech.core.menu.standard.DustFactoryMenu;
import io.taraxacum.finaltech.setup.FinalTechItems;
import io.taraxacum.finaltech.util.TextUtil;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Be used to make dust
 * input 16 kinds of item and 1024 items in total to make a dust
 * if you input more than 16 kinds of item or more than 1024 item in total
 * it will output unordered dust
 * @author Final_ROOT
 */
public class DustFactoryDirt extends AbstractStandardMachine {
    public static final int TYPE_DIFFICULTY = 16;
    public static final int AMOUNT_DIFFICULTY = 1024;

    public DustFactoryDirt(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    protected AbstractStandardMachineMenu setMachineMenu() {
        return new DustFactoryMenu(this);
    }

    @Override
    protected void tick(@Nonnull Block block, @Nonnull SlimefunItem slimefunItem, @Nonnull Config config) {
        BlockMenu inv = BlockStorage.getInventory(block);
        OrderedDustOperation operation = (OrderedDustOperation)this.getMachineProcessor().getOperation(block);

        for (int slot : this.getInputSlot()) {
            ItemStack inputItem = inv.getItemInSlot(slot);
            if (inputItem == null) {
                continue;
            }
            if (operation == null) {
                operation = new OrderedDustOperation();
                this.getMachineProcessor().startOperation(block, operation);
            }
            operation.addItem(inputItem);

            if (operation.isFinished()) {
                if (operation.isOrderedDust() && InvUtils.fitAll(inv.toInventory(), new ItemStack[] {FinalTechItems.ORDERED_DUST}, this.getOutputSlot())) {
                    inv.pushItem(new ItemStack(FinalTechItems.ORDERED_DUST), this.getOutputSlot());
                    this.getMachineProcessor().endOperation(block);
                    operation = null;
                } else if (InvUtils.fitAll(inv.toInventory(), new ItemStack[] {FinalTechItems.UNORDERED_DUST}, this.getOutputSlot())) {
                    inv.pushItem(new ItemStack(FinalTechItems.UNORDERED_DUST), this.getOutputSlot());
                    this.getMachineProcessor().endOperation(block);
                    operation = null;
                }
            }
            inv.consumeItem(slot, inputItem.getAmount());
        }

        if (operation == null) {
            operation = new OrderedDustOperation();
            this.getMachineProcessor().startOperation(block, operation);
        }
        CustomItemStack progress = new CustomItemStack(Material.REDSTONE, "&f完成进度",
                "&7匹配的物品种类" + operation.getTypeCount() + "/" + TYPE_DIFFICULTY,
                "&7输入的物品总数" + operation.getAmountCount() + "/" + AMOUNT_DIFFICULTY);
        inv.replaceExistingItem(22, progress);
    }

    @Override
    public void registerDefaultRecipes() {
        this.registerDescriptiveRecipe(TextUtil.COLOR_PASSIVE + "制造 " + FinalTechItems.UNORDERED_DUST.getDisplayName(),
                "",
                TextUtil.COLOR_NORMAL + "输入至少 " + TextUtil.COLOR_NUMBER + AMOUNT_DIFFICULTY + "个" + TextUtil.COLOR_NORMAL + " 任意物品",
                TextUtil.COLOR_NORMAL + "输入至少 " + TextUtil.COLOR_NUMBER + TYPE_DIFFICULTY + "种" + TextUtil.COLOR_NORMAL + " 不同物品",
                TextUtil.COLOR_NORMAL + "同时满足以上两个条件时生成一个 " + FinalTechItems.UNORDERED_DUST.getDisplayName());
        this.registerDescriptiveRecipe(TextUtil.COLOR_PASSIVE + "制造 " + FinalTechItems.ORDERED_DUST.getDisplayName(),
                "",
                TextUtil.COLOR_NORMAL + "输入恰好 " + TextUtil.COLOR_NUMBER + AMOUNT_DIFFICULTY + "个" + TextUtil.COLOR_NORMAL + " 任意物品",
                TextUtil.COLOR_NORMAL + "输入恰好 " + TextUtil.COLOR_NUMBER + TYPE_DIFFICULTY + "种" + TextUtil.COLOR_NORMAL + " 不同物品",
                TextUtil.COLOR_NORMAL + "同时满足以上两个条件时生成一个 " + FinalTechItems.ORDERED_DUST.getDisplayName());
    }
}
