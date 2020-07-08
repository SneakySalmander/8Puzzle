package puzzle;

import java.util.Scanner;

//Need to check input to make sure all values in string are numbers.
public class Main 
{
    public static void main(String[] args) 
    {	
    	Scanner kb = new Scanner(System.in);
    	int input = 0;
    	System.out.println("Welcome to the Eight Puzzle Game!\n");
		do
		{
			System.out.print("Select a menu option:\n" + "1. Enter your own custom eight puzzle layout.\n"
								+ "2. Generate a random eight puzzle layout.\n" + "3. Exit the program.\n"
								+ "Option: ");
			input = kb.nextInt();
    		kb.nextLine();
	        while(input != 1 && input != 2 && input != 3)
	        {      
	            System.out.print("Invalid Input Detected. Try again: ");
	            input = kb.nextInt();
	        }//end while
	    	//This portion of the main is only part user needs to run & visualize algorithm
			EightPuzzle puzzle = getUserInput(kb, input);
	        AStar unsolved_puzzle = new AStar(puzzle.getPuzzleVals());   
	        displayResults(unsolved_puzzle, puzzle);  	
		}
    	while(input != 3);
    	kb.close();
    }//end main
    public static EightPuzzle getUserInput(Scanner kb, int option)
    {       
        EightPuzzle puzzle = new EightPuzzle("012345678", 2);
        boolean even_parity = false;
        switch(option) 
        {
        	case 1: //User enters their own puzzle
        	{
	            boolean duplicates_found = false, empty_space = false,
	            		letters_detected = false;
	            while (!even_parity || !empty_space || duplicates_found || letters_detected) 
	            {
	            	//Reinitialize conditions for each loop
	            	String user_vals = "";
	            	even_parity = false; empty_space = false; duplicates_found = false;
	            	letters_detected = false;
	                System.out.println("Enter an 3x3 puzzle in the format: ");
	                System.out.println("x x x\nx x x\nx x x");
	
	                //Records values from keyboard
	                for(int i = 0; i < 3; i++) 
	                {
	                	user_vals += kb.nextLine();
	                }
	             	//Removes all extra spaces from user input
	                user_vals = user_vals.replaceAll("\\s", "");   
	                even_parity = puzzle.isSolvable();
	      
	                for (int i = 0; i < user_vals.length(); i++) 
	                {
	                	if(user_vals.charAt(i) == ('0')) 
	                	{
	                		empty_space = true;
	                	}//end if
	                	try 
	                	{
	                		Integer.parseInt(String.valueOf(user_vals.charAt(i)));
	                	}//end try
	                	catch(NumberFormatException e)
	                	{
	                		letters_detected = true;
	                	}//end catch
	                    for(int j = i + 1; j < user_vals.length(); j++) 
	                    {
	                        if(user_vals.charAt(i) == user_vals.charAt(j)) 
	                        {
	                            duplicates_found = true;
	                            break;
	                        }//end if
	                    }//end inner for
	                }//end outer for
	                //The follow conditions check to display appropriate error message
	                if(!even_parity)
	                {
	                    System.out.println("Invalid Puzzle. Parity is Odd. Please Try Again.");
	                }//end if
	                if(letters_detected)
	                {
	                	System.out.println("Invalid Puzzle. No Letters are Allowed." 
    							+ " Please Try Again.");
	                }
                	if(!empty_space)
                	{
                		System.out.println("Invalid Puzzle. No Empty Space Was Given."
                							+ " Please Try Again.");
                	}//end if
                	if(duplicates_found)
                	{
                		System.out.println("Invalid Puzzle. Duplicate Values Entered."
            					+ " Please Try Again.");
                	}//end if
                	puzzle.setEightPuzzle(user_vals);
	            }//end while 
	            break;
	        }//end case 1
        	case 2://Puzzle is randomly generated
	        {
                while(!even_parity)
                {
                	/*This argument affects the number of times the empty
    	        	*space is randomly shifted around the puzzle.*/
                	puzzle.shuffleTiles(175);
                	even_parity = puzzle.isSolvable();
                }//end if
	            break;
	        }//end case 2
        	case 3://Exits the program
	        {
	        	System.out.println("Thank you for playing the eight puzzle!");
	        	System.exit(0);
	        }//end case 3
    	}//end switch
        return puzzle;
    }//end getUserInput
    //Displays detailed results needed for project report
    public static void displayResults(double average_nodes, double average_steps, double average_duration,
    									double sample_size, boolean use_explored_set)
    {
    	System.out.println("Number of Puzzles Solved at Target Depth: " + sample_size);
    	System.out.println("Average Number of Nodes Generated: " + average_nodes/sample_size);
    	System.out.println("Average Depth of Solution: " + average_steps/sample_size);
    	System.out.println("Average Time Elapsed(seconds): " + average_duration/sample_size);
    	System.out.println("Explored Set Status: " + use_explored_set);
    }//end displayResults
    
