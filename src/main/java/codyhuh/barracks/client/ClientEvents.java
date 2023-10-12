package codyhuh.barracks.client;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.client.model.FaaModel;
import codyhuh.barracks.client.renders.AllegiantArrowRenderer;
import codyhuh.barracks.client.renders.FaaRenderer;
import codyhuh.barracks.client.screen.LetterScreen;
import codyhuh.barracks.registry.ModEntities;
import codyhuh.barracks.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Barracks.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        EntityRenderers.register(ModEntities.ALLEGIANT_ARROW.get(), AllegiantArrowRenderer::new);
        MenuScreens.register(ModMenus.LETTER.get(), LetterScreen::new);
    }

    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(ModEntities.FAA.get(), FaaRenderer::new);
    }

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(ModModelLayers.FAA, FaaModel::createBodyLayer);
    }
}