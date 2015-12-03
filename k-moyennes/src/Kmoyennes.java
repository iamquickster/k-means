import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	private Point[] data;
	private Point[] kMoyennes;

	public Kmoyennes(Point[] data) {
		this.data = data;
	}


	public Kmoyennes(List<Point> data) {
		this.data = new Point[data.size()];
		data.toArray(this.data);
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

	}


	public Point[] solve(int k, int nbThreads) throws NoDataException {
		if(data == null) {
			throw new NoDataException();
		}
		if(k > data.length) {
			throw new NoDataException();
		}
		if( k < 1 || nbThreads < 0) {
			throw new IllegalArgumentException();
		}
		kMoyennes = new Point[k];
		if(nbThreads == 1) {
			return kMoyennesSeq(data, k);
			
		} else {
			return kMoyennes(data, k, nbThreads);
		}
		
		
	}


	private Point[] kMoyennes(Point[] data, int k, int nbThreads) {
		// TODO Auto-generated method stub
		return null;
	}


	private Point[] kMoyennesSeq(Point[] data, int k) {
		Point[] tmpMoyennes = new Point[k];
		List<Point>[] clusters = new ArrayList[k];
		int slice = data.length / k;
		for (int i = 0 ; i < k ; i++){
			tmpMoyennes[i] = data[i*slice];
			clusters[i] = new ArrayList<Point>(slice);
		}
		
		boolean isNewMoyenne = true;
		int iterations = 0;
		while (isNewMoyenne && iterations < 1000){
			System.out.println("Iteration :" + iterations);
			System.out.println("Moyennes :");
			for(Point moy : tmpMoyennes) {
				System.out.print(moy + "-");
			}
			System.out.println();
			for(Point p: data) {
				System.out.println("\tPoint :" + p);
				Double minDistance = Double.POSITIVE_INFINITY;
				int closest = -1;
				for(int i = 0; i < k; i++) {
					double tmpDistance = p.distance(tmpMoyennes[i]);
					System.out.println("\t\tDistance" + i + ":" + tmpDistance);
					if(tmpDistance < minDistance) {
						minDistance = tmpDistance;
						closest = i;
					}
				}
				System.out.println("\tCluster :" + closest);
				clusters[closest].add(p);
			}
			isNewMoyenne = false;
			for(int i = 0; i < k ; i++) {
				Point newMoyenne = Point.center(clusters[i]);
				System.out.println("\tNew: " + newMoyenne + " - Old: " + tmpMoyennes[i]);
				if(!isNewMoyenne) {
					isNewMoyenne |= !newMoyenne.equalsWithConfidence(tmpMoyennes[i], 0.0001);
					
				}
				tmpMoyennes[i] = newMoyenne;
				clusters[i].clear();
			}
			iterations++;
		}
		return tmpMoyennes;
		
	}


}
