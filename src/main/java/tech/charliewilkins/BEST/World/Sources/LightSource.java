package tech.charliewilkins.BEST.World.Sources;

import java.awt.Color;

import tech.charliewilkins.BEST.World.World;

public class LightSource extends Source {
    public LightSource(int x, int y, int health, World world) {
        super(x, y, health, world, Color.YELLOW);
    }
}
