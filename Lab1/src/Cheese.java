/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/11/12
 * Time: 5:20 PM
 */
public class Cheese extends Food {

    public Cheese() {
        super("Сыр");
        calories = 500; // const
    }

    public void consume() {
        System.out.println(this + " съеден (" + calories + " ккал)");
    }

    @Override
    public int calculateCalories() {
        return this.calories;
    }

    // Переопределять метод equals() в данном классе не нужно, т.к. он
    // не добавляет новых полей данных, а сравнение по внутреннему полю name
    // уже реализовано в базовом классе
    // Переопределять метод toString() в данном классе не нужно, т.к. он
    // не добавляет внутренних полей данных, а возврат поля name уже
    // реализован в версии toString() базового класса
}
