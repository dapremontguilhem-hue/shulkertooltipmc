package com.shulkerview;

import net.minecraft.client.item.TooltipData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Map;

/** Contenu (27 slots) + couleur de la shulker box. */
public record ShulkerTooltipData(List<ItemStack> stacks, int color) implements TooltipData {
    public static final int COLUMNS = 9;
    public static final int ROWS = 3;

    private static final int DEFAULT_COLOR = 0x956895; // shulker non teintée

    private static final Map<String, Integer> COLORS = Map.ofEntries(
            Map.entry("white", 0xE9ECEC), Map.entry("orange", 0xF07613),
            Map.entry("magenta", 0xBD44B3), Map.entry("light_blue", 0x3AB3DA),
            Map.entry("yellow", 0xFED83D), Map.entry("lime", 0x80C71F),
            Map.entry("pink", 0xF38BAA), Map.entry("gray", 0x474F52),
            Map.entry("light_gray", 0x9D9D97), Map.entry("cyan", 0x169C9C),
            Map.entry("purple", 0x8932B8), Map.entry("blue", 0x3C44AA),
            Map.entry("brown", 0x835432), Map.entry("green", 0x5E7C16),
            Map.entry("red", 0xB02E26), Map.entry("black", 0x1D1D21)
    );

    /** Retourne null si ce n'est pas une shulker box ou si elle est vide. */
    public static ShulkerTooltipData from(ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem blockItem)
                || !(blockItem.getBlock() instanceof ShulkerBoxBlock)) {
            return null;
        }

        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) return null;

        DefaultedList<ItemStack> slots = DefaultedList.ofSize(COLUMNS * ROWS, ItemStack.EMPTY);
        container.copyTo(slots);

        boolean hasItems = false;
        for (ItemStack s : slots) {
            if (!s.isEmpty()) { hasItems = true; break; }
        }
        if (!hasItems) return null;

        return new ShulkerTooltipData(List.copyOf(slots), colorOf(stack));
    }

    private static int colorOf(ItemStack stack) {
        String path = Registries.ITEM.getId(stack.getItem()).getPath();
        String suffix = "_shulker_box";
        if (path.endsWith(suffix)) {
            String dye = path.substring(0, path.length() - suffix.length());
            Integer c = COLORS.get(dye);
            if (c != null) return c;
        }
        return DEFAULT_COLOR;
    }
}
