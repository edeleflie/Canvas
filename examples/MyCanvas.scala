import java.awt.{Color}
    
(new MyCanvas).main(null)
Thread.sleep(60000)

class MyCanvas extends Canvas {
  
    // Some variables to control positions
    var change = 1.01
    var xIncrement = 0
    var yIncrement = 0
    var rnd = new scala.util.Random
    
    // Define some shapes
    val ellipse = new Ellipse(200, 200, 80, 80, Color.GREEN, 10f, new Color(255,0,0,200))
    val rectangle = new Rectangle(400, 100, 10, 10, Color.black, 2f, new Color(210,210,255))
    val polygon = new Polygona( Array(10,300,40,50), Array(30,120,230,200), Color.BLUE, 0.2f, new Color(255,255,0,200))
    val mouseCircle = new Ellipse(0, 0, 40, 40, Color.GREEN, 0.1f, new Color(255,200,200,50))
    
    // Put the shapes on the "canvas" (in a specific order)
    canvas += (rectangle, polygon, ellipse, mouseCircle)
    
    // Make the shapes move
    override def animate() = {
        
        ellipse.moveBy(xIncrement, yIncrement)
        rectangle.scaleBy( change )
        mouseCircle.moveTo(mouseX, mouseY)
        polygon.rotateBy(1)
        
        if ( mouseCircle.hasCollidedWith(ellipse) ) ellipse.fillColor =  new Color(rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255),200)
    }
    
    // Respond to key presses
    override def keyPressed(key:Int) = {
        xIncrement = 0 ; yIncrement = 0
        if (key == 37) xIncrement = -2
        else if (key == 38) yIncrement = -2
        else if (key == 39) xIncrement = 2
        else if (key == 40) yIncrement = 2
    }
}