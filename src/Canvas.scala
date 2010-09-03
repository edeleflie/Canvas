/**
 * 
 */
import scala.actors.Actor.{actor, loop, reactWithin, exit}
import scala.actors.TIMEOUT
import scala.swing._
import scala.swing.event._
import scala.math._
import scala.collection.mutable.ListBuffer

import java.awt.{Color, Shape, BasicStroke, Toolkit, RenderingHints, Font}
import java.awt.event.{ActionEvent}
import java.awt.image.{BufferedImage}
import java.awt.geom._
import java.awt.font._

import javax.swing._

class Canvas extends SimpleSwingApplication {
    var width = 600; var height = 500
    var mouseX = 0;
    var mouseY = 0;
    
    // The Array of drawable objects
    val canvas = new ListBuffer[Drawable]
    
    lazy val panel = new Panel() {
        
        val buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        var drawn = false
        focusable = true
        background = Color.white
        preferredSize = new Dimension(width, height)
        this.peer.setIgnoreRepaint(true)
        this.peer.setDoubleBuffered(true)
        
        def draw() = {
            val b:Graphics2D = buffer.createGraphics()
            b.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            
            drawBackground(b)
            // drawBackgroundOnce(b)

            canvas.toList.foreach { drawable => 
                drawable.draw(b)
            }
            
            b.dispose()
        }
        
        //paint the panel background
        def drawBackground(g2D: Graphics2D) = {
            g2D.setColor(Color.white)
            g2D.fill(new Rectangle2D.Double(0, 0, width, height))
        }
        
        //paint the panel background
        def drawBackgroundOnce(g2D: Graphics2D) = {
            if (!drawn) { drawBackground(g2D); drawn = true }
        }
        
        def copyBufferToScreen() = {
            var g: Graphics2D = this.peer.getGraphics().asInstanceOf[Graphics2D]
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.drawImage(buffer,0,0,this.peer)
            Toolkit.getDefaultToolkit().sync()
            g.dispose()
        }
    }
    
    // These need to be overriden
    def animate() = {}
    def keyPressed(key:Int) = {}
    
    // listen to outside influences
    panel.listenTo(panel.mouse.clicks, panel.mouse.moves, panel.keys)
    
    panel.reactions += {
        case MouseMoved(src, point, mods) => { mouseX = point.x ; mouseY = point.y } 
        case MouseDragged(src, point, mods) => println("mouse dragged " + point)
        case MousePressed(src, point, i1, i2, b) => println("mouse pressed " + point)
        case MouseReleased(src, point, i1, i2, b) => println("mouse released " + point)
        case KeyPressed(_, key, _, _) => {
            keyPressed( key.hashCode() )
        } 
    }
    
    val rotator = actor {
        loop {
          reactWithin(15) {
            case TIMEOUT => {
                    animate()
                    panel.draw()
                    panel.copyBufferToScreen()
                }
            }
        }
    }
  
    def top = new MainFrame {
        title = "Basic Animation"
        contents = panel
    }
}