package simulator;
import map.Map;
import javax.swing.*;

import map.Map;

import java.awt.*;


public class Simulator {
	private static JFrame appFrame = null;
	private static JPanel mapCards = null;
	private static JPanel buttons = null;
	
	private static Map map = null;
	
	private static void __run__() {
		// create app frame
		appFrame = new JFrame();
		appFrame.setTitle("Group 11 Simulator");
		appFrame.setSize(new Dimension(690,700));
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		appFrame.setLocation(dimension.width / 2 - appFrame.getSize().width /2, dimension.height /2 - appFrame.getSize().height / 2);
	
		buttons = new JPanel();
		mapCards = new JPanel(new CardLayout());
		
		map = new Map();
		
       	appFrame.getContentPane().add(buttons, BorderLayout.PAGE_START); 
     	appFrame.getContentPane().add(mapCards, BorderLayout.CENTER );
      	
       	__iniButton__();
       	
       	__iniMap__();
        
        appFrame.setVisible(true);
        
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
	public static void __iniMap__() {
		
		mapCards.add(map, "map");
		CardLayout cl = ((CardLayout) mapCards.getLayout());
		cl.show(mapCards, "map");
		
	}
	
	//initialise buttons
	private static void __iniButton__() {
		buttons.setLayout(new GridLayout());
		__addButtons__();
	}
	
	private static void __format__(JButton button) {
		button.setFont(new Font("Arial",Font.BOLD,10));
		button.setFocusPainted(false);
		}
	
	private static void __addButtons__() {
		__loadMapButton__("Load Map");
		
		__loadExplorationButton__("Exploration-Hug");
		
		__loadExplorationButton__("Exploration-BFS");
		
		__loadFastestPathButton__("Fastest Path");
	}
	
	private static void __loadMapButton__(String name) {
		JButton button_LoadMap = new JButton(name);
		__format__(button_LoadMap);
		// need to add method to create map here and load various map in
		buttons.add(button_LoadMap);
	}
	
	private static void __loadExplorationButton__(String name) {
		JButton button_Exploration_ = new JButton(name);
		__format__(button_Exploration_);
		// need to add method to create exploration algo
		buttons.add(button_Exploration_);
	}
	
	private static void __loadFastestPathButton__(String name) {
		JButton button_FastestPath_ = new JButton(name);
		__format__(button_FastestPath_);
		// need to add method to create fastest path algo
		buttons.add(button_FastestPath_);
	}
	
	public static void main(String[] args) {
		__run__();
	}
	
	
}
