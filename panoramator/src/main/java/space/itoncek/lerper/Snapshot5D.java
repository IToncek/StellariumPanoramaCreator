package space.itoncek.lerper;

import java.time.LocalDate;
import java.time.LocalTime;

public record Snapshot5D(double azi, double alt, double fov, LocalDate day, LocalTime hour) {

    @Override
    public String toString() {
        return "Snapshot4D[" +
                "azi=" + azi + ", " +
                "alt=" + alt + ", " +
                "fov=" + fov + ", " +
                "time=" + day + ']';
    }

}
