package org.example;

import myLearn.DataHandling;
import myLearn.KnnClassifier;
import myLearn.LogisticRegression;
import org.ejml.simple.SimpleMatrix;

public class PredictControl {
    private PredictControl(){}
    public static void predictControl(String moduleName,String uploadFile)
    {
        SimpleMatrix result=null;
        if(moduleName.charAt(0)=='L'||moduleName.charAt(0)=='l')
        {
            LogisticRegression l=new LogisticRegression();
            l.fitFromFile(moduleName);
            SimpleMatrix test_x= DataHandling.loadMatrixFromCsv_xy(uploadFile).get("x");
            //System.out.println(test_x);
            //实际应该使用loadMatrixFromCsv
            result=l.predict(test_x);
            //System.out.println(result);
        }
        if(moduleName.charAt(0)=='K'||moduleName.charAt(0)=='k')
        {
            KnnClassifier k=new KnnClassifier();
            k.fitFromFile(moduleName);
            SimpleMatrix test_x= DataHandling.loadMatrixFromCsv_xy(uploadFile).get("x");
            //实际应该使用loadMatrixFromCsv
            result =k.predict(test_x);
            //System.out.println(result);
        }
    }
}
