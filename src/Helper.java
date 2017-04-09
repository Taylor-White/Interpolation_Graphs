//*******************************************
// Helper.java
// 
// Has functions for generally useful functions
//
// xMin(ArrayList<Coordinates>) - Returns double
// xMax(ArrayList<Coordinates>) - Returns double
// sort(ArrayList<Coordinates>) - Returns ArrayList<Coordinates>
// exponent(double, int)        - Returns double
// gaussian(double[][], double[]) - Returns double[]
//
// Copyright, 2015
// Taylor White
// 3/20/2015
//*******************************************

import java.util.ArrayList;

public class Helper {
	
	/* Returns the minimum x coordinate */
	public static double xMin(ArrayList<Coordinates> coordinates){
        if(coordinates.size() == 0)
           return -100;
        double min = coordinates.get(0).getX();
        for(int i=0; i<coordinates.size()-1; ++i){
           if (coordinates.get(i+1).getX() < coordinates.get(i).getX())
              min = coordinates.get(i+1).getX();
        }
        return min;
     } 
	
	/* Returns the maximum x coordinate */
    public static double xMax(ArrayList<Coordinates> coordinates){
        if(coordinates.size() == 0)
           return 100;
        double max = coordinates.get(0).getX();
        
        for(int i=0; i<coordinates.size()-1; ++i){
           if (coordinates.get(i+1).getX() > coordinates.get(i).getX())
              max = coordinates.get(i+1).getX();
        }
        return max;
     } 
    
    /* Returns the sorted set of coordinates */
    public static ArrayList<Coordinates> sort(ArrayList<Coordinates> coordinates){
        if(coordinates.size() < 2){
	        return coordinates;
        } else if(coordinates.size() == 2){
        	if(coordinates.get(0).getX() > coordinates.get(1).getX()){
	            Coordinates tempCoordinate = new Coordinates(coordinates.get(0).getX(), coordinates.get(0).getY());
	            coordinates.set(0,coordinates.get(1));
	            coordinates.set(1,tempCoordinate);
	        }
	        return coordinates;
        } else{
        	for(int i=0; i<coordinates.size()-1; ++i){
        		for(int j=0; j<coordinates.size()-1; ++j){
        			if(coordinates.get(j).getX() > coordinates.get(j+1).getX()){
        				Coordinates tempCoordinate = new Coordinates(coordinates.get(j).getX(), coordinates.get(j).getY());
        				coordinates.set(j,coordinates.get(j+1));
        				coordinates.set(j+1,tempCoordinate);
        			}   
        		}
        	} 
       } 	
       return coordinates;
    }   
    
    /* Returns the result of the exponential function double "value" to the "exp" */
    public static double exponent(double value, int exp){
        if(exp == 0)
           return 1;
        return value*exponent(value, --exp);
     }
    
    /* Gaussian elimination.  Returns an array representing the solution to the input matrix */
    public static double[] gaussian(double[][] mat, double[] v){
        int n = v.length;
        int[] l = new int[n];
        double[] s = new double[n];
        int smax = 0;
        double rmax = 0;
        double xmult = 0;
        double r;
        for(int row=0;row<n;++row){
           l[row] = row; 
           smax = row;
           for(int col=0;col<n;++col){
              if(Math.abs(mat[row][col])>Math.abs(mat[smax][col]));
                 smax = col;
           } 
           s[row] = smax;
        }  
        int j = 0;
        for(int k=0;k<n-1;++k){
           rmax = 0;
           for(int i=k;i<n;++i){         
              r = Math.abs(mat[l[i]][k]/s[l[i]]);
              if(r > rmax){
                 rmax = r;
                 j=i;
              }  
           }
           int tmp = l[j];
           l[j] = l[k];
           l[k] = tmp;
           for(int i = k+1; i<n; ++i){
              xmult = mat[l[i]][k]/mat[l[k]][k];
              mat[l[i]][k] = xmult;
              for(int jtemp=k+1; jtemp<n;++jtemp){
            	  mat[l[i]][jtemp] = mat[l[i]][jtemp] - (xmult)*mat[l[k]][jtemp];
                 j=jtemp;
              }
           }
        }
        for(int i=0;i<mat.length;++i){
           for(int k=0;k<mat[i].length;++k){
           }
        }
        for(int k=0; k<n-1; ++k){
           for(int i=k+1;i<n;++i){
              v[l[i]] = v[l[i]] - mat[l[i]][k]*v[l[k]];
           }
        }
        double[] x = new double[n];
        double sum;
        x[n-1]=v[l[n-1]]/mat[l[n-1]][n-1];
        for(int i = n-2; i>=0; --i){
           sum = v[l[i]];
           for(int w=i+1; w<n; ++w){
              sum = sum-mat[l[i]][w]*x[w];
           }
           x[i] = sum/mat[l[i]][i];
        }
        return x;
    } 
}
