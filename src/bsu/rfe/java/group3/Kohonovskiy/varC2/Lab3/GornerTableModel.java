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
        return 2;
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
        } else {
            // Если запрашивается значение 2-го столбца, то это значение
            // многочлена
            Double result = 0.0;
            // Вычисление значения в точке по схеме Горнера.
            // Вспомнить 1-ый курс и реализовать
            // ...
            return result;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                // Название 1-го столбца
                return "Значение X";
            default:
                // Название 2-го столбца
                return "Значение многочлена";
        }
    }

    public Class<?> getColumnClass(int col) {
        // И в 1-ом и во 2-ом столбце находятся значения типа Double
        return Double.class;
    }
}

/*
// 2.2 Вложенные циклы. Схема Горнера
#include <stdio.h>
#include <math.h>

int main( void )
{
	int n;
	int i, j;
	double x, sum = 0, temp;

	scanf("%d", &n);

	for( i = 0; i <= 10; i++ )
	{
		x = 0.4 * i;
		for( j = n, t = 0; j >= 1; j-- )
		{
			sum += pow(-1, j) * j * pow(x, j);
			t += j + 1;
		}
		printf("x[%d] = %.3lf, summa = %.3lf, %d\n", i, x, sum, t);
		sum = 0;
	}

	puts("----------------------------");

	temp = pow(-1, n) * n;
	for( i = 0; i <= 10; i++ )
	{
		x = 0.4 * i;
		sum = temp;
		for( j = n-1, t = 0; j >= 0; j-- )
		{
			sum = pow(-1, j) * j + x * sum;
			t += 2;
		}
		printf("x[%d] = %.3lf, summa = %.3lf, %d\n", i, x, sum, t);
	}

    return 0;
}
*/