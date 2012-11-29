package bsu.rfe.java.group3.Kohonovskiy.varC2.Lab3;

import javax.swing.table.AbstractTableModel;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/26/12
 * Time: 7:45 AM
 */


@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        // В данной модели два столбца
        return 4;
    }

    public int getRowCount() {
        // Вычислить количество точек между началом и концом отрезка
        // исходя из шага табулирования
        return new Double(Math.ceil((to - from) / step)).intValue() + 1;
    }

    public Object getValueAt(int row, int col) {
        // Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step * row;
        if (col == 0) {
            // Если запрашивается значение 1-го столбца, то это X
            return x;
        } else if (col == 1) {
            // Если запрашивается значение 2-го столбца, то это значение многочлена
            Double result = 0.0;
            // Вычисление значения в точке по схеме Горнера.
            for (int i = coefficients.length; i > 0; --i) {
                result = coefficients[i-1] + result * x;
            }
            return result;
        } else if (col == 2) {
            // Если запрашивается значение 3-го столбца, то это
            // значение многочлена вычисленное с помощью функции возведения в степень Math.pow()
            Double result = 0.0;
            for (int i = coefficients.length - 1; i >= 0; --i) {
                result += coefficients[i] * Math.pow(x, i);
            }
            return result;
        } else {
            // Если запрашивается значение 4-го столбца, то это
            // разница между значениями второго и третьего столбцов.
            return (Double)getValueAt(row, 1) - (Double)getValueAt(row, 2);
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                // Название 1-го столбца
                return "Значение X";
            case 1:
                // Название 2-го столбца
                return "Значение многочлена";
            case 2:
                return "Значение многочлена (Math.pow())";
            case 3:
                return "delta X";
            default:
                return "---";
        }
    }

    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}
