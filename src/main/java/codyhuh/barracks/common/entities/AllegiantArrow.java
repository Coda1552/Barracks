package codyhuh.barracks.common.entities;

import codyhuh.barracks.registry.ModItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class AllegiantArrow extends AbstractArrow {
    private boolean retrieving;

    public AllegiantArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        pickup = Pickup.DISALLOWED;
        retrieving = false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.ALLEGIANT_ARROW.get());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = getOwner();

        for (int i = 0; i < 10; i++) {
            level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0xa22098).toVector3f(), 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            //level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0x9247d1).toVector3f(), 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            //level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0xff5555).toVector3f(), 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }

        if (isCurrentlyGlowing()) {
            setGlowingTag(false);
        }

        if (owner instanceof Player player) {
            if (player.isShiftKeyDown()) {

                Vec3 vec3 = new Vec3(player.getX() - this.getX(), player.getY() + (double)player.getEyeHeight() - this.getY(), player.getZ() - this.getZ());
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(0.25D)));

                retrieving = true;

                if (inGround) {
                    setDeltaMovement(getDeltaMovement().x, 0.1D, getDeltaMovement().y);
                    inGround = false;
                }
            }
            else {
                double range = 16.0D;
                Vec3 eyePos = player.getEyePosition(1.0F);
                Vec3 viewVec = player.getViewVector(1.0F);
                Vec3 endVec = eyePos.add(viewVec.x * range, viewVec.y * range, viewVec.z * range);

                BlockHitResult result = level().clip(new ClipContext(eyePos, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                BlockHitResult modifiedResult = result.withPosition(result.getBlockPos().relative(result.getDirection(), 1));

                Vec3 pos = modifiedResult.getLocation();

                setDeltaMovement(pos.x - position().x, pos.y - position().y, pos.z - position().z);
                setDeltaMovement(getDeltaMovement().scale(0.5D));

                retrieving = false;
            }
        }
    }

    // Crappy way to get around the sound spam when trying to retrieve a stuck arrow...
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (retrieving) {
            BlockState blockstate = this.level().getBlockState(pResult.getBlockPos());
            blockstate.onProjectileHit(this.level(), blockstate, pResult, this);
            Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
            this.setDeltaMovement(vec3);
            Vec3 vec31 = vec3.normalize().scale(0.05F);
            this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
            this.shakeTime = 7;
        }
        else {
            super.onHitBlock(pResult);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();

        if (getOwner() instanceof Player player) {
            if (player == entity) {
                discard();

                if (!player.isCreative() && !player.getInventory().add(new ItemStack(ModItems.ALLEGIANT_ARROW.get()))) {
                    Vec3 pos = player.position();

                    level().addFreshEntity(new ItemEntity(level(), pos.x, pos.y, pos.z, new ItemStack(ModItems.ALLEGIANT_ARROW.get())));
                }
            }
            else {
                DamageSource damageSource = damageSources().arrow(this, entity);
                player.setLastHurtMob(entity);

                boolean flag = entity.getType() == EntityType.ENDERMAN;

                float damage = 5.0F;

                if (entity.hurt(damageSource, damage)) {
                    if (flag) {
                        return;
                    }

                    this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

                    if (entity instanceof LivingEntity livingentity) {
                        if (!this.level().isClientSide && this.getPierceLevel() <= 0) {
                            livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                        }

                        if (!this.level().isClientSide) {
                            EnchantmentHelper.doPostHurtEffects(livingentity, player);
                            EnchantmentHelper.doPostDamageEffects(player, livingentity);
                        }

                        this.doPostHurtEffects(livingentity);
                        if (livingentity instanceof Player && player instanceof ServerPlayer && !this.isSilent()) {
                            ((ServerPlayer) player).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                        }
                    }
                }
            }
        }
    }
}