package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.items.AllegiantArrowItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Barracks.MOD_ID);

    public static final RegistryObject<Item> ALLEGIANT_ARROW = ITEMS.register("allegiant_arrow", () -> new AllegiantArrowItem(new Item.Properties()));
}
