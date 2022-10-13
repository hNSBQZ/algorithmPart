package org.example;


import myLearn.UsingController;
import myLearn.utils.FiveTuple;
import myLearn.utils.FourTuple;
import org.ejml.simple.SimpleMatrix;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

//        //Csv与excel相互转换的例子
//        CsvAndXls.CsvToExcel("FileForTrain/JDT.csv","JDT.xls");
//        CsvAndXls.ExcelToCsv("JDT.xls","abc.csv");
//        //knn训练例子

        FourTuple<SimpleMatrix, Double, Double, Double> dataPack= UsingController.trainingKnnControl(7,"FileForTrain/Lucene.csv","Modules/Knn1",0.8);
        System.out.println("混淆矩阵，准确率，精度，召回率");
        System.out.println(dataPack.first);
        System.out.println(dataPack.second);
        System.out.println(dataPack.third);
        System.out.println(dataPack.fourth);

//        //线性回归训练例子
//        FiveTuple<double[], SimpleMatrix, Double, Double, Double> dataPack1 = UsingController.trainingLogisticRegressionControl(3000, 0.000001, 0, 0.1, 797, "FileForTrain/JDT.csv", "Modules/lgr2", 0.8);
//        System.out.println("混淆矩阵，准确率，精度，召回率");
//        //first是一个数组，里面为损失函数变化值
//        System.out.println(dataPack1.second);
//        System.out.println(dataPack1.third);
//        System.out.println(dataPack1.fourth);
//        System.out.println(dataPack1.fifth);

        //用户预测例子
        UsingController.predictControl("Modules/Knn1","FileForPerdict/Lucene.csv");
    }
}
