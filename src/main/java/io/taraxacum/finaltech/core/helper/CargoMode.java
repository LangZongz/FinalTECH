package io.taraxacum.finaltech.core.helper;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.api.factory.BlockStorageHelper;
import io.taraxacum.finaltech.api.factory.BlockStorageIconHelper;
import io.taraxacum.finaltech.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

/**
 * @author Final_ROOT
 */
public final class CargoMode {
    public static final String KEY = "cm";

    public static final String VALUE_INPUT_MAIN = "im";
    public static final String VALUE_OUTPUT_MAIN = "om";
    public static final String VALUE_STRONG_SYMMETRY = "ss";
    public static final String VALUE_WEAK_SYMMETRY = "ws";

    private static final ItemStack INPUT_MAIN_ICON = new CustomItemStack(Material.WATER_BUCKET, FinalTech.getLanguageString("helper", "cargo-mode", "input-main", "name"), FinalTech.getLanguageStringArray("helper", "cargo-mode", "input-main", "lore"));
    private static final ItemStack OUTPUT_MAIN_ICON = new CustomItemStack(Material.LAVA_BUCKET, FinalTech.getLanguageString("helper", "cargo-mode", "output-main", "name"), FinalTech.getLanguageStringArray("helper", "cargo-mode", "output-main", "lore"));
    private static final ItemStack STRONG_SYMMETRY_ICON = new CustomItemStack(Material.MILK_BUCKET, FinalTech.getLanguageString("helper", "cargo-mode", "strong-symmetry", "name"), FinalTech.getLanguageStringArray("helper", "cargo-mode", "strong-symmetry", "lore"));
    private static final ItemStack WEAK_SYMMETRY_ICON = new CustomItemStack(Material.MILK_BUCKET, FinalTech.getLanguageString("helper", "cargo-mode", "weak-symmetry", "name"), FinalTech.getLanguageStringArray("helper", "cargo-mode", "weak-symmetry", "lore"));

    public static final BlockStorageIconHelper HELPER = BlockStorageIconHelper.newInstanceOrGet(BlockStorageHelper.ID_CARGO, KEY, new LinkedHashMap<>() {{
        this.put(VALUE_STRONG_SYMMETRY, STRONG_SYMMETRY_ICON);
        this.put(VALUE_WEAK_SYMMETRY, WEAK_SYMMETRY_ICON);
        this.put(VALUE_INPUT_MAIN, INPUT_MAIN_ICON);
        this.put(VALUE_OUTPUT_MAIN, OUTPUT_MAIN_ICON);
    }});
}
