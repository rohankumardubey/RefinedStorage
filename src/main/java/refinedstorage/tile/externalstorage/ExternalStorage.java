package refinedstorage.tile.externalstorage;

import net.minecraft.item.ItemStack;
import refinedstorage.RefinedStorageUtils;
import refinedstorage.api.storage.IStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class ExternalStorage implements IStorage {
    private List<ItemStack> cache = new ArrayList<ItemStack>();

    public abstract int getCapacity();

    public boolean updateCache() {
        List<ItemStack> items = getItems();

        if (items.size() != cache.size()) {
            cache = items;

            return true;
        } else {
            for (int i = 0; i < items.size(); ++i) {
                if (!RefinedStorageUtils.compareStack(items.get(i), cache.get(i))) {
                    cache = items;

                    return true;
                }
            }
        }

        return false;
    }

    public void updateCacheForcefully() {
        cache = getItems();
    }
}
