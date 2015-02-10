public class Cocktail extends Food {
    private final String drink;
    private final String fruit;

    public Cocktail(String drink, String fruit) {
        super("Коктейль");
        this.drink = drink;
        this.fruit = fruit;
        calories = 100;
        if( drink.contains("Vodka") ) calories += 50;
        if( fruit.contains("Tomato") ) calories += 40;

    }

    @Override
    public void consume() {
        System.out.println(this + " выпит =) (" + calories + " ккал)");
    }

    @Override
    public String toString() {
        return super.toString() + " из" + drink.toUpperCase() + " и кусочками фрукта под названием " + fruit.toUpperCase() ;
    }

    @Override
    public boolean equals(Object arg) {
        return super.equals(arg) && arg instanceof Cocktail && drink.equals(((Cocktail) arg).drink) && fruit.equals(((Cocktail) arg).fruit);
    }

    @Override
    public int calculateCalories() {
        return this.calories;
    }
}
