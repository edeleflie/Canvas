
import java.awt._
import java.awt.Color

class Polygona (xPoints:Array[Int], yPoints:Array[Int], lineColor:Color, lineThickness:Float, fillColor:Color) extends Drawable( lineColor, lineThickness, fillColor)  {

   shape  = new java.awt.Polygon(xPoints, yPoints, xPoints.length)
   
}