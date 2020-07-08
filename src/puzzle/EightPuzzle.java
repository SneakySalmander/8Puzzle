package puzzle;

import java.util.Random;

class EightPuzzle 
{
    private StringBuffer puzzle_vals;	//StringBuffer must be used since Strings are immutable
    private EightPuzzle previous_state;	//Predecessor of current state
    private int heuristic;				//1 = Hamming, 2 = Manhattan   
    private int depth_of_state;  		//Level/Depth of current state
    private int aStarVal;				//f(n) = g(n) + h(n)
    private int empty_index;			//Location of the empty space
    
    public EightPuzzle(String puzzle_layout, int h) 
    {
        // call private constructor with the depth_of_state of 0 and previous_state as null
        this(puzzle_layout, 0, null, h);
    }//end public constructor
    
    private EightPuzzle(String puzzle_layout, int d, EightPuzzle p, int h)
    {
        // initialize puzzle_vals and fill the board with the puzzle layout passed
        this.puzzle_vals = new StringBuffer(puzzle_layout);
        
        // determine where the empty index is
        for (int i = 0; i < 9; i++) 
        {
            // check each slot for the value of '0'
            if (this.puzzle_vals.charAt(i) == '0')
            {
                this.empty_index = i;
                break;
            }//end if
        }//end for 
        this.heuristic = h; 		// set heuristic to argument passed
        this.depth_of_state = d;    // set depth_of_state to argument passed 
        this.previous_state = p;        	// set previous_state to argument passed
        this.setAStarVal();        	// initialize A* value
    }//end private constructor
    
    //Calculates f(n) = g(n) + h(n) depending on the heuristic used with A*
    private void setAStarVal()
    {
        if(this.heuristic == 1)//Hamming Distance
        {
            this.aStarVal = this.depth_of_state + this.calculateHamming();
        }//end if
        else//Manhattan Distance
        {
            this.aStarVal = this.depth_of_state + this.calculateManhattan();
        }//end else
    }//end setAStarVal
    private int calculateHamming() 
    {
        int number_of_misplaced_tiles = 0;
        // Loop through every tile to check if it is misplaced
        for (int currentIndex = 0; currentIndex < 9; currentIndex++) 
        {
        	//If index doesn't equal value in puzzle (excluding '0' space)
            if ((currentIndex + 48) != this.puzzle_vals.charAt(currentIndex) 
            	&&  this.puzzle_vals.charAt(currentIndex) != '0')
            {
                number_of_misplaced_tiles++;//match found
            }//end if
        }//end for
        return number_of_misplaced_tiles;
    }//end calculateHamming
    private int calculateManhattan() 
    {
        int manhattan_distance = 0;
        
        /*Thank you professor for writing this algorithm on the board in 
         * class on Monday 2/17. I was stuck here and the explanation from
         * class helped me a lot. Specifically the difference between what
         * "/" does and what "%" does in this context.*/
        for (int currentIndex = 0; currentIndex < 9; currentIndex++) 
        {
        	if((currentIndex + 48) != this.puzzle_vals.charAt(currentIndex) && 
        		this.puzzle_vals.charAt(currentIndex) != '0')
        	{
	            int index_row = currentIndex / 3;
	            int index_col = currentIndex % 3;
	            int value_row = (int) (this.puzzle_vals.charAt(currentIndex) - 48) / 3;//ASCI Conversion / 3;
	            int value_col = (int) (this.puzzle_vals.charAt(currentIndex) - 48) % 3;//ASCI Conversion % 3;
	            manhattan_distance += Math.abs(index_row - value_row) + Math.abs(index_col - value_col);
        	}//end if
        }//end for
        return manhattan_distance;
    }//end calculateManhattan

    public boolean inGoalState()
    {
        return this.puzzle_vals.toString().equals("012345678");
    }//end inGoalState
  
    public boolean isSolvable() 
    {
        int number_of_inversions = 0;
        for (int i = 0; i < 8; i++) 
        {
            //'0' represents the empty tile
            if (this.puzzle_vals.charAt(i) != '0') 
            {
                for (int j = i + 1; j < 9; j++) 
                {
                    if (this.puzzle_vals.charAt(j) != '0' && (int) this.puzzle_vals.charAt(i) > (int) this.puzzle_vals.charAt(j)) 
                    {
                        number_of_inversions++;
                    }//end if
                }//end inner for
            }//end if
        }//end outer for
        //An even number of inversions  means the initial state is solvable
        return number_of_inversions % 2 == 0;
    }//end isSolvable

