import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Point {
	
	/*
	 * Variables globales
	 */
	int x, y;
	
	/*
	 * Constructeur
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * ParsePoints
	 */
	public static List<Point> parsePoints(BufferedReader in) throws IOException {
		List<Point> result = new ArrayList<Point>();
		
		for (String ligne = in.readLine(); ligne != null; ligne = in.readLine()) {
			result.add(parse(ligne));
		}
		
		return result;
	}
	
	/*
	 * Parse
	 */
	private static Point parse(String p) {
		int x = p.charAt(1) - '0';
		int y = p.charAt(3) - '0';
		Point result = new Point(x, y);
		
		return result ;		
	}
	
}
