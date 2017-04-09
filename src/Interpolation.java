//*******************************************
// SplineInterpCalculator.java
// 
// Contains arraylists for storing x and y coordinate values. 
// Also stores the z values used in cubic spline interpolation.
// Contains functions used for updating x and y coordinates, and
// calculating a cubic spline that goes through these points.
// Also has functions for creating the chart
// filled with the data, and has a few other miscellaneous functions
// such as xMax() for returning the max x value and sort for returning
// a sorted version of the points inputed from the user.
// 
// Copyright, 2015
// Taylor White
// 3/12/2015
//*******************************************

import java.util.*;

import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;

import org.jfree.data.xy.*;

public class Interpolation
{	
   private ArrayList <Double> p = new ArrayList<Double>();
   private ArrayList <Double> q = new ArrayList<Double>();  
   private ArrayList <Double> z = new ArrayList<Double>();
   private static ArrayList <Double> c = new ArrayList<Double>(); 
   private static ArrayList <Coordinates> coordinates = new ArrayList<Coordinates>();
   
   /* 
    *Fills the x and y arraylists with values from the x and y JTextFields.
    *Resets z arraylist.  
    *
    *Input: JTextField, JTextField.
    */
   public void setCoordinates(ArrayList<Coordinates> newCoordinates){
      coordinates.clear();
      for(int i=0; i<newCoordinates.size(); ++i){//Loops through text fields for point coordinates
         try{//Try to get inputs for each point. If contents are invalid, skip it
            coordinates.add(newCoordinates.get(i));
         }catch (Exception e){ //Skip blank or invalid Fields
            System.out.println("Blank or invalid number");
         }
      }
      return;
   }
   public void setCoef(double a, double n){
	   p.clear();
	   setPValues();
	   z.clear();
	   setZValues();
	   q.clear();
	   setQValues();
	   c.clear();
	   setCValues(a, n);
	   return;
   }
   public XYSeries getPolyInterpolation(double Dneg, double Dpos){
 	   final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new polyInterp(), Dneg, Dpos, 1000, "Polynomial Interpolation");  
 	   return plot;
   }  
   public double polyInterpolation(double x,int i, int counter){
      if(i == counter){//Stop recursion if there are no more coordinates
        return 1;
      }
      return p.get(i) + (x-coordinates.get(i).getX()) * polyInterpolation(x, ++i, counter);
   } 
   public XYSeries getLinearSpline(double Dneg, double Dpos){
	  final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new linearSpline(), Dneg, Dpos, 1000, "Linear Spline");  
	  return plot;
   }
   public XYSeries getQuadraticSpline(double Dneg, double Dpos){
 	  final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new quadraticSpline(), Dneg, Dpos, 1000, "Quadratic Spline");  
 	  return plot;
   }
   public XYSeries getNatCubicSpline(double Dneg, double Dpos){
 	  final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new natSpline(), Dneg, Dpos, 1000, "Natural Cubic Spline");  
 	  return plot;
   }
   public XYSeries getClaCubicSplineInterp(double Dneg, double Dpos){
 	  final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new clampedSpline(), Dneg, Dpos, 1000, "Clamped Spline Interpolation");  
  	  return plot;
   }

   
 
   class polyInterp implements Function2D
	{
    /*
     *Input: double x
     *Returns: double y
     */
     public double getValue(double x)
		{
        double t = polyInterpolation(x, 0, coordinates.size());
        return t;
		}
	} 
   
    class linearSpline implements Function2D
	{
     /*
      *Input: double x
      *Returns: double y
      */
      public double getValue(double x)
		{
      int n;
      n = coordinates.size()-1;
      if(coordinates.size() == 0)
    	  return 0;
      if(coordinates.size() == 1)
         return coordinates.get(0).getY();
      int i = n-1;
      if(i>=0){
         while(i>0){
            if(x-coordinates.get(i).getX()>0)
               break;
            --i;
         }   
      }
      double yValue = coordinates.get(i).getY()+((coordinates.get(i+1).getY()-coordinates.get(i).getY())/(coordinates.get(i+1).getX()-coordinates.get(i).getX()))*(x-coordinates.get(i).getX());
      return yValue; //Calculate Coefficients
		}
	}   

    class quadraticSpline implements Function2D
 	{
      /*
       *Input: double x
       *Returns: double y
       */
       public double getValue(double x)
 		{
           int n;
           n = coordinates.size()-1;
           if(coordinates.size() == 0)
         	  return 0;
           if(coordinates.size() == 1)
              return coordinates.get(0).getY();
           int i = n-1;
           if(i>=0){
              while(i>0){
                 if(x-coordinates.get(i).getX()>0)
                    break;
                 --i;
              }   
           }
           double yValue = ((q.get(i+1)-q.get(i))/(2*(coordinates.get(i+1).getX()-coordinates.get(i).getX())))*(x-coordinates.get(i).getX())*(x-coordinates.get(i).getX())+q.get(i)*(x-coordinates.get(i).getX())+coordinates.get(i).getY();
           return yValue; //Calculate Coefficients
 		}
 	}       

    class natSpline implements Function2D
	{
     /*
      *Input: double x
      *Returns: double y
      */
    	public double getValue(double x)
		{
    		int n;
    		n = coordinates.size()-1;
    		if(coordinates.size() == 1)
    			return coordinates.get(0).getY();
    		return interpolation(n, x); //Calculate Coefficients
		}
	}
    class clampedSpline implements Function2D
 	{
      /*
       *Input: double x
       *Returns: double y
       */
       public double getValue(double x)
 		{
          return claInterpolation(coordinates.size()-1, x); //Calculate Coefficients
 		}
 	}      
    

     public static double claInterpolation(int n, double x){
    	 if(coordinates.size() == 1){
    		 return coordinates.get(0).getY();
    	 }
       int i=n-1;
       double val = 0;
       if(i>=0){
          while(i>0){
             if(x-coordinates.get(i).getX()>0)
                break;
             --i;
          }
          double t_i = coordinates.get(i).getX();
          double t_j = coordinates.get(i+1).getX();
          double h = t_j-t_i;
          double tmp = (c.get(i)/2)+(x-t_i)*(c.get(i+1)-c.get(i))/(6*h);
          tmp = -(h/6)*(c.get(i+1)+2*c.get(i))+(coordinates.get(i+1).getY()-coordinates.get(i).getY())/h+(x-t_i)*tmp;
          val = coordinates.get(i).getY()+(x-t_i)*tmp;
       }
       return val;
     }  
    public void setPValues(){
    p.clear();
    int n = coordinates.size();
      for(int i=0; i<n; ++i){
         p.add(coordinates.get(i).getY());//Set the a[i] array using values from y[i]
      }
      for(int j=1; j<n; ++j){//Itteratively stores the coefficients into the array a[i]
         for(int i=n-1; i>=j; --i){
            double temp = (p.get(i)-p.get(i-1))/(coordinates.get(i).getX()-coordinates.get(i-j).getX());
            p.set(i, temp);
         }
      }   
      return;
    }
    public void setQValues(){
        q.clear();
        int n;
        n = coordinates.size();
        if(n < 2)
           return;
        double[] qtemp = new double[n];  
        qtemp[0]=0;
        for(int i=0; i<n-1; ++i){
           qtemp[i+1]=-1*qtemp[i]+2*((coordinates.get(i+1).getY()-coordinates.get(i).getY())/(coordinates.get(i+1).getX()-coordinates.get(i).getX()));  
        } 
        for(int i=0; i<n; ++i){
           q.add(qtemp[i]);   
        }   
     }      
    public void setZValues(){
      int n;
      n = coordinates.size();
      if(n < 2)
         return;
      double[] v = new double[n+1];
      double[] h = new double[n+1];
      double[] b = new double[n+1];
      double[] u = new double[n+1];     
      double[] ztemp = new double[n+1];
      
      for(int i=0; i<n-1; ++i){
         h[i]=coordinates.get(i+1).getX()-coordinates.get(i).getX();
         b[i]=(coordinates.get(i+1).getY()-coordinates.get(i).getY())/h[i];
      }
      u[0]=1;
      u[1]=2*(h[0]+h[1]);  
      v[0]=0;
      v[1]=6*(b[1]-b[0]);       
      for(int i=2; i<n-1; ++i){
         u[i]=2*(h[i-1]+h[i])-((h[i-1]*h[i-1])/u[i-1]);  
         v[i]=6*(b[i]-b[i-1])-(h[i-1]*v[i-1]/u[i-1]);
      }     
      u[n-1]=1;    
      ztemp[n]=0;
      for(int i=n-1; i>0; --i){
         ztemp[i]=(v[i]-h[i]*ztemp[i+1])/u[i];  
      } 
      ztemp[0]=0;               
      for(int i=0; i<n; ++i){
         z.add(ztemp[i]);//Set the z values so the program can run through the interpolation() function without running coef() again
      }
      return;
    } 
    public void setCValues(double dfa, double dfn){
        c.clear();
        int n;
        n = coordinates.size();
        if(n < 2)
           return;
        double[] v = new double[n];
        double[] h = new double[n+1];
        double[] b = new double[n+1];
        double[] u = new double[n+1];     
        
        for(int i=0; i<n-1; ++i){
           h[i]=coordinates.get(i+1).getX()-coordinates.get(i).getX();
           b[i]=(coordinates.get(i+1).getY()-coordinates.get(i).getY())/h[i];
        }
        u[0]=2*h[0];
        v[0]=-6*dfa-6*b[0];      
        for(int i=1; i<n-1;
         ++i){
           u[i]=2*(h[i-1]+h[i]);  
           v[i]=6*(b[i]-b[i-1]);
        }     
        u[n-1]=2*h[n-2];
        v[n-1]=6*b[n-2]+6*dfn;    
        double[][] g = new double[n][n];
        for(int i=0; i<n; ++i){
           for(int j=0; j<n; ++j){
              if(j==i){
                 g[i][j]=u[j];
              } else if(j==i-1){
                 g[i][j]=h[j];
              } else if(j==i+1){
                 g[i][j]=h[i];
              }
           }
        } 
        double[] x = new double[n];
        x = Helper.gaussian(g, v);
        for(int i=0; i<n; ++i){
           c.add(x[i]); 
        } 
        return;
      }
    /*
     *Uses the coefficients and an x value to calculate the corresponding y
     *value in the natural cubic spline. 
     *
     *Inputs: int n, double x
     *Returns: double y
     */
    public double interpolation(int n, double x){
      int i=n-1;
      double val = 0;
      if(i>=0){
         while(i>0){
            if(x-coordinates.get(i).getX()>0)
               break;
            --i;
         }
         double t_i = coordinates.get(i).getX();
         double t_j = coordinates.get(i+1).getX();
         double h = t_j-t_i;
         double tmp = (z.get(i)/2)+(x-t_i)*(z.get(i+1)-z.get(i))/(6*h);
         tmp = -(h/6)*(z.get(i+1)+2*z.get(i))+(coordinates.get(i+1).getY()-coordinates.get(i).getY())/h+(x-t_i)*tmp;
         val = coordinates.get(i).getY()+(x-t_i)*tmp;
      }
      return val;
    }
}   