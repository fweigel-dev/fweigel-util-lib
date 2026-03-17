package dev.fweigel.mobutils.core.client.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;

/**
 * Utility methods for working with collections of item stacks.
 */
public final class ItemStackUtils {

    private ItemStackUtils() {}

    /**
     * Returns the first non-empty stack if all non-empty stacks share the same item type.
     * Returns {@link ItemStack#EMPTY} if the collection is empty, all stacks are empty,
     * or more than one item type is present.
     */
    public static ItemStack getUniformItem(Iterable<ItemStack> stacks) {
        Item singleType = null;
        ItemStack firstStack = ItemStack.EMPTY;
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                if (singleType == null) {
                    singleType = stack.getItem();
                    firstStack = stack;
                } else if (singleType != stack.getItem()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return firstStack;
    }

    /**
     * Overload for {@link Container} (e.g. block entities).
     */
    public static ItemStack getUniformItem(Container container) {
        Item singleType = null;
        ItemStack firstStack = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (singleType == null) {
                    singleType = stack.getItem();
                    firstStack = stack;
                } else if (singleType != stack.getItem()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return firstStack;
    }
}
