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
}
