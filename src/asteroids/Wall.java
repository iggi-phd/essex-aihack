package asteroids;                                                              
                                                                                
import math.Vector2d;                                                           
                                                                                
import java.awt.*;                                                              
import java.awt.geom.Rectangle2D;   
import java.awt.geom.AffineTransform;
                                                                                
public class Wall extends GameObject {
    private Rectangle2D wall;
    private Color color = Color.yellow;
    private int[] xp;
    private int[] yp;

    public Wall(Vector2d _s, double _width, double _height) {                          
        super(new Vector2d(_s), new Vector2d(_width, _height));
        this.wall = new Rectangle2D.Double(this.s.x, this.s.y, _width, _height);
        this.xp = new int[4];
        xp[0] = (int) (s.x-wall.getWidth()/2);
        xp[1] = (int) (s.x+wall.getWidth()/2);
        xp[2] = (int) (s.x+wall.getWidth()/2);
        xp[3] = (int) (s.x-wall.getWidth()/2);
        this.yp = new int[4];
        yp[0] = (int) (s.y+wall.getHeight()/2);
        yp[1] = (int) (s.y+wall.getHeight()/2);
        yp[2] = (int) (s.y-wall.getHeight()/2);
        yp[3] = (int) (s.y-wall.getHeight()/2);
    }

    public Wall(double _x, double _y, double _width, double _height) { 
        super(new Vector2d(_x, _y), new Vector2d(_width, _height));
        this.wall = new Rectangle2D.Double(_x, _y, _width, _height);
        this.xp = new int[4];                                                
        xp[0] = (int) (s.x-wall.getWidth()/2);                                             
        xp[1] = (int) (s.x+wall.getWidth()/2);                                             
        xp[2] = (int) (s.x+wall.getWidth()/2);                                             
        xp[3] = (int) (s.x-wall.getWidth()/2);                                             
        this.yp = new int[4];                                                
        yp[0] = (int) (s.y+wall.getHeight()/2);                                            
        yp[1] = (int) (s.y+wall.getHeight()/2);                                            
        yp[2] = (int) (s.y-wall.getHeight()/2);                                            
        yp[3] = (int) (s.y-wall.getHeight()/2);
    }
    
    public int getXEdge(int i) {
        assert(i>=0 && i <4);
        return xp[i];
    }

    public int getYEdge(int i) {
        assert(i>=0 && i <4);
        return yp[i];
    }

    public boolean dead() {
        assert(true);
        return false;
    }

    public void update() {
        assert(true);
    }

    public void hit() {
        dead = false;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();                                  
        g.translate(s.x, s.y);                                                  
        g.setColor(color);                                                      
        g.fillPolygon(xp, yp, xp.length);                                       
        g.setTransform(at); 
    }
    
    public Wall copy() {
        Wall w = new Wall(s, wall.getWidth(), wall.getHeight());
        return w;
    }

    public Rectangle2D getBound() {
        return wall;
    }
} 
