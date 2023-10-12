package codyhuh.barracks.common.entities;

import codyhuh.barracks.common.entities.ai.FishConstantSwimGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
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
        moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.01F, 0.2F, true);
        lookControl = new SmoothSwimmingLookControl(this, 30);
    }

    @Override
    protected void registerGoals() {
        swimGoal = new FishConstantSwimGoal(this, 1.0D, 100);
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(2, swimGoal);
        this.goalSelector.addGoal(3, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSchoolSize() {
        return 6;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (swimGoal != null && pTravelVector.equals(Vec3.ZERO)) {
            swimGoal.trigger();
        }

        super.travel(pTravelVector);
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
