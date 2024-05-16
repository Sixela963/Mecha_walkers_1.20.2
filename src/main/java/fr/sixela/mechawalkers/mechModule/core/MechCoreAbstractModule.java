package fr.sixela.mechawalkers.mechModule.core;

import fr.sixela.mechawalkers.mechModule.AbstractModule;

public abstract class MechCoreAbstractModule extends AbstractModule {

    protected boolean coreSpecialInput = false;

    public void setCoreSpecialInputActive(boolean input) {
        this.coreSpecialInput = input;
        return;
    }
    public abstract boolean getCorePowered();
    public abstract float getMovementPower();
}
