import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    // Начальные размеры окна приложения
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Объект диалогового окна для выбора файлов
    private JFileChooser fileChooser = null;

    // Пункты меню
    private final JMenuItem saveToGraphicsMenuItem;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showGridMenuItem;
    private JCheckBoxMenuItem rotateGraphMenuItem;

    // Компонент-отображатель графика
    private final GraphicsDisplay display = new GraphicsDisplay();

    // Флаг, указывающий на загруженность данных графика
    private boolean fileLoaded = false;

    private Double[][] graphicsData;

    private MainFrame() {
        // Вызов конструктора предка Frame
        super("Построение графиков функций на основе заранее подготовленных файлов и обработка событий мыши");
        // Установка размеров окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        // Развертывание окна на весь экран
        setExtendedState(MAXIMIZED_BOTH);
        // Создать и установить полосу меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Добавить пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        // Создать действие по открытию файла
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        fileMenu.add(openGraphicsAction);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToGraphicsFile(
                            fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = new JMenuItem(saveToGraphicsAction);
        fileMenu.add(saveToGraphicsMenuItem);
        saveToGraphicsMenuItem.setEnabled(false);


        // Создать пункт меню "График"
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);


        // Создать действие для реакции на активацию элемента "Показывать оси координат"
        Action showAxisAction = new AbstractAction("Показывать оси координат") {

            public void actionPerformed(ActionEvent event) {
                // свойство showAxis класса GraphicsDisplay истина, если элемент меню
                // showAxisMenuItem отмечен флажком, и ложь - в противном случае
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        // Добавить соответствующий элемент в меню
        graphicsMenu.add(showAxisMenuItem);
        // Элемент по умолчанию включен (отмечен флажком)
        showAxisMenuItem.setSelected(true);


        // Повторить действия для элемента "Показывать маркеры точек"
        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                // по аналогии с showAxisMenuItem
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        // Элемент по умолчанию включен (отмечен флажком)
        showMarkersMenuItem.setSelected(true);


        Action showGraphAction = new AbstractAction("Повернуть график") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setSetRotate(rotateGraphMenuItem.isSelected());
            }
        };
        rotateGraphMenuItem = new JCheckBoxMenuItem(showGraphAction);
        graphicsMenu.add(rotateGraphMenuItem);
        rotateGraphMenuItem.setSelected(false);


        Action showGridAction = new AbstractAction("Показать сетку") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowGrid(showGridMenuItem.isSelected());
            }
        };
        showGridMenuItem = new JCheckBoxMenuItem(showGridAction);
        graphicsMenu.add(showGridMenuItem);
        showGridMenuItem.setSelected(true);


        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        fileMenu.addMenuListener(new GraphicsMenuListener() {
            public void menuSelected(MenuEvent e) {
                saveToGraphicsMenuItem.setEnabled(fileLoaded);
            }
        });

        // Установить GraphicsDisplay в цент граничной компоновки
        getContentPane().add(display, BorderLayout.CENTER);
    }

    void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (Double[] point : graphicsData) {
                out.writeDouble(point[0]);
                out.writeDouble(point[1]);
            }
            out.close();
        } catch (Exception e) {
            // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
            // так как мы файл создаѐм, а не открываем для чтения
        }
    }

    // Считывание данных графика из существующего файла
    void openGraphics(File selectedFile) {
        try {
            // Шаг 1 - Открыть поток чтения данных, связанный с входным файловым потоком
            DataInputStream in = new DataInputStream(new
                    FileInputStream(selectedFile));
            /*
            * Шаг 2 - Зная объъм данных в потоке ввода можно вычислить,
            * сколько памяти нужно зарезервировать в массиве:
            * Всего байт в потоке - in.available() байт;
            * Размер одного числа Double - Double.SIZE бит, или Double.SIZE/8 байт;
            * Так как числа записываются парами, то число пар меньше в 2 раза
            */
            graphicsData = new Double[in.available() / (Double.SIZE / 8) / 2][];
            // Шаг 3 - Цикл чтения данных (пока в потоке есть данные)
            int i = 0;
            while (in.available() > 0) {
                // Первой из потока читается координата точки X
                Double x = in.readDouble();
                // Затем - значение графика Y в точке X
                Double y = in.readDouble();
                // Прочитанная пара координат добавляется в массив
                graphicsData[i++] = new Double[]{x, y};
            }
            // Шаг 4 - Проверка, имеется ли в списке в результате чтения хотя бы одна пара координат
            if (graphicsData != null && graphicsData.length > 0) {
                // Да - установить флаг загруженности данных
                fileLoaded = true;
                // Вызывать метод отображения графика
                display.showGraphics(graphicsData);
            }
            // Шаг 5 - Закрыть входной поток
            in.close();
        } catch (FileNotFoundException ex) {
            // В случае исключительной ситуации типа "Файл не найден" показать сообщение об ошибке
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", " Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            // В случае ошибки ввода из файлового потока показать сообщение об ошибке
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", " Ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    public static void main(String[] args) {
        // Создать и показать экземпляр главного окна приложения
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Класс-слушатель событий, связанных с отображением меню
    private class GraphicsMenuListener implements MenuListener {
        // Обработчик, вызываемый перед показом меню
        public void menuSelected(MenuEvent e) {
            // Доступность или недоступность элементов меню "График" определяется загруженностью данных
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridMenuItem.setEnabled(fileLoaded);
            rotateGraphMenuItem.setEnabled(fileLoaded);
            saveToGraphicsMenuItem.setEnabled(fileLoaded);
        }

        // Обработчик, вызываемый после того, как меню исчезло с экрана
        public void menuDeselected(MenuEvent e) {
        }

        // Обработчик, вызываемый в случае отмены выбора пункта меню (очень редкая ситуация)
        public void menuCanceled(MenuEvent e) {
        }
    }
}