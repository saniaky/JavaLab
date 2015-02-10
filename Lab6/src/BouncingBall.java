import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;
    private static final int UPDATE_RATE = 60; // Number of refresh per second

    private Field field;

    private int radius;
    private Color color;
    private double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;

    public BouncingBall(Field field) {
        this.field = field;
        radius = new Double(Math.random() * (MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        // Абсолютное значение скорости зависит от диаметра мяча,
        // чем он больше, тем медленнее
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Начальное направление скорости тоже случайно,
        // угол в пределах от 0 до 2PI
        double angle = Math.random() * 2 * Math.PI;
        // Вычисляются горизонтальная и вертикальная компоненты скорости
        speedX = speed * Math.cos(angle);
        speedY = speed * Math.sin(angle);
        // Цвет мяча выбирается случайно
        color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        // Начальное положение мяча случайно
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
        // Создаѐм новый экземпляр потока, передавая аргументом
        // ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        // Запускаем поток
        thisThread.start();
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
    // то завершается и поток
    public void run() {
        try {
            while (true) {
                field.canMove(this);

                x += speedX;
                y += speedY;

                if (x - radius < 0) {
                    speedX = -speedX;
                    x = radius;
                } else if (x + radius > field.getWidth()) {
                    speedX = -speedX;
                    x = field.getWidth() - radius;
                }

                if (y - radius < 0) {
                    speedY = -speedY;
                    y = radius;
                } else if (y + radius > field.getHeight()) {
                    speedY = -speedY;
                    y = field.getHeight() - radius;
                }
                // Delay for timing control and give other threads a chance
                Thread.sleep(1000 / UPDATE_RATE);   // milliseconds
            }
        } catch (InterruptedException ex) {
        }
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}