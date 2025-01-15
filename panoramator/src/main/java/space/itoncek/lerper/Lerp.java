package space.itoncek.lerper;

public class Lerp {

	public static double EaseIn(double t) {
		return Math.pow(t, 2);
	}

	public static double Flip(double x) {
		return 1 - x;
	}

	public static double EaseOut(double t) {
		return Flip(Math.pow(Flip(t), 2));
	}

	public static double EaseInOut(double t) {
		// ((1 - t) * (t * t)) + (t * 1 - ((1-t)*(1-t)))
		return lerp(EaseIn(t), EaseOut(t), t);
	}

	// Precise method, which guarantees v = b when t = 1. This method is monotonic only when a * b < 0.
	// Lerping between same values might not produce the same value
	public static double lerp(double a, double b, double t) {
		return (1d - t) * a + t * b;
	}

}
