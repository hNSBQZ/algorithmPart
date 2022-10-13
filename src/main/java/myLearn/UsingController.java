package myLearn;

import myLearn.classifiers.KnnClassifier;
import myLearn.classifiers.LogisticRegression;
import myLearn.dataController.DataHandling;
import myLearn.dataController.ErrorAnalysis;
import myLearn.utils.FiveTuple;
import myLearn.utils.FourTuple;
import org.ejml.simple.SimpleMatrix;
import myLearn.fileController.CsvHandling;
import java.util.HashMap;

import static myLearn.dataController.ErrorAnalysis.*;

/**
 * 提供完整的训练和预测流程便于调用
 * @author hqz
 * @version 1.0
 *
 */
public class UsingController {
    private UsingController() {
    }

    /**
     *训练knn模型
     * @param k knn算法k值
     * @param TrainingFile 用于训练的数据文件路径
     * @param moduleName 用于存储模型的文件路径
     * @param rate 测试集划分比率
     * @return 四元组，第一项为混淆矩阵，第二项准确率，第三项精准度，第四项回归率
     */
    public static FourTuple<SimpleMatrix, Double, Double, Double> trainingKnnControl(int k, String TrainingFile, String moduleName, double rate) {
        KnnClassifier knn = new KnnClassifier(k);
        HashMap<String, SimpleMatrix> temp = CsvHandling.loadMatrixFromCsv_xy(TrainingFile);
        SimpleMatrix X = temp.get("x");
        SimpleMatrix y = temp.get("y");

        //训练集划分
        temp = DataHandling.train_test_split(X, y, rate);
        SimpleMatrix train_x = temp.get("train_x");
        SimpleMatrix train_y = temp.get("train_y");
        SimpleMatrix test_x = temp.get("test_x");
        SimpleMatrix test_y = temp.get("test_y");

        knn.fit(train_x, train_y);
        knn.storeModule(moduleName);
        SimpleMatrix predict_y = knn.predict(test_x);

        SimpleMatrix confusionMatrix = ErrorAnalysis.cal_matrix(predict_y, test_y);
        double acc = cal_accuracy(predict_y, test_y);
        double pre = cal_precision(predict_y, test_y);
        double recall = cal_recall(predict_y, test_y);

        FourTuple<SimpleMatrix, Double, Double, Double> dataPackage = new FourTuple<SimpleMatrix, Double, Double, Double>(confusionMatrix, acc, pre, recall);
        return dataPackage;
    }

    /**
     *训练线性回归模型
     * @param TrainingRound 训练轮数
     * @param threshold 阈值
     * @param lambda 正则化参数
     * @param learningRate 学习率
     * @param batchSize 梯度下降的向量下降数量
     * @param TrainingFile 用于训练的数据文件路径
     * @param moduleName 用于存储模型的文件路径
     * @param rate 测试集划分比率
     * @return 五元组，分别为损失函数变化数组，混淆矩阵，准确率，精度，回归率
     */
    public static FiveTuple<double[], SimpleMatrix, Double, Double, Double> trainingLogisticRegressionControl(int TrainingRound, double threshold, double lambda, double learningRate, int batchSize, String TrainingFile, String moduleName, double rate) {
        HashMap<String, SimpleMatrix> temp = CsvHandling.loadMatrixFromCsv_xy(TrainingFile);
        SimpleMatrix X = temp.get("x");
        SimpleMatrix y = temp.get("y");

        //训练集划分
        temp = DataHandling.train_test_split(X, y, rate);
        SimpleMatrix train_x = temp.get("train_x");
        SimpleMatrix train_y = temp.get("train_y");
        SimpleMatrix test_x = temp.get("test_x");
        SimpleMatrix test_y = temp.get("test_y");

        LogisticRegression l = new LogisticRegression(TrainingRound, threshold, lambda, learningRate, batchSize);
        double[] Jw = l.fit(train_x, train_y);
        l.storeModule(moduleName);
        SimpleMatrix predict_y = l.predict(test_x);
        SimpleMatrix confusionMatrix = ErrorAnalysis.cal_matrix(predict_y, test_y);
        double acc = cal_accuracy(predict_y, test_y);
        double pre = cal_precision(predict_y, test_y);
        double recall = cal_recall(predict_y, test_y);
        FiveTuple<double[], SimpleMatrix, Double, Double, Double> dataPackage = new FiveTuple<double[], SimpleMatrix, Double, Double, Double>(Jw, confusionMatrix, acc, pre, recall);
        return dataPackage;
    }

    /**
     * 将预测结果添加到用户上传的数据文件上
     * @param moduleName 模型名
     * @param uploadFile 用户上传的数据文件路径
     */
    public static void predictControl(String moduleName, String uploadFile) {
        //System.out.println(moduleName);
        char moduleType = moduleName.charAt(moduleName.lastIndexOf('/') + 1);
        SimpleMatrix result = null;

        if (moduleType == 'L' || moduleType == 'l') {
            LogisticRegression l = new LogisticRegression();
            l.fitFromFile(moduleName);
            SimpleMatrix test_x = CsvHandling.loadMatrixFromCsv(uploadFile);
            //System.out.println(test_x);
            //实际应该使用loadMatrixFromCsv
            result = l.predict(test_x);
            String newFileName = uploadFile.substring(0, uploadFile.lastIndexOf('.')) + "-result.csv";
            CsvHandling.appendNewRowToCsv(uploadFile, newFileName, result);
        }
        if (moduleType == 'K' || moduleType == 'k') {
            KnnClassifier k = new KnnClassifier();
            k.fitFromFile(moduleName);
            SimpleMatrix test_x = CsvHandling.loadMatrixFromCsv(uploadFile);
            //实际应该使用loadMatrixFromCsv
            result = k.predict(test_x);
            String newFileName = uploadFile.substring(0, uploadFile.lastIndexOf('.')) + "-result.csv";
            CsvHandling.appendNewRowToCsv(uploadFile, newFileName, result);
        }
    }

}
