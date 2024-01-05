package space.itoncek;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Mover mover = new Mover(new File("X:\\Astrocrew Říjen\\midrender\\e"));
        move(0, 250, mover);
        move(251, 301, mover);
        move(302, 452, mover);
        move(453, 503, mover);
        move(504, 604, mover);
        move(605, 655, mover);
        move(656, 864, mover);
        move(865, 925, mover);
        move(926, 1126, mover);
        move(1127, 1177, mover);
        move(1178, 1350, mover);
        move(1351, 1401, mover);
        move(1402, 1602, mover);
    }

    public static void move(int start, int stop, Mover mover) {
        moveOffset(start, stop - start, mover);
    }

    public static void moveOffset(int start, int lenght, Mover mover) {
        int[] tasks = new int[lenght + 1];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = i + start;
        }
        mover.moveFiles(tasks);
    }
}