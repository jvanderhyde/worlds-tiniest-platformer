//Types of events that can affect what music is played
//James Vanderhyde, 22 May 2010

public class MusicEvent
{
    public static final int
        START = 1,
        STARTSTOP = 2,
        STARTLOOP = 3,
        CONTINUE = 4,
        CONTINUESTOP = 5,
        CONTINUELOOP = 6,
	TEMPOCHANGE = 7;

    private int type;
    private long startTick;

    private long loopStartTick;
    private long stopTick;

    private Object data;

    public static MusicEvent createStartStop(long start, long stop)
    {
        if (stop<start)
            throw new IllegalArgumentException("Stopping point must be after start");
        return new MusicEvent(STARTSTOP,start,start,stop);
    }

    public static MusicEvent createContinueStop(long stop)
    {
        return new MusicEvent(CONTINUESTOP,0,0,stop);
    }

    public static MusicEvent createStartLoop(long start, long loopback, long stop)
    {
        if (stop<loopback)
            throw new IllegalArgumentException("Stopping point must be after loopback");
        if (stop<start)
            throw new IllegalArgumentException("Stopping point must be after start");
        return new MusicEvent(STARTLOOP,start,loopback,stop);
    }

    public static MusicEvent createContinueLoop(long loopback, long stop)
    {
        if (stop<loopback)
            throw new IllegalArgumentException("Stopping point must be after loopback");
        return new MusicEvent(CONTINUELOOP,0,loopback,stop);
    }

    public static MusicEvent createStart(long start)
    {
        return new MusicEvent(START,start,start,-1);
    }

    public static MusicEvent createContinue()
    {
        return new MusicEvent(CONTINUE,0,0,-1);
    }

    public static MusicEvent createTempoChange(long start, float factor)
    {
	return new MusicEvent(TEMPOCHANGE,start,start,start,factor);
    }

    private MusicEvent(int type,long start,long loop,long stop)
    {
        this.type=type;
        this.startTick=start;
        this.loopStartTick=loop;
        this.stopTick=stop;
	this.data=null;
    }

    private MusicEvent(int type,long start,long loop,long stop,Object data)
    {
        this.type=type;
        this.startTick=start;
        this.loopStartTick=loop;
        this.stopTick=stop;
	this.data=data;
    }

    public int getType()
    {
        return type;
    }

    public long getLoopStartTick()
    {
        if ((type!=STARTLOOP) && (type!=CONTINUELOOP))
            throw new IllegalStateException("Loop not defined");
        return loopStartTick;
    }

    public long getStartTick()
    {
        return startTick;
    }

    public long getStopTick()
    {
        return stopTick;
    }

    public boolean isStartType()
    {
	return (type==START) || (type==STARTSTOP) || (type==STARTLOOP);
    }

    public boolean isContinueType()
    {
	return (type==CONTINUE) || (type==CONTINUESTOP) || (type==CONTINUELOOP);
    }

    public Object getData()
    {
	return data;
    }

}
