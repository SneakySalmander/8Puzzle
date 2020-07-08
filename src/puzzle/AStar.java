package puzzle;

import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;

/*
* Given a certain configuration of an 8-Puzzle input as a String
* AStar will solve it and return the amount of nodes produced in 
* the solution. It can solve the puzzle with two heuristics.
* (1) The Hamming Heuristic or (2) The Manhattan Heuristic
*/
class AStar 
{    
    private EightPuzzle initial_state;
    private String state;
    private int number_of_nodes;//Displayed w/Solution
    private int number_of_steps;//Displayed w/Solution
    
    /*Default Constructor (when AStar is called without passing an initial state)
     * This never occurs in my implementation but I included it for good practice.*/
    public AStar() 
    {
    	this.state = "012345678";
    	this.number_of_nodes = 0;
    	this.number_of_steps = 0;
    }//end Default Constructor
    
    public AStar(String puzzle_layout)
	{
        this.state = puzzle_layout;
        this.number_of_nodes = 0;
        this.number_of_steps = 0;
    }//end Constructor
    
    public int solveHamming(boolean use_explored_set, int target_depth)
    {
        return this.applyAStar(1, use_explored_set, target_depth);
    }//end solveHamming
    public int solveManhattan(boolean use_explored_set, int target_depth)
    {
        return this.applyAStar(2, use_explored_set, target_depth);
    }//end solveManhattan
    private int applyAStar(int heuristic, boolean use_explored_set, int target_depth)
	{
    	//Must be reset since I run algorithm more than once in Main.java
        this.number_of_nodes = 0;
        
        //Redefinition of Comparator is required to order elements in PriorityQueue
        Comparator<EightPuzzle> comparator = new Comparison();
        // PriorityQueue is used as the frontier in A* algorithm
        PriorityQueue<EightPuzzle> frontier = new PriorityQueue<>(10, comparator);
        // HashMap is used to keep track of nodes expanded by the A* algorithm
        HashMap<String, EightPuzzle> explored_set = new HashMap<>();
		//Initialize EightPuzzle using the selected initial state and chosen heuristic
        this.initial_state = new EightPuzzle(this.state, heuristic);
		
        frontier.add(this.initial_state);//Pushes initial state onto PriorityQueue
        
        //Beginning of A* Algorithm
        while(frontier.size() > 0)
		{
			//Always removes state node with lowest value from f(n) = g(n) + h(n)
            EightPuzzle puzzle = frontier.remove();
            
            //This block was only used for generating data for the project report table
            if((!use_explored_set && puzzle.getDepth() > 20)) //|| puzzle.getDepth() > target_depth) 
            {
            	System.out.println("Depth of Solution Exceeds 24 or Target Depth. Moving on to next puzzle.");
            	return 0;
            }//end if
            
            if(puzzle.inGoalState())//Checks to see if current state of node is the goal state
			{
                int steps = -1;//Start at -1 to avoid counting initial state			
				//Stack allowing a backtracking algorithm to print nodes
				Stack<EightPuzzle> path = new Stack<>();
                while(puzzle != null)
				{
                    steps++;
					path.push(puzzle);
                    puzzle = puzzle.getPrevious();
				}//end while
                while (path.size() > 1) 
				{
            		path.pop().printPuzzle(); 
					System.out.println("\nNext State:");
                }//end while
                path.pop().printPuzzle();
                this.number_of_steps = steps;
                return this.number_of_nodes;
            }//end if     
            // Create an EightPuzzle if the current puzzle's empty were to move up
            EightPuzzle temp_eight_puzzle = puzzle.shiftSpaceUp();
            this.checkExplorationStatus(explored_set, temp_eight_puzzle, frontier);
            // Create an EightPuzzle if the current puzzle's empty were to move down
            temp_eight_puzzle = puzzle.shiftSpaceDown();
            this.checkExplorationStatus(explored_set, temp_eight_puzzle, frontier);
            // Create an EightPuzzle if the current puzzle's empty were to move left
            temp_eight_puzzle = puzzle.shiftSpaceLeft();
            this.checkExplorationStatus(explored_set, temp_eight_puzzle, frontier);
            // Create an EightPuzzle if the current puzzle's empty were to move right
            temp_eight_puzzle = puzzle.shiftSpaceRight();
            this.checkExplorationStatus(explored_set, temp_eight_puzzle, frontier);
            
            if(use_explored_set)//Toggles the use of explored set in algorithm
            {	
	            //Add "explored" EightPuzzle to HashMap "explored set"
	            explored_set.put(puzzle.getPuzzleVals(), puzzle);
            }//end if
        }//end while    
        //Returns 0 if search fails to find a solution
        return 0;
    }//end applyAStar
    
    private void checkExplorationStatus(HashMap<String, EightPuzzle> explored_set, EightPuzzle temp_eight_puzzle, 
    								 	PriorityQueue<EightPuzzle> frontier) 
    {
    	if (temp_eight_puzzle != null)
        {
          this.number_of_nodes++;
          if(!explored_set.containsKey(temp_eight_puzzle.getPuzzleVals())) 
          {
        	  frontier.add(temp_eight_puzzle);
          }//end if
        }//end if
    }//end checkExplorationStatus
    
    public int getNumSteps()
    {
      return this.number_of_steps;
    }//end getNumSteps
    
    public int getNumNodes()
    {
    	return this.number_of_nodes;
    }//end getNumNodes
    
    /* Allows PriorityQueue to sort the nodes in the state space tree by their 
    * A* algorithm value denoted by f(n) = g(n) + h(n) where g(n) is the depth of 
    * the node and h(n) is heuristic value of the node */
    private class Comparison implements Comparator<EightPuzzle> 
    {
        @Override
        public int compare(EightPuzzle first_puzzle, EightPuzzle second_puzzle)
    	{
    		//Return -1 if first EightPuzzle is less than second EightPuzzle
            if (first_puzzle.getAStarVal() < second_puzzle.getAStarVal())
            {
                return -1;
            }//end if
    		//Return 1 if EightPuzzle is greater than second EightPuzzle
            else if (first_puzzle.getAStarVal() > second_puzzle.getAStarVal())
            {
                return 1;
            }//end else if
    		//Return 0 when EightPuzzles are equal
            else
    		{
    			return 0;
    		}//end else
        }//end compare
    }//end Comparison
}//end AStar