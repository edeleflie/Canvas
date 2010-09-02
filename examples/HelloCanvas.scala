import java.awt.{Color}
    
(new HelloCanvas).main(null)
Thread.sleep(60000)

class HelloCanvas extends Canvas {
    
    // Define a shape
    val mouseCicrle = new Ellipse( 0,  0, 80, 80, 1f, Color.GREEN, true, Color.RED)
    
    // Add the shape to the canvas
    canvas += mouseCicrle
    
    // Make the shape move
    override def animate() = {
        mouseCicrle.moveTo(mouseX, mouseY)
    }
    
}