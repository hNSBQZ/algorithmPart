package myLearn.classifiers;

import myLearn.dataController.DataHandling;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
/**
 * 实现逻辑回归
 * @author hqz
 * @version 1.0
 *
 */
public class LogisticRegression {
    private int TrainingRound = 0;
    private double threshold = 0;
    private double lambda = 0;
    private double learningRate = 0;
    private int batchSize = 0;
    private SimpleMatrix w = null;

    /**
     *
     * @param TrainingRound 训练轮数
     * @param threshold 收敛阈值
     * @param lambda 正则化参数
     * @param learningRate 学习率
     * @param batchSize 批量下降每一批的数量，数量大于样本数则按样本数计算
     */
    public LogisticRegression(int TrainingRound, double threshold, double lambda, double learningRate, int batchSize) {
        this.TrainingRound = TrainingRound;
        this.threshold = threshold;
        this.lambda = lambda;
        this.learningRate = learningRate;
        this.batchSize = batchSize;
    }//初始化分类器，选择学习率，正则化参数，阈值，训练轮数，批量梯度下降批数

    public LogisticRegression() {
    }

    /**
     *用于训练模型w
     * @param train_x SimpleMatrix类型的特征矩阵x
     * @param train_y SimpleMatrix类型的01标签矩阵y
     * @return 返回double类型数组，用于记录过程中损失函数变化，-1代表训练中止
     *
     */
    public double[] fit(SimpleMatrix train_x, SimpleMatrix train_y)//返回训练过程中的损失函数值数组，数组结尾赋值为-1
    {
        double[] JwRecord = new double[TrainingRound + 1];
        train_x = DataHandling.min_max_handing(train_x);
        //System.out.println(train_x);
        int m = train_x.numRows();//样本数量
        if (batchSize > m) batchSize = m;
        int dimension = train_x.numCols();//特征数量
        w = new SimpleMatrix(1, dimension + 1);
        w.fill(0.1);//初始化模型
        SimpleMatrix stuff = new SimpleMatrix(m, 1);//填充矩阵
        stuff.fill(1);
        train_x = stuff.combine(0, 1, train_x);
        for (int i = 0; i < TrainingRound; i++) {
            SimpleMatrix tw = w.copy();
            for (int j = 0; j < dimension + 1; j++) {
                //设clean发生的概率为1
                //System.out.println("j is"+j);
                double tempSum = 0;
                //System.out.println(m+":"+batchSize);
                int[] selectedRow = DataHandling.randomSet(0, m, batchSize);//批量梯度下降,随机选取下降的向量
                for (int k = 0; k < batchSize; k++) {
                    //System.out.println("k is"+k);
                    int x = selectedRow[k];
                    double hx = 1 / (1 + Math.pow(Math.E, -(tw.mult(train_x.rows(x, x + 1).transpose()).get(0))));
                    tempSum += (hx - train_y.rows(x, x + 1).get(0)) * train_x.get(x, j);
                }
                double tempWj = tw.get(j) * (1 - learningRate * lambda / m) - learningRate / m * tempSum;
                w.set(j, tempWj);
            }
            //System.out.println("i is"+i);
            double regularization = 0, mainPart = 0, Jw = 0;
            for (int j = 0; j < m; j++) {
                double hx = 1 / (1 + Math.pow(Math.E, -(tw.mult(train_x.rows(j, j + 1).transpose()).get(0))));
                double y = train_y.rows(j, j + 1).get(0);
                mainPart += -y * Math.log(hx) - (1 - y) * Math.log(1 - hx);
            }
            for (int j = 0; j < dimension + 1; j++) {
                double wj = w.get(j);
                regularization += wj * wj;
            }
            Jw = mainPart / m + lambda / (2 * m) * regularization;
            //System.out.println(Jw);
            JwRecord[i] = Jw;
            if (i != 0 && JwRecord[i - 1] - JwRecord[i] < threshold) {
                JwRecord[i + 1] = -1;
                break;
            }
        }
        //System.out.println(w);
        return JwRecord;
    }

    /**
     * 从指定的模型中加载系数矩阵w
     * @param fileName 指定的模型的路径
     *
     */
    public void fitFromFile(String fileName) {
        //从指定文件名里加载模型
        FileReader fr = null;
        try {
            fr = new FileReader(new File(fileName));
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            br.close();
            fr.close();
            String[] tempStrw = line.split(",");
            double[] tempw = new double[tempStrw.length];
            for (int i = 0; i < tempStrw.length; i++)
                tempw[i] = Double.parseDouble(tempStrw[i]);
            w = new SimpleMatrix(1, tempStrw.length, true, tempw);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *将训练好的模型（系数矩阵w）永久化存储
     * @param fileName 要存储的模型文件路径
     *
     */
    public void storeModule(String fileName) {
        //以模型名命名文件名，将模型永久化存储
        String tempStrw = "";
        for (int i = 0; i < w.numCols(); i++)
            tempStrw += (w.get(i) + ",");
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("");
            writer.write(tempStrw);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *
     * @param test_x 用于预测的特征矩阵x
     * @return 返回预测出来的SimpleMatrix类型的01标签向量
     */
    public SimpleMatrix predict(SimpleMatrix test_x) {

        if (w == null) return null;
        //System.out.println(w);
        test_x = DataHandling.min_max_handing(test_x);
        int m = test_x.numRows();
        double[] pred_y = new double[m];
        SimpleMatrix stuff = new SimpleMatrix(m, 1);//填充矩阵
        stuff.fill(1);
        test_x = stuff.combine(0, 1, test_x);
        //System.out.println(test_x);
        for (int i = 0; i < m; i++) {
            double y = 1 / (1 + Math.pow(Math.E, -(w.mult(test_x.rows(i, i + 1).transpose())).get(0)));
            y = (y > 0.5) ? 1 : 0;
            pred_y[i] = y;
        }
        return new SimpleMatrix(m, 1, true, pred_y);//返回预测结果矩阵
    }
}
