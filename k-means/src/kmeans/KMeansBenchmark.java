package kmeans;

import java.util.List;

public class KMeansBenchmark {
	public static void main(String[] args) {
		
		KMeansBenchmark.runBenchmark();

	}

	public static Point[] runBenchmark() {
		System.out.println("---------Performance Test--------");
		System.out.println("Execution time by number of threads:");
		int nbThreadMax = Runtime.getRuntime().availableProcessors();
		List<Point> dataSet = Point.generateRandomSet(1000);
		int k = 100;
		int grain = 100;
		Point[] threadResults = new Point[nbThreadMax];
		for(int i = 0; i < nbThreadMax ; i++) {
			threadResults[i] = new Point((double) (i+1), (double) KMeansTest.runExecTest(dataSet, k, i + 1, grain));
		}
		
		
		for(Point result: threadResults) {
			System.out.println(result);
		}
				
		return threadResults;
		
	}
}
