package myLearn;

import org.ejml.simple.SimpleMatrix;

public class errorAnalysis {
    private errorAnalysis(){};

    public double cal_accuracy(SimpleMatrix pred_y, SimpleMatrix true_y) {

        return 1;
    }

    public double cal_precision(SimpleMatrix pred_y,SimpleMatrix true_y)
    {
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并返回精度
        return 1;
    }

    public double cal_recall(SimpleMatrix pred_y,SimpleMatrix true_y)
    {
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并返回召回率
        return 1;
    }

    public SimpleMatrix cal_matrix(SimpleMatrix pred_y,SimpleMatrix true_y)
    {
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并以二维列表返回混淆矩阵
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并返回准确率
        int TP = 0, FN = 0, FP = 0, TN = 0;
        for (int i = 0; i < pred_y.numRows(); i++) {
            if (Math.abs(pred_y.get(i) - 1) < 0.001 && Math.abs(true_y.get(i) - 1) < 0.001)
                TP += 1;
            else if (Math.abs(pred_y.get(i) - 1) > 0.001 && Math.abs(true_y.get(i) - 1) < 0.001)
                FN += 1;
            else if (Math.abs(pred_y.get(i) - 1) < 0.001 && Math.abs(true_y.get(i) - 1) > 0.001)
                FP += 1;
            else
                TN += 1;
        }
        SimpleMatrix confusionMatrix = new SimpleMatrix(2, 2);
        confusionMatrix.set(0,TP);
        confusionMatrix.set(0,FN);
        confusionMatrix.set(0,FP);
        confusionMatrix.set(0,TN);
        return confusionMatrix;
    }
}
