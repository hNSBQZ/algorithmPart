package myLearn;
import com.opencsv.CSVReader;
import org.ejml.simple.SimpleMatrix;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class dataHandling {
    private dataHandling(){}

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
        double[][] train_x_D = new double[train_Row][total_Col-1],
                train_y_D = new double[train_Row][1],
                test_x_D = new double[total_Row-train_Row][total_Col-1],
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
            for(int j = 0;j < total_Col-1;j++){
                test_x_D[i][j] = X.get(temp,j);
            }
            test_y_D[i][0] = y.get(temp,0);
            i++;
        }

        while(train_iterator.hasNext()){
            int temp = ((Integer)train_iterator.next()).intValue();
            for(int j = 0;j < total_Col-1;j++){
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
    public static int[] randomSet(int min,int max,int n)
    {
        //随机抽样，某个范围内抽n个
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            System.out.println(num);
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
}
