//An abstract class for a game in a JPanel
//James Vanderhyde, 18 May 2000
//Modified by James Vanderhyde, 30 May 2010
// Changed superclass from Applet to JPanel

import java.awt.*;

/**
 * An abstract class that handles starting, pausing, and destroying
 * of a game. It runs as a JPanel.
 * @author jvanderhyde
 */
public abstract class GamePanel extends javax.swing.JPanel
    implements Runnable
{
    protected static int vTop=0;
    protected static int vLeft=0;
    protected static int vBottom=400;
    protected static int vRight=400;
    protected boolean threadDone;
    protected final long SLEEPTIME=20;
    protected boolean active;
    protected GraphicsConfiguration gc;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(vRight-vLeft,vBottom-vTop));
	threadDone=false;
        active=false;
	gc=null;
    }
    
    public abstract void paintGame(Graphics g);
    public abstract void updateGame(long timeSinceLastUpdate);
    
    public void run()
    {
	//As long as run() is called after the component is on screen, this should work.
	gc=this.getGraphicsConfiguration();

	long oldTime,newTime;

        System.out.println("Game created.");
	oldTime=System.currentTimeMillis();	
	while (!threadDone)
	{
	    try
	    {
	        Thread.sleep(SLEEPTIME);
	    }
	    catch (InterruptedException e)
	    {
	    }
	    newTime=System.currentTimeMillis();
            if (active)
            {
                updateGame(newTime-oldTime);
                repaint();
            }
	    oldTime=newTime;
	}
        System.out.println("Game destroyed.");
    }
    
    public void activateGame()
    {
        System.out.println("Game activated.");
        active=true;
    }

    public void deactivateGame()
    {
        System.out.println("Game deactivated.");
        active=false;
    }

    public void destroy()
    {
	threadDone=true;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
	paintGame(g);
    }
    
}
