package myLearn;

import org.ejml.simple.SimpleMatrix;

public class logisticRegression {

    private int TrainingRound;
    private float threshold;
    private float lambda;
    private float learningRate;
    private int batchSize;
    private SimpleMatrix module;
    public logisticRegression(int TrainingRound,float threshold,float lambda,float learningRate,int batchSize)
    {
        this.TrainingRound=TrainingRound;
        this.threshold=threshold;
        this.lambda=lambda;
        this.learningRate=learningRate;
        this.batchSize=batchSize;
    }//初始化分类器，选择邻居数量，要感兴趣可以提供选择距离方法，k近邻搜索方法
    public void fit(SimpleMatrix train_x, SimpleMatrix train_y)
    {
        int m=train_x.numRows();//样本数量
        int dimension=train_x.numCols();//特征数量
        SimpleMatrix w=new SimpleMatrix(1,dimension+1);
        w.fill(0.1);//初始化模型
        SimpleMatrix stuff=new SimpleMatrix(m,1);//填充矩阵
        train_x=stuff.combine(0,1,train_x);
        for(int i=0;i<TrainingRound;i++)
        {
            SimpleMatrix tw=w.copy();
            for(int j=0;j<dimension+1;j++)
            {
                double tempSum=0;
                int []selectedRow=dataHandling.randomSet(0,m,batchSize);//批量梯度下降,随机选取下降的向量
                for(int k=0;k<batchSize;k++)
                {
                    int x=selectedRow[k];
                    double hx=1/(1+Math.pow(Math.E,-(tw.mult(train_x.rows(x,x).transpose()).get(0))));
                    tempSum+=(hx-train_y.rows(x,x).get(0))*train_x.get(x,j);
                }
                double tempWj=tw.get(j)*(1-learningRate*lambda/m);
            }
        }

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
