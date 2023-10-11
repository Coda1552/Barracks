package codyhuh.barracks.common.items;

import codyhuh.barracks.Barracks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SpindlyGauntletItem extends Item implements Vanishable {

    public SpindlyGauntletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        if (held && entity instanceof Player player) {
            EntityHitResult result = Barracks.getLookAtEntity(player, level, 64.0D);

            if (result != null && result.getEntity() instanceof LivingEntity living) {
                living.hurt(living.damageSources().generic(), 5.0F);
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2, 0, true, false, false));

            }
        }
    }

}
