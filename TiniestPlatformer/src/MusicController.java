//Controls the music for the game.
//James Vanderhyde, 22 May 2010

import javax.sound.midi.*;
import java.io.*;
import java.net.URL;

public class MusicController
{
    private Sequencer sequencer;
    private Sequence sequence;

    //A thread that waits the right amount of time
    // and then tells the sequencer to stop.
    private Stopper stopper;

    public MusicController()
    {
	init("Zarathustra modified fixed.mid",false);
	stopper=null;
    }

    private void clearStopper()
    {
	if (stopper!=null)
	{
	    stopper.interrupt();
	    stopper=null;
	}
    }

    public void processEvent(MusicEvent evt)
    {
	clearStopper();

        switch (evt.getType())
        {
        case MusicEvent.START:
            this.setStart(evt.getStartTick());
            break;
        case MusicEvent.STARTSTOP:
            this.setStartStop(evt.getStartTick(),evt.getStopTick());
            break;
        case MusicEvent.STARTLOOP:
            this.setStartLoop(evt.getStartTick(), evt.getLoopStartTick(), evt.getStopTick());
            break;
        case MusicEvent.CONTINUE:
            this.setContinue();
            break;
        case MusicEvent.CONTINUESTOP:
            this.setContinueStop(evt.getStopTick());
            break;
        case MusicEvent.CONTINUELOOP:
            this.setContinueLoop(evt.getLoopStartTick(), evt.getStopTick());
            break;
	case MusicEvent.TEMPOCHANGE:
	    this.changeTempo((Float)evt.getData());
	    break;
        }
    }

    public void setStartStop(long start, long stop)
    {
        this.stopPlaying();
        sequencer.setLoopCount(0);
        sequencer.setTickPosition(start);
        stopper=new Stopper(stop-start);
        sequencer.start();
        stopper.start();
    }

    public void setContinueStop(long stop)
    {
        sequencer.setLoopCount(0);
        stopper=new Stopper(stop-sequencer.getTickPosition());
        stopper.start();
    }

    public void setLoop(long start, long stop)
    {
        this.setStartLoop(start, start, stop);
    }

    public void setStartLoop(long start, long loopback, long stop)
    {
        this.stopPlaying();
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.setLoopEndPoint(-1);//so the start point is always valid
        sequencer.setLoopStartPoint(loopback);
        sequencer.setLoopEndPoint(stop);
        sequencer.setTickPosition(start);
        sequencer.start();
    }

    public void setContinueLoop(long loopback, long stop)
    {
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.setLoopEndPoint(-1);//so the start point is always valid
        sequencer.setLoopStartPoint(loopback);
        sequencer.setLoopEndPoint(stop);
    }

    public void setStart(long start)
    {
        this.stopPlaying();
        sequencer.setLoopCount(0);
        sequencer.setTickPosition(start);
        sequencer.start();
    }

    public void setContinue()
    {
        sequencer.setLoopCount(0);
    }

    public void changeTempo(float factor)
    {
	sequencer.setTempoFactor(factor);
    }

    public void stopPlaying()
    {
        if (sequencer.isRunning())
            sequencer.stop();
	clearStopper();
    }
    
    public long getNanos(long ticks)
    {
        return (long)(ticks*sequence.getMicrosecondLength()*1000/sequence.getTickLength()/sequencer.getTempoFactor());
    }

    public void setTempoFactor(float factor)
    {
        sequencer.setTempoFactor(factor);
    }

    private class Stopper extends Thread
    {
        private long millis;
        private int nanos;
        private long finalTick;

        public Stopper(long ticks)
        {
            if (ticks<0) throw new IllegalArgumentException("The number of ticks must be nonnegative.");

            finalTick=sequencer.getTickPosition()+ticks;

            long totalNanos=getNanos(ticks);
            this.millis=totalNanos/1000000;
            this.nanos=(int)(totalNanos%1000000);
        }
        @Override
        public void run()
        {
            try
            {
                sleep(millis-50,nanos);
                while (sequencer.getTickPosition()<finalTick) sleep(10);
                stopPlaying();
            }
            catch (InterruptedException e)
	    {}
        }
    }
    
    private class InitException extends RuntimeException
    {
        public InitException(String message, Throwable cause)
        {
            super(message,cause);
        }
        public InitException(String message)
        {
            super(message);
        }
        public InitException()
        {
            super();
        }
    }

    //Sets up the sequencer and sequence.
    // Postcondition: sequencer and sequence are not null.
    // Throws: MusicController.InitExcpetion if any setup errors occur.
    private void init(String filename,boolean useURL)
    {
	sequencer=null;
	try
	{
	    sequencer = MidiSystem.getSequencer(); 
	    if (sequencer == null)
	    {
                throw new InitException("No MIDI sequencer available.");
	    }
	    else
	    {
		sequencer.open();
	    }
	}
	catch (MidiUnavailableException e)
	{
            throw new InitException("MIDI is unavailable.",e);
	}
	
	sequence=null;
	try
	{
	    if (useURL)
	    {
		URL midiURL = new URL(filename);
		sequence = MidiSystem.getSequence(midiURL);
	    }
	    else
	    {
		//File midiFile = new File(filename);
		URL midiURL=this.getClass().getResource(filename);
		sequence = MidiSystem.getSequence(midiURL);
	    }
	    sequencer.setSequence(sequence);
	}
	catch (NullPointerException e) {
            throw new InitException("No midi file specified.",e);
	}
	catch (InvalidMidiDataException e) {
            throw new InitException("Unreadable/unsupported midi file: " + filename,e);
	}
	catch (IOException e) {
            throw new InitException("Error reading file: " + filename,e);
	}
	if (sequence==null)
	{
            throw new InitException("Error creating midi sequence.");
	}
        System.out.println("MIDI system initialized.");
    }

    //Cleans up resources so the program exits cleanly.
    // Postcondition: The sequencer is closed.
    public void destroy()
    {
	clearStopper();
	if (sequencer.isOpen())
	    sequencer.close();
        System.out.println("MIDI system destroyed.");
    }

}
