package com.refinedmods.refinedstorage.apiimpl.autocrafting.task.v6;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.IComparer;
import com.refinedmods.refinedstorage.api.util.IStackList;
import com.refinedmods.refinedstorage.api.util.StackListEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public final class IoUtil {
    private static final int DEFAULT_EXTRACT_FLAGS = IComparer.COMPARE_NBT;

    private IoUtil() {
    }

    public static List<ItemStack> extractFromInternalItemStorage(List<ItemStack> list, IStorageDisk<ItemStack> storage, Action action) {
        List<ItemStack> extracted = new ArrayList<>();

        for (ItemStack stack : list) {
            ItemStack result = storage.extract(stack, stack.getCount(), DEFAULT_EXTRACT_FLAGS, action);

            if (result.isEmpty() || result.getCount() != stack.getCount()) {
                if (action == Action.PERFORM) {
                    throw new IllegalStateException("The internal crafting inventory reported that " + stack + " was available but we got " + result);
                }

                return null;
            }

            extracted.add(result);
        }

        return extracted;
    }

    public static List<FluidStack> extractFromInternalFluidStorage(List<FluidStack> list, IStorageDisk<FluidStack> storage, Action action) {
        List<FluidStack> extracted = new ArrayList<>();

        for (FluidStack stack : list) {
            FluidStack result = storage.extract(stack, stack.getAmount(), DEFAULT_EXTRACT_FLAGS, action);

            if (result.isEmpty() || result.getAmount() != stack.getAmount()) {
                if (action == Action.PERFORM) {
                    throw new IllegalStateException("The internal crafting inventory reported that " + stack + " was available but we got " + result);
                }

                return null;
            }

            extracted.add(result);
        }

        return extracted;
    }

    public static void extractItemsFromNetwork(IStackList<ItemStack> toExtractInitial, INetwork network, IStorageDisk<ItemStack> internalStorage) {
        if (toExtractInitial.isEmpty()) {
            return;
        }

        List<ItemStack> toRemove = new ArrayList<>();

        for (StackListEntry<ItemStack> toExtract : toExtractInitial.getStacks()) {
            ItemStack result = network.extractItem(toExtract.getStack(), toExtract.getStack().getCount(), Action.PERFORM);

            if (!result.isEmpty()) {
                internalStorage.insert(toExtract.getStack(), result.getCount(), Action.PERFORM);

                toRemove.add(result);
            }
        }

        for (ItemStack stack : toRemove) {
            toExtractInitial.remove(stack);
        }

        if (!toRemove.isEmpty()) {
            network.getCraftingManager().onTaskChanged();
        }
    }

    public static void extractFluidsFromNetwork(IStackList<FluidStack> toExtractInitial, INetwork network, IStorageDisk<FluidStack> internalStorage) {
        if (toExtractInitial.isEmpty()) {
            return;
        }

        List<FluidStack> toRemove = new ArrayList<>();

        for (StackListEntry<FluidStack> toExtract : toExtractInitial.getStacks()) {
            FluidStack result = network.extractFluid(toExtract.getStack(), toExtract.getStack().getAmount(), Action.PERFORM);

            if (!result.isEmpty()) {
                internalStorage.insert(result, result.getAmount(), Action.PERFORM);

                toRemove.add(result);
            }
        }

        for (FluidStack stack : toRemove) {
            toExtractInitial.remove(stack);
        }

        if (!toRemove.isEmpty()) {
            network.getCraftingManager().onTaskChanged();
        }
    }
}
