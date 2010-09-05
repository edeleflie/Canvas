import java.awt.{Color}
import scala.collection.mutable.ListBuffer
    
(new NervousRectangles(700,500)).main(null)
Thread.sleep(60000)

class NervousRectangles(width:Int, height:Int) extends Canvas(width, height) {
  
    // The random number generator
    val rnd = new scala.util.Random
    
    // Define lots of rectangles
    val lotsaShapes  = new ListBuffer[Drawable]
    for (x <- 0 to 20) {
        lotsaShapes += new Rectangle(rnd.nextInt(400), rnd.nextInt(400), rnd.nextInt(300), rnd.nextInt(300), Color.white, 0.2f, new Color(rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255), 100))     
    }

    // Put all the shapes on the "canvas"
    lotsaShapes.foreach (shape => canvas += shape)
    
    // Make the shapes move
    override def animate() = {
        lotsaShapes.foreach {shape => 
            shape.moveBy(rnd.nextInt(9) - 4, rnd.nextInt(9) - 4)
        }   
    }
}