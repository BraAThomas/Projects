package internals;

public class StockpileCoinBox {
	//Declares the amount of Coins there are in the machine at load up
	 int numQuarters = 500;
	 int numDimes = 500;
	 int numNickles = 500;
	 int numDollars = 0;
	
	RecievingCoinBox insert = null;
	
	public StockpileCoinBox() {
		
	}
	//Declares the RecievingCoinBox type to allow for variable exchange between the two files. 
	public RecievingCoinBox setStockPile(RecievingCoinBox box)
	{
		this.insert = box;
		return this.insert;

	}
}
