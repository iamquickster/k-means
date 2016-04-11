package kmeans.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import kmeans.KMeans;
import kmeans.KMeansBenchmark;
import kmeans.KMeansTest;
import kmeans.Point;

public class KMeansController {
	public static void main(String[] args) {
		List<String> params = Arrays.asList(args);
		if (params.contains("test")) {
			KMeansView view = new KMeansView();
			List<KMeans> tests = KMeansTest.runTests();
			for (KMeans test : tests) {
				view.display(test);
			}

		} else if (params.contains("benchmark")) {

			KMeansView view = new KMeansView();
			Point[] results = KMeansBenchmark.runBenchmark();
			view.display(results, "Temps Execution", "Nb Threads", "Milisecondes");
			Point[] performanceResults = new Point[results.length];
			for (int i = 0; i < results.length; i++) {
				performanceResults[i] = new Point(results[i].x, results[0].y / results[i].y);
			}

			view.display(performanceResults, "Performance", "nbThreads", "Ratio");
		} else {
			if (!params.isEmpty()) {

				try {
					List<Point> points = Point.parsePoints(new BufferedReader(new FileReader(new File(params.get(0)))));
					KMeans problem = new KMeans(points);

					int k = 3;
					int nbThreads = Runtime.getRuntime().availableProcessors();
					if (params.size() > 1) {

						k = Integer.parseInt(params.get(1));

						if (params.size() > 2) {
							nbThreads = Integer.parseInt(params.get(2));
						}
					}

					problem.solve(k, nbThreads);

					KMeansView view = new KMeansView();

					view.display(problem);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
