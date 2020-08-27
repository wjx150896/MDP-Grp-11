package Simulator;

import java.awt.Color;
import javax.swing.*;
import java.util.*;

public class Map extends JPanel{
	
	private ArrayList<ArrayList<Cell>> cells;
	private ArrayList<Cell> rows;
	
	public Map() {
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(120, 50, 450, 600);
		
		for(int j = 0; j <= 19; j++) {
			for(int i = 0; i <= 19; i++) {
				this.rows.add(new Cell(i,j));
				this.add(new Cell(i, j));
			}
			this.cells.add(rows);
		}
		
		Cell obstacle = findCell(7,10);
		obstacle.setCellType(Cell.CellType.OBSTACLE);;
	}
	
	public Cell findCell(int xPos, int yPos) {
		return this.cells.get(yPos).get(xPos);
	}
}
