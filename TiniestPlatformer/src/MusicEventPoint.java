//A point in the game world that activates the next music event
// when the player crosses it.
//Created by James Vanderhyde, 16 June 2010

public class MusicEventPoint
{
    private double worldX,worldY;
    private boolean alreadyActivated;

    public MusicEventPoint(double x, double y)
    {
        worldX=x;
        worldY=y;
        alreadyActivated=false;
    }

    public double getWorldX()
    {
        return worldX;
    }

    public boolean isAlreadyActivated()
    {
        return alreadyActivated;
    }

    public void activate()
    {
        alreadyActivated=true;
    }

}
