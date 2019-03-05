import java.awt.Color;

// Entity class outlines properties and functionality
// of an entity, many instances of which are stored and
// depicted on the 2D canvas
public class Entity {
	
	// variable definitions
	private int x;
	private int y;
	private Color c;
	int r, g, b;
	int power;
	int health;
	int reproductivePower;
	char team;
	
	// getters and setters
	public int getX() {
		return x;
	}
	public void setX(int newValue) {
		x = newValue;
	}
	public int getY() {
		return y;
	}
	public void setY(int newValue) {
		y = newValue;
	}
	public Color getColor() {
		return c;
	}
	public void setColor(Color newValue) {
		c = newValue;
	}
	
	// Constructor initialises variables
	public Entity(int x, int y, Color c, int rp, int p, int h, char t) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
		this.reproductivePower = rp;
		this.power = p;
		this.health = h;
		this.team = t;
	}
	
	public void updateColor() {
		this.c = new Color(this.r, this.g, this.b);
	}
	
	// MakeMove() method randomly chooses
	// one of 4 options, each of which is a different
	// action that the entity can perform.
	public void makeMove() {
		if(this.health <= 0) {
			this.x = -1;
			this.y = -1;
			SwingPaint.deadEntities.add(this);
		}
		checkOverCrowding();
		int r = (int)(Math.random() * 4);
		if(r == 0) {
			changeColor();
		} else if(r == 1) {
			moveLocation();
		} else if(r == 2) {
			reproduce();
		} else if(r == 3) {
			mutate();
		}
	}
	
	// Randomly increments r, g or b value
	// by 2. This serves to slowly change colour over time.
	// This colour change is passed down to future generations.
	public void changeColor() {
		int r = (int)(Math.random() * 6);
		if(r == 0 && this.r < 240) {
			this.r += 2;
		} else if(r == 1 && this.g < 240) {
			this.g += 2;
		} else if(r == 2 && this.b < 240) {
			this.b += 2;
		} else if(r == 3 && this.r > 10) {
			this.r -= 2;
		} else if(r == 4 && this.g > 10) {
			this.g -= 2;
		} else if(r == 5 && this.b > 10) {
			this.b -= 2;
		}
		updateColor();
	}
	
	// x, y co-ordinate position changed by +- 10.
	public void moveLocation() {
		int r = (int)(Math.random() * 4);
		int xpos = this.x, ypos = this.y;
		if(r == 0 && x < 390) {
			xpos += 10;
		} else if(r == 1 && y < 320){
			ypos += 10;
		} else if(r == 2 && x > 0) {
			xpos -= 10;
		} else if(r==3 && y > 0) {
			ypos -= 10;
		}
		if(SwingPaint.findEntity(xpos, ypos) != null
				&& SwingPaint.findEntity(xpos, ypos).team != this.team) {
			Entity winner = fight(this, SwingPaint.findEntity(xpos, ypos), xpos, ypos);
			winner.setX(xpos);
			winner.setY(ypos);
		} else {
			this.setX(xpos);
			this.setY(ypos);
		}
	}
	
	// Fight determined by health, power and luck.
	public Entity fight(Entity e, Entity f, int xpos, int ypos) {
		int eScore = e.health * e.power * (int)(Math.random() * 5);
		int fScore = f.health * f.power * (int)(Math.random() * 5);
		if(eScore > fScore) {
			f.setColor(Color.RED);
			SwingPaint.deadEntities.add(f);
			return e;
		}
		e.setColor(Color.RED);
		SwingPaint.deadEntities.add(e);
		return f;
	}
	
	// New entity created if there is enough space around parent.
	// Child entity inherites characteristics from parent.
	public void reproduce() {
		int r = (int)(Math.random() * this.reproductivePower);
		if (r == 0) {
			int xpos = -1; int ypos = -1;
			if(SwingPaint.findEntity(this.x+10, this.y) == null) {
				xpos = this.x+10;
				ypos = this.y;
			} else if (SwingPaint.findEntity(this.x-10, this.y) == null) {
				xpos = this.x-10;
				ypos = this.y;
			} else if (SwingPaint.findEntity(this.x, this.y+10) == null) {
				xpos = this.x;
				ypos = this.y+10;
			} else if (SwingPaint.findEntity(this.x, this.y-10) == null) {
				xpos = this.x;
				ypos = this.y-10;
			}
			if(xpos >=0 && ypos >=0 && xpos < 400 && ypos < 330) {
				this.reproductivePower++;
				this.health--;
				Entity e = new Entity(xpos, ypos, this.c, this.reproductivePower,
						this.power, this.health, this.team);
				SwingPaint.newEntities.add(e);
			}
		}
	}
	
	// Mutations can occur at random.
	public void mutate() {
		int r = (int)(Math.random() * 6);
		if(r == 0) {
			this.power++;
		} else if(r == 1) {
			this.power--;
		} else if(r == 2) {
			this.health++;
		} else if(r == 3) {
			this.health--;
		} else if(r == 4) {
			this.reproductivePower++;
		} else if(r == 5) {
			this.reproductivePower--;
		}
	}
	
	// Overcrowding can cause entities to die
	// (This also helps with performance)
	public void checkOverCrowding() {
		if(SwingPaint.findEntity(this.x+10, this.y) != null
				&& SwingPaint.findEntity(this.x-10, this.y) != null
				&& SwingPaint.findEntity(this.x, this.y+10) != null
				&& SwingPaint.findEntity(this.x, this.y-10) != null
				&& SwingPaint.findEntity(this.x+10, this.y+10) != null
				&& SwingPaint.findEntity(this.x-10, this.y+10) != null
				&& SwingPaint.findEntity(this.x+10, this.y-10) != null
				&& SwingPaint.findEntity(this.x+10, this.y-10) != null) {
			int r = (int)(Math.random() * 2);
			if(r == 0) {
				SwingPaint.deadEntities.add(this);
			}
		} 
	}
}
