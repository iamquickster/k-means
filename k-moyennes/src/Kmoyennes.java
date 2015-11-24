import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/*
 * 
 * Auteur : Maxime Lafond 
 *          Pascal Feo
 * 
 */
public class Kmoyennes {

	
	private List<Point> data;

	public Kmoyennes(List<Point> data) {
		this.data = data;
	}

	public static void main(String[] args) {
		try {		
			BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
			List<Point> points = Point.parsePoints(in);
			Kmoyennes problem = new Kmoyennes(points);
			System.out.println(problem.solve());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Point> solve() {
		return data;
		
	}
}
