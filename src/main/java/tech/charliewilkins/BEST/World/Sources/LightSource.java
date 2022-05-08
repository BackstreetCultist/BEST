package tech.charliewilkins.BEST.World.Sources;

import java.awt.Color;

import tech.charliewilkins.BEST.World.World;

public class LightSource extends Source {
    public LightSource(int x, int y, World world) {
        super(x, y, world, Color.YELLOW);
    }
}
