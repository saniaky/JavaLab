import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/16/12
 * Time: 8:31 PM
 */

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    // Размеры окна приложения в виде констант
    private static final int WIDTH = 500;
    private static final int HEIGHT = 320;

    // Текстовые поля для считывания значений переменных,
    // как компоненты, совместно используемые в различных методах
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;

    // Текстовое поле для отображения результата,
    // как компонент, совместно используемый в различных методах
    private JTextField textFieldResult;

    // Группа радио-кнопок для обеспечения уникальности выделения в группе
    private ButtonGroup radioButtons = new ButtonGroup();

    // Контейнер для отображения радио-кнопок
    private Box hboxFormulaType = Box.createHorizontalBox();

    private int formulaId = 1;
    String formulaPath = "/Users/saniaky/Dropbox/JavaLab/src/bsu/rfe/java/group3/Kohonovskiy/varC2/Lab2/formula";
    ImagePanel formula = new ImagePanel(formulaPath + "1.gif");


    Double mem1 = 0.0, mem2 = 0.0, mem3 = 0.0;
    private int activeVarId = 1;
    private JTextField textFieldVar1;
    private JTextField textFieldVar2;
    private JTextField textFieldVar3;
    private Box hboxMemoryType = Box.createHorizontalBox();
    private ButtonGroup radioActiveVarButtons = new ButtonGroup();

    // Формула No1 для рассчѐта
    public Double calculate1(Double x, Double y, Double z) {
        return Math.pow((Math.log((1 + x) * (1 + x)) + Math.cos(Math.PI * z * z * z)), Math.sin(y)) +
                Math.pow((Math.exp(x * x) + Math.cos(Math.exp(z)) + Math.pow(1 / y, 1 / 2)), 1 / x);
    }

    // Формула No2 для рассчѐта
    public Double calculate2(Double x, Double y, Double z) {
        return Math.pow(Math.cos(Math.PI * x * x * x) + Math.log((1 + y) * (1 + y)), 1 / 4) *
                (Math.exp(z * z) + Math.pow(1 / x, 1 / 2) + Math.cos(Math.exp(y)));
    }

    // Вспомогательный метод для добавления кнопок на панель
    private void addRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId = formulaId;
                formula.updateImage(formulaPath + formulaId + ".gif");
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);
    }


    private void addActiveVarButton(String buttonName, final int activeVarId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.activeVarId = activeVarId;

            }
        });
        radioActiveVarButtons.add(button);
        hboxMemoryType.add(button);
    }

    // Конструктор класса
    public MainFrame() {
        super("Вычисление формулы");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();


        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);


        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());
        //hboxFormulaType.setBorder(BorderFactory.createLineBorder(Color.YELLOW));


        //formula.setBorder(BorderFactory.createLineBorder(Color.CYAN));


        addActiveVarButton("Переменная 1", 1);
        addActiveVarButton("Переменная 2", 2);
        addActiveVarButton("Переменная 3", 3);
        radioActiveVarButtons.setSelected(radioActiveVarButtons.getElements().nextElement().getModel(), true);

        JLabel labelForVar1 = new JLabel("#1:");
        JLabel labelForVar2 = new JLabel("#2:");
        JLabel labelForVar3 = new JLabel("#3:");
        textFieldVar1 = new JTextField("0", 15);
        textFieldVar2 = new JTextField("0", 15);
        textFieldVar3 = new JTextField("0", 15);
        textFieldVar1.setMaximumSize(textFieldVar1.getPreferredSize());
        textFieldVar2.setMaximumSize(textFieldVar2.getPreferredSize());
        textFieldVar3.setMaximumSize(textFieldVar3.getPreferredSize());

        JButton buttonMC = new JButton("MC");
        JButton buttonMPlus = new JButton("M+");

        Box hboxVarButtons = Box.createHorizontalBox();
        hboxVarButtons.add(buttonMC);
        hboxVarButtons.add(buttonMPlus);
        Box hboxVarFields = Box.createHorizontalBox();
        hboxVarFields.add(labelForVar1);
        hboxVarFields.add(textFieldVar1);
        hboxVarFields.add(labelForVar2);
        hboxVarFields.add(textFieldVar2);
        hboxVarFields.add(labelForVar3);
        hboxVarFields.add(textFieldVar3);

        buttonMC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeVarId == 1) {
                    textFieldVar1.setText("0");
                    mem1 = 0.0;
                } else if (activeVarId == 2) {
                    textFieldVar2.setText("0");
                    mem2 = 0.0;
                } else {
                    textFieldVar3.setText("0");
                    mem3 = 0.0;
                }
            }
        });

        buttonMPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(activeVarId == 1) {
                    mem1 += Double.parseDouble(textFieldResult.getText());
                    textFieldVar1.setText(mem1.toString());
                }
                else if(activeVarId == 2) {
                    mem2 += Double.parseDouble(textFieldResult.getText());
                    textFieldVar2.setText(mem2.toString());
                }
                else {
                    mem3 += Double.parseDouble(textFieldResult.getText());
                    textFieldVar3.setText(mem3.toString());
                }
            }
        });




        // Создать область с полями ввода для X и Y
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());

        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());

        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());

        Box hboxVariables = Box.createHorizontalBox();
        //hboxVariables.setBorder(BorderFactory.createLineBorder(Color.RED));
        hboxVariables.add(Box.createHorizontalGlue());

        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalGlue());

        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalGlue());

        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalGlue());


        // Создать область для вывода результата
        JLabel labelForResult = new JLabel("Результат:");
        //labelResult = new JLabel("0");
        textFieldResult = new JTextField("0", 15);
        textFieldResult.setMaximumSize(textFieldResult.getPreferredSize());
        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        //hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));


        // Создать область для кнопок
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        //hboxButtons.setBorder(BorderFactory.createLineBorder(Color.GREEN));


        // Связать области воедино в компоновке BoxLayout
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(formula);
        contentBox.add(hboxVariables);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(hboxMemoryType);
        contentBox.add(hboxVarButtons);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxVarFields);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);
    }

    // Главный метод класса
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
