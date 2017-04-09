//*******************************************
// Interpolation.java
// 
// Creates a graph that demonstrates different types of interpolation 
// 
// Future changes: Fix comments/print statements, random points, drag points, more best fit (exp, sin, cos, etc)
//
// Copyright, 2015
// Taylor White
// 3/20/2015
//*******************************************

import java.util.*;

import javax.swing.*;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Shape;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;


public class MainView extends ApplicationFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ArrayList <Coordinates> coordinates = new ArrayList<Coordinates>();
	static int counter = 0;
	static int NUM_OF_FUNCTIONS = 10;
    UserPanel userpanel = new UserPanel();
    static Interpolation interpolation = new Interpolation();	
    static BestFit bestFit = new BestFit();
    static Menu jMenu = new Menu();
    double a = 0.0;
    double n = 0.0;  
    double dNeg = -100;
    double dPos = 100;
    static int nthFitVal = 0;
/*
    *Constructor. Creates the panels, labels, text fields, action listeners,
    *arrays, and layouts
    */
   public MainView(String s)
	{
      super(s);

      //Create Panels
      final JPanel container = new JPanel();
      final ChartPanel chart = createChart(createXYDataset(-100,100, new boolean[NUM_OF_FUNCTIONS]), -100, 100, new boolean[NUM_OF_FUNCTIONS]);//Create chart using default settings
      final JPanel userPanel = userpanel.createUserPanel();
      final JMenuBar menu = jMenu.createMenu();
      container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
      userpanel.getAddPoint().addActionListener(new ActionListener() {//Creates action listener for "add point"
          @Override
          public void actionPerformed(ActionEvent e) {//When user clicks "add point", this function will run
             System.out.println("Add point");
             userpanel.addToPointPanel();//Create new point panel complete with x and y coordinates
             
             container.revalidate();
             container.repaint();
          }
    }); 
      userpanel.getUpdate().addActionListener(new ActionListener() {//Creates action listener for "update"
      @Override
      public void actionPerformed(ActionEvent e) {//When user clicks "update", this function will run
         System.out.println("update");
         coordinates = userpanel.updateCoordinates();
         coordinates= Helper.sort(coordinates);
  
            try{//Attempt to create a chart using the users domain
               a = Double.parseDouble(getDFAString());
               n = Double.parseDouble(getDFNString());
            }   
            catch (Exception invadlidDomain){//If dfa and dfn fields can't be read, make them 0
               a = 0.0;
               n = 0.0;
               //dfa.setText("0");
               //dfn.setText("0");
            } 
            
            try{//Attempt to create a chart using the users nth value for best fit
               nthFitVal = Integer.parseInt(getNthString());
            }   
            catch (Exception invadlidExponent){//If dfa and dfn fields can't be read, make them 0
               nthFitVal = 0;
               //nthFit.setText("4");
            }                
               
            //graphCoordinates.updateCoordinates(xList, yList, a, n, nthFitVal);//Update array of x and y values by passing in the list of x and y text fields

            try{//Attempt to create a chart using the users domain
               dPos = Double.parseDouble(getDomainPosString());
               dNeg = Double.parseDouble(getDomainNegString());
               //dPos = Double.parseDouble(userPanel.getDomainPos().getText());
               if(dNeg > dPos){
                  double tmp = dNeg;
                  dNeg = dPos;
                  dPos = tmp;
                  setDomainNeg(dNeg);//Set text fields to reflect new domain values
                  setDomainPos(dPos);
               }
            }  
            catch (Exception invadlidDomain){//Create a chart using the greatest and lowest x values as the domain
               double max = Helper.xMax(coordinates);//Get maximum x value
               double min = Helper.xMin(coordinates);//Get minimum x value
               dNeg = min;
               dPos = max;
            } 
            if(dNeg == dPos){ //Domain ranges can not be the same value
               dNeg = dNeg-1;
               dPos = dPos+1;
               //domainNeg.setText(Double.toString(dNeg));//Set text fields to reflect new domain values
               //domainPos.setText(Double.toString(dPos));                  
            }    
            interpolation.setCoordinates(coordinates);
            bestFit.setCoordinates(coordinates);
            boolean[] checks = new boolean[NUM_OF_FUNCTIONS];
            System.out.println("checks: " + checks.length);
            
            if(getLinearFitCheck().isSelected()) {
              System.out.println("Linear Best Fits Checked");
              bestFit.setLValues();
              checks[0] = true;
            }
            if(getQuadFitCheck().isSelected()) {
              System.out.println("Quadratic Best Fits Checked");
              bestFit.setQValues();
              checks[1] = true;
            }  
            if(getCubicFitCheck().isSelected()) {
            	bestFit.setCValues();
              System.out.println("Cubic Best Fits Checked");
              checks[2] = true;
            }      
            if(getNthFitCheck().isSelected()) {
              System.out.println("nth Best Fits Checked");
              bestFit.setNValues(nthFitVal);
              checks[3] = true;
            }                                          
            if(getPolyCheck().isSelected()) {
              System.out.println("Polynomial Checked");
              interpolation.setPValues();
              checks[4] = true;
            }
            if(getLinCheck().isSelected()) {
               System.out.println("Linear Checked");
               //No coefficients
               checks[5] = true;
            }   
            if(getQuadCheck().isSelected()) {
               System.out.println("Quadratic Checked");
               interpolation.setQValues();
               checks[6] = true;
            }     
            if(getNatCubicCheck().isSelected()) {
               System.out.println("Natural Cubic Checked");
               interpolation.setZValues();
               checks[7] = true;
            }    
            if(getClaCubicCheck().isSelected()) {
               System.out.println("Clamped Cubic Checked");
               interpolation.setCValues(a, n);
               checks[8] = true;
            }                     
            container.removeAll();
            

            ChartPanel chart = createChart(createXYDataset(dNeg,dPos, checks),dNeg,dPos, checks);//Create a new chart using new values
            chart.revalidate();
            chart.repaint();
            container.add(chart);
            container.add(userPanel);
            container.revalidate();
            container.repaint();
        }


  });
      //frame.setJMenuBar(theJMenuBar);
      //Setup container
      container.setPreferredSize(new Dimension(1000, 680));
      container.add(chart);
      container.add(userPanel);
		setContentPane(container);      

	}

   public static ChartPanel createChart(XYDataset dataset, double Dneg, double Dpos, boolean[] checks){
	      //Add coordinates to chart
	      JFreeChart chart = ChartFactory.createXYLineChart(
	         "Graphing Points", 
	         "X", 
	         "Y", 
	         dataset, 
	         PlotOrientation.VERTICAL, 
	         true, 
	         true, 
	         false
	         );         
	      XYPlot xyplot = (XYPlot)chart.getPlot();
	      XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
	      xylineandshaperenderer.setSeriesLinesVisible(0, false);
	      xylineandshaperenderer.setSeriesShapesVisible(0, true);
	      Shape circle = new Ellipse2D.Double(-3, -3, 6, 6);
	      xylineandshaperenderer.setSeriesShape(0, circle);
	      for(int i=1; i<NUM_OF_FUNCTIONS; ++i){
	    	  xylineandshaperenderer.setSeriesLinesVisible(i, true);
		      xylineandshaperenderer.setSeriesShapesVisible(i, false);
	      }          
	      
	      xylineandshaperenderer.setSeriesPaint(0,Color.RED);
	      int i=1;
	      if(checks[0]==true){//Linear Fit
	         xylineandshaperenderer.setSeriesPaint(i,new Color(255,255,255));
	         i++;
	      }
	      if(checks[1]==true){//Quadratic Fit
	         xylineandshaperenderer.setSeriesPaint(i,new Color(110,150,0));
	         i++;
	      }            
	      
	      if(checks[2]==true){//Cubic Fit
	         xylineandshaperenderer.setSeriesPaint(i,new Color(0,143,0));
	         i++;
	      }
	      if(checks[3]==true){//Nth Fit
	         xylineandshaperenderer.setSeriesPaint(i,new Color(3,41,3)); 
	         i++;
	      }  
	      if(checks[4]==true){//Polynomial Interpolation
	         xylineandshaperenderer.setSeriesPaint(i,new Color(255,0,0));
	         i++;
	      }  
	      if(checks[5]==true){//Linear Spline
	         xylineandshaperenderer.setSeriesPaint(i,new Color(84,164,255));
	         i++;
	      }  
	      if(checks[6]==true){//Quadratic Spline
	         xylineandshaperenderer.setSeriesPaint(i,new Color(22,40,181));
	         i++;
	      } 
	      if(checks[7]==true){//Cubic Spline (natural)
	         xylineandshaperenderer.setSeriesPaint(i,new Color(186,0,161));
	         i++;
	      }  
	      if(checks[8]==true){//Cubic Spline (clamped)
	         xylineandshaperenderer.setSeriesPaint(i,new Color(240,0,120));
	         i++;
	      }  
	     
	      xylineandshaperenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
	      xylineandshaperenderer.setDefaultEntityRadius(6);
	      xyplot.setRenderer(xylineandshaperenderer);

	      xyplot.setDomainGridlinesVisible(true);
	      xyplot.setRangeGridlinesVisible(true);
	      xyplot.setDomainGridlinePaint(Color.darkGray);
	      xyplot.setRangeGridlinePaint(Color.darkGray);
	      xyplot.setDomainCrosshairVisible(true);
	      xyplot.setRangeCrosshairVisible(true);

			NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
	      domain.setRange(Dneg, Dpos);
	      
	      xyplot.getDomainAxis().setLowerMargin(0.0D);
			xyplot.getDomainAxis().setUpperMargin(0.0D);
	      return new ChartPanel(chart);     
	    }  
   

   /*
    *Returns the dataset for (x,y) coordinates using the sampleFunction2DToSeries.
    *Also creates a second set of data containing the points inputed from the user.
    *
    *Input: double, double
    *Returns: XYDataset
    */
   public static XYDataset createXYDataset(double Dneg, double Dpos, boolean[] checks){ 
     coordinates = Helper.sort(coordinates);
     final XYSeries coordinatePlot = new XYSeries( "points" );
     for(int i=0; i < coordinates.size(); ++i){                
        coordinatePlot.add( coordinates.get(i).getX() , coordinates.get(i).getY() );
     }
     final XYSeriesCollection dataset = new XYSeriesCollection( );
     
     dataset.addSeries( coordinatePlot ); 
     
     if(checks[0]==true){
        dataset.addSeries( bestFit.getLinearFitCheck(Dneg, Dpos) );
     }  
     if(checks[1]==true){
        dataset.addSeries( bestFit.getQuadraticFitCheck(Dneg, Dpos) );
     }   
     if(checks[2]==true){
        dataset.addSeries( bestFit.getCubicFitCheck(Dneg, Dpos) );
     } 
     if(checks[3]==true){
        dataset.addSeries( bestFit.getNthbBestFit(Dneg, Dpos) );
     }                             
     
     if(checks[4]==true){
        dataset.addSeries( interpolation.getPolyInterpolation(Dneg, Dpos) );
     }
     if(checks[5]==true){  
        dataset.addSeries( interpolation.getLinearSpline(Dneg, Dpos) );
     }  
     if(checks[6]==true){
        dataset.addSeries( interpolation.getQuadraticSpline(Dneg, Dpos) );
     }  
     if(checks[7]==true){
        dataset.addSeries( interpolation.getNatCubicSpline(Dneg, Dpos) );
     }  
     if(checks[8]==true){          
        dataset.addSeries( interpolation.getClaCubicSplineInterp(Dneg, Dpos) );
     }                   
     return dataset;
  }   
   
	private String getDomainNegString() {
		return userpanel.domainNegToString();
	}
	private String getDomainPosString() {
		return userpanel.domainPosToString();
	}		
	private String getDFAString() {
		return userpanel.getDFAString();
	}
	private String getDFNString() {
		return userpanel.getDFNString();
	}
	private String getNthString() {
		return userpanel.getNthString();
	}
	private void setDomainPos(double p) {
		userpanel.setDomainPos(p);
		return;
	}	
	private void setDomainNeg(double n) {
		userpanel.setDomainNeg(n);
		return;
	}
	private JCheckBox getLinearFitCheck() {
		return userpanel.getLinearFitCheck();
	}
	private JCheckBox getQuadFitCheck() {
		return userpanel.getQuadFitCheck();
	}
	private JCheckBox getCubicFitCheck() {
		return userpanel.getCubicFitCheck();
	}
	private JCheckBox getNthFitCheck() {
		return userpanel.getNthFitCheck();
	}
	private JCheckBox getPolyCheck() {
		return userpanel.getPolyCheck();
	}
	private JCheckBox getLinCheck() {
		return userpanel.getLinearCheck();
	}
	private JCheckBox getQuadCheck() {
		return userpanel.getQuadCheck();
	}
	private JCheckBox getNatCubicCheck() {
		return userpanel.getNatCubicCheck();
	}
	private JCheckBox getClaCubicCheck() {
		return userpanel.getClampedCubicCheck();
	}   
   
   /*
    *Main method. Uses SplineInterpolation constructor to create window.
    */
	public static void main(String args[])
	{
		MainView main = new MainView("Interpolation");
		main.pack();
		RefineryUtilities.centerFrameOnScreen(main);
		main.setVisible(true);
	}
}