public abstract class Food implements Consumable, Nutritious {
    private String name = null;
    int calories = -1;

    public Food(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object arg) {
        if (!(arg instanceof Food)) return false;
        if (name == null || ((Food) arg).name == null) return false;
        return name.equals(((Food) arg).name);
    }

    @Override
    public String toString() {
        return name;
    }

}
