package kmeans;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Point {

	public double x, y;
	

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * ParsePoints
	 */
	public static List<Point> parsePoints(BufferedReader in) throws IOException {
		List<Point> result = new ArrayList<Point>();
		
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			result.add(parse(line));
		}
		
		return result;
	}
	
	/*
	 * Parse
	 */
	private static Point parse(String p) {
		int comma = p.indexOf(',');
		double x = Double.parseDouble(p.substring(1, comma));
		double y = Double.parseDouble(p.substring(comma + 1, p.length() - 1));
		Point result = new Point(x, y);
		
		return result ;		
	}

	public double distance(Point p) {
		return Math.sqrt(Math.pow(x-p.x, 2) + Math.pow(y-p.y, 2));
	}	

	public static Point center(List<Point> points) {
		double xSum = 0;
		double ySum = 0;
		for(Point p : points) {
			xSum += p.x;
			ySum += p.y;
		}
		return new Point(xSum/points.size(), ySum/points.size());
	}

	public boolean equalsWithConfidence(Point point, double epsilon) {
		if(point == null) {
			return false;
		}
		
		return Math.abs(point.x - x) < epsilon && Math.abs(point.y - y) < epsilon;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public static List<Point> generateRandomSet(int k) {
		List<Point> result = new ArrayList<Point>();
		Random r = new Random();
		
		for(int i = 0; i < k ;i++) {
			result.add(new Point(r.nextDouble()*100, r.nextDouble()*100));
		}
		return result;
	}
	
}
