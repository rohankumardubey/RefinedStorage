package com.refinedmods.refinedstorage.apiimpl.network.node.cover;

import com.refinedmods.refinedstorage.RSItems;
import net.minecraft.world.item.ItemStack;

public enum CoverType {
    NORMAL,
    HOLLOW;

    public ItemStack createStack() {
        return new ItemStack(this == NORMAL ? RSItems.COVER.get() : RSItems.HOLLOW_COVER.get());
    }
}
