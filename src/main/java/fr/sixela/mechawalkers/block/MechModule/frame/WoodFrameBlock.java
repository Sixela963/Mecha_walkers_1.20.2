package fr.sixela.mechawalkers.block.MechModule.frame;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.frame.WoodFrameModule;

public class WoodFrameBlock extends MechFrameAbstractBlock{

    public WoodFrameBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public WoodFrameModule getModule() {
        return new WoodFrameModule();
    }
}
