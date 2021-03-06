package Editor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Entities.Block;
import Entities.Orientation;
import General.Helpers;
import General.Helpers.EntityID;

public class LevelEditor extends JLabel implements MouseListener {
	
	private BufferedImage board; 
	
	//stores where a click began.  to be used to
	private Point clickStart;
	
	private List <Block> entitiesInLevel;

	public LevelEditor (Dimension dim) {
		
		this.board = new BufferedImage (dim.width, dim.height, BufferedImage.TYPE_3BYTE_BGR);
		this.entitiesInLevel = new ArrayList <Block> ();
		addMouseListener(this);
		this.setIcon(new ImageIcon(board));
		this.setSize(dim);
		this.setVisible(true);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
				
		//drawWhiteBox(Helpers.round(arg0.getX()), Helpers.round(arg0.getY()), 10, 10);
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.clickStart = e.getPoint();
	}
	
	/**
	 * Draws an image to board
	 * @param img the image that is to be drawn
	 * @param x Starting x pos
	 * @param y Starting y pos
	 */
	public void drawImg (BufferedImage img, int x, int y) {
		
		for (int i = 0; i < img.getWidth(); i++) {
			
			for (int j = 0; j < img.getHeight(); j++) {
				board.setRGB(i+x, j+y, img.getRGB(i, j));
			}
			
		}
		
		repaint();
		revalidate();
		
	}
	
	/**
	 * Draws a white box to board
	 * @param x Start x
	 * @param y Start y
	 * @param width width of box
	 * @param height height of box
	 */
	public void drawWhiteBox (int x, int y, int width, int height) {
		
		Graphics2D curImage = this.board.createGraphics();
		curImage.setColor(Color.WHITE);
		curImage.fillRect(x,y,width,height);
		curImage.dispose();
		
		repaint();
		revalidate();
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		BufferedImage img = Block.getImageData(Helpers.EntityID.redX);
	
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		
		Point clickRelease = e.getPoint();
		
		int startX = Helpers.round((int)this.clickStart.getX());
		int startY = Helpers.round((int)this.clickStart.getY());
		int endX = Helpers.round((int)clickRelease.getX());
		int endY = Helpers.round((int)clickRelease.getY());
		
		Orientation dir;
		
		if (Math.abs(endX-startX) > Math.abs(endY-startY)) {
			dir = Orientation.HORI;
		}
		
		//only allow changes in one dimension
		else if (Math.abs(endX-startX) < Math.abs(endY-startY)) {
			dir = Orientation.VERT;
		}
		
		else {
			dir = Orientation.NONE;
		}
		
		Block newEntity = new Block (new Point(startX, startY), new Point (endX, endY), new Dimension (imgWidth, imgHeight), dir, Helpers.EntityID.redX);
		entitiesInLevel.add(newEntity);
		
		Point start = newEntity.getStartPos();
		Dimension dim = newEntity.getEntitySize();
		
		int curX = start.x;
		int curY = start.y;
		
		//continue drawing while there is a new block to draw
		for (int i = 0; i <= newEntity.getTimesRepeat(); i++) {
			
			//draw image, and then reset the drawing flag
			drawImg(img, curX, curY);
			
			curX += dir.equals(Orientation.HORI) ? dim.width : 0;
			curY += dir.equals(Orientation.VERT) ? dim.height : 0;
			
		} 
		
		repaint();
		revalidate();
		
	}
	
	public void saveEntities (File path) {
		
		PrintWriter output = null;
		
		try {
			output = new PrintWriter (path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Block curEnt: entitiesInLevel) {
			
			String curLine = curEnt.saveInfo() + "\r";
			output.write(curLine);
			
		}
		
		output.flush();
		output.close();
		
	}
	
}
