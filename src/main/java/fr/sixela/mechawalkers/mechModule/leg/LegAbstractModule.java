package fr.sixela.mechawalkers.mechModule.leg;

import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.mechModule.AbstractModule;

public abstract class LegAbstractModule extends AbstractModule {

    public abstract float getSpeedMultiplier();
    public abstract float getStep();
    public abstract float getJumpPower();
    public abstract float getSideStepFactor();
    public abstract float getLegTurnSpeed();
    public abstract float getBackwardsFactor();
    public abstract boolean getTakeFallDamage();

}
