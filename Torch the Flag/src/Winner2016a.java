import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// This agent was written by Michael Mahler
class Winner2016a implements IAgent {

	//generated genetically (hard coded for simplicity)
	int side = 63;
	int counter, dodges, index, attack, defend, kill;
	Random rndm;
	BufferedImage image;


//===================
// REQUIRED
//===================
	Winner2016a() throws Exception {
		reset();
		rndm = new Random();
		image = ImageIO.read(new File("terrain.png"));
	}

	public void reset() {
		counter = 0;
		rndm = new Random();
	}

	public void update(Model m) {
		for(int i = 0; i < m.getSpriteCountSelf(); i++) {
			if(m.getEnergySelf(i) > 0)
				chooseRole(m, i);
		}

		counter++;
	}




//===================
// UTILITIES
//===================
	//the distance formula
	public static float sq_dist(float x_1, float y_1, float x_2, float y_2) {
		return (x_1 - x_2) * (x_1 - x_2) + (y_1 - y_2) * (y_1 - y_2);
	}

	public static float dist(float x_1, float y_1, float x_2, float y_2) {
		return (float)Math.sqrt((x_1 - x_2) * (x_1 - x_2) + (y_1 - y_2) * (y_1 - y_2));
	}




//===================
// FORKING
//===================
	//these methods are for forking the universe to prevent infinite recursion
	static class Shadow implements IAgent {
		float dx;
		float dy;

		Shadow(float destX, float destY) {
			dx = destX;
			dy = destY;
		}

		public void reset() {}

		public void update(Model m) {
			for(int i = 0; i < 3; i++) {
				if (dx > 0 && dx < Model.XMAX && dy > 0 && dy < Model.YMAX)
					m.setDestination(i, dx, dy);
			}
		}
	}

	static class OpponentShadow implements IAgent {
		int index;
		OpponentShadow() {}

		public void reset() {}

		float nearestOpponent(Model m, float x, float y) {
			index = -1;
			float dd = Float.MAX_VALUE;
			for(int i = 0; i < m.getSpriteCountOpponent(); i++) {
				if(m.getEnergyOpponent(i) < 0)
					continue; // don't care about dead opponents
				float d = sq_dist(x, y, m.getXOpponent(i), m.getYOpponent(i));
				if(d < dd) {
					dd = d;
					index = i;
				}
			}
			return dd;
		}

		float nearestTarget(Model m, float x, float y) {
			index = -1;
			float dd = Float.MAX_VALUE;
			for(int i = 0; i < m.getBombCount(); i++) {
				float d = sq_dist(x, y, m.getBombTargetX(i), m.getBombTargetY(i));
				if(d < dd) {
					dd = d;
					index = i;
				}
			}
			return dd;
		}

		void evade(Model m, int i) {
			if(nearestTarget(m, m.getX(i), m.getY(i)) <= 2.0f * Model.BLAST_RADIUS * Model.BLAST_RADIUS) {
				float dx;
				dx = m.getX(i) + m.getBombTargetX(index);
				float dy;
				if (m.getY(i) > Model.YFLAG_OPPONENT)
					 dy = m.getY(i) - m.getBombTargetY(index);
				else
					dy = m.getY(i) + m.getBombTargetY(index);
				if(dx == 0 && dy == 0)
					dx = 1.0f;
				m.setDestination(i, m.getX(i) + dx, m.getY(i) + dy);
			}
		}

