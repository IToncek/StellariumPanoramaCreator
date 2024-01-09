package space.itoncek;

import java.util.List;

public record Rotation(int azi, int alt, String dir) {
    public static List<Rotation> generateCubemapRotations() {
        return List.of(new Rotation(0,0, "n"),
                new Rotation(90,0,"e"),
                new Rotation(180,0,"s"),
                new Rotation(270,0,"w"),
                new Rotation(0,-89,"d"),
                new Rotation(0,89,"u"));
    }
}
