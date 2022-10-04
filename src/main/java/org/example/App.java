package org.example;


import myLearn.DataHandling;
import myLearn.ErrorAnalysis;
import myLearn.KnnClassifier;
import myLearn.LogisticRegression;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //代码示例
        //训练模型
        //1.管理员提交用来训练的文档,从中提取标签矩阵和特征矩阵

        HashMap<String,SimpleMatrix> temp= DataHandling.loadMatrixFromCsv_xy("JDT.csv");
        SimpleMatrix X=temp.get("x");
        SimpleMatrix y=temp.get("y");
        System.out.println(X.numCols());

        //训练集划分
        temp=DataHandling.train_test_split(X,y,0.8);
        SimpleMatrix train_x=temp.get("train_x");
        SimpleMatrix train_y=temp.get("train_y");
        SimpleMatrix test_x=temp.get("test_x");
        SimpleMatrix test_y=temp.get("test_y");
        System.out.println(test_x.numCols());

        LogisticRegression l1=new LogisticRegression(3000,0.000001,0,0.1,797);
        l1.fit(train_x,train_y);
        l1.storeModule("LogisticRegression1");
        SimpleMatrix predict_y=l1.predict(test_x);
        SimpleMatrix confusionMatrix=ErrorAnalysis.cal_matrix(predict_y,test_y);
        System.out.println(confusionMatrix);

        KnnClassifier k1=new KnnClassifier();
        k1.fit(train_x,train_y);
        k1.storeModule("Knn1");
        predict_y=k1.predict(test_x);
        System.out.println(predict_y);
        System.out.println(test_y);
        confusionMatrix=ErrorAnalysis.cal_matrix(predict_y,test_y);
        System.out.println(confusionMatrix);

        PredictControl.predictControl("LogisticRegression1","JDT.csv");
    }
}
