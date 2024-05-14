package fr.sixela.mechawalkers.block.MechModule;

import fr.sixela.mechawalkers.mechModule.AbstractModule;
import net.minecraft.world.level.block.Block;

public abstract class MechModuleAbstractBlock extends Block {
    public MechModuleAbstractBlock(Properties pProperties) {
        super(pProperties);
    }

    public abstract <T extends AbstractModule> T getModule();
}