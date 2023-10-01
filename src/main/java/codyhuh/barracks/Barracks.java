package codyhuh.barracks;

import codyhuh.barracks.registry.ModEntities;
import codyhuh.barracks.registry.ModItems;
import codyhuh.barracks.registry.ModMenus;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(Barracks.MOD_ID)
public class Barracks {
    public static final String MOD_ID = "barracks";

    public Barracks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        ModMenus.MENUS.register(bus);

        bus.addListener(this::populateTabs);
    }

    private void populateTabs(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.COMBAT) {
            for (RegistryObject<Item> item :ModItems.ITEMS.getEntries()) {
                e.accept(item.get());
            }
        }
    }
}
