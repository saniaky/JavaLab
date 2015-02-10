import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends JPanel {
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            // Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball : balls) {
            ball.paint(canvas);
        }
    }

    // Метод добавления нового мяча в список
    public void addBall() {
        //Заключается в добавлении в список нового экземпляра BouncingBall
        // Всю инициализацию положения, скорости, размера, цвета
        // BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }

    // Метод синхронизированный, т.е. только один поток может
    // одновременно быть внутри
    public synchronized void pause() {
        // Включить режим паузы
        paused = true;
    }

    // Метод синхронизированный, т.е. только один поток может
    // одновременно быть внутри
    public synchronized void resume() {
        // Выключить режим паузы
        paused = false;
        // Будим все ожидающие продолжения потоки
        notifyAll();
    }

    // Синхронизированный метод проверки, может ли мяч двигаться
    // (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            // Если режим паузы включен, то поток, зашедший
            // внутрь данного метода, засыпает
            wait();
        }
    }
}