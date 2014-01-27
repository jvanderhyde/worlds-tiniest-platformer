//Keeps track of some key presses and releases
// Provides the same function for arrow keys, WASD, and IJKL.
//Created by James Vanderhyde, 30 may 2010

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyHandler
    implements KeyListener
{
    private static final int
        SPACE   =       0,
        UP      =       1,
        DOWN    =       2,
        LEFT    =       3,
        RIGHT   =       4;
    private static final int
        NUM_KEYS=       5;

    private boolean[] keyFlags;

    public KeyHandler()
    {
        keyFlags=new boolean[NUM_KEYS];
        releaseAll();
    }

    public final void releaseAll()
    {
        for (int i=0; i<NUM_KEYS; i++)
            keyFlags[i]=false;
    }

    public void keyPressed(KeyEvent evt)
    {
        //System.out.println("Key pressed: "+evt.getKeyCode());
        switch (evt.getKeyCode())
        {
        case KeyEvent.VK_SPACE:
            keyFlags[SPACE]=true;
            break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_I:
        case KeyEvent.VK_W:
            keyFlags[UP]=true;
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_K:
        case KeyEvent.VK_S:
            keyFlags[DOWN]=true;
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_J:
        case KeyEvent.VK_A:
            keyFlags[LEFT]=true;
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_L:
        case KeyEvent.VK_D:
            keyFlags[RIGHT]=true;
            break;
        default:
        }
    }

    public void keyReleased(KeyEvent evt)
    {
        //System.out.println("Key released: "+evt.getKeyCode());
        switch (evt.getKeyCode())
        {
        case KeyEvent.VK_SPACE:
            keyFlags[SPACE]=false;
            break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_I:
        case KeyEvent.VK_W:
            keyFlags[UP]=false;
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_K:
        case KeyEvent.VK_S:
            keyFlags[DOWN]=false;
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_J:
        case KeyEvent.VK_A:
            keyFlags[LEFT]=false;
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_L:
        case KeyEvent.VK_D:
            keyFlags[RIGHT]=false;
            break;
        default:
        }
    }

    public void keyTyped(KeyEvent evt) {}

    public boolean isSpacePressed()
    {
        return keyFlags[SPACE];
    }

    public boolean isUpPressed()
    {
        return keyFlags[UP];
    }

    public boolean isDownPressed()
    {
        return keyFlags[DOWN];
    }

    public boolean isLeftPressed()
    {
        return keyFlags[LEFT];
    }

    public boolean isRightPressed()
    {
        return keyFlags[RIGHT];
    }

}
