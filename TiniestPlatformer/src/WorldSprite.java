//A sprite that has world coordinates
//Created by James Vanderhyde, 30 July 2010

import java.awt.geom.Rectangle2D;

public class WorldSprite extends ImageSprite
{
    private double worldX,worldY;
    private double worldXMin;
    private double worldXMax;

    public WorldSprite()
    {
        super();
    }

    public WorldSprite(String imageFileName)
    {
        super(imageFileName);
    }

    public WorldSprite(String imageFileName,double x, double y, double xMin, double xMax)
    {
        super(imageFileName);
	worldX=x;
	worldY=y;
	worldXMin=xMin;
	worldXMax=xMax;
    }

    //Attempts to move the sprite by the specified amount in the world.
    // Returns the actual amount moved.
    public double moveX(double amount)
    {
        //Check boundary of world.
	// Snaps to boundary edge if sprite is past edge or wants to pass edge.
        if (worldX+amount<worldXMin)
            amount = worldXMin-worldX;
        if (worldX+amount>worldXMax)
            amount = worldXMax-worldX;

        //Move
        worldX+=amount;

        //Return amount moved
        return amount;
    }

    public double moveY(double amount)
    {
        worldY+=amount;
        y=worldY;
        return amount;
    }

    public double getWorldX()
    {
        return worldX;
    }

    public double getWorldY()
    {
        return worldY;
    }

    public Rectangle2D.Double getWorldRect()
    {
        return new Rectangle2D.Double(worldX-width/2, worldY-height/2,width,height);
    }


}
