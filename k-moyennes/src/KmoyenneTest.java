import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class KmoyenneTest {
	public static void main(String[] args) {
		int k =  Integer.parseInt(args[1]);
		
		try {		
			BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
			List<Point> pointsData = Point.parsePoints(in);
			Kmoyennes problem = new Kmoyennes(pointsData);
			Point[] answer = problem.solve(k, 1);
			System.out.println("Reponse:");
			for(Point moyenne : answer) {
				System.out.println(moyenne);
			}
		} catch (IOException | NoDataException e) {
			e.printStackTrace();
		}
	}
}
