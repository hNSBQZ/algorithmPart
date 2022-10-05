package myLearn;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataHandling {
    //该类用于数据从文件中的读取
    private DataHandling(){}

    //读取文件，最后一行作为标签clean为1，buggy为0分为两个SimpleMatrix类型矩阵，X，y
    //返回一个哈希表X的标签为“x”,y的标签为“y”
    public static HashMap<String,SimpleMatrix>loadMatrixFromCsv_xy(String fileName){
        HashMap<String,SimpleMatrix> hashMap= new HashMap<String,SimpleMatrix>();
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
            double [][]maxtrix_x = new double [list.size()-1][list.get(0).length-1];
            double [][]maxtrix_y = new double[list.size()-1][1];
            for(int i = 1;i < list.size();i++){
                for(int j = 0;j < list.get(i).length;j++){
                    String [] temp = list.get(i);
                    if(!(temp[j].equals("clean")||temp[j].equals("buggy"))){
                        maxtrix_x[i-1][j] = Double.parseDouble(temp[j]);
                        //System.out.println(temp[j]);
                    }else if(temp[j].equals("clean")){
                        maxtrix_y[i-1][0] = 1;
                    }else{
                        maxtrix_y[i-1][0] = 0;
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
        hashMap.put("x",simpleMatrix_x);
        hashMap.put("y",simpleMatrix_y);
        return hashMap;
    }
    //读取文件将整体作为作为一个大矩阵
    public static SimpleMatrix loadMatrixFromCsv(String fileName)
    {
        SimpleMatrix simpleMatrix = null;
        try {
            ArrayList <String[]>list = new ArrayList();
            DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
            CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
            String[] strs;
            while ((strs = csvReader.readNext()) != null) {
                //System.out.println(Arrays.deepToString(strs));
                list.add(strs);
            }
            csvReader.close();
            double [][]maxtrix = new double [list.size()-1][list.get(0).length];
            for(int i = 1;i < list.size();i++){
                for(int j = 0;j < list.get(i).length;j++){
                    String [] temp = list.get(i);
                    if(!(temp[j].equals("clean")||temp[j].equals("buggy"))){
                        maxtrix[i-1][j] = Double.parseDouble(temp[j]);
                        //System.out.println(temp[j]);
                    }else if(temp[j].equals("clean")){
                        maxtrix[i-1][j] = 1;
                    }else{
                        maxtrix[i-1][j] = 0;
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
    //传入特征矩阵X和标签y矩阵传入，选择比率，返回HashMap，键值分别为train_x,train_y,test_x,test_y
    public static HashMap<String,SimpleMatrix> train_test_split(SimpleMatrix X, SimpleMatrix y,double train_Size)
    {
        /*
         * 读取矩阵X与结果矩阵Y，并按照test_size的比例进行测试集与训练集划分
         * 返回一个map，四个对应的字符串键值分别为train_x,train_y,test_x,test_y,
         * */
        SimpleMatrix train_x,train_y,test_x,test_y;
        HashMap hashMap = new HashMap<String,SimpleMatrix>();
        HashSet train_Set = new HashSet<Integer>();
        HashSet test_Set = new HashSet<Integer>();
        //HashSet temp = new HashSet<Integer>();
        int total_Col = X.numCols();
        int total_Row = X.numRows();
        int train_Row = (int)(total_Row*train_Size);
        double[][] train_x_D = new double[train_Row][total_Col],
                train_y_D = new double[train_Row][1],
                test_x_D = new double[total_Row-train_Row][total_Col],
                test_y_D = new double[total_Row-train_Row][1];
//        train_x = new SimpleMatrix(train_Row,total_Col);
//        train_y = new SimpleMatrix(train_Row,1);
//        test_x = new SimpleMatrix(total_Row-train_Row,total_Col);
//        test_y = new SimpleMatrix(total_Row-train_Row,1);

        for(int i = 0;i < total_Row;i++){
            test_Set.add(i);
        }
//        int[] train_Index = new int[train_Row];
        do{
            train_Set.add((int)(Math.random()*(total_Row-1)));
        }while (train_Set.size() < train_Row);
        test_Set.removeAll(train_Set);
        Iterator train_iterator = train_Set.iterator();
        Iterator test_iterator = test_Set.iterator();
        int i = 0;
        int i_ = 0;

        while(test_iterator.hasNext()){
            int temp = ((Integer)test_iterator.next()).intValue();
            for(int j = 0;j < total_Col;j++){
                test_x_D[i][j] = X.get(temp,j);
            }
            test_y_D[i][0] = y.get(temp,0);
            i++;
        }

        while(train_iterator.hasNext()){
            int temp = ((Integer)train_iterator.next()).intValue();
            for(int j = 0;j < total_Col;j++){
                train_x_D[i_][j] = X.get(temp,j);

            }
            train_y_D[i_][0] = y.get(temp,0);
            i_++;
        }
        train_x = new SimpleMatrix(train_x_D);
        train_y = new SimpleMatrix(train_y_D);
        test_x = new SimpleMatrix(test_x_D);
        test_y = new SimpleMatrix(test_y_D);
        hashMap.put("train_x",train_x);
        hashMap.put("train_y",train_y);
        hashMap.put("test_x",test_x);
        hashMap.put("test_y",test_y);
        return hashMap;
    }

    //传入特征矩阵，将特征矩阵的数据归一话处理
    public static SimpleMatrix min_max_handing(SimpleMatrix x){
        double [][] min_max_matrix = new double[x.numRows()][x.numCols()];
        for(int i = 0;i < x.numRows();i++ ){
            for(int j = 0;j < x.numCols();j++) {
                min_max_matrix[i][j]= x.get(i,j);
            }
        }
        for(int j = 0;j < min_max_matrix[0].length;j++){
            double max = min_max_matrix[0][j],min= min_max_matrix[0][j];
            for(int i = 0;i<min_max_matrix.length;i++){
                if(max<min_max_matrix[i][j]){
                    max = min_max_matrix[i][j];
                }
            }
            for(int i = 0;i<min_max_matrix.length;i++){
                if(min>min_max_matrix[i][j]){
                    min = min_max_matrix[i][j];
                }
            }
            if(min==max){
                for(int i = 0;i<min_max_matrix.length;i++)
                {
                    min_max_matrix[i][j] = 0.5;
                }
            }else{
                for(int i = 0;i<min_max_matrix.length;i++){
                    min_max_matrix[i][j] = (min_max_matrix[i][j]-min)/(max-min);
                }
            }

        }
        return new SimpleMatrix(min_max_matrix);
    }

    //创造随机数数组
    public static int[] randomSet(int min,int max,int n)
    {
        //随机抽样，某个范围内抽n个
        int[] result = new int[n];
        for(int i=0;i<n;i++)
            result[i]=-1;
        int count = 0;
        while(count < n) {
            int num =new Random().nextInt(max);
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    private static String[] insert(String[] arr, String... str)
    {
        int size=arr.length;
        int newSize=size+str.length;
        String[] tmp =new String[newSize];
        for(int i=0;i<size;i++)
        {
            tmp[i]=arr[i];
        }
        for(int i=size;i<newSize;i++)
        {
            tmp[i]=str[i-size];
        }
        return tmp;
    }
    public static void appendNewRowToCsv(String oldFileName,String newFileName,SimpleMatrix result) {
        ArrayList <String[]>list = new ArrayList();
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(new File(oldFileName)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String[] strs;
        while (true) {
            try {
                if (!((strs = csvReader.readNext()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(Arrays.deepToString(strs));
            list.add(strs);
        }
        try {
            csvReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.set(0,insert(list.get(0),"result"));
        for(int i=1;i<list.size();i++)
        {
            String resultStr=(result.get(i-1)==1)?"clean":"buggy";
            list.set(i,insert(list.get(i),resultStr));
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
    }

}
