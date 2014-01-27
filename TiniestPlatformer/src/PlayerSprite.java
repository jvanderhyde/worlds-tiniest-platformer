//All the player-specific behaviors
//Created by James Vanderhyde, 2 June 2010

public class PlayerSprite extends WorldSprite
{
    private double velY;
    private boolean onTheGround;
    private double forwardProgress;
    private boolean touchingEnemy;

    public PlayerSprite()
    {
        super("main character.png",100,323,100,7740);
        velY=0.0;
        onTheGround=true;
        forwardProgress=this.getWorldX();

        setX(this.getWorldX());
        y=this.getWorldY();
    }

    @Override
    public double moveX(double amount)
    {
	double newAmount = super.moveX(amount);

	//Update forward progress
        if (this.getWorldX()>forwardProgress)
            forwardProgress=this.getWorldX();

	return newAmount;
    }

    public void changeYVelocity(double amount)
    {
        velY+=amount;
    }

    public boolean isOnTheGround()
    {
        return onTheGround;
    }

    public double getYVelocity()
    {
        return velY;
    }

    public void jump(double initialVelocity)
    {
        velY=initialVelocity;
        onTheGround=false;
    }

    public void stopJumping()
    {
        if (velY<0.0)
            velY=0.0;
    }

    public void fall()
    {
        onTheGround=false;
    }
    
    public void hitTheFloor()
    {
        velY=0;
        onTheGround=true;
    }

    public double getForwardProgress()
    {
        return forwardProgress;
    }

    public void touchedByEnemy()
    {
	touchingEnemy=true;
    }

    public void clearOfEnemy()
    {
	touchingEnemy=false;
    }

    public boolean isTouchingEnemy()
    {
	return touchingEnemy;
    }

    public boolean jumpIsAllowed()
    {
	return (this.getWorldX()<6433-(this.width/2+2));
    }

}
