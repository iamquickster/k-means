package kmoyennes;

import java.util.List;

public class KMoyennesBenchmark {
	public static void main(String[] args) {
		
		KMoyennesBenchmark.runBenchmark();

	}

	public static Point[] runBenchmark() {
		System.out.println("---------Mesure de performance--------");
		System.out.println("Temps d'execution par nombre de threads:");
		int nbThreadMax = Runtime.getRuntime().availableProcessors();
		List<Point> dataSet = Point.generateRandomSet(1000);
		int k = 100;
		int grain = 100;
		Point[] threadResults = new Point[nbThreadMax];
		for(int i = 0; i < nbThreadMax ; i++) {
			threadResults[i] = new Point((double) (i+1), (double) KmoyenneTest.runExecTest(dataSet, k, i + 1, grain));
		}
		
		
		for(Point result: threadResults) {
			System.out.println(result);
		}
				
		return threadResults;
		
	}
}
