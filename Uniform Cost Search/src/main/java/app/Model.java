package app;// The contents of this file are dedicated to the public domain.
// (See http://creativecommons.org/publicdomain/zero/1.0/)

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import static app.ExtensionsKt.getBufferedImage;

class Model {
	private Controller controller;
	private byte[] terrain;
	private ArrayList<Sprite> sprites;

	Model(Controller c) {
		this.controller = c;
	}

	void initGame() throws Exception {
		BufferedImage bufferedImage = ImageIO.read(getBufferedImage("textures/terrain.png"));
		if(bufferedImage.getWidth() != 60 || bufferedImage.getHeight() != 60)
			throw new Exception("Expected the terrain image to have dimensions of 60-by-60");
		terrain = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
		sprites = new ArrayList<>();
		sprites.add(new Sprite(100, 100));
	}

	// These methods are used internally. They are not useful to the agents.
	byte[] getTerrain() { return this.terrain; }
	ArrayList<Sprite> getSprites() { return this.sprites; }

	void update() {
		for (Sprite sprite : sprites) sprite.update();
	}

	// 0 <= x < MAP_WIDTH.
	// 0 <= y < MAP_HEIGHT.
	float getTravelSpeed(Position pos) {
			int xx = (int)(pos.getX() * 0.1f);
			int yy = (int)(pos.getY() * 0.1f);
			if(xx >= 60) {
				xx = 119 - xx;
				yy = 59 - yy;
			}
			int p = 4 * (60 * yy + xx);
			return Math.max(0.2f, Math.min(3.5f, -0.01f * (terrain[p + 1] & 0xff) + 0.02f * (terrain[p + 3] & 0xff)));
	}

	Controller getController() { return controller; }
	Position getPos() { return sprites.get(0).pos; }
	Position getDestination() { return sprites.get(0).destination; }

	void setDestination(Position pos) {
		sprites.get(0).destination = pos;
	}

	double getDistanceToDestination(int sprite) {
		Sprite s = sprites.get(sprite);
		return s.pos.distanceTo(s.destination);
	}

	class Sprite {
		Position pos;
		Position destination;

		Sprite(float x, float y) {
			pos = new Position(x, y);
			destination = pos;
		}

		void update() {
			float speed = Model.this.getTravelSpeed(pos);
			Position d = destination.minus(pos);
			float dist = (float)Math.sqrt(d.getX() * d.getX() + d.getY() * d.getY());
			float t = speed / Math.max(speed, dist);
			d.timesAssign(t);
			pos.plusAssign(d);
		}
	}
}
