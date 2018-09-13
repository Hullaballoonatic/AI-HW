package app;// The contents of this file are dedicated to the public domain.
// (See http://creativecommons.org/publicdomain/zero/1.0/)

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import static app.ExtensionsKt.getResource;

class Model {
	private static final float EPSILON = 0.0001f; // A small number
	public static final float X_MAX = 1200.0f - EPSILON; // The maximum horizontal screen position. (The minimum is 0.)
	public static final float Y_MAX = 600.0f - EPSILON; // The maximum vertical screen position. (The minimum is 0.)

	private Controller controller;
	private byte[] terrain;
	private ArrayList<Sprite> sprites;

	Model(Controller c) {
		this.controller = c;
	}

	void initGame() throws Exception {
		BufferedImage bufferedImage = ImageIO.read(getResource("textures/terrain.png"));
		if(bufferedImage.getWidth() != 60 || bufferedImage.getHeight() != 60)
			throw new Exception("Expected the terrain image to have dimensions of 60-by-60");
		terrain = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
		sprites = new ArrayList<>();
		sprites.add(new Sprite(100, 100));
		AStarSearchCostComparator.setLowestCost(getLowest());
	}

	// These methods are used internally. They are not useful to the agents.
	byte[] getTerrain() { return this.terrain; }
	ArrayList<Sprite> getSprites() { return this.sprites; }

	void update() {
		// Update the agents
		for (Sprite sprite : sprites) sprite.update();
	}

	double getCost(float x, float y) {
		return getDistanceToDestination(0) / getTravelSpeed(x, y);
	}

	float getTravelSpeed(float x, float y) {
			int xx = (int)(x * 0.1f);
			int yy = (int)(y * 0.1f);
			if(xx >= 60)
			{
				xx = 119 - xx;
				yy = 59 - yy;
			}
			int pos = 4 * (60 * yy + xx);
			return Math.max(0.2f, Math.min(3.5f, -0.01f * (terrain[pos + 1] & 0xff) + 0.02f * (terrain[pos + 3] & 0xff)));
	}

	private float getLowest() {
		float min = getTravelSpeed(0,0);
		float tmp;
		for (int x = 0; x < X_MAX; x+=10) {
			for (int y = 0; y < Y_MAX; y+=10) {
				if ((tmp = getTravelSpeed(x,y)) < min) min = tmp;
			}
		}
		return min;
	}

	Controller getController() { return controller; }
	float getX() { return sprites.get(0).x; }
	float getY() { return sprites.get(0).y; }

	private void setDestination(float x, float y) {
		Sprite s = sprites.get(0);
		s.xDestination = x;
		s.yDestination = y;
	}

	void setDestination(GameState a) {
		setDestination(a.getX(), a.getY());
	}

	double getDistanceToDestination(int sprite) {
		Sprite s = sprites.get(sprite);
		return Math.sqrt((s.x - s.xDestination) * (s.x - s.xDestination) + (s.y - s.yDestination) * (s.y - s.yDestination));
	}

	class Sprite {
		float x;
		float y;
		float xDestination;
		float yDestination;

		Sprite(float x, float y) {
			this.x = x;
			this.y = y;
			this.xDestination = x;
			this.yDestination = y;
		}

		void update() {
			float speed = Model.this.getTravelSpeed(this.x, this.y);
			float dx = this.xDestination - this.x;
			float dy = this.yDestination - this.y;
			float dist = (float)Math.sqrt(dx * dx + dy * dy);
			float t = speed / Math.max(speed, dist);
			dx *= t;
			dy *= t;
			this.x += dx;
			this.y += dy;
			this.x = Math.max(0.0f, Math.min(X_MAX, this.x)); //prevent go out the border
			this.y = Math.max(0.0f, Math.min(Y_MAX, this.y));
		}
	}
}
