package bsu.rfe.java.group3.Kohonovskiy.varC2.Lab1;

import java.lang.reflect.*;
import java.util.*;

public class MainApp {

    public static void main(String args[]) throws Exception {
        Food[] breakfast = new Food[20];


        // -------------------------------------------------------

        int itemsSoFar = 0;
        boolean sort = false;
        boolean countCalories = false;
        for (String arg : args) {
            String[] parts = arg.split("/");

            if (parts[0].equals("-calories")) {
                countCalories = true;
                continue;
            }
            if (parts[0].equals("-sort")) {
                sort = true;
                continue;
            }

            // Подключение через Reflection API
            try {
                Class myClass = Class.forName("bsu.rfe.java.group3.Kohonovskiy.varC2.Lab1." + parts[0]);

                if (parts.length == 1) {
                    // дополнительных параметров нет
                    Constructor constructor = myClass.getConstructor();
                    breakfast[itemsSoFar] = (Food) constructor.newInstance();
                } else if (parts.length == 2) {
                    // параметр в parts[1]
                    Constructor constructor = myClass.getConstructor(String.class);
                    breakfast[itemsSoFar] = (Food) constructor.newInstance(parts[1]);
                } else if (parts.length == 3) {
                    Constructor constructor = myClass.getConstructor(String.class, String.class);
                    breakfast[itemsSoFar] = (Food) constructor.newInstance(parts[1], parts[2]);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Class " + e + " not found");
                itemsSoFar--;
            } catch (NoSuchMethodException e) {
                System.out.println("Method " + e + " not found");
                itemsSoFar--;
            }
            itemsSoFar++;
        }

        int[] used = new int[itemsSoFar];
        for (int i = 0; i < itemsSoFar; i++) {
            if( used[i] == 1 ) continue;
            breakfast[i].consume();
            int count = 1;
            for (int j = i + 1; j < itemsSoFar; j++) {
                if (breakfast[j].equals(breakfast[i]) && used[j] != 1)
                {
                    used[j] = 1;
                    count++;
                }
            }
            System.out.println(" в количестве " + count);
        }


        if (countCalories) {
            int totalCalories = 0;
            for (int i = 0; i < itemsSoFar; i++) {
                totalCalories += breakfast[i].calculateCalories();
            }
            System.out.println("Калорийность завтрака: " + totalCalories);
        }

        if (sort) {
            Arrays.sort(breakfast, new Comparator() {
                public int compare(Object f1, Object f2) {
                    if (f1 == null) return 1;
                    if (f2 == null) return -1;
                    if (((Food) f1).calculateCalories() == ((Food) f2).calculateCalories()) return 0;
                    if (((Food) f1).calculateCalories() < ((Food) f2).calculateCalories()) return 1;
                    else return -1;
                }
            });
        }

    }

}
