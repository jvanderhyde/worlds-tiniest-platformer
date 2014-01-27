//The world's tiniest platformer
//Created by James Vanderhyde, 30 May 2010

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class PlatformerGame extends GamePanel
{
    private KeyHandler keys;
    private EventCoordinator events; 

    private double playerSpeed;
    private double gravity;
    private double playerJumpVel;
    private double goalXCoord;

    private double enemySpeed;

    //used to avoid repeadtedly jumping by holding jump key
    private boolean jumpKeyReleased=true;
    
    private BackgroundSprite background;
    private PlayerSprite player;
    private EnemySprite enemy;
    private ImageSprite storyScroll;
    private ImageSprite smallCastle;

    private Color bkgndColor;
    private Font storyFont,gameOverFont;

    private boolean gameOn;
    private long gameOverSplashTime;
    private int numPlayerDeaths;
    private boolean goalReached;
    private long gameTime;
    private long totalTime;
    private boolean gameOverTrigger;

    public PlatformerGame()
    {
        keys=new KeyHandler();
        this.addKeyListener(keys);

        playerSpeed=0.15;
        gravity=0.0015;
        playerJumpVel=-0.5;
        goalXCoord=6380+150;

	enemySpeed=0.12;

        events=new EventCoordinator(playerSpeed);

        bkgndColor=new Color(255,250,211); //yellowed paper
        storyFont=new Font("Serif",Font.PLAIN,12);
        gameOverFont=new Font("SansSerif",Font.PLAIN,18);
	storyScroll=new ImageSprite("scroll2.png");
	smallCastle=new ImageSprite("castle and path.png");

        gameOn=false;
        gameOverSplashTime=0;
        numPlayerDeaths=0;
        goalReached=false;
	gameTime=0;
	totalTime=0;
	gameOverTrigger=false;
    }

    public java.awt.event.KeyListener getKeyHandler()
    {
	return keys;
    }

    public void startGame()
    {
	events.createEvents();
        background=new BackgroundSprite(this.gc,this.bkgndColor);
        player=new PlayerSprite();
	enemy=new EnemySprite();

        gameOn=true;
	gameTime=0;
        System.out.println("Game on!");
    }

    public void gameOver()
    {
        gameOverTrigger=true;
        System.out.println("Player death");
        numPlayerDeaths++;
	totalTime+=gameTime;
        gameOverSplashTime=0;
	events.stopMusic(); //add a sound effect?
    }

    public void gameOverCaught()
    {
        gameOver();
    }

    public void gameOverGoalReached()
    {
        goalReached=true;
        gameOverTrigger=true;
        System.out.println("Player reached goal!");
	totalTime+=gameTime;
    }

    @Override
    public void paintGame(Graphics g)
    {
	//set up clip for use by sprites
	g.setClip(vLeft, vTop, vRight-vLeft, vBottom-vTop);
        
        if (gameOn)
        {
            //draw game
            background.paint(g);
            player.paint(g);
	    enemy.paint(g);
            //background.paintEventPoints(g, events.getEventPointsX());
            g.setColor(bkgndColor);
            background.paintForeground(g);
        }
        else
        {
	    //blank out screen
	    g.setColor(bkgndColor);
	    g.fillRect(vLeft, vTop, vRight-vLeft, vBottom-vTop);

            if (goalReached)
            {
                //keep the castle visible
                background.paint(g);
                g.setColor(bkgndColor);
                background.paintForeground(g);

                //draw win splash
		storyScroll.setX(175);
		storyScroll.setY(310);
		storyScroll.paint(g);
                g.setColor(Color.DARK_GRAY);
                g.setFont(storyFont);
                g.drawString("You reached the castle!", 100, 240);
                g.setFont(gameOverFont);
                g.drawString("Congratulations!", 100, 270);
                g.setFont(storyFont);
                g.drawString("Time elapsed: "+totalTime/1e3+" seconds", 100, 300);

            }
            else if (numPlayerDeaths>0)
            {
                //draw "Game over" splash
		storyScroll.setX(200);
		storyScroll.setY(150);
		storyScroll.paint(g);
                g.setColor(Color.DARK_GRAY);
                g.setFont(storyFont);
		if (player.isTouchingEnemy())
		{
		    g.drawString("The troll caught you. You will never reach the castle.", 70, 100);
		}
		else
		{
		    g.drawString("You fell into a deep pit. You will never reach the castle.", 70, 100);
		}
                g.setFont(gameOverFont);
                g.drawString("Game Over", 150, 130);
            }
	    else
	    {
		//draw start splash
		storyScroll.setX(200);
		storyScroll.setY(115);
		storyScroll.paint(g);

		int topY=50;
                g.setColor(Color.DARK_GRAY);
                g.setFont(gameOverFont);
                g.drawString("The World's Tiniest Platformer", 70, topY);
                g.setFont(storyFont);
                g.drawString("You can see the castle in the distance. Can you reach it?", 70, topY+30);
                g.drawString("Click to start. Use arrow keys to move.", 100, topY+60);

		smallCastle.setX(200);
		smallCastle.setY(270);
		smallCastle.paint(g);
	    }
        }
    }

    @Override
    public void updateGame(long timeSinceLastUpdate)
    {
	if (gameOverTrigger)
	{
	    gameOn=false;
	    gameOverTrigger=false;
	}

        if (gameOn)
        {
	    //Update the enemy
	    double enemyDeltaX=updateEnemyHoriz(timeSinceLastUpdate);

            //Update the player
            double playerDeltaX=updatePlayerHoriz(timeSinceLastUpdate);
            double playerDeltaY=updatePlayerVert(timeSinceLastUpdate);

	    //This is a scrolling game, so check for horizontal scrolling
	    boolean scroll=checkScrolling(playerDeltaX);

	    //Update object screen coordinates
	    if (scroll)
	    {
		background.setX(background.getX()-playerDeltaX);
		enemy.setX(enemy.getX()+enemyDeltaX-playerDeltaX);
	    }
	    else
	    {
                player.setX(player.getX()+playerDeltaX);
		enemy.setX(enemy.getX()+enemyDeltaX);
	    }
	    //System.out.println("Player: "+player.getX()+"   Enemy: "+enemy.getX());

            //Check for music events
            events.playNextUnplayedMusicEvent(player.getForwardProgress());

	    //Record passing time
	    gameTime+=timeSinceLastUpdate;
        }
        else
        {
            if (goalReached)
            {
                //Player has won. Do nothing.
            }
            else
            {
                //game over
                gameOverSplashTime+=timeSinceLastUpdate;
                if (gameOverSplashTime>=6000)
                    startGame();
            }
        }
    }

    private boolean isJumpKeyPressed()
    {
        return (keys.isSpacePressed() || keys.isUpPressed());
    }

    private double updatePlayerVert(long timeSinceLastUpdate)
    {
        if (player.isOnTheGround() && player.jumpIsAllowed() && isJumpKeyPressed() && jumpKeyReleased)
        {
            player.jump(playerJumpVel);
            jumpKeyReleased=false;
        }
        if (!isJumpKeyPressed())
        {
            player.stopJumping();
            if (player.isOnTheGround())
                jumpKeyReleased=true;
        }

        double deltaY=player.getYVelocity()*timeSinceLastUpdate;

        //check for downward vertical collisions
        if (deltaY>0.0)
        {
	    double newDelt = deltaY;
            newDelt = background.checkVertCollision(newDelt, player.getWorldRect());
	    newDelt = enemy.checkVertCollision(newDelt, player.getWorldRect());
            if (newDelt != deltaY)
            {
                deltaY=newDelt;
                player.hitTheFloor();
            }
        }

        //check for empty space below
        if (player.isOnTheGround())
        {
            if ((background.checkVertCollision(1.0, player.getWorldRect()) != 0.0) &&
		(enemy.checkVertCollision(1.0, player.getWorldRect()) != 0.0))
                player.fall();
        }

        //check for falling into a pit
        if (player.getWorldY()+deltaY>400+player.getWorldRect().height+100)
        {
            //end game and start over
            gameOver();
        }

        //apply gravity
        if (!player.isOnTheGround())
            player.changeYVelocity(gravity*timeSinceLastUpdate);

        //Absolute movement
        deltaY=player.moveY(deltaY);

	return deltaY;
    }

    private double updatePlayerHoriz(long timeSinceLastUpdate)
    {
        double deltaX=0;

        //Determine amount to move the player horizontally
        if (keys.isLeftPressed())
        {
            deltaX-=0.8*playerSpeed*timeSinceLastUpdate;
        }
        if (keys.isRightPressed())
        {
            deltaX+=playerSpeed*timeSinceLastUpdate;
        }

        //Check for horizontal collisions
        if (Math.abs(deltaX)>0)
            deltaX=background.checkHorizCollision(deltaX, player.getWorldRect());
	if (enemy.checkHorizCollision(deltaX, player.getWorldRect()))
	    player.touchedByEnemy();
	else
	    player.clearOfEnemy();
        
        //World coords, absolute movement
        deltaX=player.moveX(deltaX);
	
	//Check whether caught by enemy
	if (enemy.isAlive() && player.isTouchingEnemy())
	{
	    gameOverCaught();
	}

        //Check whether goal has been reached
        if (player.getForwardProgress()>=this.goalXCoord)
        {
            gameOverGoalReached();
        }

	return deltaX;
    }

    private double updateEnemyHoriz(long timeSinceLastUpdate)
    {
        double deltaX=-enemySpeed*timeSinceLastUpdate;

        if (enemy.isAlive() && enemy.canSeeSprite(player))
        {
	    //System.out.println("Enemy moving");
            deltaX=enemy.moveX(deltaX);
        }
	else
	{
	    deltaX=0;
	}

	return deltaX;
    }
    
    //Horizontal scrolling
    private boolean checkScrolling(double deltaX)
    {
	//Scroll thresholds
	double scrollLeft=this.getWidth()/4.0;
	double scrollRight=this.getWidth()/3.0;

        //Relative movment. Move either player or background.
        if ((((player.getX()<scrollLeft) && (deltaX<0)) ||
             ((player.getX()>scrollRight) && (deltaX>0))) &&
            ((background.getX()-deltaX<background.width/2) &&
             (background.getX()-deltaX>-background.width/2)))
        {
            //We move the background if the player is trying to move left
            // into the left third of the screen or trying to move right
            // into the right third of the screen.
            // We also check to make sure we do not scroll past the edge
            // of the background graphic.
	    return true;
        }
        else
        {
	    return false;
        }
    }
    
    @Override
    public void deactivateGame()
    {
        super.deactivateGame();
        keys.releaseAll();
    }

    @Override
    public void destroy()
    {
        super.destroy();
        events.destroy();
    }

}
