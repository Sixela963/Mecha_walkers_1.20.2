package fr.sixela.mechawalkers.mechModule.frame;

import fr.sixela.mechawalkers.mechModule.AbstractModule;

public abstract class MechFrameAbstractModule extends AbstractModule {
    public abstract float getMaxHealth();
    public abstract float getArmor();
    public abstract float getArmorToughness();
    public abstract float getMass();
}
