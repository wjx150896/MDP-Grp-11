package map;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel {
	
	//Enum for cell types which will help in color coding and simulation later on
	enum CellType{
		UNEXPLORED,
		EXPLORED,
		STARTZONE,
		GOALZONE,
		OBSTACLE
	}
	
	private CellType cellType = CellType.UNEXPLORED;
	private int xPos;
	private int yPos;
	
	public Cell(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		
		//Preset some cell types based on their x-coordinate and y-coordinate
		if(this.xPos == 0 || this.xPos == 14 || this.yPos == 0 || this.yPos == 19) {
			this.cellType = CellType.OBSTACLE;
		}
		else if(this.xPos <= 2 && this.yPos <= 2) {
			this.cellType = CellType.STARTZONE;
		}
		else if(this.xPos >= 12 && this.yPos >= 17) {
			this.cellType = CellType.GOALZONE;
		}
		
		//Set border of cell to black to enhance visibility of each individual cell
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Set color of cell based on their cell type
		switch(this.cellType) {
		case UNEXPLORED:
			this.setBackground(Color.yellow);
			break;
		case EXPLORED:
			this.setBackground(Color.white);
			break;
		case STARTZONE:
			this.setBackground(Color.red);
			break;
		case GOALZONE:
			this.setBackground(Color.green);
			break;
		case OBSTACLE:
			this.setBackground(Color.black);
			break;
		}
		
		//Set position of cell based on their x-coordinate and y-coordinate
		this.setBounds(this.xPos * 3, this.yPos * 3, 3, 3);
	}
	
	
	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}
	
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}
	
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}
	
	public CellType getCellType() {
		return this.cellType;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getyPos() {
		return this.yPos;
	}
}
