package org.example;
import myLearn.dataHandling;
import myLearn.logisticRegression;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.util.HashMap;

import static myLearn.dataHandling.*;
import static myLearn.errorAnalysis.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SimpleMatrix x=loadMatrixFromCsv_xy("JDT.csv").get("x");
        SimpleMatrix y=loadMatrixFromCsv_xy("JDT.csv").get("y");

        x=min_max_handing(x);
        //System.out.println(x);
        HashMap<String,SimpleMatrix>testAndTrain=train_test_split(x,y,0.8);
        SimpleMatrix train_x=testAndTrain.get("train_x");
        SimpleMatrix test_x=testAndTrain.get("test_x");
        SimpleMatrix train_y=testAndTrain.get("train_y");
        SimpleMatrix test_y=testAndTrain.get("test_y");

        logisticRegression lg=new logisticRegression(3000,0.001,0,0.1,200);
        lg.fit(train_x,train_y);
        SimpleMatrix pred_y=lg.predict(test_x);
        System.out.println(pred_y);
        SimpleMatrix confusionMatrix=cal_matrix(pred_y,test_y);
        System.out.println(confusionMatrix);
    }
}
