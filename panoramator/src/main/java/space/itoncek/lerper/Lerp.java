package space.itoncek.lerper;

public class Lerp {

    public static double EaseIn(double t)
    {
        return Math.pow(t,2);
    }

    public static double Flip(double x)
    {
        return 1 - x;
    }

    public static double EaseOut(double t)
    {
        return Flip(Math.pow(Flip(t), 2));
    }

    public static double EaseInOut(double t)
    {
        return lerp(EaseIn(t), EaseOut(t), t);
    }

    // Precise method, which guarantees v = v1 when t = 1. This method is monotonic only when v0 * v1 < 0.
    // Lerping between same values might not produce the same value
    public static double lerp(double v0, double v1, double t) {
        return (1d - t) * v0 + t * v1;
    }
}
