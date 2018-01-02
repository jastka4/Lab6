package tb.sockets.server;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

public class Battleship
{
    private int length;
    private List coordinates = new Vector<Point>();
    private int hits;
    private boolean sunken;

    public Battleship(int length)
    {
        this.length = length;
        this.hits = 0;
        this.sunken = false;
    }

    protected void addCoordinate(Point coordinate)
    {
        this.coordinates.add(coordinate);
    }

    private String pointToString(Point point)
    {
        return String.valueOf(((int)point.getX() + "" + (int)point.getY()));
    }

    public List getCoordinates() {
        return coordinates;
    }

    public String getCoordinatesAsString()
    {
        String points = "";

        for(Object coordinate: coordinates)
        {
            points += pointToString((Point) coordinate);
        }

        return points;
    }

    public void addHit()
    {
        this.hits++;
        if(this.hits == this.length)
        {
            this.sunken = true;
        }
    }

    public boolean checkIfSunken()
    {
        return sunken;
    }
}
