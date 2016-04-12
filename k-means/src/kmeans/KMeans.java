package kmeans;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
 * Auteur : Pascal Feo
 * 			Maxime Lafond 
 *         
 */
public class KMeans {

	static final double CONFIDENCE = 0.0001;
	public int GRAIN_SIZE = 100;

	
	private Point[] data;
	private Point[] answer;

	public KMeans(Point[] data) {
		this.data = data;
	}

	public KMeans(List<Point> data) {
		this.data = new Point[data.size()];
		data.toArray(this.data);
	}

	/*
	 * Main
	 */
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		int nbThreads = Integer.parseInt(args[1]);

		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(args[3])));
			List<Point> pointsData = Point.parsePoints(in);
			KMeans problem = new KMeans(pointsData);
			Point[] answer = problem.solve(k, nbThreads);
			for (Point mean : answer) {
				System.out.println(mean);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Point[] solve(int k, int nbThreads) {
		if (data == null) {
			return new Point[0];
		}
		if (k > data.length) {
			return data;
		}
		if (k < 1 || nbThreads < 0) {
			return null;
		}
		if (nbThreads == 1) {
			return kMeansSeq(data, k);

		} else {
			return kMeans(data, k, Math.min(nbThreads, data.length));
		}

	}

	private Point[] kMeans(Point[] data, int k, int nbThreads) {
		ExecutorService pool = Executors.newFixedThreadPool(nbThreads);
		
		int cores = Runtime.getRuntime().availableProcessors();
		if(nbThreads > cores) {
			nbThreads = cores;
		}
		
		int blockSize = this.GRAIN_SIZE;/*data.length / nbThreads;*/
		int slice = data.length / k;

		// initialize
		Point[] tmpMeans = new Point[k];
		List<Point>[] clusters = new ArrayList[k];
		for (int i = 0; i < k; i++) {
			clusters[i] = new ArrayList<Point>(slice);
			clusters[i].add(data[i * slice]);
		}

		boolean isNewMean = true;
		while (isNewMean) {

			Future<Point>[] newMeans = new Future[k];

			// calculate means
			for (int i = 0; i < clusters.length; i++) {
				newMeans[i] = pool
						.submit(new MeanTask(clusters[i], 0, clusters[i].size(), blockSize, pool));
			}

			isNewMean = false;
			for (int i = 0; i < newMeans.length; i++) {
				try {
					if (!newMeans[i].get().equalsWithConfidence(tmpMeans[i], CONFIDENCE)) {
						isNewMean = true;
					}
					tmpMeans[i] = newMeans[i].get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			if (!isNewMean) {
				this.answer = tmpMeans;
				pool.shutdownNow();
				return tmpMeans;
			}

			for (int i = 0; i < clusters.length; i++) {
				clusters[i].clear();
			}

			// classify points
			try {
				clusters = pool
						.submit(new PointClassificationTask(data, 0, data.length, tmpMeans, blockSize, pool))
						.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}
		pool.shutdownNow();
		this.answer = tmpMeans;
		return tmpMeans;
	}

	private Point[] kMeansSeq(Point[] data, int k) {
		Point[] tmpMeans = new Point[k];
		List<Point>[] clusters = new ArrayList[k];
		int slice = data.length / k;
		for (int i = 0; i < k; i++) {
			tmpMeans[i] = data[i * slice];
			clusters[i] = new ArrayList<Point>(slice);
		}

		boolean isNewMean = true;
		while (isNewMean) {
			for (Point p : data) {
				Double minDistance = Double.POSITIVE_INFINITY;
				int closest = -1;
				for (int i = 0; i < k; i++) {
					double tmpDistance = p.distance(tmpMeans[i]);
					if (tmpDistance < minDistance) {
						minDistance = tmpDistance;
						closest = i;
					}
				}
				clusters[closest].add(p);
			}
			isNewMean = false;
			Point[] newMeans = new Point[k];
			for (int i = 0; i < k; i++) {
				newMeans[i] = Point.center(clusters[i]);
				clusters[i].clear();
				if (!newMeans[i].equalsWithConfidence(tmpMeans[i], CONFIDENCE)) {
					isNewMean = true;
				}
				tmpMeans[i] = newMeans[i];

			}
		}
		this.answer = tmpMeans;
		return tmpMeans;

	}

	public Point[] getData() {
		return data;
	}

	public Point[] getAnswer() {
		return this.answer;
	}

}
