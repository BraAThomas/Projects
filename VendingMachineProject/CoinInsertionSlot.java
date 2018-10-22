package internals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//Program that accepts input from Console in the form of Strings, which get converted to coins, which get added to the
//RecievingCoinBox. 
//Extends the DollarInsertionSlot to be able to accept dollars as well.
public class CoinInsertionSlot extends DollarInsertionSlot{
	RecievingCoinBox insert = null;  //Sets Objects used in the program as null to init.
	DollarInsertionSlot dbox = null;
	double current = 0;
	
	public CoinInsertionSlot(){
	}
	// Determins from human input what coins are to be entered when.
	public double input(double amount, RecievingCoinBox insert) throws IOException {
		
		this.insert = insert;
		
		while (current < amount) //Loops the console input to allow the user to continuously put in coins 
								// until they have reached the price of the beverage they desired.
		{
	        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));  //Reads from Console
	        String s = reader.readLine(); 
	        
	        //If statements to check for the possible inputs a user has to put in
			if (s.compareTo("q") == 0)
			{
				insert.numQuarters += 1; //increments the number of quarters the user inputs
				current = current + .25; //adds the current dollar amount the user inputs to what has been put in
				System.out.println("Coin accepted. amount: " + amount + " current: " + current); //Prints out what they put in,
																								// and how much the drink cost
			}
			else if (s.compareTo("di") == 0)
			{
				insert.numDimes += 1;  //same as quarters
				current = current + .10; //same as quarters
				System.out.println("Coin accepted. amount: " + amount + " current: " + current); //same as quarters
			}
			else if (s.compareTo("n") == 0)
			{
				insert.numNickles += 1; //same as quarters
				current = current + .05; //same as quarters
				System.out.println("Coin accepted. amount: " + amount + " current: " + current); //same as quarters
			}
			else if(s.compareTo("do") == 0)
			{
				addDollar(insert); //Refers to the addDollar method inside of DollarInsertionSlot to add the dollar to the
								  // amount the user puts in
				current = current + 1.00; //same as quarters
				System.out.println("Coin accepted. amount: " + amount + " current: " + current); //same as quarters
			}
			else
			{
				System.out.println("Not a valid coin input"); //Warns the user they put in a wrong code for coins/dollars
			}
		}
		return amount; //returns the amount of money given in dollars
	}
}
