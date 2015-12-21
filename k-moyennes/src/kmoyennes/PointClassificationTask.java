package kmoyennes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PointClassificationTask implements Callable<List<Point>[]> {

	private Point[] data;
	private Point[] references;
	private int taskSize;
	private int startIndex;
	private int endIndex;
	private ExecutorService pool;

	public PointClassificationTask(Point[] data, int startIndex, int endIndex, Point[] tmpMoyennes, int taskSize, ExecutorService pool) {
		this.data = data;
		this.references = tmpMoyennes;
		this.taskSize = taskSize;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.pool = pool;

	}

	@Override
	public List<Point>[] call() throws Exception {
//		System.out.println("Classification task:" + startIndex + " - " + endIndex);
		List<Point>[] clusters = new ArrayList[references.length];
		int clusterSize = data.length / clusters.length;
		for (int i = 0; i < clusters.length; i++) {
			clusters[i] = new ArrayList<Point>(clusterSize);
		}
		if (endIndex - startIndex - 1 <= taskSize) {
			for (int j = startIndex ; j < endIndex; j++) {
				Point p = data[j];
				Double minDistance = Double.POSITIVE_INFINITY;
				int closest = -1;
				for (int i = 0; i < references.length; i++) {
					double tmpDistance = p.distance(references[i]);
					if (tmpDistance < minDistance) {
						minDistance = tmpDistance;
						closest = i;
					}
				}
				clusters[closest].add(p);
			}
			return clusters;
		} else {
			int nbTasks = (int) Math.ceil(data.length / taskSize);
			Future<List<Point>[]>[] result = new Future[nbTasks];
			for (int i = 0; i < nbTasks ; i++) {
				if(nbTasks -1 != i) {
					result[i] = pool.submit(new PointClassificationTask(data, i*taskSize, (i+1)*taskSize, references, taskSize, pool));					
				} else {
					result[i] = pool.submit(new PointClassificationTask(data , i*taskSize, data.length, references, taskSize, pool));
				}
			}
			
			//Reduce
			for(int i = 0 ; i < result.length; i++)  {
				for(int j = 0; j < clusters.length ; j++) {
					clusters[j].addAll(result[i].get()[j]);
				}
			}
			return clusters;
		}
	}

}
