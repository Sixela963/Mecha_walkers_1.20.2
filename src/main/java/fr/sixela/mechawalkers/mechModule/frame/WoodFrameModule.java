package fr.sixela.mechawalkers.mechModule.frame;

public class WoodFrameModule extends MechFrameAbstractModule{
    @Override
    public float getMaxHealth() {
        return 10f;
    }

    @Override
    public float getArmor() {
        return 7f;
    }

    @Override
    public float getArmorToughness() {
        return 0f;
    }

    @Override
    public float getMass() {
        return 300f;
    }

    @Override
    public float getPowerCost() {
        return 10f;
    }
}
