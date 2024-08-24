package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.items.AllegiantArrowItem;
import codyhuh.barracks.common.items.GuiOpenerItem;
import codyhuh.barracks.common.items.SpindlyGauntletItem;
import codyhuh.barracks.common.items.SpiritSnatcherItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Barracks.MOD_ID);

    // Weapons
    public static final RegistryObject<Item> ALLEGIANT_ARROW = ITEMS.register("allegiant_arrow", () -> new AllegiantArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> SPINDLY_GAUNTLET = ITEMS.register("spindly_gauntlet", () -> new SpindlyGauntletItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).durability(320)));
    public static final RegistryObject<Item> SPIRIT_SNATCHER = ITEMS.register("spirit_snatcher", () -> new SpiritSnatcherItem(Tiers.DIAMOND, 3, -2.4F, new Item.Properties().rarity(Rarity.create("spirit", ChatFormatting.GREEN)).stacksTo(1).durability(320)));

    // Spawn Eggs
    public static final RegistryObject<Item> FAA_SPAWN_EGG = ITEMS.register("faa_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FAA, 0x000000, 0xffffff, new Item.Properties()));

    // Other
    public static final RegistryObject<Item> GUI_OPENER = ITEMS.register("gui_opener", () -> new GuiOpenerItem(new Item.Properties()));
}
