package internals;

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

public class VendingMachine{
	
	static double total;  //Declares the total in dollars of the coins the user input to CoinInsertionSlot
	static double amount; //Declares the amount of money a drink cost
	
	StockpileCoinBox stockpile = new StockpileCoinBox();  //Declarations of Objects to be used 
	BillBox billbox = new BillBox();
	static ChangeLight light = new ChangeLight();
	static CoinInsertionSlot coinslot = new CoinInsertionSlot();
	RecievingCoinBox interbox = null;
	DollarInsertionSlot dollarslot = new DollarInsertionSlot();
	
	 int numQuarters;  //Declares the number of coins of each type.
	 int numDimes;
	 int numNickles;
	 int numPennies;
	 int numDollars;
	
	//Declares the constructor for VendingMachine
	public VendingMachine(RecievingCoinBox interbox) 
	{
		//Links to the RecievingCoinBox amounts for each coin to be passed back and forth.
		this.interbox = interbox;
		try {
			interbox.DumpCoins(total, amount, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 VendingMachine vender = null; //Declares the Object of the VendingMachine to be used
	
	 //Method used to allow the user to select which drink they would like from the Vending Machine.
	public static  String DrinkSelector(RecievingCoinBox interbox) throws IOException
	{
		String lite;
		System.out.println("Welcome to the Virtual Vending Machine!");
		System.out.println("Serving out pixel drinks since 2018!");
		if (light.Light()) //Checks whether the exact-change-only light is on or off.
			lite = "off";
		else
			lite = "on";
		//Prints out the introduction to the Machine and the drinks available and their prices
		System.out.println("The change-only light is: " + lite);
		System.out.println("Please please decide which drink you want below, then input the name as it is show. ");
		System.out.println("Coke: $1.50, Pepsi: $1.50, Fanta: 1.25, Mountain dew: $1.50");
		System.out.println("Sprit: $1.50, Dr. Pepper: $1.50, Root Beer: $1.25, GingerAle: $1.00");
		System.out.print("Quit: Exits the program");
		System.out.println(""); //Prints an extra line for the user to have space to type.
		
		return Selector(interbox); //Runs the selector program to figure out which drink the user wants.
	}
	
	//A selector method to allow the user to pick a drink they want.
	public static String Selector(RecievingCoinBox interbox) throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  //Allows the user-interface
        String s = reader.readLine(); 
        
		String drink;
		
		if(s.compareTo("Coke") == 0) //If statement to get the user's drink choice.
		{
			amount = MoneyInputer("Coke", 1.5, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Coke"; //Sets the drink choice to the variable drink.
		}
		else if(s.compareTo("Pepsi") == 0)
		{
			amount = MoneyInputer("Pepsi", 1.5, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Pepsi";
		}
		else if(s.compareTo("Fanta") == 0)
		{
			amount = MoneyInputer("Fanta", 1.25, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Fanta";
		}
		else if(s.compareTo("Mountain Dew") == 0)
		{
			amount = MoneyInputer("Mountain Dew", 1.5, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Mountain Dew";
		}
		else if(s.compareTo("Sprit") == 0)
		{
			amount = MoneyInputer("Sprit", 1.5, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Sprit";
		}
		else if(s.compareTo("Dr. Pepper") == 0)
		{
			amount = MoneyInputer("Dr. Pepper", 1.5, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "Dr. Pepper";
		}
		else if(s.compareTo("Root Beer") == 0)
		{
			amount = MoneyInputer("RootBeer", 1.25, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "RootBeer";
		}
		else if(s.compareTo("GingerAle") == 0)
		{
			amount = MoneyInputer("GingerAle", 1, interbox); //Runs MoneyInputer with the drink, amount, and RecievingCoinBox obj
			drink = "GingerAle";
		}
		else if(s.compareTo("Quit") == 0)
		{
			drink = ""; //Allows the user to quit if they made a horrible, life-ending mistake
		}
		else
		{
			System.out.println("Invalid name, please input the name as it is show."); 
			Selector(interbox); //Re-runs the if statement to allow the user to input a proper name
			drink = "";
		}
		return drink;
	}
	
	//Method to allow the user to input money
	public static double MoneyInputer(String drink, double amount, RecievingCoinBox interbox) throws IOException
	{
		//Printed statements to let the user know of their options in terms of payment.
		System.out.println("You have chosen: " + drink + " Please input: " + amount);
		System.out.println("Please input what you see in '...': ");
		System.out.println("'do'- Dollars or $1.00");
		System.out.println("'q'- Quarters or $0.25");
		System.out.println("'di'- Dimes or $0.10");
		System.out.println("'n'- Nickles or $0.05");
	        
	       return coinslot.input(amount, interbox); //Runs the input method of CoinInsertionSlot file with amount it cost +
	       											// the object of the RecievingCoinBox to allow money to filter there.
	}
	
	//Main method to run the Machine
	public static void main(String[] args) throws IOException
	{
		//Declares locally the variables needed to pass coin amounts and totals to the CoinBoxes
		RecievingCoinBox interbox = new RecievingCoinBox(amount, coinslot);
		VendingMachine vender = new VendingMachine(interbox);
		System.out.println("Hello!");
		
		String drink = DrinkSelector(interbox);
		//Setting of value for the different coins
		double q = .25;
		double d = .1;
		double n = .05;
		// Calculates the total of the user inputted coins
		total = 1 * interbox.numDollars +
				interbox.numQuarters * q +
				interbox.numDimes * d +
				interbox.numNickles * n;
		System.out.println("Here is the total: " + total);
		//Calls on the DumpCoins method of the RecievingCoinBox file with the total input coins, the amount the drink cost,
		// and the object for the vending machine to allow for its coin amounts to change.
		interbox.DumpCoins(total, amount, vender);
		
		//Print statements to add some realizism (maybe?) and to show the user their change outcomes.
		System.out.println("*Clink*");
		System.out.println("*Thump*");
		System.out.println("Your drink, " + drink + ", has been delivered!");
		System.out.println("Here is your change: ");
		System.out.println(vender.numQuarters + " : number of Quarters");
		System.out.println(vender.numDimes + " : number of Dimes");
		System.out.println(vender.numNickles + " : number of Nickles");
		System.out.println("Would you like to buy another drink?: ");
		
		//Allows for the user to buy another drink
		System.out.println("Y -for yes or N -for no");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        String p = reader.readLine(); 
		System.out.println("");
		
		if(p.compareTo("Y")==0) //If statement to see if the user wants another drink.
			DrinkSelector(interbox);
		else
			System.out.println("Thank you for coming to the virtual soda machine!");
			
	}

}
