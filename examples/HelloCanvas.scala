import java.awt.{Color}
    
(new HelloCanvas()).main(null)
Thread.sleep(60000)

class HelloCanvas() extends Canvas() {
    
    // Define a shape
    val mouseCicrle = new Ellipse( 0,  0, 60, 60, Color.GREEN, 1f, new Color(255,0,0,70))
    
    // Add the shape to the canvas
    canvas += mouseCicrle
    
    // Tell the cavans to keep drawing the background
    keepDrawingBackground = false
    
    // Make the shape move
    override def animate() = {
        mouseCicrle.moveTo(mouseX, mouseY)
    }
    
}