package kmoyennes.app;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import kmoyennes.Kmoyennes;
import kmoyennes.Point;

public class KMoyennesView{

	private static long nextId =0L;
	private List<JFrame> charts  = new ArrayList<JFrame>();
	
	private static long nextId() {
		return nextId++;
		
	}
	
	

	public void display(Kmoyennes model) {
		int dataSize = model.getData().length;
		XYSeries allPoints = new XYSeries("Points");
		for (int i = 0 ; i < dataSize; i++) {
			Point point = model.getData()[i];
			allPoints.add(point.x, point.y);
		}
		XYSeries series = new XYSeries("Moyennes");
		
		for (Point point : model.getAnswer()) {
			series.add(point.x, point.y);
		}
		
		XYSeriesCollection dataSet = new XYSeriesCollection();
		dataSet.addSeries(series);
		dataSet.addSeries(allPoints);
		
		String plotId = "" + nextId();
		JFreeChart chart = ChartFactory.createScatterPlot(plotId , "X", "Y", dataSet );
		
		JFrame window = new ChartFrame(plotId, chart );
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
