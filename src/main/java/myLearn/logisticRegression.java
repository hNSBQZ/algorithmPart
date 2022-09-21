package myLearn;

import org.ejml.simple.SimpleMatrix;

public class logisticRegression {
    public logisticRegression(int TrainingRound,float treshold,float lambda,float learningRate,int batchSize)
    {

    }//初始化分类器，选择邻居数量，要感兴趣可以提供选择距离方法，k近邻搜索方法
    public void fit(SimpleMatrix train_x, SimpleMatrix train_y)
    {
        //训练模型，其实就是把训练集的点都记录下来。。。数据结构你自己选
    }
    public void fitFromFile(String fileName)
    {
        //从指定文件名里加载模型
    }
    public void storeModule(String fileName)
    {
        //以模型名命名文件名，将模型永久化存储
    }
    public SimpleMatrix predict(SimpleMatrix test_y)
    {
        return new SimpleMatrix(1,10);//返回预测结果矩阵
    }
}
