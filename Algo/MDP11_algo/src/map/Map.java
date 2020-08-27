package map;

import java.awt.Color;
import java.awt.*;
import javax.swing.*;

import java.util.*;

public class Map extends JPanel{
	
	private ArrayList<ArrayList<Cell>> cells;
	private ArrayList<Cell> rows;
	
	private final Cell[][]grid;
	
	public Map() {
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(120, 50, 450, 600);
		
		grid = new Cell[20][20];
		
		for(int j = 0; j <= 19; j++) {
			for(int i = 0; i <= 19; i++) {
				grid[i][j] = new Cell(i,j);
//				this.rows.add(new Cell(i,j));
//				this.add(new Cell(i, j));
				
			}
//			this.cells.add(rows);
		}
		
//		Cell obstacle = findCell(7,10);
//		obstacle.setCellType(Cell.CellType.OBSTACLE);;
	}
	
	public Cell findCell(int xPos, int yPos) {
		return this.cells.get(yPos).get(xPos);
	}
}
