package fr.sixela.mechawalkers.block.MechModule.frame;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.frame.IronFrameModule;

public class IronFrameBlock extends MechFrameAbstractBlock{

    public IronFrameBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public IronFrameModule getModule() {
        return new IronFrameModule();
    }
}
