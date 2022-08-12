package io.taraxacum.finaltech.api.dto;

import io.taraxacum.finaltech.util.ItemStackUtil;
import io.taraxacum.finaltech.util.MachineUtil;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Final_ROOT
 * @since 2.0
 */
public class AdvancedCraft {
    @Nonnull
    private List<ItemAmountWrapper> inputItemList;
    @Nonnull
    private List<ItemAmountWrapper> outputItemList;
    @Nonnull
    private List<List<Integer>> consumeSlotList;
    private int matchCount;
    private int offset;

    private AdvancedCraft(@Nonnull List<ItemAmountWrapper> inputItemList, @Nonnull List<ItemAmountWrapper> outputItemList, @Nonnull List<List<Integer>> consumeSlotList, int matchCount, int offset) {
        this.inputItemList = inputItemList;
        this.outputItemList = outputItemList;
        this.consumeSlotList = consumeSlotList;
        this.matchCount = matchCount;
        this.offset = offset;
    }

    @Nonnull
    public List<ItemAmountWrapper> getInputItemList() {
        return inputItemList;
    }

    public void setInputItemList(@Nonnull List<ItemAmountWrapper> inputItemList) {
        this.inputItemList = inputItemList;
    }

    @Nonnull
    public List<ItemAmountWrapper> getOutputItemList() {
        return outputItemList;
    }

    public void setOutputItemList(@Nonnull List<ItemAmountWrapper> outputItemList) {
        this.outputItemList = outputItemList;
    }

    @Nonnull
    public List<List<Integer>> getConsumeSlotList() {
        return consumeSlotList;
    }

