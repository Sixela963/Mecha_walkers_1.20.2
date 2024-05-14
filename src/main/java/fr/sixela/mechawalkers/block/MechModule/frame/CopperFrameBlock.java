package fr.sixela.mechawalkers.block.MechModule.frame;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.frame.CopperFrameModule;

public class CopperFrameBlock extends MechFrameAbstractBlock{

    public CopperFrameBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public CopperFrameModule getModule() {
        return new CopperFrameModule();
    }
}
