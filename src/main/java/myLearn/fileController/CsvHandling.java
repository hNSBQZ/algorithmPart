package myLearn.fileController;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用于从csv中读取数据集和向csv中写入预测结果
 * @author hqz wl
 * @version 1.0
 *
 */
public class CsvHandling {
    //读取文件，最后一行作为标签clean为1，buggy为0分为两个SimpleMatrix类型矩阵，X，y
    //返回一个哈希表X的标签为“x”,y的标签为“y”
    private CsvHandling(){}

    /**
     *默认最后一列为标签，前面各列为特征，读取csv文件数据
     * @param fileName 目标文件路径
     * @return 哈希表，键值y对应特征矩阵，键值x对应特征矩阵
     *
     */
    public static HashMap<String, SimpleMatrix> loadMatrixFromCsv_xy(String fileName) {
        HashMap<String, SimpleMatrix> hashMap = new HashMap<String, SimpleMatrix>();
        SimpleMatrix simpleMatrix_x = null;
        SimpleMatrix simpleMatrix_y = null;
        try {
            ArrayList<String[]> list = new ArrayList();
            DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
            CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
            String[] strs;
            while ((strs = csvReader.readNext()) != null) {
                //System.out.println(Arrays.deepToString(strs));
                list.add(strs);
            }
            csvReader.close();
            double[][] maxtrix_x = new double[list.size() - 1][list.get(0).length - 1];
            double[][] maxtrix_y = new double[list.size() - 1][1];
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    String[] temp = list.get(i);
                    if (!(temp[j].equals("clean") || temp[j].equals("buggy"))) {
                        maxtrix_x[i - 1][j] = Double.parseDouble(temp[j]);
                        //System.out.println(temp[j]);
                    } else if (temp[j].equals("clean")) {
                        maxtrix_y[i - 1][0] = 1;
                    } else {
                        maxtrix_y[i - 1][0] = 0;
                    }

                }
            }
            /*
            测试
            * */
//            for(double[] d:maxtrix){
//                for(double d_:d){
//                    System.out.print(d_+" ");
//                }
//                System.out.println("");
//            }
            simpleMatrix_x = new SimpleMatrix(maxtrix_x);
            simpleMatrix_y = new SimpleMatrix(maxtrix_y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put("x", simpleMatrix_x);
        hashMap.put("y", simpleMatrix_y);
        return hashMap;
    }

    /**
     *
     * @param fileName 目标文件路径
     * @return 将csv文件中的内容作为整个特征矩阵返回
     */
    public static SimpleMatrix loadMatrixFromCsv(String fileName) {
        SimpleMatrix simpleMatrix = null;
        try {
            ArrayList<String[]> list = new ArrayList();
            DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
            CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
            String[] strs;
            while ((strs = csvReader.readNext()) != null) {
                //System.out.println(Arrays.deepToString(strs));
                list.add(strs);
            }
            csvReader.close();
            double[][] maxtrix = new double[list.size() - 1][list.get(0).length];
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    String[] temp = list.get(i);
                    if (!(temp[j].equals("clean") || temp[j].equals("buggy"))) {
                        maxtrix[i - 1][j] = Double.parseDouble(temp[j]);
                        //System.out.println(temp[j]);
                    } else if (temp[j].equals("clean")) {
                        maxtrix[i - 1][j] = 1;
                    } else {
                        maxtrix[i - 1][j] = 0;
                    }

                }
            }
            /*
            测试
            * */
//            for(double[] d:maxtrix){
//                for(double d_:d){
//                    System.out.print(d_+" ");
//                }
//                System.out.println("");
//            }
            simpleMatrix = new SimpleMatrix(maxtrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleMatrix;
    }

    /**
     *
     * @param arr 字符串数组
     * @param str 多个字符串
     * @return 将多个字符串加入后的新数组
     */
    private static String[] insert(String[] arr, String... str) {
        int size = arr.length;
        int newSize = size + str.length;
        String[] tmp = new String[newSize];
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[i];
        }
        for (int i = size; i < newSize; i++) {
            tmp[i] = str[i - size];
        }
        return tmp;
    }

    /**
     *将结果追加到数据文件后面并生成新的csv文件
     * @param oldFileName 原始用于提供预测数据的文件路径
     * @param newFileName 用于保存结果的文件路径
     * @param result 预测的结果矩阵
     *
     */
    public static void appendNewRowToCsv(String oldFileName, String newFileName, SimpleMatrix result) {
        ArrayList<String[]> list = new ArrayList();
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(new File(oldFileName)));
            CSVReader csvReader = null;
            csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
            String[] strs;
            while (true) {
                if (!((strs = csvReader.readNext()) != null)) break;
                //System.out.println(Arrays.deepToString(strs));
                list.add(strs);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        list.set(0, insert(list.get(0), "result"));
        for (int i = 1; i < list.size(); i++) {
            String resultStr = (result.get(i-1) == 1) ? "clean" : "buggy";
            list.set(i, insert(list.get(i), resultStr));
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        CSVWriter writer = new CSVWriter(osw);
        writer.writeAll(list);
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
