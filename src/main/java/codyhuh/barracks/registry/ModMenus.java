package codyhuh.barracks.registry;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.menus.LetterMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Barracks.MOD_ID);

    public static final RegistryObject<MenuType<LetterMenu>> LETTER = MENUS.register("letter", () -> new MenuType<>(LetterMenu::new, FeatureFlags.VANILLA_SET));
}