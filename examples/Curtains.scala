import java.awt.{Color}
import scala.collection.mutable.ListBuffer
    
(new Curtains(900,400)).main(null)
Thread.sleep(60000)

class Curtains(width:Int, height:Int) extends Canvas(width, height) {
    
    // The random number generator
    val rnd = new scala.util.Random
    
    // these hold the x, y coordinats of the line
    val xPoints = new Array[Double](51)
    val yPoints = new Array[Double](51)
    
    xPoints(0) = 0 ; yPoints(0) = 500
    for (i <- 1 to xPoints.length - 1) { xPoints(i) = i * 18 }
    for (i <- 1 to yPoints.length - 1) { yPoints(i) = yPoints(i-1) + (rnd.nextInt(30) - 15) }
    
    // Define variations on the line
    val lotsaLines  = new ListBuffer[Drawable]
    lotsaLines += new Curve( xPoints, yPoints, new Color (255,0,0,10), 0.4f)
    
    for (j <- 0 to  50) {
        // vary the y positions of the line
        for (i <- 0 to (yPoints.length - 1)) {
            yPoints(i) = yPoints(i) + rnd.nextInt(9) - 4 - 6
        }
        // add the line
        lotsaLines += new Curve( xPoints, yPoints, new Color (255,rnd.nextInt(150),0,50), 0.4f)
    }
    
    // Add the shape to the canvas
    lotsaLines.foreach (shape => canvas += shape)

    // Tell the cavans to keep drawing the background
    keepDrawingBackground = false
        
    override def animate() = {
        for (i <- 0 to (lotsaLines.length -1)) { 
            lotsaLines(i).scaleBy( 1 + (0.0002 * (lotsaLines.length - i)) )
            lotsaLines(i).moveBy(0,-0.3)
        }
    }
}