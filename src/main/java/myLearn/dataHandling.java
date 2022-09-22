package myLearn;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.Map;

public class dataHandling {
    private dataHandling(){}

    public static SimpleMatrix loadMatrixFromCsv(String fileName)
    {
        /*从文件fileName中读取数据并生成矩阵*/
        return new SimpleMatrix(1,1);
    }

    public static Map<String,SimpleMatrix> train_test_split(SimpleMatrix X, SimpleMatrix y, double test_Size)
    {
        /*
        * 读取矩阵X与结果矩阵Y，并按照test_size的比例进行测试集与训练集划分
        * 返回一个map，四个对应的字符串键值分别为train_x,train_y,test_x,test_y,
        * */
        return new HashMap<String, SimpleMatrix>();
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
