//Frame for running the game
//James Vanderhyde, 22 May 2010

import javax.swing.JFrame;
import java.awt.event.WindowEvent;

public final class Main
{
    private static Main instance = null; //singleton
    
    public static synchronized Main getInstance()
    {
	if (instance==null)
	    instance=new Main();
	return instance;
    }
    
    public static void main(String[] args)
    {
	Main.getInstance().setup();
    }
    
    private JFrame f;
    private PlatformerGame game;
    private Thread gameLoop;
    
    private Main()
    {
	f=null;
	game=null;
	gameLoop=null;
    }
    
    private void setup()
    {
        f=new JFrame("World's Tiniest Platformer");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        game = new PlatformerGame();
	
        f.add(game);
        f.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent evt)
            {
                game.destroy();
            }
            @Override
            public void windowDeactivated(WindowEvent evt)
            {
                game.deactivateGame();
            }
            @Override
            public void windowActivated(WindowEvent evt)
            {
                game.requestFocus();
                game.activateGame();
            }
        });
	
	game.addMouseListener(new java.awt.event.MouseAdapter()
	{
	    @Override
	    public void mouseReleased(java.awt.event.MouseEvent evt)
	    {
		startGame();
	    }
	});

        f.pack();
        f.setVisible(true);
    }
    
    private void startGame()
    {
	if (gameLoop==null)
	{
            gameLoop=new Thread(game);
	    gameLoop.start();
	    game.startGame();
	}
    }
}
