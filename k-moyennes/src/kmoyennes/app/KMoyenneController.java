package kmoyennes.app;

import java.util.Arrays;
import java.util.List;

import kmoyennes.KMoyennesBenchmark;
import kmoyennes.KmoyenneTest;
import kmoyennes.Kmoyennes;
import kmoyennes.Point;

public class KMoyenneController {
	public static void main(String[] args) {
		List<String> params = Arrays.asList(args);
		if (params.contains("test")) {
			KMoyennesView view = new KMoyennesView();
			List<Kmoyennes> tests = KmoyenneTest.runTests();
			for(Kmoyennes test : tests) {
				view.display(test);
			}

		} else if(params.contains("benchmark")){
			
			KMoyennesView view = new KMoyennesView();
			Point[] results = KMoyennesBenchmark.runBenchmark();
			view.display(results, "Temps Execution", "Nb Threads", "Milisecondes");
			Point[] performanceResults = new Point[results.length];
			for(int i = 0 ; i < results.length ; i++) {
				performanceResults[i] = new Point(results[i].x, results[0].y / results[i].y);				
			}

			
			view.display(performanceResults, "Performance", "nbThreads", "Ratio");
		}
	}
}
