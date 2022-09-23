package myLearn;

import org.ejml.simple.SimpleMatrix;

import java.io.*;

public class logisticRegression {

    private int TrainingRound;
    private float threshold;
    private float lambda;
    private float learningRate;
    private int batchSize;
    private SimpleMatrix w=null;
    public logisticRegression(int TrainingRound,float threshold,float lambda,float learningRate,int batchSize)
    {
        this.TrainingRound=TrainingRound;
        this.threshold=threshold;
        this.lambda=lambda;
        this.learningRate=learningRate;
        this.batchSize=batchSize;
    }//初始化分类器，选择学习率，正则化参数，阈值，训练轮数，批量梯度下降批数
    public double[] fit(SimpleMatrix train_x, SimpleMatrix train_y)//返回训练过程中的损失函数值数组，数组结尾赋值为-1
    {
        double[]JwRecord=new double[TrainingRound];
        int index=0;
        int m=train_x.numRows();//样本数量
        int dimension=train_x.numCols();//特征数量
        w=new SimpleMatrix(1,dimension+1);
        w.fill(0.1);//初始化模型
        SimpleMatrix stuff=new SimpleMatrix(m,1);//填充矩阵
        train_x=stuff.combine(0,1,train_x);
        for(int i=0;i<TrainingRound;i++)
        {
            SimpleMatrix tw=w.copy();
            for(int j=0;j<dimension+1;j++)
            {
                //设clean发生的概率为1
                double tempSum=0;
                int []selectedRow=dataHandling.randomSet(0,m,batchSize);//批量梯度下降,随机选取下降的向量
                for(int k=0;k<batchSize;k++)
                {
                    int x=selectedRow[k];
                    double hx=1/(1+Math.pow(Math.E,-(tw.mult(train_x.rows(x,x).transpose()).get(0))));
                    tempSum+=(hx-train_y.rows(x,x).get(0))*train_x.get(x,j);
                }
                double tempWj=tw.get(j)*(1-learningRate*lambda/m)-learningRate/m*tempSum;
                w.set(j,tempWj);
            }
            double regularization=0,mainPart=0,Jw=0;
            for(int j=0;j<m;j++)
            {
                double hx=1/(1+Math.pow(Math.E,-(tw.mult(train_x.rows(j,j).transpose()).get(0))));
                double y=train_y.rows(j,j).get(0);
                mainPart+=-y*Math.log(hx)-(1-y)*Math.log(1-hx);
            }
            for(int j=0;j<dimension+1;j++)
            {
                double wj=w.get(j);
                regularization+=j*j;
            }
            Jw=1/m*mainPart+lambda/(2*m)*regularization;
            JwRecord[index]=Jw;
            if(index==0)continue;
            if(JwRecord[index-1]-Jw<threshold){JwRecord[index+1]=-1;break;}
            index++;
        }
        return JwRecord;
    }
    public void fitFromFile(String fileName) throws IOException {
        //从指定文件名里加载模型
        FileReader fr=new FileReader(new File(fileName));
        BufferedReader br=new BufferedReader(fr);
        String line=br.readLine();
        br.close();
        fr.close();
        String []tempStrw=line.split(",");
        double[] tempw=new double[tempStrw.length];
        for(int i=0;i<tempStrw.length;i++)
            tempw[i]=Double.parseDouble(tempStrw[i]);
        w=new SimpleMatrix(1,tempStrw.length,true,tempw);
    }
    public void storeModule(String fileName)
    {
        //以模型名命名文件名，将模型永久化存储

    }
    public SimpleMatrix predict(SimpleMatrix test_x)
    {
        if(w==null)return null;
        int m=test_x.numRows();
        double []pred_y=new double[m];
        for(int i=0;i<m;i++)
        {
            double y=1/(1+Math.pow(Math.E,-(w.mult(test_x.cols(i,i).transpose())).get(0)));
            y=(y>0.5)?1:0;
            pred_y[i]=y;
        }
        return new SimpleMatrix(m,1,true,pred_y);//返回预测结果矩阵
    }
}
