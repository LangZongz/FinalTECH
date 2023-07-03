package io.taraxacum.finaltech.core.item.machine.unit;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.core.inventory.AbstractMachineInventory;
import io.taraxacum.finaltech.core.inventory.unit.OneLineStorageUnitInventory;
import io.taraxacum.libs.plugin.dto.ItemAmountWrapper;
import io.taraxacum.finaltech.core.interfaces.RecipeItem;
import io.taraxacum.libs.plugin.dto.LocationData;
import io.taraxacum.libs.plugin.util.ItemStackUtil;
import io.taraxacum.finaltech.util.MachineUtil;
import io.taraxacum.finaltech.util.RecipeUtil;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Final_ROOT
 */
public class DistributeRightStorageUnit extends AbstractStorageUnit implements RecipeItem {
    public DistributeRightStorageUnit(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item) {
        super(itemGroup, item);
    }

    @Nullable
    @Override
    protected AbstractMachineInventory setMachineInventory() {
        return new OneLineStorageUnitInventory(this);
    }

    @Override
    protected void tick(@Nonnull Block block, @Nonnull SlimefunItem slimefunItem, @Nonnull LocationData locationData) {
        Inventory inventory = FinalTech.getLocationDataService().getInventory(locationData);
        if(inventory == null) {
            return;
        }

        int beginSlot = 0;
        int endSlot = 0;
        int i;

        ItemAmountWrapper itemAmountWrapper = null;
        for (i = 0; i < this.getInputSlot().length; i++) {
            if (!ItemStackUtil.isItemNull(inventory.getItem(i))) {
                itemAmountWrapper = new ItemAmountWrapper(inventory.getItem(i));
                beginSlot = i;
                endSlot = i++;
                break;
            }
        }

        for (; i < this.getInputSlot().length; i++) {
            if (ItemStackUtil.isItemNull(inventory.getItem(i))) {
                endSlot = i;
            } else if (ItemStackUtil.isItemSimilar(itemAmountWrapper, inventory.getItem(i))) {
                itemAmountWrapper.addAmount(inventory.getItem(i).getAmount());
                endSlot = i;
            } else {
                int amount = itemAmountWrapper.getAmount() / (endSlot + 1 - beginSlot);
                if (amount > 0) {
                    ItemStack itemStack = ItemStackUtil.cloneItem(itemAmountWrapper.getItemStack());
                    for (int j = beginSlot; j <= endSlot; j++) {
                        itemStack.setAmount(amount);
                        inventory.setItem(j, itemStack);
                    }
                    itemStack.setAmount(amount + (itemAmountWrapper.getAmount() % (endSlot + 1 - beginSlot)));
                    inventory.setItem(beginSlot, itemStack);
                }
                itemAmountWrapper = new ItemAmountWrapper(inventory.getItem(i));
                beginSlot = i;
                endSlot = i;
            }
        }

        if (beginSlot != endSlot) {
            int amount = itemAmountWrapper.getAmount() / (endSlot + 1 - beginSlot);
            if (amount > 0) {
                ItemStack itemStack = ItemStackUtil.cloneItem(itemAmountWrapper.getItemStack());
                for (int j = beginSlot; j <= endSlot; j++) {
                    itemStack.setAmount(amount);
                    inventory.setItem(j, itemStack);
                }
                itemStack.setAmount(amount + itemAmountWrapper.getAmount() % (endSlot + 1 - beginSlot));
                inventory.setItem(beginSlot, itemStack);
            }
        }
    }

    @Override
    public void registerDefaultRecipes() {
        RecipeUtil.registerDescriptiveRecipe(FinalTech.getLanguageManager(), this,
                String.valueOf(MachineUtil.calMachineSlotSize(this)));
    }
}
