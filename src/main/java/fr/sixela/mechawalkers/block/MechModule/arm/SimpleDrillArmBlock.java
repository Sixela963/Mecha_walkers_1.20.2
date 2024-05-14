package fr.sixela.mechawalkers.block.MechModule.arm;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.arm.SimpleDrillArmModule;

public class SimpleDrillArmBlock extends MechArmAbstractBlock{
    public SimpleDrillArmBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SimpleDrillArmModule getModule() {
        return new SimpleDrillArmModule();
    }
}