    /*Generates the random puzzle for user by randomly moving the empty_space
     * n times in one of 4 possible directions.*/
    public void shuffleTiles(int number_of_shuffles) 
    {
        Random rand = new Random();
        int direction_to_move;

        for (int i = 0; i < number_of_shuffles; i++) 
        {
            direction_to_move = rand.nextInt(4);
            // up = 0, left = 1, right = 2, down = 3
            if(direction_to_move == 0)//empty space is moved up
            {
	            //make sure empty_index isn't already in top row
	            if (this.empty_index > 2) 
	            {
	                // to move "up", swap the empty tile with the character 3 tiles before the empty
	                this.puzzle_vals.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index - 3));
	                this.empty_index -= 3;
	                this.puzzle_vals.setCharAt(this.empty_index, '0');
	            }//end if
            }//end if
            else if(direction_to_move == 1)
            {	
            	//empty space is moved left
	            //make sure empty_index isn't already in left column
	            if (this.empty_index % 3 != 0) 
	            {
	                this.puzzle_vals.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index - 1));
	                this.empty_index--;
	                this.puzzle_vals.setCharAt(this.empty_index, '0');
	            }//end if
        	}//end else if
            else if (direction_to_move == 2)//empty space is moved right
            {
		        //make sure empty_index isn't already in right column
		        if (this.empty_index % 3 != 2) 
		        {
		            this.puzzle_vals.setCharAt(this.empty_index, this.puzzle_vals.charAt(empty_index + 1));
		            this.empty_index++;
		            this.puzzle_vals.setCharAt(this.empty_index, '0');
		        }//end if
            }//end else if
            else//empty space is moved down
            {
                //make sure empty_index isn't already in bottom row
	            if (this.empty_index < 6) 
	            {
	                this.puzzle_vals.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index + 3));
	                this.empty_index += 3;
	                this.puzzle_vals.setCharAt(this.empty_index, '0');
	            }//end if
            }//end else
        }//end for
    }//end shuffleTiles

    public void setEightPuzzle(String new_vals)
    {
        //Assign puzzle_vals to new StringBuffer using new values
        this.puzzle_vals = new StringBuffer(new_vals);
        this.setAStarVal();
    }//end setEightPuzzle
    //Returns value of f(n) = g(n) + h(n)
    public int getAStarVal()
    {
        return this.aStarVal;
    }//end getAStarVal
    //Returns previous_state state
    public EightPuzzle getPrevious()
    {
        return this.previous_state;
    }//end getprevious_state
    public int getDepth()
    {
    	return this.depth_of_state;
    }//end getDepth
    //Return puzzle values as a string
    public String getPuzzleVals()
    {
        return this.puzzle_vals.toString();
    }//end getPuzzleVals
 
    public EightPuzzle shiftSpaceUp()
    {
        // StringBuffer to store a copy and manipulate the current board values
        StringBuffer new_val = new StringBuffer(this.puzzle_vals.toString());
        // if empty isn't on top row, produce up movement
        if (this.empty_index > 2) 
        {
            new_val.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index - 3));
            new_val.setCharAt(this.empty_index - 3, '0');
            // return a new EightPuzzle as a successor produced from a upward movement
            return new EightPuzzle(new_val.toString(), this.depth_of_state + 1, this, this.heuristic);
        }//end if 
        else 
        {
            return null;
        }//end else
    }//end shiftSpaceUp
    
    public EightPuzzle shiftSpaceDown()
    {
        // StringBuffer to store a copy and manipulate the current board values
        StringBuffer new_val = new StringBuffer(this.puzzle_vals.toString());
        // if empty isn't on the bottom row, move down
        if (this.empty_index < 6) 
        {
            new_val.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index + 3));
            new_val.setCharAt(this.empty_index + 3, '0');
            // return a new EightPuzzle as a successor produced from moving down
            return new EightPuzzle(new_val.toString(), this.depth_of_state + 1, this, this.heuristic);
        }//end if 
        else 
        {
            return null;
        }//end else
    }//end shiftSpaceDown
    
    public EightPuzzle shiftSpaceLeft()
    {
        // StringBuffer to store a copy and manipulate the current board values
        StringBuffer new_val = new StringBuffer(this.puzzle_vals.toString());
        // if empty isn't on left column, move left
        if (this.empty_index % 3 != 0) 
        {
            new_val.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index - 1));
            new_val.setCharAt(this.empty_index - 1, '0');
            // return a new EightPuzzle as a successor produced from moving left
            return new EightPuzzle(new_val.toString(), this.depth_of_state + 1, this, this.heuristic);
        }//end if 
        else 
        {
            return null;
        }//end else
    }//end shiftSpaceLeft
    
    public EightPuzzle shiftSpaceRight()
    {
        // StringBuffer to store a copy and manipulate the current board values
        StringBuffer new_val = new StringBuffer(this.puzzle_vals.toString());
        // if empty isn't on right column, move right
        if (this.empty_index % 3 != 2) 
        {
            new_val.setCharAt(this.empty_index, this.puzzle_vals.charAt(this.empty_index + 1));
            new_val.setCharAt(this.empty_index + 1, '0');
            // return a new EightPuzzle as a successor produced from moving right
            return new EightPuzzle(new_val.toString(), this.depth_of_state + 1, this, this.heuristic);
        }//end if
        else 
        {
            return null;
        }//end else
    }//end shiftSpaceRight

    //Simply prints the values of the state of the EightPuzzle
    public void printPuzzle() 
    {
    	for(int i = 1; i <= puzzle_vals.length(); i++) 
    	{
    		System.out.print(this.puzzle_vals.charAt(i-1) + " ");
    		//Prints a new line for each row of 3 values
    		if(i % 3 == 0) 
    		{
    			System.out.println();
    		}//end if
    	}//end for
    }//end printPuzzle
}//end EightPuzle