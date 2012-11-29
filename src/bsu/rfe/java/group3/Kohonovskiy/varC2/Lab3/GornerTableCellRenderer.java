package bsu.rfe.java.group3.Kohonovskiy.varC2.Lab3;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/26/12
 * Time: 7:51 AM
 */
public class GornerTableCellRenderer implements TableCellRenderer {

    private final double EPS = 10E-6;

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
    // (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
    // стога сена - таблица
    private String needle = null;
    private boolean primeSelect = false;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        // Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
        // Не использовать группировку (т.е. не отделять тысячи
        // ни запятыми, ни пробелами), т.е. показывать число как "1000",
        // а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
        // Установить в качестве разделителя дробной части точку, а не
        // запятую. По умолчанию, в региональных настройках
        // Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        // Разместить надпись внутри панели
        panel.add(label);
        // Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        // ровняем в зависимости от контента
        if ((Double) value < 0)
             panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        else if ((Double) value > 0)
             panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        else
             panel.setLayout(new FlowLayout(FlowLayout.CENTER));


        // Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
        // Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if (col == 1 && needle != null && needle.equals(formattedDouble)) {
            // Номер столбца = 1 (т.е. второй столбец) + иголка не null
            // (значит что-то ищем) +
            // значение иголки совпадает со значением ячейки таблицы -
            // окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        } else {
            // Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }


        // если включен режим поиска простых чисел
        if (primeSelect) {
            double n = (Double)value;
            if (Math.abs(n - Math.round(n)) - 0.1 < EPS) {
                if (isPrime((Math.round(n)))) {
                    panel.setBackground(Color.CYAN);
                }
            }
        }

        return panel;
    }


    // проверка числа на простоту
    private boolean isPrime(long n) {
        if (n <= 0) return false;
        if (n <= 2) return true;
        for (int i = 2; i * i <= n; ++i) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public void setPrimeFind(boolean v){
        primeSelect = v;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }


}