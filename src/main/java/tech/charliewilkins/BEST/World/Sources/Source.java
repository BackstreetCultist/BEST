package tech.charliewilkins.BEST.World.Sources;

public class Source {
    // Coordinates
    protected final int x;
    protected final int y;

    protected final int diameter;

    public Source(int x, int y) {
        this.x = x;
        this.y = y;

        this.diameter = 10;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
