package org.example;


import myLearn.*;
import myLearn.utils.FiveTuple;
import myLearn.utils.FourTuple;
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

        FourTuple<SimpleMatrix, Double, Double, Double> dataPack=UsingController.trainingKnnControl(9,"JDT.csv","Knn2",0.8);
        System.out.println("混淆矩阵，准确率，精度，召回率");
        System.out.println(dataPack.first);
        System.out.println(dataPack.second);
        System.out.println(dataPack.third);
        System.out.println(dataPack.fourth);
        FiveTuple<double[],SimpleMatrix, Double, Double, Double> dataPack1=UsingController.trainingLogisticRegressionControl(3000,0.000001,0,0.1,797,"JDT.csv","lgr2",0.8);
        System.out.println("混淆矩阵，准确率，精度，召回率");
        //first是一个数组，里面为损失函数变化值
        System.out.println(dataPack1.second);
        System.out.println(dataPack1.third);
        System.out.println(dataPack1.fourth);
        System.out.println(dataPack1.fifth);
        UsingController.predictControl("LogisticRegression1","Lucene.csv");
    }
}
