import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Point {
	int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static List<Point> parsePoints(BufferedReader in) throws IOException {
		List<Point> result = new ArrayList<Point>();
		result.add(parse(in.readLine()));
		return result;
	}

	private static Point parse(String p) {
		int x = p.charAt(1);
		int y = p.charAt(3);
		Point result = new Point(x, y);
		return result ;		
	}
}
