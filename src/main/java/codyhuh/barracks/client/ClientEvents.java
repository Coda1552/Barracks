package codyhuh.barracks.client;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.client.render.AllegiantArrowRenderer;
import codyhuh.barracks.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Barracks.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        EntityRenderers.register(ModEntities.ALLEGIANT_ARROW.get(), AllegiantArrowRenderer::new);
    }
}
