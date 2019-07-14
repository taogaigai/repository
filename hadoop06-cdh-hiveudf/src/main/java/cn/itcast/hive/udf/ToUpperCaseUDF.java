package cn.itcast.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class ToUpperCaseUDF extends UDF {
    /**
     * 继承UDF这个类 然后重写evalute这个方法，方法的参数就是
     * 我们传入进来的数据
     */
    public Text evaluate(final Text text) {
        if (null != text && !text.toString().equals("")) {
            //将我们的一行数据转换成大写
            String str = text.toString().toUpperCase();
            text.set(str);
            return text;
        }
        text.set("");
        return text;
    }
}
