package space.itoncek.lerper;

public record Snapshot5D(double azi, double alt, double fov, long day, double hour) {

    @Override
    public String toString() {
        return "Snapshot4D[" +
                "azi=" + azi + ", " +
                "alt=" + alt + ", " +
                "fov=" + fov + ", " +
                "time=" + day + ']';
    }

}
