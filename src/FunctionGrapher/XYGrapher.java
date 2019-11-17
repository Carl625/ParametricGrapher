package FunctionGrapher;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public abstract class XYGrapher /*extends GoodDrawGraph*/ {
	
	private JFrame frame;
	private Graph graph;
	private int[] pixelStart;
	private int graphHeight;
	private int graphWidth;
	
	abstract public Coordinate xyStart();
	
	abstract public double xRange();
	
	abstract public double yRange();
	
	abstract public Coordinate getPoint(int pointNum);
	
	public void drawGraph(int xPixelStart, int yPixelStart, int pixelsWide, int pixelsHigh) {
		
		pixelStart = new int[] {xPixelStart, yPixelStart};
		graphHeight = pixelsHigh;
		graphWidth = pixelsWide;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Grapher");
		
		graph = new Graph();
		graph.add(Box.createRigidArea(new Dimension(pixelsWide, pixelsHigh)));
		
		frame.setContentPane(graph);
		frame.repaint();
		frame.setVisible(true);
		frame.pack();
	}
	
	class Graph extends JPanel {
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Coordinate graphStart = xyStart();
			
			double xAxisOffset = pixelStart[0] + ((0.0 - graphStart.getX()) / xRange()) * graphWidth;
			double yAxisOffset = pixelStart[1] + (graphHeight - (((0.0 - graphStart.getY()) / yRange()) * graphHeight));
			
			g.setColor(Color.green);
			if (xAxisOffset > 0 && xAxisOffset < graphWidth) {
				g.drawLine((int) xAxisOffset, 0, (int) xAxisOffset, graphHeight);
			}
			
			if (yAxisOffset > 0 && yAxisOffset < graphHeight) {
				g.drawLine(0, (int) yAxisOffset, graphWidth, (int) yAxisOffset);
			}
			
			g.setColor(Color.black);
			Coordinate lastCoord = null;
			
			int pointNum = 0;
			Coordinate point = getPoint(pointNum);
			
			 while (point != null) {
				 
				double xPixel = pixelStart[0] + (point.getX() - graphStart.getX()) * (graphWidth / xRange());
				double yPixel = pixelStart[1] + (graphStart.getY() + yRange() - point.getY()) * (graphHeight / yRange());
				
				if (lastCoord != null) {
					if (point.drawTo() && lastCoord.drawFrom()) {
						g.drawLine((int) lastCoord.getX(), (int) lastCoord.getY(), (int) xPixel, (int) yPixel);
					}
					
					//g.drawString(Double.toString(point.getY()), (int) xPixel, (int) yPixel);
				}
				
				System.out.println(point.getX() + ", " + point.getY());
				
				
				lastCoord = new Coordinate(xPixel, yPixel, point.drawFrom(), point.drawTo());
				pointNum++;
				point = getPoint(pointNum);
			}
		}
	}
}
