package myLearn;
/*用与挑选部分特征,比较进阶，可写可不写*/

import org.ejml.simple.SimpleMatrix;

public class selectKBest {

    private int k;
    public selectKBest(int k)
    {
        this.k=k;
    }
    public SimpleMatrix fit_transform(SimpleMatrix X,SimpleMatrix Y)
    {
        //根据X中的各数据与y的关系选择出前k个特征向量生成新的X矩阵
        return new SimpleMatrix(1,1);
    }
}
