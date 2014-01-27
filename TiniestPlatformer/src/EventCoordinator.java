//This class cooridnates the MusicEvents with their world points.
// It also checks for player collisions with the event points.
//Created by James Vanderhyde, 16 June 2010

import java.util.ArrayList;

public class EventCoordinator
{
    private MusicController music;

    private ArrayList<MusicEvent> events;

    //Trumpet notes for game events
    private long[] noteTicksMichaelFord =
    {
            0,           //(drum roll)
         1532,           //C (Trumpet 1)
         2328,           //G
         3100,           //C
         4496,           //E, Eb
         7704,           //C
         8448,           //G
         9188,           //C
        10660,           //Eb, E
        13824,           //C
        14592,           //G
        15360,           //C
        16816,           //E, F
        18492,           //A, B, C (Trombone)
        19576,           //D (Trumpet 2)
        20080,           //E
        20256,           //F
        20460,           //G
        21424,           //E, F, G (Trumpet 1)
        22284,           //A
        22716,           //B
        23176,           //C
        26144            //(end)
    };

    //Trumpet notes for game events
    private long[] noteTicks =
    {
            0,           //(drum roll)
         7680,           //C (Trumpet 1)
         8640,           //G
         9600,           //C
        11400,           //E, Eb
	14080,		 //Tympani loop
        14400,           //C
        15360,           //G
        16320,           //C
        18120,           //Eb, E
	20800,		 //Tympani loop
        21120,           //C
        22080,           //G
        23040,           //C
        24840,           //E, F
        26880,           //A, B, C (Trombone)
        28320,           //D (Trumpet 2)
        28800,           //E
        29040,           //F
        29280,           //G
        30480,           //E, F, G (Trumpet 1)
        31680,           //A
        32160,           //B
        32640,           //C
        37680            //(end)
    };

    private double[] eventLocs =
    {
	0,
	395,
	645,
	894,
	1362,
	1891,
	2141,
	2390,
	2858,
	3387,
	3637,
	3886,
	4354,
	4884,
	5258,
	5382,
	5445,
	5507,
	5819,
	6131,
	6255,
	6380
    };

    private ArrayList<MusicEventPoint> eventPoints;
    private double playerSpeed;

    public EventCoordinator(double playerSpeed)
    {
        music=new MusicController();
        //music.setTempoFactor(2f);
        this.playerSpeed=playerSpeed;
    }

    private void addEventsMichaelFord()
    {
        events.add(MusicEvent.createStartLoop(0,1400,1515));		//(drum roll)
        events.add(MusicEvent.createContinueStop(2245));		//C (Trumpet 1)
        events.add(MusicEvent.createStartStop(2328,3021));		//G
        events.add(MusicEvent.createStartStop(3100,4373));		//C
        events.add(MusicEvent.createStartLoop(4496,6652,7147));		//E, Eb
        events.add(MusicEvent.createContinueStop(8380));		//C
        events.add(MusicEvent.createStartStop(8448,9133));		//G
        events.add(MusicEvent.createStartStop(9188,10521));		//C
        events.add(MusicEvent.createStartLoop(10660,12916,13423));	//Eb, E
        events.add(MusicEvent.createContinueStop(14485));		//C
        events.add(MusicEvent.createStartStop(14585,15295));		//G
        events.add(MusicEvent.createStartStop(15355,16705));		//C
        events.add(MusicEvent.createStartLoop(16790,18080,18200));	//E, F
        events.add(MusicEvent.createContinueLoop(19440,19535));		//A, B, C (Trombone)
        events.add(MusicEvent.createContinueStop(19960));		//D (Trumpet 2)
        events.add(MusicEvent.createStartStop(20060,20225));		//E
        events.add(MusicEvent.createStartStop(20255,20420));		//F
        events.add(MusicEvent.createStartStop(20450,21270));		//G
        events.add(MusicEvent.createStartStop(21310,22225));		//E, F, G (Trumpet 1)
        events.add(MusicEvent.createStartStop(22255,22665));		//A
        events.add(MusicEvent.createStartStop(22680,23155));		//B
        events.add(MusicEvent.createStart(23170));			//C
    }

    private void startStopEvent(int i)
    {
        events.add(MusicEvent.createStartStop(noteTicks[i],noteTicks[i+1]-9));
    }

    private void startLoopEvent(int i)
    {
        events.add(MusicEvent.createStartLoop(noteTicks[i],noteTicks[i+1],noteTicks[i+2]-1));
    }

    private void continueStopEvent(int i)
    {
        events.add(MusicEvent.createContinueStop(noteTicks[i+1]-9));
    }

    private void tempoChange(int i,float factor)
    {
	events.add(MusicEvent.createTempoChange(noteTicks[i], factor));
    }

    private void addEventsJV()
    {
        startStopEvent(0);		//(drum roll)
        startStopEvent(1);	    	//C (Trumpet 1)
        startStopEvent(2);		//G
        startStopEvent(3);		//C
        startLoopEvent(4);		//E, Eb
        continueStopEvent(6);		//C
        startStopEvent(7);		//G
        startStopEvent(8);		//C
        startLoopEvent(9);		//Eb, E
        continueStopEvent(11);		//C
        startStopEvent(12);		//G
        startStopEvent(13);		//C
        startStopEvent(14);		//E, F
        startStopEvent(15);		//A, B, C (Trombone)
        startStopEvent(16);		//D (Trumpet 2)
        startStopEvent(17);		//E
        startStopEvent(18);		//F
        startStopEvent(19);		//G
        startStopEvent(20);		//E, F, G (Trumpet 1)
        startStopEvent(21);		//A
        startStopEvent(22);		//B
        startStopEvent(23);		//C
    }

    public void printLocsFromEvents()
    {
        for (int i=0; i<events.size(); i++)
        {
	    //The location of an event point is proportional to
	    // the start tick of the corresponding music event,
	    // unless it's a continue event, in which case we use
	    // the stop tick of the previous music event.
	    // (The event prior to a continue must exist and
	    // must be a loop event, which always has a stop.)
	    MusicEvent e = events.get(i);
	    long noteTick;
	    if (e.isStartType())
		noteTick=e.getStartTick();
	    else
		noteTick=events.get(i-1).getStopTick();
	    double x=music.getNanos(noteTick)/1e6*playerSpeed;
	    System.out.println(x);
        }
    }

    public void createEvents()
    {
        events=new ArrayList<MusicEvent>();

	addEventsJV();

        eventPoints=new ArrayList<MusicEventPoint>();
	//printLocsFromEvents();

        for (int i=0; i<events.size(); i++)
        {
            eventPoints.add(new MusicEventPoint(eventLocs[i],0));
        }
    }

    public double[] getEventPointsX()
    {
        double[] r=new double[eventPoints.size()];
        int i=0;
        for (MusicEventPoint p:eventPoints)
            r[i++]=p.getWorldX();
        return r;
    }
    
    public void playNextUnplayedMusicEvent(double playerX)
    {
        //Assuming each event point has a corresponding MusicEvent in the arrays,
        // we search the list of event points to find one that is less than
        // playerX but has not already been activated.
        for (int i=0; i<eventPoints.size(); i++)
        {
            MusicEventPoint p=eventPoints.get(i);
            if ((!p.isAlreadyActivated()) &&
                (p.getWorldX()<=playerX))
            {
                p.activate();
                music.processEvent(events.get(i));
            }
        }
    }

    public void stopMusic()
    {
	music.stopPlaying();
    }

    public void destroy()
    {
        music.destroy();
    }


}
