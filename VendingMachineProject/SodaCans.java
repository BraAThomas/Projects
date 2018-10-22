package internals;

public class SodaCans {
	
	int quantity;
	String name;
	
	//Sets up the Constructor for every soda to have this type
	public SodaCans(int amount, String type)
	{
		quantity = amount;
		name = type;
	}
	//Declaration of every soda in the machine, and how many there are.
	static SodaCans coke = new SodaCans(100, "Coke");
	static SodaCans sprit = new SodaCans(100, "Sprit");
	static SodaCans drPepper = new SodaCans(100, "Dr. Pepper");
	static SodaCans MountainDew = new SodaCans(100, "MountainDew");
	static SodaCans fanta = new SodaCans(100, "Fanta");
	static SodaCans rootbeer = new SodaCans(100, "RootBeet");
	static SodaCans pepsi = new SodaCans(100, "Pepsi");
	static SodaCans gingerale = new SodaCans(100, "Ginger Ale");

}