    public void setConsumeSlotList(@Nonnull List<List<Integer>> consumeSlotList) {
        this.consumeSlotList = consumeSlotList;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Consume item after we know how a machine should work.
     */
    public void consumeItem(@Nonnull Inventory inventory) {
        for (int i = 0; i < this.inputItemList.size(); i++) {
            int consumeItemAmount = this.inputItemList.get(i).getAmount() * this.matchCount;
            for (int slot : this.consumeSlotList.get(i)) {
                ItemStack item = inventory.getItem(slot);
                int itemConsumeAmount = Math.min(consumeItemAmount, item.getAmount());
                item.setAmount(item.getAmount() - itemConsumeAmount);
                consumeItemAmount -= itemConsumeAmount;
                if (consumeItemAmount == 0) {
                    break;
                }
            }
        }
    }

    @Nonnull
    public MachineRecipe calMachineRecipe(int ticks) {
        if (this.matchCount > 0) {
            return new MachineRecipe(ticks, ItemStackUtil.calEnlargeItemArray(this.inputItemList, this.matchCount), ItemStackUtil.calEnlargeItemArray(this.outputItemList, this.matchCount));
        } else {
            return new MachineRecipe(ticks, new ItemStack[0], new ItemStack[0]);
        }
    }

    /**
     * Cal the craft work a machine will do.
     * @param inventory the container that contains items and all operation will do here.
     * @param inputSlots where the items will be consumed to match the {@link MachineRecipe}.
     * @param advancedMachineRecipeList list of {@link AdvancedMachineRecipe} that a machine can work to.
     * @param quantityModule how many times a machine will work in max.
     * @param offset machine-recipe will begin in the given offset.
     * @return
     */
    @Nullable
    public static AdvancedCraft craftAsc(@Nonnull Inventory inventory, int[] inputSlots, @Nonnull List<AdvancedMachineRecipe> advancedMachineRecipeList, int quantityModule, int offset) {
        Map<Integer, ItemWrapper> inputItemSlotMap = MachineUtil.getSlotItemWrapperMap(inventory, inputSlots);
        List<List<Integer>> consumeSlotList = new ArrayList<>(inputItemSlotMap.size());
        List<Integer> skipSlotList = new ArrayList<>(inputItemSlotMap.size());
        int matchCount;
        int matchAmount;
        List<Integer> slotList = new ArrayList<>(inputItemSlotMap.size());
        for (int i = 0, length = advancedMachineRecipeList.size(); i < length; i++) {
            AdvancedMachineRecipe advancedMachineRecipe = advancedMachineRecipeList.get((i + offset) % length);
            List<ItemAmountWrapper> recipeInputItemList = advancedMachineRecipe.getInput();
            matchCount = quantityModule;
            for (ItemAmountWrapper recipeInputItem : recipeInputItemList) {
                matchAmount = 0;
                slotList.clear();
                for (Map.Entry<Integer, ItemWrapper> inputItemEntry : inputItemSlotMap.entrySet()) {
                    if (skipSlotList.contains(inputItemEntry.getKey())) {
                        continue;
                    }
                    if (ItemStackUtil.isItemSimilar(recipeInputItem, inputItemEntry.getValue())) {
                        matchAmount += inputItemEntry.getValue().getItemStack().getAmount();
                        skipSlotList.add(inputItemEntry.getKey());
                        slotList.add(inputItemEntry.getKey());
                    }
                    if (matchAmount / recipeInputItem.getAmount() >= matchCount) {
                        break;
                    }
                }
                matchCount = Math.min(matchCount, matchAmount / recipeInputItem.getAmount());
                if (matchCount == 0) {
                    break;
                }
                consumeSlotList.add(slotList);
            }

            if (matchCount > 0) {
                List<ItemAmountWrapper> recipeOutputItemList = advancedMachineRecipe.getOutput();
                return new AdvancedCraft(advancedMachineRecipe.getInput(), recipeOutputItemList, consumeSlotList, matchCount, (i + offset) % length);
            }
            skipSlotList.clear();
            consumeSlotList.clear();
        }
        return null;
    }
    @Nullable
    public static AdvancedCraft craftDesc(@Nonnull Inventory inventory, int[] inputSlots, @Nonnull List<AdvancedMachineRecipe> advancedMachineRecipeList, int quantityModule, int offset) {
        Map<Integer, ItemWrapper> inputItemSlotMap = MachineUtil.getSlotItemWrapperMap(inventory, inputSlots);
        List<List<Integer>> consumeSlotList = new ArrayList<>(inputItemSlotMap.size());
        List<Integer> skipSlotList = new ArrayList<>(inputItemSlotMap.size());
        int matchCount;
        int matchAmount;
        List<Integer> slotList = new ArrayList<>(inputItemSlotMap.size());
        for (int i = 0, length = advancedMachineRecipeList.size(); i < length; i++) {
            AdvancedMachineRecipe advancedMachineRecipe = advancedMachineRecipeList.get((offset - i + length + length) % length);
            List<ItemAmountWrapper> recipeInputItemList = advancedMachineRecipe.getInput();
            matchCount = quantityModule;
            for (ItemAmountWrapper recipeInputItem : recipeInputItemList) {
                matchAmount = 0;
                slotList.clear();
                for (Map.Entry<Integer, ItemWrapper> inputItemEntry : inputItemSlotMap.entrySet()) {
                    if (skipSlotList.contains(inputItemEntry.getKey())) {
                        continue;
                    }
                    if (ItemStackUtil.isItemSimilar(recipeInputItem, inputItemEntry.getValue())) {
                        matchAmount += inputItemEntry.getValue().getItemStack().getAmount();
                        skipSlotList.add(inputItemEntry.getKey());
                        slotList.add(inputItemEntry.getKey());
                    }
                    if (matchAmount / recipeInputItem.getAmount() >= matchCount) {
                        break;
                    }
                }
                matchCount = Math.min(matchCount, matchAmount / recipeInputItem.getAmount());
                if (matchCount == 0) {
                    break;
                }
                consumeSlotList.add(slotList);
            }

            if (matchCount > 0) {
                List<ItemAmountWrapper> recipeOutputItemList = advancedMachineRecipe.getOutput();
                return new AdvancedCraft(advancedMachineRecipe.getInput(), recipeOutputItemList, consumeSlotList, matchCount, (offset - i + length + length) % length);
            }
            skipSlotList.clear();
            consumeSlotList.clear();
        }
        return null;
    }
}
