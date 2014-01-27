//An enemy to jump over
//Created by James Vanderhyde, 29 July 2010

import java.awt.geom.Rectangle2D;

public class EnemySprite extends WorldSprite
{
    private boolean alive;

    public EnemySprite()
    {
        super("troll.png",4820,330,3350,4820);
        //super("troll.png",550,330,200,550);
        alive=true;

        setX(this.getWorldX());
        y=this.getWorldY();
    }

    public double checkVertCollision(double deltaY,Rectangle2D.Double original)
    {
	//We only check collisions if the enemy is alive
	if (!this.isAlive()) return deltaY;

        //We will ignore upward movement, since there are no overhangs in this game.
        if (deltaY<0.0) return deltaY;

        Rectangle2D.Double attempted=(Rectangle2D.Double)original.clone();

        //Expand the rectangle by the movement to form a swept polygon
        attempted.height+=deltaY;

	//Consider only the back half of the player
	attempted.width/=2;
	
        //System.out.println("Vert. collision detection: "+attempted);

        //Check for intersection with the back half of this sprite
        Rectangle2D.Double p=this.getWorldRect();
	p.width/=2;
	p.x+=p.width;
	if (p.intersects(attempted))
	{
	    //System.out.println("vertical collision");
	    if (Math.abs(deltaY)<BackgroundSprite.collisionEpsilon)
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

	    //Jumping on the back of the enemy is the player's way of killing it.
	    this.die();
	}

        return deltaY;
    }

    public boolean checkHorizCollision(double deltaX,Rectangle2D.Double original)
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

        //Check for intersection with this sprite
        Rectangle2D.Double p=this.getWorldRect();
	if (p.intersects(attempted))
	{
	    //System.out.println("horizontal collision");
	    return true;
	}

        return false;
    }

    public boolean isAlive()
    {
	return alive;
    }

    public void die()
    {
	if (alive)
	{
	    alive=false;
	    this.setImage("troll dead.png");
	    this.moveY(20);
	}
    }

    public boolean canSeeSprite(WorldSprite object)
    {
	double dist=this.getWorldX()-object.getWorldX();
	if ((0<=dist) && (dist<=400))
	    return true;
	return false;
    }

}
