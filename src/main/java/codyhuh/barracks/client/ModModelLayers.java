package codyhuh.barracks.client;

import codyhuh.barracks.Barracks;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    public static final ModelLayerLocation FAA = create("faa");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(new ResourceLocation(Barracks.MOD_ID, name), "main");
    }
}
