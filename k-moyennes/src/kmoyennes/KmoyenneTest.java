package kmoyennes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class KmoyenneTest {
	public static void main(String[] args) {
		KmoyenneTest.runTests();
	}

	private static Kmoyennes kTest(int k, int nbThreads) {

		List<Point> testSet = Point.generateRandomSet(k);
		Kmoyennes problem = new Kmoyennes(testSet);
		Point[] answer = problem.solve(k, nbThreads);
		int nbValid = 0;
		for (Point mean : answer) {
			for (Point test : testSet) {
				if (mean.equalsWithConfidence(test, Kmoyennes.CONFIDENCE)) {
					nbValid++;
				}
			}
		}
		assert nbValid == testSet.size();
		return problem;
	}

	private static Kmoyennes gridTest(int nbThreads, int gridSize) {
		int nbPoints = (int) Math.pow(gridSize, 2);
		ArrayList<Point> grid = new ArrayList<Point>(nbPoints);
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				grid.add(new Point((double) i, (double) j));
			}
		}
		Kmoyennes problem = new Kmoyennes(grid);
		Point[] answer = problem.solve(4, nbThreads);

		ArrayList<Point> expectedResults = new ArrayList<Point>();
		expectedResults.add(new Point(gridSize / 4, gridSize / 4));
		expectedResults.add(new Point((gridSize / 4) * 3, (gridSize / 4) * 3));
		expectedResults.add(new Point(gridSize / 4, (gridSize / 4) * 3));
		expectedResults.add(new Point((gridSize / 4) * 3, gridSize / 4));
		int nbValidPoints = 0;
		for (Point point : answer) {
			for (Point expectedResult : expectedResults) {
				if (point.equalsWithConfidence(expectedResult, 0.05)) {
					nbValidPoints++;
				}
			}
		}

		assert nbValidPoints == 4;
		return problem;
	}

	public static List<Kmoyennes> runTests() {
		List<Kmoyennes> result = new ArrayList<Kmoyennes>();
		int k = 10;
		int nbThreads = 10;
		int maxGridSize = 40;

		System.out.println(
				"Grid Test: Test sur une grille de point de grandeur variable avec nombre de thread variable.");
		System.out.println("La reponse doit converge vers le milieu des 4 quadrants:");
		System.out
				.println("Exemple: Taille de la Grille: 40 Reponse: [(9.5,9.5), (9.5,29.5), (29.5,29.5), (29.5,9.5)]");
		for (int j = 1; j < nbThreads; j++) {
			System.out.println(MessageFormat.format("Grid Size: {0}, nbThreads: {1}", maxGridSize, j));
			Kmoyennes problem = gridTest(j, maxGridSize);
			Point[] answer = problem.solve(4, j);
			result.add(problem);

			String answerS = "";
			for (Point mean : answer) {
				answerS += mean.toString();
			}
			System.out.println(MessageFormat.format("Reponse: {0}", answerS));
		}

		System.out.println("K Test: Test sur differentes valeurs de k.");
		System.out.println("La reponse doit converge vers des moyennes identitique aux points initiales:");
		System.out.println(
				"Exemple: K: 4, points: [(25,25), (25,75), (75,25), (75,75)] Reponse: [(25,25), (25,75), (75,25), (75,75)]");
		for (int i = 1; i < k; i++) {
			System.out.print(MessageFormat.format("K : {0}, nbThreads: {1}:", i, 4));
			Kmoyennes problem = kTest(i, 4);
			System.out.print("Data : ");
			String dataS = "";
			for (Point point : problem.getData()) {
				dataS += point;
			}
			System.out.println(dataS);

			Point[] answer = problem.solve(i, 4);
			result.add(problem);
			String answerS = "";
			for (Point mean : answer) {
				answerS += mean.toString();
			}
			System.out.println(MessageFormat.format("Reponse: {0}", answerS));

		}
		return result;
	}

	public static long runExecTest(List<Point> dataSet, int k, int nbThreads, int grain) {
		
		Kmoyennes test = new Kmoyennes(dataSet);
		test.GRAIN_SIZE = grain;
		long startTime = System.currentTimeMillis();
		test.solve(k, nbThreads);
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
		
		
	}
}
