package cn.itcast.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class ParseJsonUDF extends UDF {

    public Text evaluate(final Text text) {

        return text;
    }
}
