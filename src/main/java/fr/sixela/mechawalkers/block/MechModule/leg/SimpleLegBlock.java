package fr.sixela.mechawalkers.block.MechModule.leg;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.leg.LegAbstractModule;
import fr.sixela.mechawalkers.mechModule.leg.SimpleLegModule;

public class SimpleLegBlock extends MechLegAbstractBlock{
    public SimpleLegBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SimpleLegModule getModule() {
        return new SimpleLegModule();
    }
}
