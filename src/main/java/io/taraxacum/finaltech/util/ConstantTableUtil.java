package io.taraxacum.finaltech.util;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * @author Final_ROOT
 */
public class ConstantTableUtil {
    public static final String CONFIG_ID = "id";

    public static final String CONFIG_CHARGE = "energy-charge";

    public static final String CONFIG_SLEEP = "sleep";

    public static final String CONFIG_UUID = "owner";

    public static final int ITEM_COPY_CARD_AMOUNT = ConfigUtil.getOrDefaultItemSetting(16777216, "COPY_CARD", "amount");

    public static final int ITEM_SINGULARITY_AMOUNT = ConfigUtil.getOrDefaultItemSetting(256, "SINGULARITY", "amount") + Slimefun.getInstalledAddons().size() * 16;

    public static final int ITEM_SPIROCHETE_AMOUNT = ConfigUtil.getOrDefaultItemSetting(64, "SPIROCHETE", "amount") + (int) (Math.pow(Slimefun.getRegistry().getAllSlimefunItems().size(), 0.5) * 4);

    public static final int ITEM_MAX_STACK = 64;

    public static final double WARNING_TPS = 19.9;

    public static final double FULL_TPS = 20;

    public static final String SLIMEFUN_TICK_INTERVAL = String.format("%.2f", Slimefun.getTickerTask().getTickRate() / 20.0);

    public static final String RAW_MATERIAL_ID_PREFIX = "minecraft_";
}