		public void update(Model m) {
			for(int i = 0; i < m.getSpriteCountSelf(); i++) {
				nearestOpponent(m, m.getX(i), m.getY(i));

				if(index >= 0) {
					float eX = m.getXOpponent(index);
					float eY = m.getYOpponent(index);
					if(sq_dist(eX, eY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
						m.throwBomb(i, eX, eY);
				}
				evade(m, i);
			}
		}
	}

	void forkevade(Model m, int i, float bombX, float bombY) {
		dodges++;

		float bestX = m.getX(i);
		float bestY = m.getY(i);
		float bestDodge = 0;
		for(int sim = 0; sim < 8; sim++) { // try 8 candidate destinations
			float x = (float)(m.getX(i)+(Math.cos((Math.PI*sim)/4)*Model.BLAST_RADIUS));
			float y = (float)(m.getY(i)+(Math.sin((Math.PI*sim)/4)*Model.BLAST_RADIUS));

			// Fork the universe and simulate it for 10 time-steps
			Controller cFork = m.getController().fork(new Shadow(x, y), new OpponentShadow());
			Model mFork = cFork.getModel();
			for(int j = 0; j < 10; j++)
				cFork.update();

			// See how close the current sprite got to the opponent's flag in the forked universe
			float dodge = sq_dist(mFork.getX(i), mFork.getY(i), bombX, bombY);

			if(dodge > bestDodge) {
				bestDodge = dodge;
				bestX = x;
				bestY = y;
			}
		}

		// Head for the point that worked out best in simulation
		m.setDestination(i, bestX, bestY);
	}


//===================
// GAME STATE UTILS
//===================

	float nearestTarget(Model m, float x, float y) {
		index = -1;
		float max_float = Float.MAX_VALUE;
		for(int i = 0; i < m.getBombCount(); i++) {
			float d = sq_dist(x, y, m.getBombTargetX(i), m.getBombTargetY(i));
			if(d < max_float) {
				max_float = d;
				index = i;
			}
		}
		return max_float;
	}

	float nearestOpponent(Model m, float x, float y) {
		index = -1;
		float max_float = Float.MAX_VALUE;
		for(int i = 0; i < m.getSpriteCountOpponent(); i++) {
			if (!(m.getEnergyOpponent(i) < 0)) {
				float d = sq_dist(x, y, m.getXOpponent(i), m.getYOpponent(i));
				if(d < max_float) {
					max_float = d;
					index = i;
				}
			}

		}
		return max_float;
	}

	float nearestOpponentToFlag(Model m) {
		index = -1;
		float max_float = Float.MAX_VALUE;
		for(int i = 0; i < m.getSpriteCountOpponent(); i++) {
			if (!(m.getEnergyOpponent(i) < 0)) {
				float d = sq_dist(Model.XFLAG, Model.YFLAG, m.getXOpponent(i), m.getYOpponent(i));
				if(d < max_float) {
					max_float = d;
					index = i;
				}
			}
		}
		return max_float;
	}

	float nextToRevive(Model m, float x, float y) {
		index = -1;
		float max_float = Float.MAX_VALUE;
		for(int i = 0; i < m.getSpriteCountOpponent(); i++) {
			if (!(m.getEnergyOpponent(i) > 0)) {
				float d = sq_dist(Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT, m.getXOpponent(i), m.getYOpponent(i));
				if(d < max_float) {
					max_float = d;
					index = i;
				}
			}
		}
		return max_float;
	}

	boolean isSafe(Model m, int i, float buf){
		float X = m.getX(i);
		float Y = m.getY(i);
		return (nearestOpponent(m, X, Y) > (((Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS) * (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS))+buf));
	}

	int opponentsInRange(Model m, int i, float buf){
		float X = m.getX(i);
		float Y = m.getY(i);
		int numInRange = 0;
		for(int j = 0; j < m.getSpriteCountOpponent(); j++)
			if(m.getEnergyOpponent(j) > 0 && sq_dist(X, Y, m.getXOpponent(j), m.getYOpponent(j)) <= (((Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS) * (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS)))+buf)
				numInRange++;
		return numInRange;
	}

	int opponentsAlive(Model m){
		int numAlive = 0;
		for(int i = 0; i < m.getSpriteCountOpponent(); i++)
			if(m.getEnergyOpponent(i) > 0)
				numAlive++;
		return numAlive;
	}

	boolean revivalImminent(Model m, float offset){
		for(int i = 0; i < m.getSpriteCountOpponent(); i++)
			if(m.getEnergyOpponent(i) <= 0)
				if (sq_dist(Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT, m.getXOpponent(i), m.getYOpponent(i)) < offset*offset)
					return true;
		return false;
	}




//===================
// ROBOT ACTIONS
//===================

	//this method gets called on every sprite every update
	void chooseRole(Model m, int i){
		if (counter % side == 0){
			bestAttacker(m);
			bestDefendender(m);
		}
		while (true){

			if (((opponentsAlive(m) <= 1 && nextToRevive(m, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) >= 100*100) || (opponentsAlive(m) == 0)) && nearestOpponentToFlag(m) >= 500*500){
					beFlagAttacker(m, i);
				break;
			}
			else {

				if (defend == i){
					beDefendender(m, i);
					break;
				}
				if (attack == i){
					if (opponentsInRange(m, defend, 250) <= 1 || m.getEnergySelf(defend)>.75)
						beFlagAttacker(m, i);
					else
						beAttacker(m, i);
					break;
				}
				if (m.getFlagEnergyOpponent() < .5 && opponentsInRange(m, defend, 250) == 0)
					beFlagAttacker(m, i);
				else
					beAttacker(m, i);
				break;
			}

		}
	}

	float bestDefendender(Model m) {
		defend = -1;
		float dd = Float.MAX_VALUE;
		for(int i = 0; i < m.getSpriteCountSelf(); i++) {
			if(m.getEnergySelf(i) < 0)
				continue; // don't care about dead agents
			float d = sq_dist(Model.XFLAG, Model.YFLAG, m.getX(i), m.getY(i));
			if(d < dd) {
				dd = d;
				defend = i;
			}
		}
		return dd;
	}

	float bestAttacker(Model m) {
		attack = -1;
		float dd = Float.MAX_VALUE;
		for(int i = 0; i < m.getSpriteCountSelf(); i++) {
			if(m.getEnergySelf(i) < 0)
				continue; // don't care about dead agents
			float d = sq_dist(Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT, m.getX(i), m.getY(i));
			if(d < dd) {
				dd = d;
				attack = i;
			}
		}
		return dd;
	}

	void evade(Model m, int i) {
		if(nearestTarget(m, m.getX(i), m.getY(i)) <= 2.0f * Model.BLAST_RADIUS * Model.BLAST_RADIUS) {
			if (i == defend && Model.XFLAG > m.getX(i) && m.getEnergySelf(i) >= .5){
				float dx;
				dx = m.getX(i) + m.getBombTargetX(index);
				float dy;
				if (m.getY(i) > Model.YFLAG)
					 dy = m.getY(i) + (m.getBombTargetY(index)+5);
				else
					dy = m.getY(i) - (m.getBombTargetY(index)+5);
				if(dx == 0 && dy == 0)
					dx = 1.0f;
				m.setDestination(i, m.getX(i) + dx, m.getY(i) + dy);
			}
			else
				forkevade(m, i, m.getBombTargetX(index), m.getBombTargetY(index));
			dodges++;
		}
	}

	void pickPath(Model m, int i, float XGoal, float YGoal) {

		float bestX = m.getX(i);
		float bestY = m.getY(i);
		float best_sqdist = 0;

		float startX = m.getX(i);
		float startY = m.getY(i);

		for(int sim = 0; sim < 8; sim++) { // try 8 candidate destinations
			float x = (float)(m.getX(i)+(Math.cos((Math.PI*sim)/4)*100));
			float y = (float)(m.getY(i)+(Math.sin((Math.PI*sim)/4)*100));

			// Fork the universe and simulate it for 10 time-steps
			Controller cFork = m.getController().fork(new Shadow(x, y), new OpponentShadow());
			Model mFork = cFork.getModel();
			for(int j = 0; j < 10; j++)
				cFork.update();


			// See how much ground was covered in the forked universe
			float sqd = dist(mFork.getX(i), mFork.getY(i), startX, startY);
			if(sqd > best_sqdist) {
				best_sqdist = sqd;
				bestX = x;
				bestY = y;
			}
		}


		// Head for the point that worked out best in simulation
		m.setDestination(i, bestX, bestY);

		// Shoot at the flag if I can hit it
		if(sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
			m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
	}

	void beDefendender(Model m, int i) {
		// Find the opponent nearest to my flag
		nearestOpponentToFlag(m);
		if(index >= 0) {
			float eX = m.getXOpponent(index);
			float eY = m.getYOpponent(index);

			// Throw bombs if the enemy gets close enough
			if(sq_dist(eX, eY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
				m.throwBomb(i, eX, eY);

			// Stay between the enemy and my flag
			if(sq_dist(eX, eY, Model.XFLAG, Model.YFLAG) > Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
				m.setDestination(i, 0.5f * (Model.XFLAG + eX), 0.5f * (Model.YFLAG + eY));
			else
				m.setDestination(i, 0.75f * (Model.XFLAG + eX), 0.5f * (Model.YFLAG + eY));

		}
		else {
			// Guard the flag
			m.setDestination(i, Model.XFLAG + Model.MAX_THROW_RADIUS, Model.YFLAG);
		}

		// If I don't have enough energy to throw a bomb, rest
		if(m.getEnergySelf(i) < Model.BOMB_COST)
			m.setDestination(i, m.getX(i), m.getY(i));

		// Try not to die
		evade(m, i);
	}

	void beFlagAttacker(Model m, int i) {
		// Head for the opponent's flag

		m.setDestination(i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);

		float X = m.getX(i);
		float Y = m.getY(i);

		if (isSafe(m, i, 20)){
			if (m.getEnergySelf(i) < 1 && opponentsAlive(m) > 0 && nextToRevive(m, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT)/Model.BROKEN_CRAWL_RATE > sq_dist(X, Y, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT)*.5)
				m.setDestination(i, X, Y); // Rest
			else{
					pickPath(m, i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);
				// Avoid opponents
				nearestOpponent(m, X, Y);

				if(index >= 0) {
					float eX = m.getXOpponent(index);
					float eY = m.getYOpponent(index);
					if(sq_dist(eX, eY, X, Y) <= (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS*1.5) * (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS*1.5))
							pickPath(m, i, X + 15.0f * (X - eX), Y + 15.0f * (Y - eY));
				}

				if (opponentsInRange(m, i, 0) == 0 && !revivalImminent(m, 100)){
						m.setDestination(i, Model.XFLAG_OPPONENT - 100, Model.YFLAG_OPPONENT);
				}
				if (revivalImminent(m, 100)){
					m.setDestination(i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS +1, Model.YFLAG_OPPONENT);
					float d = nextToRevive(m, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
					if (index >= 0){
						if (d <= 100){
							float eX = m.getXOpponent(index);
							float eY = m.getYOpponent(index);
							if(sq_dist(eX, eY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
								m.throwBomb(i, eX, eY);
						}
						else
							if(sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
								m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
					}
				}
				else{
					if (counter%50==0)
						pickPath(m, i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);
					else
						m.setDestination(i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);

					// Shoot at the flag if I can hit it
					if(sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
						m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
				}
			}
		}
		else{ //it is not safe!
			float distance = nearestOpponent(m, X, Y);

			if(index >= 0) {
				float eX = m.getXOpponent(index);
				float eY = m.getYOpponent(index);

				// If the opponent is close enough to shoot at me...
				if((sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS) && (sq_dist(eX, eY, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) < Model.BLAST_RADIUS*Model.BLAST_RADIUS)){
						m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
				}
				else{
					if (distance > Model.BLAST_RADIUS || m.getEnergyOpponent(index)-Model.BOMB_DAMAGE_TO_SPRITE <=0 || m.getEnergySelf(i)-(Model.BOMB_COST+Model.BOMB_DAMAGE_TO_SPRITE) > 0)
						if (nearestTarget(m, eX, eY) > (Model.BLAST_RADIUS*Model.BLAST_RADIUS)+Model.BLAST_RADIUS+1f)
							m.throwBomb(i, eX, eY);
				}
			}
		}



		// Try not to die

		nearestOpponent(m, X, Y);
		if(index >= 0) {
			float eX = m.getXOpponent(index);
			float eY = m.getYOpponent(index);
			if (!(m.getEnergySelf(i)>=.2 && m.getEnergySelf(i)-(Model.BOMB_DAMAGE_TO_SPRITE+Model.BOMB_COST) > m.getEnergyOpponent(index)))
				evade(m, i);

		}
	}

	void beAttacker(Model m, int i) {
		float X = m.getX(i);
		float Y = m.getY(i);

		// Find the opponent nearest to me
		nearestOpponentToFlag(m);



		if(index >= 0) {
			float eX = m.getXOpponent(index);
			float eY = m.getYOpponent(index);

			if(m.getEnergySelf(i) >= m.getEnergyOpponent(index)) {

				// Get close enough to throw a bomb at the enemy
				float dx = X - eX;
				float dy = Y - eY;
				float t = 1.0f / Math.max(Model.EPSILON, (float)Math.sqrt(dx * dx + dy * dy));
				dx *= t;
				dy *= t;
				if (counter%5==0)
					pickPath(m, i, eX + dx * (Model.MAX_THROW_RADIUS - Model.EPSILON), eY + dy * (Model.MAX_THROW_RADIUS - Model.EPSILON));
				else
					m.setDestination(i, eX + dx * (Model.MAX_THROW_RADIUS - Model.EPSILON), eY + dy * (Model.MAX_THROW_RADIUS - Model.EPSILON));

				// Throw bombs
				if(sq_dist(eX, eY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
					m.throwBomb(i, eX, eY);
			}
			else {

				// If the opponent is close enough to shoot at me...
				if(sq_dist(eX, eY, X, Y) <= (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS) * (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS) + 5.0f) {
						pickPath(m, i, X + 10.0f * (X - eX), Y + 10.0f * (Y - eY));
				}
				else {
						m.setDestination(i, X, Y); // Rest
				}
			}
		}
		else {
			// Head for the opponent's flag
			m.setDestination(i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);

			// Shoot at the flag if I can hit it
			if(sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS) {
				m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
			}
		}



		// Try not to die
		evade(m, i);
	}
}
