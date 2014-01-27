//Connects mouse clicks to advance a MIDI file
//James Vanderhyde, 22 May 2010

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class MouseClicker extends java.awt.Panel
{
    private MusicController music;

    private ArrayList<MusicEvent> events;
    private Iterator<MusicEvent> currentEvent;

    public MouseClicker()
    {
        this.setPreferredSize(new Dimension(320, 240));
        this.addMouseListener(new ClickListener());

        music=new MusicController();

        Button b;
        this.setLayout(new GridLayout(3,3));
        b=new Button("Next Note");
        b.addActionListener(new NextEventActivator());
        this.add(b);
        b=new Button("Stop");
        b.addActionListener(new Stopper());
        this.add(b);

        this.createEvents();

    }

    private void createEvents()
    {
        events=new ArrayList<MusicEvent>();
        events.add(MusicEvent.createStartStop(1532,2245));
        events.add(MusicEvent.createStartStop(2328,3021));
        events.add(MusicEvent.createStartStop(3100,4373));
        events.add(MusicEvent.createStartLoop(4496,6652,7147));
        events.add(MusicEvent.createContinueStop(8329));
        events.add(MusicEvent.createStartStop(8448,9133));
        events.add(MusicEvent.createStartStop(9188,10521));
        events.add(MusicEvent.createStartLoop(10660,12916,13423));

        currentEvent=events.iterator();
    }

    private class NextEventActivator implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            if (!currentEvent.hasNext())
                currentEvent=events.iterator();

            if (currentEvent.hasNext())
            {
                music.processEvent(currentEvent.next());
            }
        }
    }

    private class Starter implements ActionListener
    {
        private long startTick, stopTick;
        public Starter(long start, long stop)
        {
            startTick=start; stopTick=stop;
        }
        public void actionPerformed(ActionEvent evt)
        {
            music.setStartStop(startTick, stopTick);
        }
    }

    private class Looper implements ActionListener
    {
        private long startTick, stopTick;
        public Looper(long start, long stop)
        {
            startTick=start; stopTick=stop;
        }
        public void actionPerformed(ActionEvent evt)
        {
            music.setLoop(startTick, stopTick);
        }
    }

    private class Stopper implements ActionListener
    {
        public Stopper()
        {
        }
        public void actionPerformed(ActionEvent evt)
        {
            music.stopPlaying();
        }
    }

    private class ClickListener extends java.awt.event.MouseAdapter
    {
        public void mouseReleased(java.awt.event.MouseEvent evt)
        {
            System.out.println("Click!");
        }
    }
}
