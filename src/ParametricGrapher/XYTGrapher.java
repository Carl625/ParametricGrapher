package ParametricGrapher;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public abstract class XYTGrapher {
	
	private JFrame frame;
	private Graph graph;
	private int[] pixelStart;
	private int graphHeight;
	private int graphWidth;
	
	abstract public Pair<Coordinate, Coordinate> xyStart();
	
	abstract public double tInitial();
	abstract public double tIncrement();
	abstract public double tRange();
	abstract public boolean graphPolar();
	
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
			
			Pair<Coordinate, Coordinate> displayRange = xyStart();
			double xRange = (displayRange.get2().getX() - displayRange.get1().getX());
			double yRange = (displayRange.get2().getY() - displayRange.get1().getY());
			
			if (graphPolar()) {
				Graphics2D g2 = (Graphics2D) g;
				double PolarXOffset = pixelStart[0] + ((0.0 - displayRange.get1().getX()) / xRange) * graphWidth;
				double PolarYOffset = pixelStart[1] + (graphHeight - (((0.0 - displayRange.get1().getY()) / yRange) * graphHeight));
				double maxDistance = Math.sqrt(Math.pow(displayRange.get2().getX(), 2) + Math.pow(displayRange.get2().getY(), 2));
				//System.out.println(maxDistance);
				
				g2.setColor(Color.green);
				for (int i = 0; i < maxDistance; i++) {
					
					g2.draw(new Ellipse2D.Double(PolarXOffset - ((i / xRange) * graphWidth), PolarYOffset - ((i / yRange) * graphHeight), ((i / xRange) * graphWidth) * 2, ((i / yRange) * graphHeight * 2)));
				}
				
				for (int i = 0; i < 24; i++) {
					
					g.drawLine((int) PolarXOffset, (int) PolarYOffset, (int) (((maxDistance * (graphWidth / xRange)) * Math.cos(i * (Math.PI / 6.0))) + PolarXOffset), (int) (((maxDistance * (graphHeight / yRange)) * Math.sin(i * (Math.PI / 6.0))) + PolarYOffset));
				}
				
				g.setColor(Color.black);
				Coordinate lastCoord = null;
				
				int pointNum = 0;
				Coordinate point = getPoint(pointNum);
				
				 while (point != null) {
					 
					double xPixel = pixelStart[0] + ((point.getX() * Math.cos(point.getY())) - displayRange.get1().getX()) * (graphWidth / xRange);
					double yPixel = pixelStart[1] + (displayRange.get1().getY() + yRange - (point.getX() * Math.sin(point.getY()))) * (graphHeight / yRange);
					
					if (lastCoord != null) {
						if (point.drawTo() && lastCoord.drawFrom()) {
							g.drawLine((int) (lastCoord.getX()), (int) (lastCoord.getY()), (int) xPixel, (int) yPixel);
						}
						
						//g.drawString(Double.toString(point.getY()), (int) xPixel, (int) yPixel);
					}
					
					//System.out.println((point.getX() * Math.cos(point.getY())) + ", " + (point.getX() * Math.sin(point.getY())));
					
					
					lastCoord = new Coordinate(xPixel, yPixel, point.drawFrom(), point.drawTo());
					pointNum++;
					point = getPoint(pointNum);
				}
			} else {
				double xAxisOffset = pixelStart[0] + ((0.0 - displayRange.get1().getX()) / xRange) * graphWidth;
				double yAxisOffset = pixelStart[1] + (graphHeight - (((0.0 - displayRange.get1().getY()) / yRange) * graphHeight));
				
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
					 
					double xPixel = pixelStart[0] + (point.getX() - displayRange.get1().getX()) * (graphWidth / xRange);
					double yPixel = pixelStart[1] + (displayRange.get1().getY() + yRange - point.getY()) * (graphHeight / yRange);
					
					if (lastCoord != null) {
						if (point.drawTo() && lastCoord.drawFrom()) {
							g.drawLine((int) lastCoord.getX(), (int) lastCoord.getY(), (int) xPixel, (int) yPixel);
						}
						
						//g.drawString(Double.toString(point.getY()), (int) xPixel, (int) yPixel);
					}
					
					//System.out.println(point.getX() + ", " + point.getY());
					
					
					lastCoord = new Coordinate(xPixel, yPixel, point.drawFrom(), point.drawTo());
					pointNum++;
					point = getPoint(pointNum);
				}
			}
		}
	}
}
