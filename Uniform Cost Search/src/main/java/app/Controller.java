package app;// The contents of this file are dedicated to the public domain.
// (See http://creativecommons.org/publicdomain/zero/1.0/)

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

class Controller implements MouseListener
{
	Agent agent;
	private Model model; // holds all the game data
	private View view; // the GUI
	private LinkedList<MouseEvent> mouseEvents; // a queue of mouse events

	private Controller() {
		this.agent = new Agent();
		this.mouseEvents = new LinkedList<>();
	}

	private void init() throws Exception {
		this.model = new Model(this);
		this.model.initGame();
	}

	boolean update() {
		agent.update(model);
		model.update();
		return true;
	}

	MouseEvent nextMouseEvent() { //get next mouse event; mouse event contains mouse x and y values
		if(mouseEvents.size() == 0)
			return null;
		return mouseEvents.remove();
	}

	public void mousePressed(MouseEvent e) {
		if(e.getY() < 600) {
			mouseEvents.add(e);
			if(mouseEvents.size() > 20) // discard events if the queue gets big
				mouseEvents.remove();
		}
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	static void playGame() throws Exception {
		Controller c = new Controller();
		c.init();
		c.view = new View(c, c.model); // instantiates a JFrame, which spawns another thread to pump events and keeps the whole program running until the JFrame is closed
		new Timer(20, c.view).start(); // creates an ActionEvent at regular intervals, which is handled by View.actionPerformed
	}

}
