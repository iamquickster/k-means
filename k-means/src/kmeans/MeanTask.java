package kmeans;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class MeanTask implements Callable<Point> {

	private List<Point> points;
	private int taskSize;
	private ExecutorService pool;
	private int startIndex;
	private int endIndex;

	public MeanTask(List<Point> points, int startIndex, int endIndex, int taskSize, ExecutorService pool) {
		this.points = points;
		this.taskSize = taskSize;
		this.pool = pool;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public Point call() throws Exception {
//		System.out.println("Mean Task:" + startIndex + " - " + endIndex);

			int nbTasks = (int) Math.ceil(points.size() / (double) taskSize);
			Point[] result = new Point[nbTasks];
			for (int i = 0; i < nbTasks ; i++) {
				if(nbTasks - 1 != i) {
					result[i] = Point.center(points.subList(i*taskSize, (i+1)*taskSize));
				} else {
					result[i] = Point.center(points.subList(i*taskSize, points.size()));
				}
			}
			
			//Reduce
			List<Point> taskMeans = new ArrayList<Point>(nbTasks);
			for(int i = 0 ; i < result.length; i++)  {
				taskMeans.add(result[i]);
			}
			return Point.center(taskMeans);
		
	}


}
