package cn.itcast.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class ReplaceUDF extends UDF {

    public Text evaluate(final Text text, final Text regex, final Text replacement) {
        text.set(text.toString().replaceAll(regex.toString(), replacement.toString()));
        return text;
    }
}
