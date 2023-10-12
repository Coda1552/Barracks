package codyhuh.barracks.client.renders;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.client.ModModelLayers;
import codyhuh.barracks.client.model.FaaModel;
import codyhuh.barracks.common.entities.Faa;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FaaRenderer extends MobRenderer<Faa, FaaModel<Faa>> {
   private static final ResourceLocation LOCATION = new ResourceLocation(Barracks.MOD_ID, "textures/entity/faa.png");

   public FaaRenderer(EntityRendererProvider.Context p_173954_) {
      super(p_173954_, new FaaModel<>(p_173954_.bakeLayer(ModModelLayers.FAA)), 0.3F);
   }

   public ResourceLocation getTextureLocation(Faa pEntity) {
      return LOCATION;
   }

   protected void setupRotations(Faa pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
      float f = 4.3F * Mth.sin(0.6F * pAgeInTicks);
      pMatrixStack.mulPose(Axis.YP.rotationDegrees(f));
      if (!pEntityLiving.isInWater()) {
         pMatrixStack.translate(0.1F, 0.1F, -0.1F);
         pMatrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
      }

   }
}