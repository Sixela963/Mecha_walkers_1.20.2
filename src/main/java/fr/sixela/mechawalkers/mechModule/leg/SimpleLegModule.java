package fr.sixela.mechawalkers.mechModule.leg;

public class SimpleLegModule extends LegAbstractModule{
    @Override
    public float getSpeedMultiplier() {
        return 1f;
    }

    @Override
    public float getStep() {
        return 1.6f;
    }

    @Override
    public float getJumpPower() {
        return 1.5f;
    }

    @Override
    public float getSideStepFactor() {
        return 0.5f;
    }

    @Override
    public float getLegTurnSpeed() {
        return 4f;
    }

    @Override
    public float getBackwardsFactor() {
        return 0.8f;
    }

    @Override
    public boolean getTakeFallDamage() {
        return false;
    }

    @Override
    public float getPowerCost() {
        return 10f;
    }
}
