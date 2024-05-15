package fr.sixela.mechawalkers.mechModule.arm;

import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.mechModule.AbstractModule;

public abstract class MechArmAbstractModule extends AbstractModule {

    public abstract void startUsing();
    public abstract void stopUsing();
    public abstract void use();
}
