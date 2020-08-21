package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.Random;
import java.util.*;						
import java.lang.Math;


class Coordinate {
	public final int X;
	public final int Y;

	public Coordinate(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public int getDirectionTo(Coordinate neighbor){
		if (neighbor.Y < this.Y) {
			return MyAgentState.NORTH;
		}
		else if (neighbor.Y > this.Y) {
			return MyAgentState.SOUTH;
		}
		else if (neighbor.X < this.X) {
			return MyAgentState.WEST;
		}
		else if (neighbor.X > this.X) {
			return MyAgentState.EAST;
		}
		return -1;
	}

	@Override
	public int hashCode() {
		return 256000*this.X + this.Y;
	}

	@Override
	public boolean equals(Object obj) {
		return this.X == ((Coordinate)obj).X && this.Y == ((Coordinate)obj).Y;
	}

	@Override
	public String toString() {
		return "X: " +this.X	+ " Y: " +this.Y;
	}
}

class MyAgentState
{
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;

	Queue<Action> actionQueue = new LinkedList<Action>();
	ArrayList<Coordinate> route = new ArrayList<Coordinate>();
	boolean isGoingHome = false;
	
	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
	    {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
	    }
		
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}
	
	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initialRandomActions = 10;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public MyAgentState state = new MyAgentState();
	public int iterationCounter = state.world.length * state.world[0].length * 4; // set to *4 as a node can go in 4 directions
	
	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction<0) 
		    	state.agent_direction +=4;
		    state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
		    state.agent_last_action = state.ACTION_TURN_RIGHT;
		    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		} 
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	
	@Override
	public Action execute(Percept percept) {
		
		// DO NOT REMOVE this if condition!!!
    	if (initialRandomActions >0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initialRandomActions ==0) {
    		// process percept for the last step of the initial random actions
    		initialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}
		
    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
    	
    	System.out.println("Iterations left = " + iterationCounter);
    	System.out.println("agent x position = " + state.agent_x_position);
    	System.out.println("agent y position = " + state.agent_y_position);
    	System.out.println("agent direction = " + state.agent_direction);

	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);
	    
	    // State update based on the percept value and the last action
	    state.updatePosition((DynamicPercept)percept);
	    if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
	    }
	    if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
	    state.printWorldDebug();

		if (dirt) {
			return suckDirt();
		}

		if (state.actionQueue.isEmpty() && state.route.isEmpty()) {
			state.route = calculatePath();
			if (state.route == null) { // no more nodes to find
				return doNothing();
			}
		}

		if (state.actionQueue.isEmpty()) {
			// Dequeue Coordinate in current route
			Coordinate nextCoordinate = state.route.remove(state.route.size()-1);
			Coordinate currentCoordinate = new Coordinate(state.agent_x_position, state.agent_y_position);

			int direction = currentCoordinate.getDirectionTo(nextCoordinate);
			switch (direction){
				case MyAgentState.EAST:
					queueMoveEast();
					break;
				case MyAgentState.WEST:
					queueMoveWest();
					break;
				case MyAgentState.NORTH:
					queueMoveNorth();
					break;
				case MyAgentState.SOUTH:
					queueMoveSouth();
					break;
				default:
					break;
			}
		}

		if (!state.actionQueue.isEmpty()) {
			return doAction(state.actionQueue.remove());
		}

	    return doNothing();
	}

	private Action doAction(Action action) {
		if (action == LIUVacuumEnvironment.ACTION_TURN_LEFT) {
			return turnLeft();
		}
		else if (action == LIUVacuumEnvironment.ACTION_TURN_RIGHT) {
			return turnRight();
		} else {
			return moveForward();
		}
	}

	// BFS to find route to any unknown node
	private ArrayList<Coordinate> calculatePath() {
		Map<Coordinate, Coordinate> parentLinks = new HashMap<Coordinate, Coordinate>();
		Queue<Coordinate> frontier = new LinkedList<Coordinate>();

		Coordinate startingCoordinate = new Coordinate(
				state.agent_x_position,
				state.agent_y_position);
		
//    	if(!searchVisitedMap(startingCoordinate)) {
//		addVisitedMap(startingCoordinate);
//	}

		frontier.add(startingCoordinate);
		parentLinks.put(startingCoordinate, null);

		while (!frontier.isEmpty()) {
			Coordinate currentNode = frontier.remove();
			int currX = currentNode.X;
			int currY = currentNode.Y;
			// build route to this node if unknown and in bounds
			if (currX >= 0 && currY >= 0 && state.world[currX][currY] == state.UNKNOWN) {
				return BuildPath(currentNode, parentLinks);
			}

			// only check vertically and horizontally
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {

					Coordinate neighbor = new Coordinate(currX + dx, currY + dy);
					if (Math.abs(dx) != Math.abs(dy) && !parentLinks.containsKey(neighbor)
							&& state.world[neighbor.X][neighbor.Y] != state.WALL) {
						parentLinks.put(neighbor, currentNode);
						frontier.add(neighbor);
					}
				}
			}
		}
		return null;
	}
	
