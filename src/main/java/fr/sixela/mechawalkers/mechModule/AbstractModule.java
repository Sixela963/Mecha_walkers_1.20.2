package fr.sixela.mechawalkers.mechModule;

import fr.sixela.mechawalkers.entity.Mecha;

public abstract class AbstractModule {

    protected Mecha mechaEntity;
    public void setMechaEntity(Mecha pMecha) {
        this.mechaEntity = pMecha;
    }
    public abstract float getPowerCost();
    public void tick() {
        return;
    }
}
