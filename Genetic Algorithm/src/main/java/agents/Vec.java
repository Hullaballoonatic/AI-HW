package agents;// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

/// Provides several useful static methods for operating on arrays of doubles
public class Vec
{
	public static Json marshal(double[] vec) {
		Json list = Json.newList();
		for (double v : vec) list.add(v);
		return list;
	}

	public static double[] unmarshal(Json n) {
		double[] vec = new double[n.size()];
		for(int i = 0; i < n.size(); i++)
			vec[i] = n.getDouble(i);
		return vec;
	}

	public static String toString(double[] vec) {
		StringBuilder sb = new StringBuilder();
		if(vec.length > 0) {
			sb.append(Double.toString(vec[0]));
			for(int i = 1; i < vec.length; i++) {
				sb.append(",");
				sb.append(Double.toString(vec[i]));
			}
		}
		return sb.toString();
	}

	public static void setAll(double[] vec, double val) {
		for(int i = 0; i < vec.length; i++)
			vec[i] = val;
	}

	public static double squaredMagnitude(double[] vec) {
		double d = 0.0;
		for (double v : vec) d += v * v;
		return d;
	}

	public static void normalize(double[] vec) {
		double mag = squaredMagnitude(vec);
		if(mag <= 0.0) {
			setAll(vec, 0.0);
			vec[0] = 1.0;
		} else {
			double s = 1.0 / Math.sqrt(mag);
			for(int i = 0; i < vec.length; i++)
				vec[i] *= s;
		}
	}

	public static void copy(double[] dest, double[] src) {
		if(dest.length != src.length)
			throw new IllegalArgumentException("mismatching sizes");
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	public static double[] copy(double[] src) {
		double[] dest = new double[src.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}

	public static void add(double[] dest, double[] src) {
		if(dest.length != src.length)
			throw new IllegalArgumentException("mismatching sizes");
		for(int i = 0; i < dest.length; i++)
			dest[i] += src[i];
	}

	public static void addScaled(double[] dest, double[] src, double scalar) {
		if(dest.length != src.length)
			throw new IllegalArgumentException("mismatching sizes");
		for(int i = 0; i < dest.length; i++)
			dest[i] += scalar * src[i];
	}

	public static void scale(double[] dest, double scalar) {
		for(int i = 0; i < dest.length; i++)
			dest[i] *= scalar;
	}

	public static double dotProduct(double[] a, double[] b) {
		if(a.length != b.length)
			throw new IllegalArgumentException("mismatching sizes");
		double d = 0.0;
		for(int i = 0; i < a.length; i++)
			d += a[i] * b[i];
		return d;
	}

	public static double squaredDistance(double[] a, double[] b) {
		if(a.length != b.length)
			throw new IllegalArgumentException("mismatching sizes");
		double d = 0.0;
		for(int i = 0; i < a.length; i++) {
			double t = a[i] - b[i];
			d += t * t;
		}
		return d;
	}

	public static void clip(double[] vec, double min, double max) {
		if(max < min)
			throw new IllegalArgumentException("max must be >= min");
		for(int i = 0; i < vec.length; i++)
			vec[i] = Math.max(min, Math.min(max, vec[i]));
	}

	public static double[] concatenate(double[] a, double[] b) {
		double[] c = new double[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

}
