package kmoyennes;
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
 * Auteur : Maxime Lafond 
 *          Pascal Feo
 */
public class Kmoyennes {

	static final double CONFIDENCE = 0.0001;
	public int GRAIN_SIZE = 100;
	/*
	 * Variables globales
	 */
	private Point[] data;
	private Point[] answer;

	public Kmoyennes(Point[] data) {
		this.data = data;
	}

	public Kmoyennes(List<Point> data) {
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
			Kmoyennes problem = new Kmoyennes(pointsData);
			Point[] answer = problem.solve(k, nbThreads);
			for (Point moyenne : answer) {
				System.out.println(moyenne);
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
			return kMoyennesSeq(data, k);

		} else {
			return kMoyennes(data, k, Math.min(nbThreads, data.length));
		}

	}

	private Point[] kMoyennes(Point[] data, int k, int nbThreads) {
		ExecutorService pool = Executors.newFixedThreadPool(nbThreads);
		
		int cores = Runtime.getRuntime().availableProcessors();
		if(nbThreads > cores) {
			nbThreads = cores;
		}
		
		int tailleBlock = this.GRAIN_SIZE;/*data.length / nbThreads;*/
		int slice = data.length / k;

		// initialize
		Point[] tmpMoyennes = new Point[k];
		List<Point>[] clusters = new ArrayList[k];
		for (int i = 0; i < k; i++) {
			clusters[i] = new ArrayList<Point>(slice);
			clusters[i].add(data[i * slice]);
		}

		boolean isNewMoyenne = true;
		while (isNewMoyenne) {

			Future<Point>[] nouvellesMoyennes = new Future[k];

			// calculer moyennes
			for (int i = 0; i < clusters.length; i++) {
				nouvellesMoyennes[i] = pool
						.submit(new MoyenneTask(clusters[i], 0, clusters[i].size(), tailleBlock, pool));
			}

			isNewMoyenne = false;
			for (int i = 0; i < nouvellesMoyennes.length; i++) {
				try {
					if (!nouvellesMoyennes[i].get().equalsWithConfidence(tmpMoyennes[i], CONFIDENCE)) {
						isNewMoyenne = true;
					}
					tmpMoyennes[i] = nouvellesMoyennes[i].get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			if (!isNewMoyenne) {
				this.answer = tmpMoyennes;
				pool.shutdownNow();
				return tmpMoyennes;
			}

			for (int i = 0; i < clusters.length; i++) {
				clusters[i].clear();
			}

			// classifier les points
			try {
				clusters = pool
						.submit(new PointClassificationTask(data, 0, data.length, tmpMoyennes, tailleBlock, pool))
						.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}
		pool.shutdownNow();
		this.answer = tmpMoyennes;
		return tmpMoyennes;
	}

	private Point[] kMoyennesSeq(Point[] data, int k) {
		Point[] tmpMoyennes = new Point[k];
		List<Point>[] clusters = new ArrayList[k];
		int slice = data.length / k;
		for (int i = 0; i < k; i++) {
			tmpMoyennes[i] = data[i * slice];
			clusters[i] = new ArrayList<Point>(slice);
		}

		boolean isNewMoyenne = true;
		while (isNewMoyenne) {
			for (Point p : data) {
				Double minDistance = Double.POSITIVE_INFINITY;
				int closest = -1;
				for (int i = 0; i < k; i++) {
					double tmpDistance = p.distance(tmpMoyennes[i]);
					if (tmpDistance < minDistance) {
						minDistance = tmpDistance;
						closest = i;
					}
				}
				clusters[closest].add(p);
			}
			isNewMoyenne = false;
			Point[] nouvellesMoyennes = new Point[k];
			for (int i = 0; i < k; i++) {
				nouvellesMoyennes[i] = Point.center(clusters[i]);
				clusters[i].clear();
				if (!nouvellesMoyennes[i].equalsWithConfidence(tmpMoyennes[i], CONFIDENCE)) {
					isNewMoyenne = true;
				}
				tmpMoyennes[i] = nouvellesMoyennes[i];

			}
		}
		this.answer = tmpMoyennes;
		return tmpMoyennes;

	}

	public Point[] getData() {
		return data;
	}

	public Point[] getAnswer() {
		return this.answer;
	}

}
