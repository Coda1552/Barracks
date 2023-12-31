package codyhuh.barracks.common.menus;

import codyhuh.barracks.registry.ModItems;
import codyhuh.barracks.registry.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LetterMenu extends AbstractContainerMenu {
    private final LetterInventory stackInventory;
    private ItemStack itemStack;

    public LetterMenu(int id, Inventory inventory) {
        this(id, inventory, ItemStack.EMPTY);
    }

    public LetterMenu(int id, Inventory playerInv, ItemStack inventoryStack) {
        super(ModMenus.LETTER.get(), id);
        LetterInventory inventory = getStackInventory(inventoryStack);
        checkContainerSize(inventory, 6);
        this.stackInventory = inventory;
        this.itemStack = inventoryStack;
        inventory.startOpen(playerInv.player);

/*        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(inventory, j + i * 3, 62 + j * 18, 35 + i * 18) {

                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return !pStack.is(ModItems.GUI_OPENER.get());
                    }
                });
            }
        }*/
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new PlayerInventorySlot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new PlayerInventorySlot(playerInv, k, 8 + k * 18, 142));
        }
    }

    private static LetterInventory getStackInventory(ItemStack stack) {
        LetterInventory inventory = new LetterInventory(6);
        if (!stack.isEmpty() && stack.hasTag()) {
            ListTag items = stack.getOrCreateTag().getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag item = items.getCompound(i);
                inventory.setItem(item.getByte("Slot"), ItemStack.of(item));
            }
        }
        return inventory;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index != 0) {
                if (index == 1) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 1 && index < 28) {
                    if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 28 && index < 37 && !this.moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stackInventory.stillValid(player);
    }

    public void removed(Player player) {
        super.removed(player);
        this.stackInventory.stopOpen(player);
        if (!player.level().isClientSide && stackInventory.isDirty()) {
            stackInventory.write(itemStack);
        }
    }

    private class PlayerInventorySlot extends Slot {
        public PlayerInventorySlot(Inventory inventory, int index, int xPosition, int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }

        @Override
        public void set(ItemStack stack) {
            if (!itemStack.isEmpty() && getItem() == itemStack) {
                itemStack = ItemStack.EMPTY;
            } else if (stack.getItem() == ModItems.GUI_OPENER.get()) {
                itemStack = stack;
            }
            super.set(stack);
        }
    }
}