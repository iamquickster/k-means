package kmeans.app;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import kmeans.KMeans;
import kmeans.Point;

public class KMeansView{

	private static long nextId =1L;
	private List<JFrame> charts  = new ArrayList<JFrame>();
	
	private static long nextId() {
		return nextId++;
		
	}
	
	

	public void display(KMeans model) {
		int dataSize = model.getData().length;
		XYSeries allPoints = new XYSeries("Points");
		for (int i = 0 ; i < dataSize; i++) {
			Point point = model.getData()[i];
			allPoints.add(point.x, point.y);
		}
		XYSeries series = new XYSeries("Means");
		
		for (Point point : model.getAnswer()) {
			series.add(point.x, point.y);
		}
		
		XYSeriesCollection dataSet = new XYSeriesCollection();
		dataSet.addSeries(series);
		dataSet.addSeries(allPoints);
		
		String plotId = "" + nextId();
		JFreeChart chart = ChartFactory.createScatterPlot(plotId , "X", "Y", dataSet );
		
		JFrame window = new ChartFrame("Result for sample #" + plotId, chart );
		this.charts.add(window);
		window.pack();
		window.setVisible(true);
	}



	public void display(Point[] dataSet, String title, String XAxis, String YAxis) {
		int dataSize = dataSet.length;
		XYSeries allPoints = new XYSeries(title);
		for (int i = 0 ; i < dataSize; i++) {
			Point point = dataSet[i];
			allPoints.add(point.x, point.y);
		}
		
		XYSeriesCollection dataSeries = new XYSeriesCollection();
		dataSeries.addSeries(allPoints);
		
		JFreeChart chart = ChartFactory.createScatterPlot(title , XAxis, YAxis, dataSeries );
		
		JFrame window = new ChartFrame(title, chart );
		this.charts.add(window);
		window.pack();
		window.setVisible(true);
	}

}
