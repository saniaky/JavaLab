package bsu.rfe.java.group3.Kohonovskiy.varC2.Lab1;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/11/12
 * Time: 5:27 PM
 */

public class Apple extends Food {

    // Новое внутреннее поле данных РАЗМЕР
    private String size;


    public Apple(String size) {
        // Вызвать конструктор предка, передав ему имя класса super("Яблоко");
        super("Яблоко");
        // Инициализировать размер яблока
        this.size = size;
        calories = 50;
        if( size.contains("Big") ) calories += 100;
        else calories += 30;
    }

    // Переопределить способ употребления яблока
    public void consume() {
        System.out.println(this + " съедено (" + calories + " ккал)");
    }

    @Override
    public int calculateCalories() {
        return this.calories;
    }

    // Селектор для доступа к полю данных РАЗМЕР
    public String getSize() {
        return size;
    }

    // Модификатор для изменения поля данных РАЗМЕР
    public void setSize(String size) {
        this.size = size;
    }

    // Переопределѐнная версия метода equals(), которая при сравнении
    // учитывает не только поле name (Шаг 1), но и проверяет совместимость
    // типов (Шаг 2) и совпадение размеров (Шаг 3)
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) { // Шаг 1
            if (!(arg0 instanceof Apple)) return false; // Шаг 2 return size.equals(((Apple)arg0).size);
            return size.equals(((Apple) arg0).size); // Шаг 3
        } else
            return false;
    }

    // Переопределѐнная версия метода toString(), возвращающая не только
    // название продукта, но и его размер
    public String toString() {
        return super.toString() + " размера '" + size.toUpperCase() + "'";
    }
}
