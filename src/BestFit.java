// Copyright Taylor White, 2015

import java.util.*;

import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;


public class BestFit {
	
	   static ArrayList <Coordinates> coordinates = new ArrayList<Coordinates>(); 
	   static ArrayList <Double> l = new ArrayList<Double>();
	   static ArrayList <Double> q = new ArrayList<Double>();
	   static ArrayList <Double> c = new ArrayList<Double>();
	   static ArrayList <Double> n = new ArrayList<Double>();
	   
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
	   public void setCoef(int nthVal){
		   l.clear();
		   setLValues();
		   q.clear();
		   setQValues();
		   c.clear();
		   setCValues();
		   n.clear();
		   setNValues(nthVal);
		   return;
	   }
	   public XYSeries getLinearFitCheck(double Dneg, double Dpos){
	 	   final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new linBestFit(), Dneg, Dpos, 1000, "Linear Best Fit");  
	 	   System.out.print("get lin best fit");
	 	   return plot;
	    }
	   public XYSeries getQuadraticFitCheck(double Dneg, double Dpos){
	 	   final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new quadBestFit(), Dneg, Dpos, 1000, "Quadratic Best Fit");  
	 	   System.out.print("get lin best fit");
	 	   return plot;
	    }
	   public XYSeries getCubicFitCheck(double Dneg, double Dpos){
	 	   final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new cubicBestFit(), Dneg, Dpos, 1000, "Cubic Best Fit");  
	 	   System.out.print("get lin best fit");
	 	   return plot;
	    }
	   public XYSeries getNthbBestFit(double Dneg, double Dpos){
	 	   final XYSeries plot = DatasetUtilities.sampleFunction2DToSeries(new nthBestFit(), Dneg, Dpos, 1000, "Nth Best Fit");  
	 	   System.out.print("get nth best fit");
	 	   return plot;
	   }
	   class linBestFit implements Function2D
		{
	     /*
	      *Input: double x
	      *Returns: double y
	      */
	      public double getValue(double x)
			{
	    	  double result = 0;
	          int exp = l.size()-1;     
	          for(int i=0; i<l.size(); ++i){
	             result = result + l.get(i)*Helper.exponent(x, exp);
	             --exp;
	          }
	          return result;
			}
		}
	   class quadBestFit implements Function2D
		{
	     /*
	      *Input: double x
	      *Returns: double y
	      */
	      public double getValue(double x)
			{
	    	  double result = 0;
	          int exp = q.size()-1;     
	          for(int i=0; i<q.size(); ++i){
	             result = result + q.get(i)*Helper.exponent(x, exp);
	             --exp;
	          }
	          return result;
			}
		}
	   class cubicBestFit implements Function2D
		{
	     /*
	      *Input: double x
	      *Returns: double y
	      */
	      public double getValue(double x)
			{
	    	  double result = 0;
	          int exp = c.size()-1;     
	          for(int i=0; i<c.size(); ++i){
	             result = result + c.get(i)*Helper.exponent(x, exp);
	             --exp;
	          }
	          return result;
			}
		}
	   class nthBestFit implements Function2D
		{
	     /*
	      *Input: double x
	      *Returns: double y
	      */
	      public double getValue(double x)
			{
	          double result = 0;
	          int exp = n.size()-1;
	          for(int i=0; i<n.size(); ++i){
	             result = result + n.get(i)*Helper.exponent(x, exp);
	             --exp;
	          }
	          return result;
			}	
		}    
	   
	   /* Set coefficients used in best fit functions */
	    public void setLValues(){
	        int exp = 1;
	        l.clear();
	        double[] result = new double[2];
	        result = setCoefficients(exp);
	        for(int i=0; i<2; ++i){
	           l.add(result[i]);   
	        } 
	     } 
	    public void setQValues(){
	        int exp = 2;
	        q.clear();
	        double[] result = new double[3];
	        result = setCoefficients(exp);
	        for(int i=0; i<3; ++i){
	           q.add(result[i]);   
	        } 
	     } 
	    public void setCValues(){
	        int exp = 3;
	        c.clear();
	        double[] result = new double[4];
	        result = setCoefficients(exp);
	        for(int i=0; i<4; ++i){
	           c.add(result[i]);   
	        } 
	     } 
	    public void setNValues(int nth){
	        int exp = nth;
	        n.clear();
	        double[] result = new double[nth+1];
	        result = setCoefficients(exp);
	        System.out.println("exp: " + exp);
	        for(int i=0; i<exp+1; ++i){
	           n.add(result[i]);   
	        } 
	     } 
	    
	    /* General formula for setting coefficients. Needed for exponents larger than 1 */
	    private double[] setCoefficients(int largestDegree){
	        int exp = largestDegree;
	        int ARRAY_SIZE = exp+1;
	        int n = coordinates.size();
	        if(exp == 0){ //Exponent = 0
	        	double[] constant = new double[]{0};
	        	for(int i=0; i<coordinates.size(); ++i){
	        		constant[0]= constant[0]+coordinates.get(i).getY();
	        	}
	        	constant[0]=constant[0]/(coordinates.size());
	        	return constant;
	        }else if(exp == 1){ //Exponent = 1
	        	double[] linear = new double[]{0, 0};
		         double sumXSqu = 0;
		         double sumX = 0;
		         double sumY = 0;
		         double sumXY = 0;
		         for(int i=0;i<n;++i){
		            sumXSqu = sumXSqu+coordinates.get(i).getX()*coordinates.get(i).getX();
		            sumX = sumX+coordinates.get(i).getX();
		            sumY = sumY+coordinates.get(i).getY();
		            sumXY = sumXY+coordinates.get(i).getY()*coordinates.get(i).getX();
		         }
		         double det = n*sumXSqu-sumX*sumX;
		         linear[0] = (1/det)*(n*sumXY-sumX*sumY);
		         linear[1] = (1/det)*(sumXSqu*sumY-sumX*sumXY);	        	
	        	return linear;
	        }else{ //Exponent > 2
		        double[][] mat = new double[ARRAY_SIZE][ARRAY_SIZE];
		        double[] v = new double[ARRAY_SIZE];
		        double[] sumX = new double[(ARRAY_SIZE-1)*2+1]; //index represents power. sumX[0] is coordinates.size() for simplicity
		        double[] sumXY = new double[ARRAY_SIZE]; //index represents X power. sumXY[0] = sum of y values
		        double[] result = new double[ARRAY_SIZE];
		        sumX[0]=n;
		        for(int i=0;i<n;++i){
		           for(int j=1; j<sumX.length; ++j){
		              sumX[j] = sumX[j]+Helper.exponent(coordinates.get(i).getX(), j);
		           }   
		           for(int j=0; j<sumXY.length; ++j){
		              sumXY[j] = sumXY[j]+Helper.exponent(coordinates.get(i).getX(), j)*coordinates.get(i).getY();
		           }                 
		        }     
		        for(int i=0; i<ARRAY_SIZE; ++i){
		           for(int j=0; j<ARRAY_SIZE; ++j){
		              mat[i][j] = sumX[(2*(ARRAY_SIZE-1))-(i+j)];
		           }
		           v[i]=sumXY[ARRAY_SIZE-1-i];  
		        } 
		        result = Helper.gaussian(mat, v);
		        return result;
	        }
	     }	    

}
