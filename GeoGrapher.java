package geographer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class GeoGrapher extends JFrame{
    
	private static final long serialVersionUID = 1L;
	
	double xradius = 15;
    double yradius = 15;
    double xscale = 1;
    double yscale = 1;
    int originx, originy;
    float xpixels, ypixels;
    double circleradius = 10;
    
    double n;
    double nstep = 0.01;
    double nstart = -15;
    double nstop = 15;
    
    double time;
    double tstep = 0.125;
    long steplength = 10;
    double tstop = 15;
    double tstart = -15;
    
    boolean doCircle = false;
    boolean doGrids = true;
    
    Color bg = Color.BLACK;
    Color circle = Color.WHITE;
    Color grid = Color.RED;
    Color shape = Color.CYAN;
    
    ExpressionTree xfunction, yfunction; // parametric functions

    public static void main(String[] args) {
    	// the expressions
    	Expression exprX, exprY;
    	exprX = new Expression();
    	exprY = new Expression();
    	
    	try {
    		exprX.setExpression(args[0]);
    		exprY.setExpression(args[1]);
    	} catch(ArrayIndexOutOfBoundsException ex1) {
    		try {
    			exprX.setExpression("n");
    			exprY.setExpression(args[0]);
    		} catch(ArrayIndexOutOfBoundsException ex2) {
    			System.out.println("Usage: java GeoGrapher [exprX] [exprY]");
        		System.out.println("Usage: java GeoGrapher [exprY]");
        		
        		// default expressions -- for debug
                exprX.setExpression("n");
                exprY.setExpression("log(e,n)");
    		}
    	}
    	
    	// get default function list
    	FunctionList defaults = new FunctionList();
    	defaults.loadFunctionList();
    	
    	// parse expressions
    	exprX.parseExpresison(defaults);
    	exprY.parseExpresison(defaults);
    	
    	// check for errors
    	boolean hasErrors = false;
    	if(exprX.hasErrors()) {
    		hasErrors = true;
        	System.out.println("Errors in X equation: ");
        	Iterator<MathError> it = exprX.getErrorIterator();
        	while(it.hasNext()) {
        		System.out.println(it.next());
        	}
    	}
    	if(exprY.hasErrors()) {
    		hasErrors = true;
        	System.out.println("Errors in Y equation: ");
        	Iterator<MathError> it = exprY.getErrorIterator();
        	while(it.hasNext()) {
        		System.out.println(it.next());
        	}
    	}
    	
    	if(!hasErrors) {
        	// make grapher
	        GeoGrapher gg = new GeoGrapher(1500,800);
	        gg.setFunctions(exprX.getTokens(), exprY.getTokens(), 
	        		exprX.getUsedFunctions(), exprY.getUsedFunctions());
	        gg.setVisible(true);
        }
    }
    
    public GeoGrapher(int width, int height) {
        setTitle("Graph");
        setSize(width, height);
        setResizable(false);
        setBackground(bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        originx = width/2;
        originy = height/2;
        
        xpixels = originx / (float)xradius;
        ypixels = originy / (float)yradius;
        
        time = tstart;
        
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
    
    public void setFunctions(String[] tokenX, String[] tokenY, FunctionList usingX, FunctionList usingY) {
    	xfunction = new ExpressionTree(tokenX, usingX);
        yfunction = new ExpressionTree(tokenY, usingY);
        
        xfunction.computeConstants();
        yfunction.computeConstants();
    }
    
    @Override
    public void paint(Graphics g) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbGraphics = dbImage.getGraphics();
        paintComponents(dbGraphics);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    @Override
    public void paintComponents(Graphics g) {
    	if(doCircle) {
    		drawCircle(g);
    	}
        
    	if(doGrids) {
    		drawAxes(g);
    		drawGridlines(g);
    	}
        
        if(xfunction != null && yfunction != null) {
        	drawShape(g);
        }
        
        // update t
        if ((time <= tstop && tstep>0) || (time >= tstop && tstep<0)) {
            time += tstep;
        } else {
            time = tstart;
        }
        
        // thread sleep
        try {
            Thread.sleep(steplength);
        } catch (InterruptedException ex) {
            Logger.getLogger(GeoGrapher.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }
    
    //(5/Math.cos(44*t+0.25*time)*Math.cos(t+0.25*time));
    //(5/Math.cos(44*t-0.25*time)*Math.sin(t-0.25*time));
    
    //((5+5*Math.cos(t+time))*Math.cos(t));
    //((5+5*Math.cos(t-time))*Math.sin(t));
    
    //(10*Math.cos(60*t+time)*Math.cos(t)); // high res
    //(10*Math.cos(60*t-time)*Math.sin(t));
    
    //((9+2*Math.cos(time))*Math.cos(60*t)*Math.cos(t-0.5*Math.PI-0.05*time)); //high res
    //((9+2*Math.sin(time))*Math.cos(60*t-0.5*Math.PI)*Math.sin(t-0.05*time));
    
    // (t*Math.cos(time)+t*Math.cos(time+0.5*Math.PI));
    // (t*Math.sin(time)+t*Math.sin(time+0.5*Math.PI));
    
    // (5/Math.cos(22*t-0.25*time)*Math.cos(t+0.25*time));
    // (5/Math.cos(22*t-0.25*time)*Math.sin(t+0.25*time));
    
    public void drawAxes(Graphics g) {
        g.setColor(grid);
        g.drawLine(originx, 0, originx, 2*originy);
        g.drawLine(0, originy, 2*originx, originy);
    }
    
    public void drawGridlines(Graphics g) {
        g.setColor(grid);
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
        g.setColor(circle);
        g.drawOval((int)Math.round(originx - (xpixels * circleradius)),
                   (int)Math.round(originy - (ypixels * circleradius)),
                   (int)Math.round(2 * circleradius * xpixels),
                   (int)Math.round(2 * circleradius * ypixels));
    }
    
    public void drawShape(Graphics g) {
        g.setColor(shape);
        
        // plug-in time
        xfunction.plugInT(time); 
        yfunction.plugInT(time);
        
        double xlast, ylast, xnext, ynext;
        int x1,y1,x2,y2;
        
        // initialize
        xfunction.plugInN(nstart);
        yfunction.plugInN(nstart);
        xnext = xfunction.getValue();
        ynext = yfunction.getValue();
        
        for (n = nstart+nstep; n < nstop; n += nstep) {
            xlast = xnext;
            ylast = ynext;
            
            xfunction.plugInN(n);
            yfunction.plugInN(n);
            xnext = xfunction.getValue();
            ynext = yfunction.getValue();
            
            if(!Double.isNaN(xlast) && !Double.isNaN(ylast) &&
               !Double.isNaN(xnext) && !Double.isNaN(ynext)) {
            	x1 = (int)Math.round(originx + xlast * xpixels);
	        	y1 = (int)Math.round(originy - ylast * ypixels);
	        	x2 = (int)Math.round(originx + xnext * xpixels);
	        	y2 = (int)Math.round(originy - ynext * ypixels);
	            
	            if (Math.abs(x2-x1) <= getWidth() && Math.abs(y2-y1) <= getHeight()) {
                    g.drawLine(x1, y1, x2, y2);
	            }
            }
        }
    }
}