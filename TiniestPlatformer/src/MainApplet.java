//Applet for running the game
//James Vanderhyde, 15 July 2010

public class MainApplet extends javax.swing.JApplet
{
    PlatformerGame game;
    Thread gameLoop;

    @Override
    public void init()
    {
        game = new PlatformerGame();
	this.add(game);
	this.addKeyListener(game.getKeyHandler());
	this.addFocusListener(new GetFocusToStart());

	gameLoop=null;
    }

    @Override
    public void start()
    {
	game.activateGame();
    }

    @Override
    public void stop()
    {
	game.deactivateGame();
    }

    @Override
    public void destroy()
    {
	game.destroy();
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

    private class GetFocusToStart extends java.awt.event.FocusAdapter
    {
	@Override
	public void focusGained(java.awt.event.FocusEvent evt)
	{
	    startGame();
	}
    }
}
