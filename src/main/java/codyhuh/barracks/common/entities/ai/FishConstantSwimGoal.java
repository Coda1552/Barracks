package codyhuh.barracks.common.entities.ai;

import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;

public class FishConstantSwimGoal extends RandomSwimmingGoal {
    private final AbstractSchoolingFish fish;

    public FishConstantSwimGoal(AbstractSchoolingFish goalOwner, double speedMod, int interval) {
        super(goalOwner, speedMod, interval);
        fish = goalOwner;
    }

    @Override
    public boolean canUse() {
        return !fish.isFollower() && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
    }

}