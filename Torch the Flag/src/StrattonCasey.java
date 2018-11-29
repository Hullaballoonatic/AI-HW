import java.util.ArrayList;

import static java.lang.Math.*;

@SuppressWarnings("Duplicates")
public class StrattonCasey implements IAgent {
    private static final double[] weights = new double[] { 0.1851, -0.3669, -0.3320, -0.2385, 0.1923, -0.1420, -0.0974, 0.0309, -0.0790, -0.0136, 0.0280, -0.2137, -0.1103, 0.3598, -0.8158, 0.1982, 0.2427, -0.1271, -0.0433, 0.1417, 0.2261, -0.1579, -0.0442, -0.0135, 0.6124, -0.3986, 0.4048, 0.1834, -0.0849, -0.3570, 0.2273, -0.1690, 0.1086, 0.0785, 0.0707, -0.4295, 0.0018, -0.3278, -0.4213, 0.0880, 0.1823, 1.3846, -0.2074, -0.2162, 0.0158, 0.2067, 0.0320, -0.1603, 0.3956, 0.0514, 0.0135, 0.1114, -0.0570, 0.1742, -0.3128, -0.1396, -0.0535, -0.1665, -0.5220, -0.1659, -0.0931, 0.0384, -0.4962, -0.1406, -0.4557, -0.0914, 0.1389, -0.4339, -0.3591, -0.0389, 0.0157, 0.2081, -0.2220, -0.3499, 0.2286, 0.0971, 0.0579, -0.3773, -0.0971, -0.0726, 0.0060, -0.0669, 0.7134, 0.0006, 0.0199, 0.0270, 0.2964, -0.2020, -0.3984, -0.0537, -0.0946, -0.0139, 0.4693, -0.1145, 0.1569, 0.0095, 0.2899, 0.3839, 0.4386, 0.1615, 0.8594, 0.6103, -0.0452, 0.0321, -0.0637, 0.0763, -0.0402, -0.4517, -0.0031, 0.2905, 0.4175, 0.0188, -0.5334, 0.0609, -0.1821, 0.1991, -0.2285, 0.0917, -0.2306, 0.2460, -0.0578, -0.0027, -0.1804, 0.1293, -0.0677, 0.1673, -0.0030, -0.0733, 0.0174, 0.0468, -0.1981, -0.0965, -0.1113, -0.1892, -0.0189, -0.0213, 0.0391, 0.0974, 0.6653, 0.6146, -0.2560, 0.1614, -0.1702, -0.0721, 0.3302, -0.1146, 0.0197, 0.1740, -0.6088, 0.2282, 0.6911, -0.0413, 0.0042, 0.6200, -0.0582, 0.1362, 0.0176, 0.2014, -0.2563, 0.0122, -0.0280, 0.1214, 0.0874, -0.1647, 0.3198, -0.3594, 0.2504, 0.1452, -0.1468, 0.3383, 0.1285, 0.2442, 0.0198, -0.2880, -0.0857, 0.0497, 0.0913, -0.0141, 0.0202, -0.3982, -0.3231, -0.1520, -0.3685, 0.1555, -0.3981, -0.2562, -2.0374, 0.2928, 0.0150, -0.2639, 1.1217, 0.0543, -0.4422, 0.3057, 0.1947, 0.4194, 0.1114, 0.4342, -0.5459, 0.0840, -0.0359, -0.0173, -0.0603, -0.0106, 0.0743, 0.0420, -0.0301, 0.0299, 0.2353, 0.0748, 0.1375, 0.2300, 0.0176, 0.1279, -0.3574, -0.0191, -0.1088, -0.1088, 0.1486, 0.0676, 0.2031, -0.0690, 0.0416, -0.0565, 0.0958, 0.0963, -0.1180, -0.0555, 0.0047, -0.0787, 0.3133, -0.0589, -0.2677, 0.3446, 0.1071, -0.3381, 0.2786, 0.3303, -0.1424, 0.1977, -0.1183, -0.0767, -0.2289, 0.1284, 0.2564, 0.0734, -0.0492, 0.2617, -0.2455, 0.0043, -0.0417, 0.4613, 0.1292, -0.1149, 0.2427, 0.1485, 0.0681, -0.0836, 0.0418, 0.1655, 0.0393, -0.3371, -0.5194, 0.4381, 0.2753, 0.2637, -0.1167, 0.1966, -0.1286, 0.4896, -0.3889, -0.0706, 0.0310, 0.0282, -0.1523, -0.3337, 0.2147, 0.0267, -0.2393, 0.2470, 0.1128, 0.0225, 0.0100, 1.1944, 0.0116, 0.0929, -0.0411, 0.0945, 0.0787, 0.0163, -0.1985 };

