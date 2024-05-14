package fr.sixela.mechawalkers.mechModule.frame;

public class CopperFrameModule extends MechFrameAbstractModule{
    @Override
    public float getMaxHealth() {
        return 20f;
    }

    @Override
    public float getArmor() {
        return 11f;
    }

    @Override
    public float getArmorToughness() {
        return 0f;
    }

    @Override
    public float getMass() {
        return 1100f;
    }

    @Override
    public float getPowerCost() {
        return 10f;
    }
}
