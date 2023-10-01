package codyhuh.barracks.common.items;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class SpindlyGauntletItem extends Item implements Vanishable {

    public SpindlyGauntletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        super.inventoryTick(stack, level, entity, slot, held);

        if (held && entity instanceof Player player) {
            List<LivingEntity> list = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), player, player.getBoundingBox().expandTowards(player.getViewVector(1.0F).multiply(128.0F, 128.0F, 128.0F)));

            if (list.isEmpty()) return;

            LivingEntity nearestEntity = list.stream().min(Comparator.comparingDouble(e -> e.distanceToSqr(player))).get();

            if (lookAtAmount(player, nearestEntity) > 0.999D) {
                nearestEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2));
            }
        }
    }

    private double lookAtAmount(Player player, LivingEntity target) {
        Vec3 vec3 = player.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(target.getX() - player.getX(), target.getEyeY() - player.getEyeY(), target.getZ() - player.getZ());
        vec31 = vec31.normalize();
        return vec3.dot(vec31);
    }

}
