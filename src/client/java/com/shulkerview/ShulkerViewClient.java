package com.shulkerview;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class ShulkerViewClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Transforme nos données (ShulkerTooltipData) en composant de rendu.
        TooltipComponentCallback.EVENT.register(data ->
                data instanceof ShulkerTooltipData d ? new ShulkerTooltipComponent(d) : null);
    }
}
