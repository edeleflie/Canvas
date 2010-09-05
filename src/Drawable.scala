/**
 *  Defines characteristics of a drawable object.
 *  Extend this to create a new drawable object.
 *  Essentially, all that needs to be done is to provide the Java2D shape object.
 *  See Rectangle.scala for a simple example.
 */

import java.awt.{Graphics2D, Color, Shape, BasicStroke, Toolkit, RenderingHints, Font}
import javax.swing._
import java.awt.geom._

class Drawable(var x:Int, var y:Int, var lineColor:Color, var lineThickness:Float, var fillColor:Color) {

    // Auxiliary constructors
    def this(lineColor:Color,  lineThickness:Float,  fillColor:Color) = this(0, 0, lineColor,lineThickness, fillColor) 
    def this( x:Int,  y:Int) = this( x, y, Color.black, 0.2f, null) 
    def this() = this( 0, 0, Color.black, 0.2f, null)  
    
    var shape: Shape = null
    
    var xOffset:Double = 0
    var yOffset:Double = 0
    var scaleX:Double = 1
    var scaleY:Double = 1
    var rotation:Double = 0
    var shearX:Double = 0
    var shearY:Double = 0
    
    
    def draw(g2D: Graphics2D) = {
        var someShape = applyTransform(g2D)
        if (fillColor != null) {
            g2D.setColor( fillColor )
            g2D.fill(someShape)
        }
        g2D.setColor( lineColor )
        g2D.setStroke(new BasicStroke(lineThickness))
        g2D.draw(someShape)        
    }
    
    /* Gets called before draw()
     * Makes sure that the rotation, offset and scale settings are all applied
     */
    def applyTransform(g2D: Graphics2D): Shape = {
        
        // get the centre point
        val bounds: Rectangle2D = shape.getBounds2D()
        val centreX: Double = bounds.getCenterX()
        val centreY: Double = bounds.getCenterY()
        
        // first, do any rotation (about the objects centre)
        if (rotation != 0) {
            val  at: AffineTransform = AffineTransform.getRotateInstance(math.Pi/180 * rotation, centreX, centreY)
            shape = at.createTransformedShape(shape)
        }
        
        // next do scaling (about the objects centre)
        if ( (scaleX != 1) || (scaleY != 1) ) {
            val  at: AffineTransform = new AffineTransform()
            
            at.translate(centreX, centreY)
        	at.scale(scaleX, scaleY)
        	at.translate(-centreX, -centreY)
              
            shape = at.createTransformedShape(shape)
        }

        // do shearing
        if ( (shearX != 0) || (shearY != 0) ) {
            val at: AffineTransform = AffineTransform.getShearInstance(scaleX, scaleY)
            shape = at.createTransformedShape(shape)
        }

        // finally, offset
        val rat:AffineTransform = new AffineTransform()
        rat.setToTranslation(xOffset, yOffset)
        shape = rat.createTransformedShape(shape)
        return shape
    }
    
    def moveBy(x:Double, y:Double) = {
        xOffset = x
        yOffset = y
    }
    
    def moveTo(x:Double, y:Double) = {
        val bounds: Rectangle2D = shape.getBounds2D()
        
        xOffset = x - bounds.getCenterX()
        yOffset = y - bounds.getCenterY()
    }
    
    def scaleBy(scale:Double) = {
        scaleX = scale
        scaleY = scale
    }
    
    def rotateBy(rotateInDegrees:Double) = {
        // println("here.........")
        rotation = rotateInDegrees
    }
    
    def rotateAround(x:Int, y:Int, rotateInDegrees:Double) = {
        // rotation = rotateInDegrees
    }
    
    def shearBy(x:Double, y:Double) = {
        shearX = x;
        shearY = y;
    }
    
    def hasCollidedWith(otherShape: Drawable): Boolean = {
        return shape.getBounds().intersects(otherShape.shape.getBounds())
    }

}