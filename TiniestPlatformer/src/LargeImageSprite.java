//2D sprite class that breaks up the image to be drawn
//Created by James Vanderhyde, 15 July 2010

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class LargeImageSprite extends ImageSprite
{
    private int tileSize=400;
    private Image[][] images;

    public LargeImageSprite()
    {
        super();
    }

    public LargeImageSprite(String imageFileName, GraphicsConfiguration gc)
    {
        super(imageFileName, gc);
	createTiles();
    }

    protected void createTiles()
    {
	//Create a BufferedImage to break up into tiles
	BufferedImage bim;
	if (image instanceof BufferedImage)
	    bim=(BufferedImage)image;
	else
	{
	    bim=new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_ARGB);
	    Graphics g=bim.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    image=bim;
	}

	//Calculate number of tiles
	int numXTiles,numYTiles;
	numXTiles=((int)width)/tileSize;
	if (((int)width)%tileSize != 0)
	    System.err.println("Warning: LargeImageSprite width truncated to multiple of "+tileSize);
	numYTiles=((int)height)/tileSize;
	if (((int)height)%tileSize != 0)
	    System.err.println("Warning: LargeImageSprite height truncated to multiple of "+tileSize);

	//Construct the array
	images=new Image[numYTiles][];
	for (int yt=0; yt<numYTiles; yt++)
	    images[yt]=new Image[numXTiles];

	//Create the tiles
	for (int yt=0; yt<numYTiles; yt++) for (int xt=0; xt<numXTiles; xt++)
	{
	    //System.out.println("Creating tile "+xt+","+yt);
	    images[yt][xt]=bim.getSubimage(xt*tileSize, yt*tileSize, tileSize, tileSize);
	}
    }

    @Override
    public void paint(Graphics g)
    {
	Rectangle viewport=g.getClipBounds();
	if (viewport==null)
	    viewport=new Rectangle(0,0,tileSize,tileSize);

	double tileX=(viewport.x-(x-width/2))/tileSize;
	double tileY=(viewport.y-(y-height/2))/tileSize;
	int pixelOriginX=(int)Math.floor(x-width/2);
	int pixelOriginY=(int)Math.floor(y-height/2);

	for (int yt=(int)Math.floor(tileY); pixelOriginY+yt*tileSize<viewport.y+viewport.height; yt++)
	{
	    if ((0<=yt) && (yt<images.length))
		for (int xt=(int)Math.floor(tileX); pixelOriginX+xt*tileSize<viewport.x+viewport.width; xt++)
		{
		    if ((0<=xt) && (xt<images[yt].length))
		    {
			g.drawImage(images[yt][xt], pixelOriginX+xt*tileSize, pixelOriginY+yt*tileSize, null);
		    }
		}
	}
    }


}
