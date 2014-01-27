//2D sprite class
//Created by James Vanderhyde, 31 May 2010

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ImageSprite
{
    //Screen coordinates
    protected double x,y;
    protected double width, height;

    protected Image image;

    public ImageSprite()
    {
        image=null;
    }

    public ImageSprite(String imageFileName)
    {
        this.setImage(imageFileName);
        x=this.width/2;
        y=this.height/2;
    }
    
    public ImageSprite(String imageFileName, GraphicsConfiguration gc)
    {
        this(imageFileName);
	this.createCompatibleImage(gc);
    }

    protected final void setImage(String name)
    {
        image=(new ImageIcon(this.getClass().getResource(name))).getImage();
        //image=(new ImageIcon(name)).getImage();
        width=image.getWidth(null);
        height=image.getHeight(null);
    }

    private void createCompatibleImage(GraphicsConfiguration gc)
    {
	BufferedImage copy;
	if (image instanceof BufferedImage)
	{
	    BufferedImage bim = (BufferedImage)image;
	    int transparency = bim.getColorModel().getTransparency(); 
	    copy = gc.createCompatibleImage(bim.getWidth(), bim.getHeight(), transparency);
	}
	else
	{
	    final int t=java.awt.Transparency.TRANSLUCENT;
	    copy = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),t);
	}
	
	//copy image
	Graphics2D g2d = copy.createGraphics();
	g2d.drawImage(image,0,0,null);
	g2d.dispose();
	image=copy;
    }
    
    public static BufferedImage loadCompatibleImage(String imageFileName,GraphicsConfiguration gc)
    {
	ImageSprite s=new ImageSprite(imageFileName,gc);
	return (BufferedImage)s.image;
    }
    
    public void paint(Graphics g)
    {
	Rectangle viewport=g.getClipBounds();
	if ((viewport==null) || (viewport.intersects(this.getBoundingRect())))
	    g.drawImage(image, (int)Math.floor(x-width/2), (int)Math.floor(y-height/2), null);
    }
    
    public Rectangle2D.Double getBoundingRect()
    {
        return new Rectangle2D.Double(x-width/2, y-height/2,width,height);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x=x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y=y;
    }
}
