import java.awt.{Color}
    
(new MyCanvas).main(null)
Thread.sleep(60000)

class MyCanvas extends Canvas {
  
    // Some variables to control positions
    var change = 0
    var xIncrement = 0
    var yIncrement = 0
    
    // Define some shapes
    val ellipse = new Ellipse(200, 200, 80, 80, 10f, Color.GREEN, true, new Color(255,0,0,200))
    val rectangle = new Rectangle(400, 100, 10, 10, 2f, Color.black, true, new Color(210,210,255))
    val polygon = new Polygon( Array(10,300,40,50), Array(30,120,230,200), 0.2f, Color.BLUE, true, new Color(255,255,0,200))
    val mouseCicrle = new Ellipse(0, 0, 40, 40, 0.1f, Color.GREEN, true, new Color(255,200,200,50))
    
    // Put the shapes on the "canvas" (in a specific order)
    canvas += (rectangle, polygon, ellipse, mouseCicrle)
    
    // Make the shapes move
    override def animate() = {
        change += 1
        ellipse.moveBy(xIncrement, yIncrement)
        rectangle.scaleBy(change * 0.05)
        polygon.rotateBy(change)
        mouseCicrle.moveTo(mouseX, mouseY)
    }
    
    // Respond to key presses
    override def keyPressed(key:Int) = {
        xIncrement = 0 ; yIncrement = 0
        if (key == 37) xIncrement += -2
        else if (key == 38) yIncrement += -2
        else if (key == 39) xIncrement += 2
        else if (key == 40) yIncrement += 2
    }
}