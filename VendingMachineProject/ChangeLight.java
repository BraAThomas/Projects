package internals;

public class ChangeLight {
	
	StockpileCoinBox check = new StockpileCoinBox(); //Sets up a Stockpile constructor
	
	public ChangeLight()
	{}
	
	//
	public void setLight(StockpileCoinBox amount) {
		this.check = amount; //Sets the Stockpile to the amounts inside the stockpile
	}
	
	public boolean Light() {
		//calculates the total dollar amount inside the stockpile
		double total = check.numQuarters * .25 + 
				check.numDimes * .1 + check.numDollars * 1 + check.numNickles * .05;
		boolean test;
		
		//Tests to see if the total is more than the most expensive drink, $1.50
		if(total > 1.5 )
			test = true; //If its true, returns true to say there is enough money
		else
			test = false;// returns false to say there is not enough money.
	return test;
	}
}
