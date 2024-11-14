package space.itoncek;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Mover {
	private final File folder;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private int lastMove = 0;

	public Mover(File file) {
		folder = file;
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.printf("\r%04d / %04d  (%d threads)", executor.getCompletedTaskCount(), executor.getTaskCount(), executor.getActiveCount());
				if (executor.getCompletedTaskCount() == executor.getTaskCount()) {
					System.exit(0);
				}
			}
		}, 100, 100);
	}

	public void moveFiles(int[] indexes) {
		lastMove++;
		int finalMove = lastMove;
		AtomicInteger fileindex = new AtomicInteger(0);
		Arrays.stream(indexes).forEach((i) -> {
			int finidx = fileindex.getAndIncrement();
			File file = new File(folder.toString() + "\\%04d.exr".formatted(i));
			File folder = new File(this.folder + "\\%d".formatted(finalMove));
			if (!folder.exists()) folder.mkdirs();
			Runnable r = () -> {
				if (file.exists()) {
					try {
						//Files.createFile(new File(this.folder + "\\%d\\%04d.txt".formatted(finalMove, finidx)).toPath());
						Files.move(file.toPath(), new File(this.folder + "\\%d\\%04d.exr".formatted(finalMove, finidx)).toPath());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			};
			executor.submit(r);
		});

	}
}
