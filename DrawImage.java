import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;


class DrawImage extends JPanel {
	public double  height = 1080;
	public double  width = 1920;
	long zoom = 1;
	double xOffset = 0;
	double yOffset = 0;
	double tempxOffset = xOffset;
	double tempyOffset = yOffset;
	boolean hasZoomed = false;
	int count = 0;
	int startingX;
	int startingY;
	int currentX;
	int currentY;
	int endX;
	int endY;
	boolean reDrawMandel = false;
	int cornerX = 0, cornerY = 0;
	double rectWidth = 0, rectHeight = 0;
	int maxIteration = 5000;
	double tempZoom = 0;
	BufferedImage nextZoom = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
	Image currentRender;

	private void doDrawing(Graphics g) {


		System.out.println("Zoom level = " + zoom);
		count=0;
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.BLACK);
		//		if(startingX < currentX){
		//			cornerX = startingX;
		//			rectWidth = currentX - startingX;
		//		}
		//		if(startingX > currentX){
		//			cornerX = currentX;
		//			rectWidth = startingX - currentX;
		//		}
		//		if(startingY < currentY){
		//			cornerY = startingY;
		//			rectHeight = currentY - startingY;
		//		}
		//		if(startingY > currentY){
		//			cornerY = currentY;
		//			rectHeight = startingY - currentY;
		//		}
		//		rectHeight = rectWidth*(height/width);

		//System.out.println("drawing rectangle at: "+cornerX + ", " + cornerY + "   width: "+ rectWidth + " height: " + rectHeight);

		g2d.drawImage(currentRender, 0, 0, this);
		System.out.println("finished render");
	}




	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		if(zoom < tempZoom || zoom > tempZoom){
			System.out.println("RECALCULATING");
			reCalculateMandelbrot();
      cornerX = 0;
      cornerY = 0;
      rectWidth=0;
      rectHeight=0;
		}

		doDrawing(g);
		count = 0;
		tempxOffset = xOffset;
		tempyOffset = yOffset;
		System.out.println("ORIGINAL RENDER");



		JButton zoomOut = new JButton("-");


		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				currentX = e.getX();
				currentY = e.getY();
				count++;
				if(count <= 1){
					int xCoOrd = (int) ((int)  cornerX+(rectWidth/2));
					int yCoOrd = (int) ((int)  cornerY+(rectHeight/2));
					xOffset = tempxOffset+((xCoOrd - width/2.0)*4.0/width/zoom);
					yOffset = tempyOffset+((yCoOrd - height/2.0)*4.0/width/zoom);
					zoom = (long)(width*zoom/rectWidth);
					//maxIteration *= zoom*0.5;
					System.out.println("Maxiterations: "+ maxIteration);
					System.out.println("mouse clicked at x: " + xCoOrd + "    y:" + yCoOrd);
					System.out.println("            height: " + height + "  width:" + width);
					System.out.println("OFFSET:          x: " + xOffset + "     y:" + yOffset);
					System.out.println("zoom: " + zoom);
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				startingX = e.getX();
				startingY = e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});


		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
//				currentX = e.getX();
//				currentY = e.getY();
//				System.out.println("Current X, Y: " + currentX + ", " + currentY);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				currentX = e.getX();
				currentY = e.getY();
				repaint();
			}
		});


		zoomOut.setBounds(50, 50, 100, 100);

		JButton reset = new JButton("Reset");
		reset.setBounds(170,50,100,100);
		removeAll();
		add(zoomOut);
		add(reset);

		rectWidth = currentX - startingX;
		cornerX = startingX;
		cornerY = startingY;

		rectHeight = rectWidth*(height/width);

		//System.out.println("drawing rectangle at: "+cornerX + ", " + cornerY + "   width: "+ rectWidth + " height: " + rectHeight);
		g.drawRect(cornerX, cornerY, (int)rectWidth, (int)rectHeight);

		zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("-");
				zoom*=0.5;
				repaint();

			}
		});
		reset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("RESETTING");
				zoom=1;
				maxIteration = 500;
				xOffset = 0;
				yOffset = 0;
        rectHeight=0;
        rectWidth=0;
				repaint();
			}
		});


	}

	public void reCalculateMandelbrot(){
    Thread thread1 = new Thread(new Runnable() {
		@Override
			public void run() {
        reCalculatePart(0, (int)width/2 + 1, 0, (int)height/4);
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
				public void run() {
        reCalculatePart(0, (int)width/2 + 1, (int)height/4, (int)height/2);
			}
		});
		Thread thread3 = new Thread(new Runnable() {
			@Override
			public void run() {
        reCalculatePart(0, (int)width/2 + 1, (int)height/2, (int)height*3/4);
			}
		});
		Thread thread4 = new Thread(new Runnable() {
			@Override
			public void run() {
        reCalculatePart(0, (int)width/2 + 1, (int)height*3/4, (int)height);
			}
		}); 
    Thread thread5 = new Thread(new Runnable() {
		@Override
			public void run() {
        reCalculatePart((int)width/2, (int)width, 0, (int)height/4);
			}
		});
		Thread thread6 = new Thread(new Runnable() {
			@Override
				public void run() {
        reCalculatePart((int)width/2, (int)width, (int)height/4, (int)height/2);
			}
		});
		Thread thread7 = new Thread(new Runnable() {
			@Override
			public void run() {
        reCalculatePart((int)width/2, (int)width, (int)height/2, (int)height*3/4);
			}
		});
		Thread thread8 = new Thread(new Runnable() {
			@Override
			public void run() {
        reCalculatePart((int)width/2, (int)width, (int)height*3/4, (int)height);
			}
		}); 
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
		thread6.start();
		thread7.start();
		thread8.start();
		
		while(thread1.isAlive() == true || thread2.isAlive() == true || thread3.isAlive() == true || thread4.isAlive() == true){
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// this wont be called as the thread will not be interrupted
			}
    }

		currentRender = nextZoom;
		tempZoom = zoom;
		System.out.println("recalculated");
	}

  private void reCalculatePart(int rStart, int rEnd, int cStart, int cEnd) {
		for (int row = rStart; row < rEnd-1; row++) {
			for (int col = cStart; col < cEnd; col++) {
				double c_re = ((row - width/2.0)*4.0/width/zoom)+xOffset;
				double c_im = ((col - height/2.0)*4.0/width/zoom)+yOffset;
				double zx = 0.0;
				double zy = 0.0;
				double zx2 = 0.0;
				double zy2 = 0.0;

				int iteration = 0;
				while (zx*zx+zy*zy < 4 && iteration < maxIteration) {
					double x_new = zx*zx - zy*zy + c_re;
					zy = 2*zx*zy + c_im;
					zx = x_new;
					zx2 = zx * zx;
					zy2 = zy * zy;
					iteration++;
				}
				double zn = Math.sqrt(zx2+zy2);
				float nsmooth = (float) (iteration + 1 - Math.log(Math.log(Math.abs(zn)))/Math.log(2));

				Color rgb = new Color(Color.HSBtoRGB(0.95f + 10 * (nsmooth/maxIteration) , (float) 0.75f , 1f));
				if (iteration < maxIteration){
					nextZoom.setRGB(row, col, rgb.getRGB());
				}
				else{
					nextZoom.setRGB(row, col, 0);
				}
			}
		}
  }
}
