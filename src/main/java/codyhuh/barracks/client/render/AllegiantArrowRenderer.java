package codyhuh.barracks.client.render;

import codyhuh.barracks.common.entities.AllegiantArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AllegiantArrowRenderer extends ArrowRenderer<AllegiantArrow> {
   public static final ResourceLocation LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

   public AllegiantArrowRenderer(EntityRendererProvider.Context pContext) {
      super(pContext);
   }

   public ResourceLocation getTextureLocation(AllegiantArrow pEntity) {
      return LOCATION;
   }
}