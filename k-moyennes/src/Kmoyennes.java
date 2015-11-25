import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Auteur : Maxime Lafond 
 *          Pascal Feo
 */
public class Kmoyennes {
	
	/*
	 * Variables globales
	 */
	private List<Point> data;

	
	/* 
	 * Constructeur 
	 */
	public Kmoyennes(List<Point> data) {
		this.data = data;
	}
	
	
	/*
	 * calculerKNombres
	 */
	private static List<Point> calculerKCoord(List<Point> pointsData, int k) {
		List<Point> pointsK = new ArrayList<Point>();
		int result;
		
		for(int i = 0; i<k; i++) {
			Random r = new Random();
			result = r.nextInt(pointsData.size()-i);
			pointsK.add(pointsData.get(result));
			pointsData.remove(result);
		}
		
		return pointsK;
	}
	
	
	/*
	 * Main  
	 */
	public static void main(String[] args) {
		int k =  Integer.parseInt(args[1]);
		
		try {		
			BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
			List<Point> pointsData = Point.parsePoints(in);
			List<Point> pointsK = calculerKCoord(pointsData, k);
			Kmoyennes problem = new Kmoyennes(pointsData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
