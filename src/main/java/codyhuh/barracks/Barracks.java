package codyhuh.barracks;

import codyhuh.barracks.registry.ModEntities;
import codyhuh.barracks.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Barracks.MOD_ID)
public class Barracks {
    public static final String MOD_ID = "barracks";

    public Barracks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
    }
}
