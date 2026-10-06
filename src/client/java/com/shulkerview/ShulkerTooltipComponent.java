package com.shulkerview;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

/** Dessine l'intérieur de la shulker : grille 9x3 teintée + items. */
public class ShulkerTooltipComponent implements TooltipComponent {
    private static final int SLOT = 18;
    private static final int FRAME_W = ShulkerTooltipData.COLUMNS * SLOT + 2;
    private static final int FRAME_H = ShulkerTooltipData.ROWS * SLOT + 2;
    private static final int MARGIN = 2;

    private final ShulkerTooltipData data;

    public ShulkerTooltipComponent(ShulkerTooltipData data) {
        this.data = data;
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return FRAME_H + MARGIN * 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return FRAME_W;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        int top = y + MARGIN;
        int base = data.color();

        int frame = argb(mix(base, 0x000000, 0.25f));
        int slotLight = argb(mix(base, 0xFFFFFF, 0.15f));
        int slotDark = argb(mix(base, 0x000000, 0.80f));
        int slotInner = argb(mix(base, 0x000000, 0.60f));

        // Cadre extérieur (couleur de la shulker)
        context.fill(x, top, x + FRAME_W, top + FRAME_H, frame);

        for (int row = 0; row < ShulkerTooltipData.ROWS; row++) {
            for (int col = 0; col < ShulkerTooltipData.COLUMNS; col++) {
                int sx = x + 1 + col * SLOT;
                int sy = top + 1 + row * SLOT;

                // Slot biseauté façon inventaire
                context.fill(sx, sy, sx + SLOT, sy + SLOT, slotLight);
                context.fill(sx, sy, sx + SLOT - 1, sy + SLOT - 1, slotDark);
                context.fill(sx + 1, sy + 1, sx + SLOT - 1, sy + SLOT - 1, slotInner);

                ItemStack stack = data.stacks().get(row * ShulkerTooltipData.COLUMNS + col);
                if (!stack.isEmpty()) {
                    context.drawItem(stack, sx + 1, sy + 1);
                    context.drawStackOverlay(textRenderer, stack, sx + 1, sy + 1);
                }
            }
        }
    }

    private static int argb(int rgb) {
        return 0xFF000000 | rgb;
    }

    /** Mélange la couleur `rgb` vers `target` (t = 0 → rgb, t = 1 → target). */
    private static int mix(int rgb, int target, float t) {
        int r = Math.round(((rgb >> 16) & 0xFF) * (1 - t) + ((target >> 16) & 0xFF) * t);
        int g = Math.round(((rgb >> 8) & 0xFF) * (1 - t) + ((target >> 8) & 0xFF) * t);
        int b = Math.round((rgb & 0xFF) * (1 - t) + (target & 0xFF) * t);
        return (r << 16) | (g << 8) | b;
    }
}
