package tb.sockets.server;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

public class Battleship
{
    private int length;
    private List coordinates = new Vector<Point>();

    public Battleship(int length)
    {
        this.length = length;

    }

    protected void addCoordinate(Point coordinate)
    {
        this.coordinates.add(coordinate);
    }

    public int getLength()
    {
        return this.length;
    }

    public List getCoordinates() {
        return coordinates;
    }
}
