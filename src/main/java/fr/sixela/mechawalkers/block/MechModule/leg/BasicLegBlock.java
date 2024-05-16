package fr.sixela.mechawalkers.block.MechModule.leg;

import fr.sixela.mechawalkers.mechModule.leg.BasicLegModule;

public class BasicLegBlock extends MechLegAbstractBlock{
    public BasicLegBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BasicLegModule getModule() {
        return new BasicLegModule();
    }
}
