/*//---------------------------------------------------------------------
 CET 350
 Group #11
 Bounce.java
 Created: 3/2/2017
 Edited: 3/9/2017
 Description: Program creates a screen and canvas in a GUI
 			  environment.  A bouncing shape of either a 
              rectangle or oval will bounce on the canvas.
              User can select to pause, create object tails,
              clear tails from the screen, adjust object speed
              or size and to quit the program when finished.
*///---------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class CannonVSBall extends Frame implements ActionListener, WindowListener, AdjustmentListener, ComponentListener, Runnable
{
   //declare program globabl variables
	private static final long serialVersionUID = 1L;
	
	private Insets I;
	private Thread theThread;
	private board c = new board();
	private Panel control = new Panel();
   private Panel screen = new Panel(); 
	
	BorderLayout bdr = new BorderLayout();
   
   boolean stop = false;
   boolean speed = false;
   boolean runProg = true; 
   int speedVar = 5; 
   int delay = 10; 

   GridBagLayout gb = new GridBagLayout();
   GridBagConstraints gc = new GridBagConstraints(); 

   Button quit = new Button();
   Button pause = new Button(); 
   Button run = new Button(); 
   Scrollbar speedSB = new Scrollbar(Scrollbar.HORIZONTAL); 
   Scrollbar sizeSB = new Scrollbar(Scrollbar.HORIZONTAL); 
   Label size = new Label(" Size "); 
   Label speedL = new Label(" Speed ");
	
    boolean shape = false; 
    boolean tails = false;  
    boolean more = true;
    int H=500;
	 int W=1000;
	 int sh, sw;
	 int xUnits;
	 int yUnits;
    int mx, my; //mouse pressed vars
    int rx, ry; //mouse released vars

   //declare component scrollbars, buttons, and labels

   
	
	CannonVSBall(){ //constructor for Bounce

     this.setMinimumSize(new Dimension (379,205));
	  setLayout(bdr); //set layout
	  setVisible(true); //set layout visible
	  MakeSheet(); //create sheet 
	  try{ //run initcomponents to assemble screen and canvas objects
       initComponents(); 
     }
	  catch(Exception e) { //catch any errors or exceptions
       e.printStackTrace();
	  }
	  SizeScreen(); //run screen size
	}
	
	public void initComponents(){ //assembles components for bounce object
		         // Main Frame Parameters
				this.setTitle(" Bounce: The Sequel ");
				this.setLayout(bdr);
				this.setResizable(true);
				this.setVisible(true);
				this.setBackground(Color.darkGray);
				this.addWindowListener(this);
				this.addComponentListener(this);
				screen.add(c);
            this.add(screen, BorderLayout.CENTER); 
            this.add(control, BorderLayout.SOUTH); 
            
            double colWeight[] = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
            double rowWeight[] = {0.5, 0.5, 0.5}; 
            int colWidth[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; 
            int rowHeight[] = {1,1,1}; 
    
            gb.rowHeights = rowHeight;
            gb.rowWeights = rowWeight; 
            gb.columnWidths = colWidth; 
            gb.columnWeights = colWeight; 
            gc.anchor = 10;
            gc.fill = 1;   
     
            gc.gridy = 10; 
            gc.gridx = 11; 
            gb.setConstraints(speedSB, gc);     //speed scrollbar
     
            gc.gridy = 10;  
             gc.gridx = 3; 
             gb.setConstraints(sizeSB, gc);     //size sb
             
             gc.gridy = 10; 
             gc.gridx = 5; 
             gb.setConstraints(pause, gc);      //pause button
    
             gc.gridy = 10; 
             gc.gridx = 7; 
             gb.setConstraints(quit, gc);       //quit button
             
             gc.gridy = 10; 
             gc.gridx = 9;
             gb.setConstraints(run, gc);        //run button
             
             gc.gridy = 5; 
             gc.gridx = 11; 
             gb.setConstraints(speedL, gc);     //speed label
             
             gc.gridy = 5; 
             gc.gridx = 3; 
             gb.setConstraints(size, gc);       //size label
                    
             control.add(speedL); 
             control.add(speedSB); 
             control.add(pause); 
             control.add(run); 
             control.add(quit); 
             control.add(sizeSB);
             control.add(size); 
             
             sizeSB.addAdjustmentListener(this); 
             sizeSB.setMinimum(10);
         	 sizeSB.setMaximum(100);
         	 sizeSB.setUnitIncrement(10);
         	 sizeSB.setValue(10); 
             
             speedSB.addAdjustmentListener(this); 
             speedSB.setMinimum(10);
         	 speedSB.setMaximum(100);
         	 speedSB.setUnitIncrement(10);
         	 speedSB.setValue(speedVar); 
             
             pause.addActionListener(this);
             quit.addActionListener(this);  
             run.addActionListener(this); 
             
             pause.setLabel(" Pause "); 
             quit.setLabel(" Quit "); 
             run.setLabel(" Run ");
             run.setBackground(Color.BLUE); 
             quit.setBackground(Color.GRAY); 
             
             control.setLayout(gb);	
		
            c.setSize(xUnits/2);
            c.setSheet(sh, sw);
           

			   SizeScreen();
				Start();

				validate();

	}

	private void Start() {  //creates thread for program 
	   if(theThread == null){
         theThread = new Thread(this);
         theThread.start();
         c.repaint();
      }   
	}

	public void MakeSheet(){ //creates sheet, calculates sheet size
		 //environment is divided into 34x24 sections (unit = 1)

			xUnits = W/34;
			yUnits = H/24;

			     I = getInsets();
		    // sh & sw are sent to Bounce for correct canvas sizing
			    sh = H - I.top - I.bottom - (yUnits*6);
                sw = W - I.left - I.right - (xUnits*2);            

//calc your inc size 
}
	
	private void SizeScreen() { //function to calculate screen size
								//uses calculated Xunits & yUnits from MakeSheet() 
	        					//ensures the components will adjust correctly upon screen resize
		
					
			    this.setSize(W, H);
				this.setLocation((xUnits), (yUnits));
				      
			       c.setSize((sw), (sh));
				   c.setLocation(xUnits, (yUnits*3));
				   c.setSheet(sh, sw);	  
	}
	
	
	public static void main(String[] args){ //main function, creates new instance of bounce
		CannonVSBall cvb = new CannonVSBall(); 
	}

	@Override
	public void run() //activate and run thread
   {
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      while(theThread != null)
      {
         if(!stop) //run thread while program is not paused
         {
            
            c.setSheet(sw,sh);  // these functions are all in bounce class
            c.boundary();
            c.step();
            c.drawit(tails);           
            
            try
            {
               Thread.sleep(delay);
            }catch(InterruptedException e){}
         }
         else
         {
           System.out.print(""); //this print line allows for the program to successfully pause/start
           //we don't know why, but if you take it out it will never leave pause
         }
     
      }
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void componentResized(ComponentEvent ce) { //function to adjust components if window is resized
	
		H = getHeight();
		W = getWidth();
		
		System.out.println(H);
		System.out.println(W);
      
		MakeSheet();
		SizeScreen();
		//let the ball object know the change too 
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// Handles scroll bar events and updates accordingly (finished)
	public void adjustmentValueChanged(AdjustmentEvent ae) { //function for scrollbar adjustments

		Object src0 = ae.getSource(); //create scrollbar source object
      
      if(src0 == speedSB){ //if user moves speed scrollbar, adjust speed
		Scrollbar s0 = (Scrollbar)ae.getSource();
		int temp = ae.getValue();
		speedSB.setValue(temp);
		//c.setSpeed(temp);
      
      delay = (100 - ae.getValue())/8;
		} else{ //if user adjusts size scrollbar, adjust size
			Scrollbar s0 = (Scrollbar)ae.getSource();
			int temp = ae.getValue();
			sizeSB.setValue(temp);
         c.setSize(ae.getValue());
		}    		
  	}
   
   public void stop() //function to end program 
   {
       theThread = null;  
		 dispose();		
		 this.setVisible(false); 
   }
               
   //NOTE: Eclipse forces several functions to be implemented (all shown w/"@Override)
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent we) {
      stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void actionPerformed(ActionEvent ae) { //Function for action peformed 
		Object src1 = ae.getSource();
      
      if(src1 == pause) 
      {
        stop = true; 
        pause.setBackground(Color.BLUE);
        run.setBackground(Color.GRAY);  
      }
      
      if(src1 == run)
      {
        stop = false; 
        run.setBackground(Color.BLUE); 
        pause.setBackground(Color.GRAY); 
      }
      
      if(src1 == quit) 
      {
        stop(); 
      }
	}
   

   

}//end class Bounce

//------------------this class does all the drawing------------------------------------
class board extends Canvas implements MouseListener, MouseMotionListener//canvas class
{

private static final long serialVersionUID = 1L;

//rectangle vector
Vector <Rectangle> walls = new Vector<Rectangle>();


//declared variables for board class
 	  int x,y,w,h; 
 	  int down,right;
 	  int sw, sh;
 	  int speed = 5;
   Point m = new Point(0,0);
   Point r = new Point(0,0);
  boolean shape, isClear, tails;
  boolean dragbox = false;
 Graphics g;
 Image b;
   
  public void setSheet(int cw, int ch){ //function to set canvas sheet
  
  sw = cw;
  sh = ch;
  
  }

  public void setSize(int H) //function to set object size 
  {
    w = H*2;
    h = H*2;
  }
  
  public void setSpeed(int S){ //function to set object speed
	  
	  speed = S;
  }
  
  public board() //constructor for board class
  {
          x = 0;
          y = 0;
       down = 1;
      right = 1;
    isClear = false;
    this.addMouseListener(this);
    this.addMouseMotionListener(this);

    setBackground(Color.GRAY); 
 
  }
  
  public void boundary(){ //function to set boundary on canvas
      
      //frame boundaries
         if(y > sh - h){
               down = -1;
         }
         if(y < 0){
               down = 1;
         }
         if(x > sw - w){
               right = -1;
         }
         if(x < 0){
               right = 1;
         }
      
      //rectangle boundaries
      Rectangle b = new Rectangle(x,y,w,h);
      
      for(int i = 0; i< walls.size(); i++){
            if(b.intersects(walls.elementAt(i))){
            
            b = b.intersection(walls.elementAt(i));
            //left bound
            if( b.getX() == walls.elementAt(i).getX()){
                  
                  right = -1;
            }
            //right bound
            if( b.getX() + b.getWidth() == walls.elementAt(i).getX() + walls.elementAt(i).getWidth()){
                  
                  right = 1;
            }
            //top bound
            if( b.getY() == walls.elementAt(i).getY()){
                  
                  down = -1;
            }
            //bottom bound
            if( b.getY() + b.getHeight() == walls.elementAt(i).getY() + walls.elementAt(i).getHeight()){
                  
                  down = 1;
            }
      }
    } 
  }
  
  
  public void drawit(boolean t){ //function to place object
    tails = t;
    repaint();    
  }
  
  public void update(Graphics sg) //function to draw object 
  {
    b = createImage(sw,sh);   //next couple lines handles double buff
    if(g != null){
    
         g.dispose();
    }
    
    g = b.getGraphics();
    

    int p = 6;  // changes border 
    
    //draws oval
    g.setColor(Color.BLACK);
    g.fillOval(x,y,w,h);
    g.setColor(Color.BLUE);
    g.fillOval(x+w/p,y+h/p,w- 2*w/p,h-2*h/p);
    
    //rectangle stuff
    if (dragbox == true){
    
         g.setColor(Color.BLACK);
         g.drawRect(Math.min(m.x,r.x),Math.min(m.y,r.y),Math.abs(m.x-r.x),Math.abs(r.y-m.y));
         
    }
    for(int i = 0; i< walls.size(); i++){
    
         g.setColor(Color.BLACK);
         g.fillRect((int)walls.get(i).getX(),(int)walls.get(i).getY(),(int)walls.get(i).getWidth(),(int)walls.get(i).getHeight());
    
    
    }
     sg.drawImage(b,0,0,null);
  }
  
  public void step(){ //function to move object across the screen
  
      x = x + right;
      y = y + down;
  }
  
  
  
      public void mousePressed(MouseEvent e)
   {
     
     m.x = e.getX();
     m.y = e.getY(); 
     repaint();
   }
  
   public void mouseReleased(MouseEvent e)
   {
     r.x = e.getX(); 
     r.y = e.getY();
     repaint();
     dragbox = false;
     Rectangle a  = new Rectangle(Math.min(m.x,r.x),Math.min(m.y,r.y),Math.abs(m.x-r.x),Math.abs(r.y-m.y));
     
     //prevents inappropriate drawing of walls
     if( a.intersects(new Rectangle(x,y,w,h)) ||  r.x > sw  || r.y > sh  || r.x < 0  || r.y < 0){
     
     }
     else{
     
       walls.addElement(a);
     
     }
   }
   
   public void mouseExited(MouseEvent e)
   {
   
   }
   
   public void mouseEntered(MouseEvent e)
   {
   
   }
   
   public void mouseClicked(MouseEvent e)
   {
    Rectangle d  = new Rectangle(e.getX(),e.getY(),1,1);
    
    
    //removes walls 
    int i = 0;
    while (i < walls.size()){
    
            if ( d.intersects(walls.elementAt(i))){
            
                  walls.remove(i);
            }else{
               i++;
            }
    }
   
    
   }
   
   public void mouseMoved(MouseEvent e)
   {
   
   }
   
   public void mouseDragged(MouseEvent e)
   {
     dragbox = true;
   
     r.x = e.getX();
     r.y = e.getY(); 
     repaint();
   } 
  
} //end class board */
