import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Point {
	
	/*
	 * Variables globales
	 */
	float x, y;
	
	/*
	 * Constructeur
	 */
	public Point(float x, float y) {
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
		int comma = p.indexOf(',');
		int x = Integer.parseInt(p.substring(1, comma));
		int y = Integer.parseInt(p.substring(comma + 1, p.length() - 1));
		Point result = new Point(x, y);
		
		return result ;		
	}

	public double distance(Point p) {
		return Math.sqrt(Math.pow(x-p.x, 2) + Math.pow(y-p.y, 2));
	}	

	public static Point center(List<Point> points) {
		float xSum = 0;
		float ySum = 0;
		for(Point p : points) {
			xSum += p.x;
			ySum += p.y;
		}
		return new Point(xSum/points.size(), ySum/points.size());
	}

	public boolean equalsWithConfidence(Point point, double epsilon) {
		
		return Math.abs(point.x - x) < epsilon && Math.abs(point.y - y) < epsilon;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
}
