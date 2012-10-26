package bsu.rfe.java.group3.Kohonovskiy.varC2.test1;

import java.util.Arrays;

public class Gauss {

    public static void main(String[] args) {
        //double[][] array = {{6.0, 13.0, -17.0, 2.0}, {13.0, 29.0, -38.0, 4.0}, {-17.0, -38.0, 50.0, -5.0}};
        //double[][] array = {{1.0, 2.0, 1.0, 1.0}, {-1.0, -2.0, 2.0, 1.0}, {0.0, 1.0, 1.0, 2.0}};
        //double[][] array = {{2.3, 5.7, -0.8, -6.49}, {3.5, -2.7, 5.3, 19.20}, {1.7, 2.3, -1.8, -5.09}};
        //double[][] array = {{2.75, 1.78, 1.11, 15.71}, {3.28, 0.71, 1.15, 43.78}, {1.15, 2.7, 3.58, 37.11}};
        //double[][] array = {{8.64, 1.71, 5.42, 10.21}, {-6.39, 4.25, 1.84, 3.41}, {4.21, 7.92, -3.41, 12.29}};
        //double[][] array = {{21.547, -95.510, -96.121, -49.93}, {10.223, -91.065, -7.343, -12.465}, {51.218, 12.264, 86.457, 60.812}};
        double[][] array = {{2.6, -4.5, -2.0, 19.07}, {3.0, 3.0, 4.3, 3.21}, {-6.0, 3.5, 3.0, -18.25}};

        System.out.print("Количество решений: " + GaussSolve(array));
    }

    public static int GaussSolve(double[][] a) {

        final double EPS = 10E-6;

        double[][] A = new double[a.length][a.length];
        A = Arrays.copyOf(a, a.length);

        int n = a.length;
        int m = a[0].length - 1;

        double[] ans = new double[m];
        int[] where = new int[m];
        Arrays.fill(where, -1);
        Arrays.fill(ans, 0);

        for (int col = 0, row = 0; col < m && row < n; ++col) {
            int sel = row;
            for (int i = row; i < n; ++i)
                if (Math.abs(a[i][col]) > Math.abs(a[sel][col]))
                    sel = i;
            if (Math.abs(a[sel][col]) < EPS)
                continue;

            for (int i = 0; i <= m; ++i) {
                double tmp = a[sel][i];
                a[sel][i] = a[row][i];
                a[row][i] = tmp;
            }
            where[col] = row;

            for (int i = 0; i < n; ++i) {
                if (i != row) {
                    double c = a[i][col] / a[row][col];
                    for (int j = col; j <= m; ++j)
                        a[i][j] -= a[row][j] * c;
                }
            }
            ++row;
        }

        for (int i = 0; i < m; ++i)
            if (where[i] != -1)
                ans[i] = a[where[i]][m] / a[where[i]][i];

        for (int i = 0; i < n; ++i) {
            double sum = 0;
            for (int j = 0; j < m; ++j)
                sum += ans[j] * a[i][j];
            if (Math.abs(sum - a[i][m]) > EPS)
                return 0;
        }

        for (int i = 0; i < m; ++i)
            if (where[i] == -1) {
                System.out.println("Infinite number of solutions");
                return -1;
            }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j <= m; ++j) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }

        for (int i = 0; i < ans.length; i++) {
            System.out.println("x[" + i + "] = " + ans[i]);
        }

        double tmp = 0.0;
        double norm = 0.0;
        double[] residualVector = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tmp += A[i][j] * ans[j];
            }
            residualVector[i] = tmp - A[i][m];
            if (norm < Math.abs(residualVector[i])) norm = residualVector[i];
            tmp = 0.0;
        }

        for (double v : residualVector) {
            System.out.print(v + " ");
        }
        System.out.println("\nНорма вектора невязки: " + norm);

        return 1;
    }
}