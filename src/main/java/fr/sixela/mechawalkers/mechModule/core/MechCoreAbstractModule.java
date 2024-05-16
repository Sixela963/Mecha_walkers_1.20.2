package fr.sixela.mechawalkers.mechModule.core;

import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.mechModule.AbstractModule;

public abstract class MechCoreAbstractModule extends AbstractModule {

    public abstract boolean getCorePowered();
    public abstract float getMovementPower();
}
