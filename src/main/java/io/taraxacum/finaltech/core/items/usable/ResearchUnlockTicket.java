package io.taraxacum.finaltech.core.items.usable;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.taraxacum.common.util.JavaUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * @author Final_ROOT
 * @since 2.0
 */
public class ResearchUnlockTicket extends UsableSlimefunItem {
    public ResearchUnlockTicket(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void function(@Nonnull PlayerRightClickEvent playerRightClickEvent) {
        Player player = playerRightClickEvent.getPlayer();
        List<Research> researchList = Slimefun.getRegistry().getResearches();
        researchList = JavaUtil.shuffle(researchList);
        Optional<PlayerProfile> playerProfile = PlayerProfile.find(player);
        Bukkit.getLogger().info("1");
        if(playerProfile.isPresent()) {
            Bukkit.getLogger().info("2");
            if(!playerProfile.get().hasUnlockedEverything()) {
                Bukkit.getLogger().info("3");
                PlayerProfile profile = playerProfile.get();
                for(Research research : researchList) {
                    if(!profile.hasUnlocked(research) && research.canUnlock(player)) {
                        ItemStack item = playerRightClickEvent.getItem();
                        item.setAmount(item.getAmount() - 1);
                        research.unlock(player, true);
                        return;
                    }
                }
            }
        }
    }
}
