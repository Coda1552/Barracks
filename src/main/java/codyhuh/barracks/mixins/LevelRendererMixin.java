package codyhuh.barracks.mixins;

import codyhuh.barracks.Barracks;
import codyhuh.barracks.common.entities.AllegiantArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Redirect(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"))
    private int barracks_getTeamColor(Entity entity) {
        int color = entity.getTeamColor();

        Minecraft mc = Minecraft.getInstance();
        EntityHitResult result = Barracks.getLookAtEntity(mc.player, mc.level, 64.0D);

        if (result != null && result.getEntity() instanceof LivingEntity) {
            color = 0xff0000;
        }

        if (entity instanceof AllegiantArrow) {
            color = 0xa22098;
        }

        return color;
    }
}
