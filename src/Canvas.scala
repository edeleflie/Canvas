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

class Polygon (xPoints:Array[Int], yPoints:Array[Int], lineThickness:Float, lineColor:Color, fill:Boolean, fillColor:Color) extends Drawable {
    
  //  Polygon(int[] xpoints, int[] ypoints, int npoints) 
    
   shape  = new java.awt.Polygon(xPoints, yPoints, xPoints.length)
   shapeLineThickness = lineThickness
   shapeLineColor = lineColor
   shapeFill = fill
   shapeFillColor = fillColor
}

class Rectangle(x:Int, y:Int, width:Int, height:Int, lineThickness:Float, lineColor:Color, fill:Boolean, fillColor:Color) extends Drawable {
    
   shape  = new java.awt.geom.Rectangle2D.Double(x, y, width, height)
   shapeLineThickness = lineThickness
   shapeLineColor = lineColor
   shapeFill = fill
   shapeFillColor = fillColor
}

class Ellipse(x:Int, y:Int, width:Int, height:Int, lineThickness:Float, lineColor:Color, fill:Boolean, fillColor:Color) extends Drawable {
    
    shape  = new java.awt.geom.Ellipse2D.Double(x, y, width, height)
    shapeLineThickness = lineThickness
    shapeLineColor = lineColor
    shapeFill = fill
    shapeFillColor = fillColor
}

class Text(x:Int, y:Int, text:String, size:Double) extends Drawable {
        
        val font = Font.getFont("Monospaced")
        
        //test()
        
        def test() = {  
             val ge:java.awt.GraphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();  
             val fontNames = ge.getAvailableFontFamilyNames()
             fontNames.foreach { name =>       
                 println(name + " = " +  Font.decode( name ) ) 
             }  

         }
        
        // for drawing text, we need to override the draw method, beause we need
        // access to the Graphics2D object
        override def draw(g2D: Graphics2D) = {
            
            val frc: FontRenderContext  = g2D.getFontRenderContext();
            val tl: TextLayout = new TextLayout(text, font , frc);
            shape = tl.getOutline(null);
            
            var someShape = applyTransform(g2D, shape)
            if (shapeFill) {
                g2D.setColor( shapeFillColor )
                g2D.fill(someShape)
            }
            g2D.setColor( shapeLineColor )
            g2D.setStroke(new BasicStroke(shapeLineThickness))
            g2D.draw(someShape)        
        }

}

trait Drawable {
    var shape: Shape = null
    var shapeLineThickness:Float = 1.0f 
    var shapeLineColor:Color = Color.black 
    var shapeFill:Boolean = true
    var shapeFillColor:Color = Color.gray
    
    var xOffset:Double = 0
    var yOffset:Double = 0
    var scaleX:Double = 1
    var scaleY:Double = 1
    var rotation:Double = 0
    var shearX:Double = 0
    var shearY:Double = 0
    
    
    def draw(g2D: Graphics2D) = {
        var someShape = applyTransform(g2D, shape)
        if (shapeFill) {
            g2D.setColor( shapeFillColor )
            g2D.fill(someShape)
        }
        g2D.setColor( shapeLineColor )
        g2D.setStroke(new BasicStroke(shapeLineThickness))
        g2D.draw(someShape)        
    }
    
    /* Gets called before draw()
     * Makes sure that the rotation, offset and scale settings are all applied
     */
    def applyTransform(g2D: Graphics2D, shape: Shape): Shape = {
        
        // get the centre point
        val bounds: Rectangle2D = shape.getBounds2D()
        val centreX: Double = bounds.getCenterX()
        val centreY: Double = bounds.getCenterY()
        var resultantShape: Shape = shape
        
        // first, do any rotation (about the objects centre)
        if (rotation != 0) {
            val  at: AffineTransform = AffineTransform.getRotateInstance(math.Pi/180 * rotation, centreX, centreY)
            resultantShape = at.createTransformedShape(resultantShape)
        }
        
        // next do scaling (about the objects centre)
        if ( (scaleX != 1) || (scaleY != 1) ) {
            val  at: AffineTransform = new AffineTransform()
            
            at.translate(centreX, centreY)
        	at.scale(scaleX, scaleY)
        	at.translate(-centreX, -centreY)
              
            resultantShape = at.createTransformedShape(resultantShape)
        }

        // do shearing
        if ( (shearX != 0) || (shearY != 0) ) {
            val at: AffineTransform = AffineTransform.getShearInstance(scaleX, scaleY)
            resultantShape = at.createTransformedShape(resultantShape)
        }

        // finally, offset
        val rat:AffineTransform = new AffineTransform()
        rat.setToTranslation(xOffset, yOffset)
        rat.createTransformedShape(resultantShape)
    }
    
    def moveBy(x:Double, y:Double) = {
        xOffset += x
        yOffset += y
    }
    
    def moveTo(x:Double, y:Double) = {
        xOffset = x
        yOffset = y
    }
    
    def scaleBy(scale:Double) = {
        scaleX = scale
        scaleY = scale
    }
    
    def rotateBy(rotateInDegrees:Double) = {
        rotation = rotateInDegrees
    }
    
    def rotateAround(x:Int, y:Int, rotateInDegrees:Double) = {
        // rotation = rotateInDegrees
    }
    
    def shearBy(x:Double, y:Double) = {
        shearX = x;
        shearY = y;
    }
}