    //Displays path and basic results used to verify accuracy of algorithm and to debug
    public static void displayResults(AStar unsolved_puzzle, EightPuzzle puzzle) 
    {
    	double start = 0, end = 0;
    	boolean use_explored_set = true;
    	System.out.println("\nPuzzle's Initial State:\n");
        puzzle.printPuzzle();
        System.out.println("\nPath of Solution\nHamming Heuristic (Tree Search):\n");
        start = System.nanoTime();
        //For solveHamming(boolean explored_set) true means explored_set is enabled
        //Max depth proven for 8-Puzzle is 31, so depth 32 will never terminate algorithm early
        System.out.println("\nNumber of Nodes Generated: " + unsolved_puzzle.solveHamming(!use_explored_set, 32));
        end = System.nanoTime();
        System.out.println("Depth of Solution: " + unsolved_puzzle.getNumSteps());
        System.out.println("Time Elapsed(nanoseconds): " + (end - start) +"\nExplored Set Status: "
        					+ !use_explored_set + "\n\n");
        
        System.out.println("\nPuzzle's Initial State:\n");
        puzzle.printPuzzle();
        System.out.println("\nPath of Solution\nManhattan Heuristic (Tree Search):\n");
        start = System.nanoTime();
        //For solveManhattan(boolean explored_set) true means explored_set is enabled
        //Max depth proven for 8-Puzzle is 31, so depth 32 will never terminate algorithm early
        System.out.println("\nNumber of Nodes Generated: " + unsolved_puzzle.solveManhattan(!use_explored_set, 32));
        end = System.nanoTime();
        System.out.println("Depth of Solution: " + unsolved_puzzle.getNumSteps());
        System.out.println("Time Elapsed(nanoseconds): " + (end - start) +"\nExplored Set Status: "
							+ !use_explored_set + "\n\n");
        
    	System.out.println("\nPuzzle's Initial State:\n");
        puzzle.printPuzzle();
        System.out.println("\nPath of Solution\nHamming Heuristic (Graph Search):\n");
        start = System.nanoTime();
        //For solveHamming(boolean explored_set) true means explored_set is enabled
        //Max depth proven for 8-Puzzle is 31, so depth 32 will never terminate algorithm early
        System.out.println("\nNumber of Nodes Generated: " + unsolved_puzzle.solveHamming(use_explored_set, 32));
        end = System.nanoTime();
        System.out.println("Depth of Solution: " + unsolved_puzzle.getNumSteps());
        System.out.println("Time Elapsed(nanoseconds): " + (end - start) +"\nExplored Set Status: "
        					+ use_explored_set + "\n\n");
        
        System.out.println("\nPuzzle's Initial State:\n");
        puzzle.printPuzzle();
        System.out.println("\nPath of Solution\nManhattan Heuristic (Graph Search):\n");
        start = System.nanoTime();
        //For solveManhattan(boolean explored_set) true means explored_set is enabled
        //Max depth proven for 8-Puzzle is 31, so depth 32 will never terminate algorithm early
        System.out.println("\nNumber of Nodes Generated: " + unsolved_puzzle.solveManhattan(use_explored_set, 32));
        end = System.nanoTime();
        System.out.println("Depth of Solution: " + unsolved_puzzle.getNumSteps());
        System.out.println("Time Elapsed(nanoseconds): " + (end - start) +"\nExplored Set Status: "
							+ use_explored_set + "\n\n");
    }//end displayResults
}//end Main