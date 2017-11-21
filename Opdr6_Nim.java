import java.util.*;
import java.io.*;

public class Opdr6_Nim
{
	public static void main(String[] args)
	{
		Nim game = new Nim();

		game.start();		
	}
}

class Nim
{
	int totalNumMatches = 11;
	NimPlayer[] players = new NimPlayer[2];
	Console c;

	public void start()
	{
		c = initConsole(); 
		initPlayers();	
		//Game loop
		do
		{
			playGame();
		}
		while(!checkIfQuitting());

		System.out.println("Thanks for playing!");
	}	

	private void playGame()
	{
		//When rebuilding: less nested loops. last one to grab match = loser, not winner! 
		//Don't have to use variable num players, it's always 2 (it's the game rules): simpler code, but harder to change if the rules change
		int currNumMatches = totalNumMatches;
		NimPlayer loser = null;
		NimPlayer winner = null;
		boolean isWon = false;

		while(!isWon)
		{			
			for(int i = 0; i < players.length; i++)
			{
				System.out.println("Current number of matches is " + currNumMatches);

				int tempCurrMatches = 0;
				int toRemove = 0;

				do
				{
					toRemove = players[i].getNumMatchesToRemove(c, currNumMatches);
					
				}while(!checkIfValidMove(currNumMatches, toRemove));
				
				System.out.println("\t" + players[i].getName() + " takes " + toRemove + " match(es)\n");
				currNumMatches -= toRemove;

				if(currNumMatches == 0)
				{
					loser = players[i];

					if(i > 0)
					{
						winner = players[i-1];
					}
					else
					{
						winner = players[players.length-1]; // last player who had a turn; this way more players are possible
					}
					isWon = true;
					break;
				}
			}
		}

		
		System.out.println(loser.getName() + " has lost.\nCongratulations " + winner.getName() +", you won!");
	}

	public Console initConsole()
	{
		c = System.console();

		if (c == null)
			{
				System.err.println("Error: No console");
				System.exit(1);
			}		

		return c;
	}

	private void initPlayers()
	{
		int numHumanPlayers = checkForPlaymode();
		String input = "";

		for(int i = 0; i < players.length; i++)
		{
			if(numHumanPlayers == 2 || i == 0)
			{
				input = c.readLine("\tName of player " + (i+1) + ": ");
				players[i] = new NimPlayer(input);
			}
			else
			{
				players[i] = new NimPlayer(true);
			}
			
		}
	}

	private int checkForPlaymode()
	{
		boolean isAnswered = false;
		String input = " ";
		int numPlayers = 0;

		while (!isAnswered)
			{
				input = c.readLine("Enter '1' for singleplayer mode or '2' for multiplayer mode: ");

				isAnswered = true; 

				if(input.matches("(.*)1(.*)"))
				{
					numPlayers = 1;
				}
				else if(input.matches("(.*)2(.*)"))
				{
					numPlayers = 2;
				}
				else
				{
					isAnswered = false;
					System.out.print("I don't understand... ");
				}
			}

		return numPlayers;
	}

	private boolean checkIfValidMove(int currNumMatches, int toRemove)
	{
		if(currNumMatches - toRemove >= 0)
		{
			return true;
		}
		else
		{
			System.out.println("Not a valid amount!");
			return false;
		}
	}

	private boolean checkIfQuitting()
	{
		boolean isAnswered = false;
		boolean isQuitting = false;
		String input = "";

		while (!isAnswered)
			{
				input = c.readLine("Play again? Y/N: ");

				isAnswered = true; 

				if(input.matches("(.*)Y(.*)") || input.matches("(.*)y(.*)") )
				{
					isQuitting = false;
				}
				else if(input.matches("(.*)N(.*)") || input.matches("(.*)n(.*)"))
				{
					isQuitting = true;
				}
				else
				{
					isAnswered = false;
					System.out.print("I don't understand... ");
				}
			}
		return isQuitting;
	}
}

class NimPlayer
{
	private String name;
	private boolean isAI;

	public NimPlayer(boolean isComputer)
	{
		this("Default", isComputer);
	}
	public NimPlayer(String name)
	{
		this.name = name;
		isAI = false;
	}
	public NimPlayer(String name, boolean isComputer)
	{
		isAI = isComputer;

		if(name == "Default")
		{
			if(isComputer)
			{
				this.name = "Computer";
			}
			else
			{
				this.name = "You";
			}
		}
		else
		{
			this.name = name;
		}
	}

	public String getName()
	{
		return name;
	}

	public int getNumMatchesToRemove(Console c, int currNumMatches)
	{
		int toRemove = 0;
		Random r = new Random();
				
		//AI
		if(isAI)
		{
			switch(currNumMatches)
			{
				case 1: 
				case 2:
				case 7: 
						toRemove = 1;
						break;
				case 3:
				case 8:
						toRemove = 2;
						break;
				case 4:
				case 9:  
						toRemove = 3;
						break;
				case 5: 
						toRemove = 4;
						break;
				default: toRemove = r.nextInt(4)+1;
						 break;
			}
			
			//toRemove = 1;
		}
		else
		{
			String input = "";

			while(input == "")
			{
				input = c.readLine(name + ", how many matches do you want to remove? Enter 1, 2, 3 or 4: ");

				if(input.matches("(.*)1(.*)"))
				{
					toRemove = 1;
				}
				else if(input.matches("(.*)2(.*)"))
				{
					toRemove = 2;
				}
				else if(input.matches("(.*)3(.*)"))
				{
					toRemove = 3;
				}
				else if(input.matches("(.*)4(.*)"))
				{
					toRemove = 4;
				}
				else
				{
					System.out.println("I don't understand...");
					input = "";
				}
			}//end while	

		}//end else

		return toRemove; 
	}//end getNumMatches
}