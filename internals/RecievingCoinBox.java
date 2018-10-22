package internals;

import java.io.IOException;
import java.lang.Math;

public class RecievingCoinBox extends StockpileCoinBox{
	//Declares the Objects to be used
	CoinInsertionSlot insert = null;
	StockpileCoinBox recieving = null;
	VendingMachine machine = null;
	//Declares the number of coins/dollars here
	 int numQuarters;
	 int numDimes;
	 int numNickles;
	 int numPennies;
	 int numDollars;
	public RecievingCoinBox(double amount, CoinInsertionSlot insert) //Declares the Contrustor for the CoinInsertionSlot to
																	// use.
	{
		this.insert = insert;

		try {
			insert.input(amount, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Method DumpCoins to dump the coins this file is holding to the StockPile
	public void DumpCoins(double total, double amount, VendingMachine machine) throws IOException
	{
		this.machine = machine;
		//Declares the objects to be used during this method.
		RecievingCoinBox obtaining = new RecievingCoinBox(amount, insert);
		StockpileCoinBox dumping = new StockpileCoinBox();
		BillBox bills = new BillBox();
		
		//Sets the dollar amount for the dollar and coins
		bills.dollars = 1;
		double q = .25;
		double d = .1;
		double n = .05;
		double change = 0;
		
		//If statement to check to make sure the user has input enough dollar amount for their drink
		if (total >= 1.50)
		{
			//Puts the coins currently in the RecievingCoinBox to the Stockpile
			dumping.numQuarters += obtaining.numQuarters;
			dumping.numDimes += obtaining.numDimes;
			dumping.numNickles += obtaining.numNickles;
			dumping.numDollars += obtaining.numDollars;
			
			//Calculates the amount of change needed for the user.
			change = total - amount;
			change = Math.floor(change * 100) / 100; //Using doubles has roundoff error in the computer's calculations, so
													// this line is to re round change to a more appropriate number
			
			//Prints out the change to be received.
			System.out.println("Change recieved: " + change);
			
			
			//Nested if statements to determine how many of each coin to give back to return the proper amount of change.
			if((change % q) == 0)
			{
				machine.numQuarters = (int) (change / q);
				
			}
			else {
				if(((change % q) % d) == 0) {
					machine.numQuarters = (int)(change / q);
					
					machine.numDimes = (int) (change/d);
				}
				else {
					if((((change % q) % d) % n) == 0) {
						machine.numQuarters = (int)(change / q);
						machine.numDimes = (int) (change/d);
						machine.numNickles = (int) (change/n);
					}
					else {
						if(((change % d) % n) == 0) {
							
							machine.numDimes = (int) (change/d);
							machine.numNickles = (int) (change/n);
						}
						else {
							if((change % d) == 0) {
								machine.numDimes = (int) (change/d);
							}
							else {
								if((change % n) == 0) {
									machine.numNickles = (int) (change/n);
								}
							}
								
						}
					}
				}
						
				}
			
			//Takes coins from the stockpile to use for the change. 
			dumping.numQuarters -= obtaining.numQuarters;
			dumping.numDimes -= obtaining.numDimes;
			dumping.numNickles -= obtaining.numNickles;
			dumping.numDollars -= obtaining.numDollars;
		}	
	}
}

