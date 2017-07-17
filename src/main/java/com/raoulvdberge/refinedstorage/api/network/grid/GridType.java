package com.raoulvdberge.refinedstorage.api.network.grid;

import net.minecraft.util.IStringSerializable;

public enum GridType implements IStringSerializable {
    NORMAL(0, "normal"),
    CRAFTING(1, "crafting"),
    PATTERN(2, "pattern"),
    FLUID(3, "fluid");

    private int id;
    private String name;

    GridType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}