    private static int targetIndex;
    private static final float oo = Float.MAX_VALUE;

    private static double[] in = new double[20];
    private static ArrayList<Layer> layers = new ArrayList<Layer>() {{
        add(new LayerLinear(20, 8));
        add(new LayerTanh(8));
        add(new LayerLinear(8, 10));
        add(new LayerTanh(10));
        add(new LayerLinear(10, 3));
        add(new LayerTanh(3));

        int start = 0;
        for (Layer layer : this) start += layer.setWeights(start);
    }};

    public static float sq_dist(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private float nearestBombTarget(Model m, float x, float y) {
        targetIndex = -1;
        float dd = oo;
        for (int i = 0; i < m.getBombCount(); i++) {
            float d = sq_dist(x, y, m.getBombTargetX(i), m.getBombTargetY(i));
            if (d < dd) {
                dd = d;
                targetIndex = i;
            }
        }
        return dd;
    }

    private float nearestOpponent(Model m, float x, float y) {
        targetIndex = -1;
        float dd = Float.MAX_VALUE;
        for (int i = 0; i < m.getSpriteCountOpponent(); i++) {
            if (m.getEnergyOpponent(i) >= 0) {
                float d = sq_dist(x, y, m.getXOpponent(i), m.getYOpponent(i));
                if (d < dd) {
                    dd = d;
                    targetIndex = i;
                }
            }
        }
        return dd;
    }

    private void avoidBombs(Model m, int i) {
        if (nearestBombTarget(m, m.getX(i), m.getY(i)) <= 2.0f * Model.BLAST_RADIUS * Model.BLAST_RADIUS) {
            float dx = m.getX(i) - m.getBombTargetX(targetIndex);
            float dy = m.getY(i) - m.getBombTargetY(targetIndex);
            if (dx == 0 && dy == 0)
                dx = 1.0f;
            m.setDestination(i, m.getX(i) + dx * 10.0f, m.getY(i) + dy * 10.0f);
        }
    }

    private void beDefender(Model m, int i) {
        // Find the opponent nearest to my flag
        nearestOpponent(m, Model.XFLAG, Model.YFLAG);
        if (targetIndex >= 0) {
            float enemyX = m.getXOpponent(targetIndex);
            float enemyY = m.getYOpponent(targetIndex);

            // Stay between the enemy and my flag
            m.setDestination(i, 0.5f * (Model.XFLAG + enemyX), 0.5f * (Model.YFLAG + enemyY));

            // Throw bombs if the enemy gets close enough
            if (sq_dist(enemyX, enemyY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
                m.throwBomb(i, enemyX, enemyY);
        } else {
            // Guard the flag
            m.setDestination(i, Model.XFLAG + Model.MAX_THROW_RADIUS, Model.YFLAG);
        }

        // If I don't have enough energy to throw a bomb, rest
        if (m.getEnergySelf(i) < Model.BOMB_COST)
            m.setDestination(i, m.getX(i), m.getY(i));

        // Try not to die
        avoidBombs(m, i);
    }

    private void beFlagAttacker(Model m, int i) {
        // Head for the opponent's flag
        m.setDestination(i, Model.XFLAG_OPPONENT - Model.MAX_THROW_RADIUS + 1, Model.YFLAG_OPPONENT);

        // Shoot at the flag if I can hit it
        if (sq_dist(m.getX(i), m.getY(i), Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS) {
            m.throwBomb(i, Model.XFLAG_OPPONENT, Model.YFLAG_OPPONENT);
        }

        // Try not to die
        avoidBombs(m, i);
    }

    private void beAggressor(Model m, int i) {
        float myX = m.getX(i);
        float myY = m.getY(i);

        // Find the opponent nearest to me
        nearestOpponent(m, myX, myY);
        if (targetIndex >= 0) {
            float enemyX = m.getXOpponent(targetIndex);
            float enemyY = m.getYOpponent(targetIndex);

            if (m.getEnergySelf(i) >= m.getEnergyOpponent(targetIndex)) {

                // Get close enough to throw a bomb at the enemy
                float dx = myX - enemyX;
                float dy = myY - enemyY;
                float t = 1.0f / max(Model.EPSILON, (float) sqrt(dx * dx + dy * dy));
                dx *= t;
                dy *= t;
                m.setDestination(i, enemyX + dx * (Model.MAX_THROW_RADIUS - Model.EPSILON), enemyY + dy * (Model.MAX_THROW_RADIUS - Model.EPSILON));

                // Throw bombs
                if (sq_dist(enemyX, enemyY, m.getX(i), m.getY(i)) <= Model.MAX_THROW_RADIUS * Model.MAX_THROW_RADIUS)
                    m.throwBomb(i, enemyX, enemyY);
            } else {

                // If the opponent is close enough to shoot at me...
                if (sq_dist(enemyX, enemyY, myX, myY) <= (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS) * (Model.MAX_THROW_RADIUS + Model.BLAST_RADIUS)) {
                    m.setDestination(i, myX + 10.0f * (myX - enemyX), myY + 10.0f * (myY - enemyY)); // Flee
                } else {
                    m.setDestination(i, myX, myY); // Rest
                }
            }
        }

        // Try not to die
        avoidBombs(m, i);
    }

    @Override
    public void reset() {
    }

    @Override
    public void update(Model m) {
        // Compute some features
        in[0] = m.getX(0) / 600.0 - 0.5;
        in[1] = m.getY(0) / 600.0 - 0.5;
        in[2] = m.getX(1) / 600.0 - 0.5;
        in[3] = m.getY(1) / 600.0 - 0.5;
        in[4] = m.getX(2) / 600.0 - 0.5;
        in[5] = m.getY(2) / 600.0 - 0.5;
        in[6] = nearestOpponent(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[7] = nearestOpponent(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[8] = nearestOpponent(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[9] = nearestBombTarget(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[10] = nearestBombTarget(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[11] = nearestBombTarget(m, m.getX(0), m.getY(0)) / 600.0 - 0.5;
        in[12] = m.getEnergySelf(0);
        in[13] = m.getEnergySelf(1);
        in[14] = m.getEnergySelf(2);
        in[15] = m.getEnergyOpponent(0);
        in[16] = m.getEnergyOpponent(1);
        in[17] = m.getEnergyOpponent(2);
        in[18] = m.getFlagEnergySelf();
        in[19] = m.getFlagEnergyOpponent();

        double[] out = forwardProp(in);

        // Do it
        for (int i = 0; i < 3; i++) {
            if (out[i] < -0.333)
                beDefender(m, i);
            else if (out[i] > 0.333)
                beAggressor(m, i);
            else
                beFlagAttacker(m, i);
        }
    }

    private double[] forwardProp(double[] in)
    {
        for (Layer layer : layers) {
            in = layer.forwardProp(in);
        }
        return in;
    }

    private static abstract class Layer {
        double[] activation;
        double[] error;


        /// General-purpose constructor
        Layer(int outputs) {
            activation = new double[outputs];
            error = new double[outputs];
        }


        int outputCount() {
            return activation.length;
        }

        abstract double[] forwardProp(double[] in);

        abstract int setWeights(int start);
    }


    private static class LayerLinear extends Layer {
        double[] bias;
        double[][] W;


        /// General-purpose constructor
        LayerLinear(int inputs, int outputs) {
            super(outputs);
            bias = new double[outputs];
            W = new double[inputs][outputs];
        }


        int setWeights(int start) {
            int oldStart = start;
            for (int i = 0; i < bias.length; i++)
                bias[i] = StrattonCasey.weights[start++];
            for (double[] r : W)
                for (int c = 0; c < r.length; c++)
                    r[c] = StrattonCasey.weights[start++];
            return start - oldStart;
        }


        double[] forwardProp(double[] in) {
            if (in.length != W.length)
                throw new IllegalArgumentException("size mismatch. " + in.length + " != " + W.length);

            System.arraycopy(bias, 0, activation, 0, activation.length);

            for (int r = 0; r < W.length; r++) {
                double[] row = W[r];
                for (int c = 0; c < W[r].length; c++)
                    activation[c] += in[r] * row[c];
            }

            return activation;
        }
    }


    private static class LayerTanh extends Layer {
        LayerTanh(int nodes) {
            super(nodes);
        }


        int setWeights(int start) {
            return 0;
        }


        double[] forwardProp(double[] in) {
            if (in.length != outputCount())
                throw new IllegalArgumentException("size mismatch. " + in.length + " != " + outputCount());
            for (int i = 0; i < activation.length; i++)
                activation[i] = tanh(in[i]);

            return activation;
        }
    }
}
