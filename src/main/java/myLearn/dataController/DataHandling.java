package myLearn.dataController;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 用于提供数据集划分，数据归一化，生成随机数序列等提供数据处理方法的静态类
 * @author hqz wl
 * @version 1.0
 *
 */
public class DataHandling {
    //该类用于数据从文件中的读取
    private DataHandling() {
    }

    /**
     *
     * @param X 特征矩阵X
     * @param y 标签矩阵y
     * @param train_Size 训练集数目与总数目的比值
     * @return 哈希表，键值分别为train_x,train_y,test_x,test_y，分别代表训练集的特征矩阵，标签矩阵，测试集的特征矩阵，标签矩阵
     */
    //传入特征矩阵X和标签y矩阵传入，选择比率，返回HashMap，键值分别为train_x,train_y,test_x,test_y
    public static HashMap<String, SimpleMatrix> train_test_split(SimpleMatrix X, SimpleMatrix y, double train_Size) {
        /*
         * 读取矩阵X与结果矩阵Y，并按照test_size的比例进行测试集与训练集划分
         * 返回一个map，四个对应的字符串键值分别为train_x,train_y,test_x,test_y,
         * */
        SimpleMatrix train_x, train_y, test_x, test_y;
        HashMap hashMap = new HashMap<String, SimpleMatrix>();
        HashSet train_Set = new HashSet<Integer>();
        HashSet test_Set = new HashSet<Integer>();
        //HashSet temp = new HashSet<Integer>();
        int total_Col = X.numCols();
        int total_Row = X.numRows();
        int train_Row = (int) (total_Row * train_Size);
        double[][] train_x_D = new double[train_Row][total_Col],
                train_y_D = new double[train_Row][1],
                test_x_D = new double[total_Row - train_Row][total_Col],
                test_y_D = new double[total_Row - train_Row][1];
//        train_x = new SimpleMatrix(train_Row,total_Col);
//        train_y = new SimpleMatrix(train_Row,1);
//        test_x = new SimpleMatrix(total_Row-train_Row,total_Col);
//        test_y = new SimpleMatrix(total_Row-train_Row,1);

        for (int i = 0; i < total_Row; i++) {
            test_Set.add(i);
        }
//        int[] train_Index = new int[train_Row];
        do {
            train_Set.add((int) (Math.random() * (total_Row - 1)));
        } while (train_Set.size() < train_Row);
        test_Set.removeAll(train_Set);
        Iterator train_iterator = train_Set.iterator();
        Iterator test_iterator = test_Set.iterator();
        int i = 0;
        int i_ = 0;

        while (test_iterator.hasNext()) {
            int temp = ((Integer) test_iterator.next()).intValue();
            for (int j = 0; j < total_Col; j++) {
                test_x_D[i][j] = X.get(temp, j);
            }
            test_y_D[i][0] = y.get(temp, 0);
            i++;
        }

        while (train_iterator.hasNext()) {
            int temp = ((Integer) train_iterator.next()).intValue();
            for (int j = 0; j < total_Col; j++) {
                train_x_D[i_][j] = X.get(temp, j);

            }
            train_y_D[i_][0] = y.get(temp, 0);
            i_++;
        }
        train_x = new SimpleMatrix(train_x_D);
        train_y = new SimpleMatrix(train_y_D);
        test_x = new SimpleMatrix(test_x_D);
        test_y = new SimpleMatrix(test_y_D);
        hashMap.put("train_x", train_x);
        hashMap.put("train_y", train_y);
        hashMap.put("test_x", test_x);
        hashMap.put("test_y", test_y);
        return hashMap;
    }

    /**
     *
     * @param x 特征矩阵x
     * @return 进行归一化后的矩阵x
     */
    //传入特征矩阵，将特征矩阵的数据归一话处理
    public static SimpleMatrix min_max_handing(SimpleMatrix x) {
        double[][] min_max_matrix = new double[x.numRows()][x.numCols()];
        for (int i = 0; i < x.numRows(); i++) {
            for (int j = 0; j < x.numCols(); j++) {
                min_max_matrix[i][j] = x.get(i, j);
            }
        }
        for (int j = 0; j < min_max_matrix[0].length; j++) {
            double max = min_max_matrix[0][j], min = min_max_matrix[0][j];
            for (int i = 0; i < min_max_matrix.length; i++) {
                if (max < min_max_matrix[i][j]) {
                    max = min_max_matrix[i][j];
                }
            }
            for (int i = 0; i < min_max_matrix.length; i++) {
                if (min > min_max_matrix[i][j]) {
                    min = min_max_matrix[i][j];
                }
            }
            if (min == max) {
                for (int i = 0; i < min_max_matrix.length; i++) {
                    min_max_matrix[i][j] = 0.5;
                }
            } else {
                for (int i = 0; i < min_max_matrix.length; i++) {
                    min_max_matrix[i][j] = (min_max_matrix[i][j] - min) / (max - min);
                }
            }

        }
        return new SimpleMatrix(min_max_matrix);
    }

    /**
     *
     * @param min 最小值
     * @param max 最大值
     * @param n 随机数数量
     * @return 随机数数组
     */
    //创造随机数数组
    public static int[] randomSet(int min, int max, int n) {
        //随机抽样，某个范围内抽n个
        int[] result = new int[n];
        for (int i = 0; i < n; i++)
            result[i] = -1;
        int count = 0;
        while (count < n) {
            int num = new Random().nextInt(max);
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

}
