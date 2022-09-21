package myLearn;

import org.ejml.simple.SimpleMatrix;

public class errorAnalysis {
    private errorAnalysis(){};

    public double cal_accuracy(SimpleMatrix pred_y, SimpleMatrix true_y)
    {
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并返回准确率
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

    public int[][] cal_matrix(SimpleMatrix pred_y,SimpleMatrix true_y)
    {
        //传入预测出的结果列表pred_y和真实的结果列表true_y,并以二维列表返回混淆矩阵
        return new int[10][10];
    }
}
