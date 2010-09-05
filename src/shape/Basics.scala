

import java.awt._
import java.awt.Color
import java.awt.font._
import java.awt.geom._

class Rectangle(x:Int, y:Int, width:Int, height:Int, lineColor:Color, lineThickness:Float, fillColor:Color) extends Drawable( x, y, lineColor, lineThickness, fillColor)  {
    
    // Auxiliary constructors
    def this(x:Int, y:Int, width:Int, height:Int, lineColor:Color, lineThickness:Float) = this(x, y, width, height, lineColor, lineThickness, null)       
    def this(x:Int, y:Int, width:Int, height:Int, lineColor:Color) = this(x, y, width, height, lineColor, 0.2f, null)       
    def this(x:Int, y:Int, width:Int, height:Int ) = this(x, y, width, height, Color.black, 0.2f, null)  
    def this(x:Int, y:Int, width:Int ) = this(x, y, width, width, Color.black, 0.2f, null)  

    shape  = new java.awt.geom.Rectangle2D.Double(x, y, width, height)
}

class Ellipse(x:Int, y:Int, width:Int, height:Int, lineColor:Color, lineThickness:Float, fillColor:Color) extends Drawable( x, y, lineColor, lineThickness, fillColor) {
    
    shape  = new java.awt.geom.Ellipse2D.Double(x - width, y - height, width * 2, height * 2)
}

class Curve(xPoints:Array[Double], yPoints:Array[Double], lineColor:Color, lineThickness:Float, fillColor:Color) extends Drawable( lineColor, lineThickness, fillColor ) {
    
    def this(xPoints:Array[Double], yPoints:Array[Double], lineColor:Color, lineThickness:Float ) = this(xPoints, yPoints, lineColor, lineThickness, null)  
    
    val path:GeneralPath = new GeneralPath();
    path.moveTo(xPoints(0),yPoints(0));
    // path.lineTo(0.0f,125.0f);
    
    for  ( i <- 1 to (xPoints.length - 1) by 2)  {
        path.quadTo( xPoints(i) , yPoints(i),  xPoints(i+1) , yPoints(i+1) )
    }
    
    shape = path.asInstanceOf[Shape]
    
    //path.curveTo(260.0f,100.0f,130.0f,50.0f,225.0f,0.0f);
    // path.closePath();
}

class Line() extends Drawable() {
    // java.awt.geom.QuadCurve2D.Double
}


class Text(x:Int, y:Int, text:String, size:Double) extends Drawable( x, y ) {
        
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
            
            var someShape = applyTransform(g2D)
            if (fillColor != null) {
                g2D.setColor( fillColor )
                g2D.fill(someShape)
            }
            g2D.setColor( lineColor )
            g2D.setStroke(new BasicStroke(lineThickness))
            g2D.draw(someShape)        
        }

}