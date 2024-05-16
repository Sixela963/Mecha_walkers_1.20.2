package fr.sixela.mechawalkers.block.MechModule.core;

import fr.sixela.mechawalkers.mechModule.core.DebugCoreModule;

public class DebugCoreBlock extends MechCoreAbstractBlock{
    public DebugCoreBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public DebugCoreModule getModule() {
        return new DebugCoreModule();
    }


}
