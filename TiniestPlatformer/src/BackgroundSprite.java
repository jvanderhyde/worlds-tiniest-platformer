//All the player-specific behaviors
//Created by James Vanderhyde, 2 June 2010

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class BackgroundSprite extends LargeImageSprite
{
    public static final double collisionEpsilon = 0.5; //in pixels

    private Rectangle[] platforms =
    {
        //new Rectangle( 300,300,  30, 30), //small test platform
	//new Rectangle(4200,360,4000, 60), //long platform past the end
        new Rectangle(6433,  0, 500, 60), //blocker at the end
        new Rectangle(   0,360,1760,160),
	//new Rectangle( 615,350, 250,170), //step near beginning
	//new Rectangle( 865,340, 895,180), //step near beginning
        new Rectangle(1840,360,1380,160),
        new Rectangle(3340,360,1530,160),
        new Rectangle(4870,310, 510,210),
        new Rectangle(5380,280,  60,240),
        new Rectangle(5440,250,  60,270),
        new Rectangle(5500,220, 240,300),
	new Rectangle(5810,160, 950,360)
    };

    private ImageSprite foregroundSprite;

    public BackgroundSprite(GraphicsConfiguration gc,Color backgroundColor)
    {
        super();

	//Load component images
	BufferedImage platformImg=ImageSprite.loadCompatibleImage("Background.png", gc);
	BufferedImage castleImg=ImageSprite.loadCompatibleImage("castle background.png", gc);
	BufferedImage treeImg=ImageSprite.loadCompatibleImage("tree.png", gc);

	//Use the size of the platforms image to create a composite image
	BufferedImage bim = gc.createCompatibleImage(platformImg.getWidth(), platformImg.getHeight());
	Graphics2D g2d = bim.createGraphics();

	//Blank out the image
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, platformImg.getWidth(), platformImg.getHeight());

	//Draw the component images
	g2d.drawImage(platformImg,0,0,null);
	g2d.drawImage(castleImg,6482-castleImg.getWidth()/2,16-castleImg.getHeight()/2,null);
	g2d.drawImage(treeImg,30-treeImg.getWidth()/2,200-treeImg.getWidth()/2-50,null);

	//Finish
	g2d.dispose();

	//Set up member variables
	image=bim;
        width=image.getWidth(null);
        height=image.getHeight(null);
        x=this.width/2;
        y=this.height/2;
	createTiles();

        //Create the foreground sprite, the shape that hides the player so
        // the player appears to enter the castle.
	foregroundSprite=new ImageSprite("castle foreground.png",gc);
	foregroundSprite.x=6420+100+40;
	foregroundSprite.y=-34+50;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

	if (false)
	{
	    g.setColor(Color.green);
	    for (Rectangle p:platforms)
		g.drawRect((int)Math.floor(x-width/2)+p.x,(int)Math.floor(y-height/2)+p.y,p.width,p.height);
	}
    }

    @Override
    public void setX(double x)
    {
	double deltaX=x-this.getX();
	super.setX(x);
	foregroundSprite.setX(foregroundSprite.getX()+deltaX);
    }

    public void paintForeground(Graphics g)
    {
	foregroundSprite.paint(g);
    }
    
    public void paintEventPoints(Graphics g, double[] locsX)
    {
        g.setColor(Color.red);
        for (double locX:locsX)
            g.fillRect((int)Math.floor(x-width/2+locX),0,1,(int)Math.floor(height));
    }

    public double checkVertCollision(double deltaY,Rectangle2D.Double original)
    {
        //We will ignore upward movement, since there are no overhangs in this game.
        if (deltaY<0.0) return deltaY;

        Rectangle2D.Double attempted=(Rectangle2D.Double)original.clone();

        //Expand the rectangle by the movement to form a swept polygon
        attempted.height+=deltaY;
        //System.out.println("Vert. collision detection: "+attempted);

        //Check for intersections with all platforms
        for (Rectangle p:platforms)
        {
            if (p.intersects(attempted))
            {
                //System.out.println("vertical collision");
                if (Math.abs(deltaY)<collisionEpsilon)
                {
                    //If we're trying to move by less than a pixel and
                    // there is a collision, don't move.
                    deltaY=0.0;
                }
                else
                {
                    //Otherwise, figure out how much we can move.
                    deltaY = p.y-(original.y+original.height);
                }
            }
        }

        return deltaY;
    }

    public double checkHorizCollision(double deltaX,Rectangle2D.Double original)
    {
        Rectangle2D.Double attempted=(Rectangle2D.Double)original.clone();

        //Expand the rectangle by the movement to form a swept polygon
        if (deltaX<0.0)
        {
            attempted.x+=deltaX;
            attempted.width-=deltaX;
        }
        else
        {
            attempted.width+=deltaX;
        }
        //System.out.println("Horiz. collision detection: "+attempted);

        //Check for intersections with all platforms
        for (Rectangle p:platforms)
        {
            if (p.intersects(attempted))
            {
                //System.out.println("horizontal collision");
                if (Math.abs(deltaX)<collisionEpsilon)
                {
                    //If we're trying to move by less than a pixel and
                    // there is a collision, don't move.
                    deltaX=0.0;
                }
                else
                {
                    //Otherwise, figure out how much we can move.
                    if (deltaX<0)
                        deltaX = original.x-(p.x+p.width);
                    else
                        deltaX = p.x-(original.x+original.width);
                }
            }
        }

        return deltaX;
    }
}
