package fr.sixela.mechawalkers.block.MechModule.core;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import fr.sixela.mechawalkers.mechModule.core.SteamEngineCoreModule;

public class SteamEngineCoreBlock extends MechCoreAbstractBlock{
    public SteamEngineCoreBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SteamEngineCoreModule getModule() {
        return new SteamEngineCoreModule();
    }


}
