package myLearn;

import myLearn.utils.FiveTuple;
import myLearn.utils.FourTuple;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;

import static myLearn.DataHandling.appendNewRowToCsv;
import static myLearn.ErrorAnalysis.*;

public class UsingController {
    private UsingController(){}

    public static FourTuple<SimpleMatrix, Double, Double, Double> trainingKnnControl(int k, String TrainingFile, String moduleName, double rate)
    {
        KnnClassifier knn=new KnnClassifier(k);
        HashMap<String,SimpleMatrix> temp= DataHandling.loadMatrixFromCsv_xy(TrainingFile);
        SimpleMatrix X=temp.get("x");
        SimpleMatrix y=temp.get("y");

        //训练集划分
        temp=DataHandling.train_test_split(X,y,rate);
        SimpleMatrix train_x=temp.get("train_x");
        SimpleMatrix train_y=temp.get("train_y");
        SimpleMatrix test_x=temp.get("test_x");
        SimpleMatrix test_y=temp.get("test_y");

        knn.fit(train_x,train_y);
        knn.storeModule(moduleName);
        SimpleMatrix predict_y=knn.predict(test_x);

        SimpleMatrix confusionMatrix=ErrorAnalysis.cal_matrix(predict_y,test_y);
        double acc=cal_accuracy(predict_y,test_y);
        double pre=cal_precision(predict_y,test_y);
        double recall=cal_recall(predict_y,test_y);

        FourTuple<SimpleMatrix, Double,Double,Double> dataPackage=new FourTuple<SimpleMatrix, Double,Double,Double>(confusionMatrix,acc,pre,recall);
        return dataPackage;
    }

    public static FiveTuple<double[], SimpleMatrix, Double, Double, Double> trainingLogisticRegressionControl(int TrainingRound, double threshold, double lambda, double learningRate, int batchSize, String TrainingFile, String moduleName, double rate)
    {
        HashMap<String,SimpleMatrix> temp= DataHandling.loadMatrixFromCsv_xy(TrainingFile);
        SimpleMatrix X=temp.get("x");
        SimpleMatrix y=temp.get("y");

        //训练集划分
        temp=DataHandling.train_test_split(X,y,rate);
        SimpleMatrix train_x=temp.get("train_x");
        SimpleMatrix train_y=temp.get("train_y");
        SimpleMatrix test_x=temp.get("test_x");
        SimpleMatrix test_y=temp.get("test_y");

        LogisticRegression l=new LogisticRegression(TrainingRound,threshold,lambda,learningRate,batchSize);
        double[] Jw=l.fit(train_x,train_y);
        l.storeModule(moduleName);
        SimpleMatrix predict_y=l.predict(test_x);
        SimpleMatrix confusionMatrix=ErrorAnalysis.cal_matrix(predict_y,test_y);
        double acc=cal_accuracy(predict_y,test_y);
        double pre=cal_precision(predict_y,test_y);
        double recall=cal_recall(predict_y,test_y);
        FiveTuple<double[],SimpleMatrix, Double,Double,Double> dataPackage=new FiveTuple<double[], SimpleMatrix, Double, Double, Double>(Jw,confusionMatrix,acc,pre,recall);
        return dataPackage;
    }

    public static void predictControl(String moduleName,String uploadFile)
    {
        System.out.println(moduleName);
        char moduleType=moduleName.charAt(moduleName.lastIndexOf('/')+1);
        SimpleMatrix result=null;

        if(moduleType=='L'||moduleType=='l')
        {
            LogisticRegression l=new LogisticRegression();
            l.fitFromFile(moduleName);
            SimpleMatrix test_x= DataHandling.loadMatrixFromCsv(uploadFile);
            //System.out.println(test_x);
            //实际应该使用loadMatrixFromCsv
            result=l.predict(test_x);
            //System.out.println(result);
            String newFileName=uploadFile.substring(0,uploadFile.lastIndexOf('.'))+"-result.csv";
            appendNewRowToCsv(uploadFile,newFileName,result);
        }
        if(moduleType=='K'||moduleType=='k')
        {
            KnnClassifier k=new KnnClassifier();
            k.fitFromFile(moduleName);
            SimpleMatrix test_x= DataHandling.loadMatrixFromCsv(uploadFile);
            //实际应该使用loadMatrixFromCsv
            result =k.predict(test_x);
            //System.out.println(result);
            String newFileName=uploadFile.substring(0,uploadFile.lastIndexOf('.'))+"-result.csv";
            appendNewRowToCsv(uploadFile,newFileName,result);
        }
    }

}
