package fr.sixela.mechawalkers.mechModule.frame;

public class IronFrameModule extends MechFrameAbstractModule{
    @Override
    public float getMaxHealth() {
        return 30f;
    }

    @Override
    public float getArmor() {
        return 15f;
    }

    @Override
    public float getArmorToughness() {
        return 0f;
    }

    @Override
    public float getMass() {
        return 1000f;
    }

    @Override
    public float getPowerCost() {
        return 10f;
    }
}
