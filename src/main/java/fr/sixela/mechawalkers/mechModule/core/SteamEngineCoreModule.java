package fr.sixela.mechawalkers.mechModule.core;

public class SteamEngineCoreModule extends MechCoreAbstractModule{

    @Override
    public float getMovementPower() {
        return 1000f;
    }
    @Override
    public float getPowerCost() {
        return -100f;
    }
}
