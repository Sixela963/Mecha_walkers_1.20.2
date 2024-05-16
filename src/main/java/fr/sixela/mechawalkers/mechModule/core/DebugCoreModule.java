package fr.sixela.mechawalkers.mechModule.core;

public class DebugCoreModule extends MechCoreAbstractModule{

    @Override
    public boolean getCorePowered() {
        return true;
    }

    @Override
    public float getMovementPower() {
        return (this.coreSpecialInput ? 3000f:1000f) ;
    }

    @Override
    public float getPowerCost() {
        return -100f;
    }
}
