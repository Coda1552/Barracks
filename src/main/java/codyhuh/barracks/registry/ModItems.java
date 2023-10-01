package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.items.AllegiantArrowItem;
import codyhuh.barracks.common.items.GuiOpenerItem;
import codyhuh.barracks.common.items.SpindlyGauntletItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Barracks.MOD_ID);

    public static final RegistryObject<Item> ALLEGIANT_ARROW = ITEMS.register("allegiant_arrow", () -> new AllegiantArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> GUI_OPENER = ITEMS.register("gui_opener", () -> new GuiOpenerItem(new Item.Properties()));
    public static final RegistryObject<Item> SPINDLY_GAUNTLET = ITEMS.register("spindly_gauntlet", () -> new SpindlyGauntletItem(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1).durability(320)));
}
