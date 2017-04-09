//*******************************************
// polyInterp.java
// 
// The coordinates object.  Stores information
// for an x and y coordinate.
// 
// Copyright, 2015
// Taylor White
// 3/12/2015
//*******************************************

public class Coordinates
{	
   private double x; //x coordinate
   private double y; //y coordinate
   
   /*
    * Constructor.  Stores an x coordinate and y coordinate
    */
   public Coordinates(double xVal, double yVal){
      this.x = xVal;
      this.y = yVal;
   }
   /*
    * Returns the x value of this coordinate object
    */   
   public double getX(){
      return this.x;
   }
   /*
    * Returns the y value of this coordinate object
    */      
   public double getY(){
      return this.y;
   } 
   
}   

