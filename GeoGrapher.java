 package geographer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class GeoGrapher extends JFrame{
    
    double xradius = 15;
    double yradius = 15;
    double xscale = 1;
    double yscale = 1;
    int originx, originy;
    float xpixels, ypixels;
    double circleradius = 10;
    
    double n;
    double nstep = Math.PI/30;
    double nstart = 0;
    double nstop = 2*Math.PI;
    
    double time;
    double tstep = Math.PI/30;
    long steplength = 30;
    double tstop = 2*Math.PI;
    double tstart = 0;
    
    boolean removeVerticalLines = false;
    
    //Function yfunction, xfunction; // "Parametric"

    public static void main(String[] args) {
        GeoGrapher gg = new GeoGrapher(1500,800);
    }
    private Image dbImage;
    private Graphics dbGraphics;
    
    public GeoGrapher(int width, int height) {
        setTitle("Graph");
        setSize(width, height);
        setResizable(false);
        setVisible(true);
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        originx = width/2;
        originy = height/2;
        
        xpixels = originx / (float)xradius;
        ypixels = originy / (float)yradius;
        
        time = tstart;
        
        //yfunction = new Function("n");
        //xfunction = new Function("n");
        
        // equations
        //"+(*(t,*(n,n)),+(*(2,n),3))"
        //"csc(+(*(2,n),t))"
        //((5+5*Math.cos(Tvalue+time))*Math.cos(Tvalue)); "*(+(5,*(5,cos(+(n,t)))),cos(n))"
        //((5+5*Math.cos(Tvalue-time))*Math.sin(Tvalue)); "*(+(5,*(5,cos(-(n,t)))),sin(n))"
        
        //yfunction.setFunction("*(+(5,*(5,cos(+(n,t)))),cos(n))");
        //xfunction.setFunction("*(+(5,*(5,cos(-(n,t)))),sin(n))");
        
        /*Operation o0 = new Add();
        yfunction.addOperation(o0,0,0);
        
        Operation o1 = new Add();
        o1.setArgument(1,3);
        yfunction.addOperation(o1,0,1);
        
        Operation o2 = new Mult();
        yfunction.addOperation(o2,0,0);
        
        Operation o3 = new Mult();
        o3.setArgument(0, 2);
        yfunction.addOperation(o3,1,0);
        
        Operation o4 = new Mult();
        //o4.setArgument(0, 2);
        yfunction.addOperation(o4,2,0);
        
        int[][]var = {{4,1},{2,1},{3,1}};
        yfunction.setUpVariable(var);
        int[][]tm = {{4,0}};
        yfunction.setUpTime(tm);*/
        //System.out.println(xpixels);
        //System.out.println(ypixels);
        
        //System.out.println(xfunction(Tstart));
        //System.out.println(yfunction(Tstart));
        //System.out.println(xfunction(Tstart + Tstep));
        //System.out.println(yfunction(Tstart + Tstep));
    }
    
    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbGraphics = dbImage.getGraphics();
        paintComponents(dbGraphics);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    @Override
    public void paintComponents(Graphics g) {
        //drawCircle(g);
        drawAxes(g);
        drawGridlines(g);
        drawShape(g);
        
        if ((time <= tstop && tstep>0) || (time >= tstop && tstep<0)) {
            time += tstep;
            try {
                Thread.sleep(steplength);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeoGrapher.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        } else {
            time = tstart;
            try {
                Thread.sleep(steplength);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeoGrapher.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
    }
    
    //(5/Math.cos(44*Tvalue+0.25*time)*Math.cos(Tvalue+0.25*time));
    //(5/Math.cos(44*Tvalue-0.25*time)*Math.sin(Tvalue-0.25*time));
    
    //((5+5*Math.cos(Tvalue+time))*Math.cos(Tvalue));
    //((5+5*Math.cos(Tvalue-time))*Math.sin(Tvalue));
    
    //(10*Math.cos(60*Tvalue+time)*Math.cos(Tvalue)); // high res
    //(10*Math.cos(60*Tvalue-time)*Math.sin(Tvalue));
    
    //((9+2*Math.cos(time))*Math.cos(60*Tvalue)*Math.cos(Tvalue-0.5*Math.PI-0.05*time)); //high res
    //((9+2*Math.sin(time))*Math.cos(60*Tvalue-0.5*Math.PI)*Math.sin(Tvalue-0.05*time));
    
    // (Tvalue*Math.cos(time)+Tvalue*Math.cos(time+0.5*Math.PI));
    // (Tvalue*Math.sin(time)+Tvalue*Math.sin(time+0.5*Math.PI));
    
    public void drawAxes(Graphics g) {
        g.setColor(Color.red);
        g.drawLine(originx, 0, originx, 2*originy);
        g.drawLine(0, originy, 2*originx, originy);
    }
    
    public void drawGridlines(Graphics g) {
        g.setColor(Color.red);
        for(double n = xpixels*xscale; n < getWidth() / 2; n += xpixels * xscale) {
            g.drawLine(originx + (int)Math.round(n),
                       originy + Math.round(ypixels/5),
                       originx + (int)Math.round(n),
                       originy - Math.round(ypixels/5));
        }
        for(double n = ypixels*yscale; n < getHeight() / 2; n += ypixels * yscale) {
            g.drawLine(originx + Math.round(xpixels/5),
                       originy + (int)Math.round(n),
                       originx - Math.round(xpixels/5),
                       originy + (int)Math.round(n));
        }
        for(double n = xpixels*xscale; n > -1 * getWidth() / 2; n -= xpixels * xscale) {
            g.drawLine(originx + (int)Math.round(n),
                       originy + Math.round(ypixels/5),
                       originx + (int)Math.round(n),
                       originy - Math.round(ypixels/5));
        }
        for(double n = ypixels*yscale; n > -1 * getHeight() / 2; n -= ypixels * yscale) {
            g.drawLine(originx + Math.round(xpixels/5),
                       originy + (int)Math.round(n),
                       originx - Math.round(xpixels/5),
                       originy + (int)Math.round(n));
        }
    }
    
    public void drawCircle(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval((int)Math.round(originx - (xpixels * circleradius)),
                   (int)Math.round(originy - (ypixels * circleradius)),
                   (int)Math.round(2 * circleradius * xpixels),
                   (int)Math.round(2 * circleradius * ypixels));
    }
    
    public void drawShape(Graphics g) {
        g.setColor(Color.CYAN);
        //yfunction.setTime(time); // plug-in time
        //xfunction.setTime(time);
        int x1, y1, x2, y2;
        for (n = nstart; n < nstop; n += nstep) {
            x1 = (int)Math.round(originx + (functionX(n) * xpixels));
            y1 = (int)Math.round(originy - (functionY(n) * ypixels));
            x2 = (int)Math.round(originx + (functionX(n + nstep) * xpixels));
            y2 = (int)Math.round(originy - (functionY(n + nstep) * ypixels));
            if (Math.abs(x2-x1) <= getWidth() && Math.abs(y2-y1) <= getHeight()) {
                if (!removeVerticalLines || (removeVerticalLines && !(x1==x2))) {
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }
    
    private double functionX(double t) {
        return ((5+5*Math.cos(t+time))*Math.cos(t));
    }
    
    private double functionY(double t) {
        return ((5+5*Math.cos(t-time))*Math.sin(t));
    }
}