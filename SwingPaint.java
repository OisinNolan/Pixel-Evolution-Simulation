import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingPaint  extends Canvas {
	
	// Set up preliminary values
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 400;
    private static final int HEIGHT = 335;
    public static boolean paused = true;
    public static boolean started = false;
    public static int speed = 300;
    public static float colourCycle = 50;
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static ArrayList<Entity> newEntities = new ArrayList<Entity>();
    public static ArrayList<Entity> deadEntities = new ArrayList<Entity>();
    
    // Setup function, generates new sets of 
    // entities, and resets some values.
    public static void setUp() {
    		paused = true;
    		speed = 300;
    		entities.removeAll(entities);
    		newEntities.removeAll(newEntities);
    		deadEntities.removeAll(deadEntities);
    		Entity b = new Entity(0, 0, randomColor(), (int)(Math.random() * 5)+2, (int)(Math.random() * 5)+2, (int)(Math.random() * 10)+4, 'a');
    		entities.add(b);
    		Entity r = new Entity(390, 0, randomColor(), (int)(Math.random() * 5)+2, (int)(Math.random() * 5)+2, (int)(Math.random() * 10)+4, 'b');
    		entities.add(r);
    		Entity g = new Entity(0, 320, randomColor(), (int)(Math.random() * 5)+2, (int)(Math.random() * 5)+2, (int)(Math.random() * 10)+4, 'c');
    		entities.add(g);
    		Entity y = new Entity(390, 320, randomColor(),  (int)(Math.random() * 5)+2, (int)(Math.random() * 5)+2, (int)(Math.random() * 10)+4, 'd');
    		entities.add(y);
    }
    
    // Generates random colours, stepping up hue
    // by ~0-50 each time, ensuring colours
    // will be different from one another and not
    // too close to red
    public static Color randomColor() {
	    	final float hue = (float) Math.random()*colourCycle;
	    	colourCycle += 50;
	    	if(colourCycle >= 250) {
	    		colourCycle = 50;
	    	}
	    	final float saturation = 0.9f;
	    	final float luminance = 1.0f;
	    	return Color.getHSBColor(hue, saturation, luminance);
    }
    
    // Scans through the canvas x and y 
    // co-ordinates, and for each co-ord
    // checks if there is an entity there,
    // in which case it updates the colour accordingly.
    @Override
    public void paint(Graphics g) {
        for(int x = 0; x < WIDTH; x+=10) {
            for(int y = 0; y < HEIGHT; y+=10) {
            		Entity e = findEntity(x, y);
            		if(e != null) {
            			g.setColor(e.getColor());
            		} else {
	                g.setColor(Color.WHITE);
            		}
            		g.fillRect(x, y, 10, 10);
            }
        }
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
	
	    		@Override
	    		public void run() {
		    			update();
		    			repaint();
	    		}
        	
        };
        timer.schedule(task, speed);
    }
    
    // Loops through every entity and commands each
    // to make a move
    public static void update() {
    		if(!paused) {
	    		for(Entity e : entities) {
	    			e.makeMove();
	    		}
	    		entities.addAll(newEntities);
	    		entities.removeAll(deadEntities);
    		}
    }
    
    // finds and returns entity at co-ords
    // specified by parameters
    public static Entity findEntity(int x, int y) {
    		for(Entity e : entities) {
    			if(e.getX() == x && e.getY() == y) {
    				return e;
    			}
    		}
    		return null;
    }
    
    // Main method sets up JFrame and UI buttons
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pixel Evolution");
        JPanel p = new JPanel();
        setUp();
        JButton startButton = new JButton("Start");
        JButton playButton = new JButton("Pause");
        JButton speedUpButton = new JButton("+");
        JButton slowDownButton = new JButton("-");
        speedUpButton.setPreferredSize(new Dimension(20, 20));
        slowDownButton.setPreferredSize(new Dimension(20, 20));
        JLabel speedLabel = new JLabel("speed");
        p.add(startButton);
        p.add(playButton);
        p.add(slowDownButton);
        p.add(speedLabel);
        p.add(speedUpButton);
        int w = 400;
        int h = 400;
        frame.setSize(w, h);
		SwingPaint canvas = new SwingPaint();
		canvas.setBounds(0, 0, 400, 500);
		p.add(canvas);
		frame.getContentPane().add(p);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Listeners for button click events
        startButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!started) {
					paused = false;
					startButton.setText("Reset");
					started = true;
				} else {
					startButton.setText("Start");
					playButton.setText("Pause");
					started = false;
					setUp();
				}
			} 
        	} );
        playButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				if(started) {
					if(paused) {
						playButton.setText("Pause");
						paused = false;
					} else {
						playButton.setText("Play");
						paused = true;
					}
				}
			} 
        	} );
        speedUpButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speed > 50) {
					speed /= 2;
				}
			} 
        	} );
        slowDownButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				if(speed < 1000) {
					speed *= 2;
				}
			} 
        	} );
    }
}