//	tried to add in isVisited to make the algorithm more optimal
//  basically it is to make the vacuum cleaner will not visit the same node twice
	
//	//Search if current position is visited
//	private boolean searchVisitedMap(Coordinate currentCoordinate) {
//		if(visited!=null && visited.containsKey(currentCoordinate)) {
//			return true;
//		}
//		return false;
//	}
//	
//	//Add current position to visited
//	private void addVisitedMap(Coordinate currentCoordinate) {
//		visited.put(currentCoordinate,true);
//	}

	private ArrayList<Coordinate> BuildPath(Coordinate goalNode, Map<Coordinate, Coordinate> parentLinks) {
		ArrayList<Coordinate> route = new ArrayList<Coordinate>();
		route.add(goalNode);

		Coordinate currentCoordinate = goalNode;
		while ((currentCoordinate = parentLinks.get(currentCoordinate)) != null) {
			route.add(currentCoordinate);
		}
		// Remove start node since we don't want to go to start from start
		route.remove(route.size()-1);
		return route;
	}
	
	private void queueMoveNorth() {
		switch (state.agent_direction) {
			case MyAgentState.EAST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.SOUTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.WEST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_RIGHT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			default:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
		}
	}
	
	private void queueMoveSouth() {
		switch (state.agent_direction) {
			case MyAgentState.WEST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.NORTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.EAST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_RIGHT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			default:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
		}
	}

	private void queueMoveEast() {
		switch (state.agent_direction) {
			case MyAgentState.SOUTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.WEST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.NORTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_RIGHT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			default:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
		}
	}

	private void queueMoveWest() {
		switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.EAST:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_LEFT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			case MyAgentState.SOUTH:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_TURN_RIGHT);
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
			default:
				state.actionQueue.add(LIUVacuumEnvironment.ACTION_MOVE_FORWARD);
				break;
		}
	}
	
	private Action turnLeft() {
		state.agent_direction = ((state.agent_direction-1) % 4);
		if (state.agent_direction<0) 
		    	state.agent_direction +=4;
	    state.agent_last_action = state.ACTION_TURN_LEFT;
	    return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	}

	private Action turnRight() {
		state.agent_direction = ((state.agent_direction+1) % 4);
	    state.agent_last_action = state.ACTION_TURN_RIGHT;
	    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	}

	private Action moveForward() {
		state.agent_last_action = state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}

	private Action suckDirt() {
		System.out.println("DIRT -> choosing SUCK action!");
	    state.agent_last_action=state.ACTION_SUCK;
	    return LIUVacuumEnvironment.ACTION_SUCK;
	}

	private Action doNothing() {
		state.agent_last_action=state.ACTION_NONE;
		return NoOpAction.NO_OP; 
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}