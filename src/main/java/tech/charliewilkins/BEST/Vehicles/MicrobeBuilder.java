package tech.charliewilkins.BEST.Vehicles;

import tech.charliewilkins.BEST.World.World;

public class MicrobeBuilder {
    private final World worldRef;

    public MicrobeBuilder(World worldRef) {
        this.worldRef = worldRef;
    }

    public Microbe build(int x, int y, String genome) {
        Microbe microbe = new Microbe(x, y, worldRef);
        return microbe;
    }
}
