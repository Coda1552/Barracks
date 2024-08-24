package codyhuh.barracks.common.entities;

import codyhuh.barracks.common.entities.util.ConstantSwimmingMoveControl;
import codyhuh.barracks.common.entities.util.FishConstantSwimGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Faa extends AbstractSchoolingFish {
    protected FishConstantSwimGoal swimGoal;

    public Faa(EntityType<? extends AbstractSchoolingFish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        moveControl = new ConstantSwimmingMoveControl(this, 25, 10, 0.01F, 0.2F, true);
        lookControl = new SmoothSwimmingLookControl(this, 30);
    }

    @Override
    protected void registerGoals() {
        swimGoal = new FishConstantSwimGoal(this, 1.0D, 100); // scallop - speed mod
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(2, swimGoal);
        //this.goalSelector.addGoal(3, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSchoolSize() {
        return 6;
    }


    @Override
    public void travel(Vec3 pTravelVector) {
/*        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.25F, pTravelVector); // scallop - move relative #
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0005D, 0.0D)); // scallop - decreased y delta movement
            }
        } else {
            super.travel(pTravelVector);
        }*/

        if (swimGoal != null && pTravelVector.equals(Vec3.ZERO)) {
            swimGoal.trigger();
        }

        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                //this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.TROPICAL_FISH_BUCKET);
    }

}
