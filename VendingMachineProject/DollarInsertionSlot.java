package internals;
//Program to accept input from the CoinInsertionSlot(CIS) in the form of dollars, 
//and add that dollar to the number in the CIS
public class DollarInsertionSlot {

	DollarInsertionSlot(){
	}
	
	public void addDollar(RecievingCoinBox box)
	{
		box.numDollars += 1;
	}
}
