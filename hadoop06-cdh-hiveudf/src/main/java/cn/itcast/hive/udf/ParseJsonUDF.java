package cn.itcast.hive.udf;

import com.google.gson.Gson;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.Collection;
import java.util.Map;

public class ParseJsonUDF extends UDF {

    public Text evaluate(final Text text) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(text.toString(), Map.class);
        Collection<Object> values = map.values();
        StringBuffer sb = new StringBuffer();
        for (Object o : values) {
            sb.append(o).append("\t");
        }
        text.set(sb.toString());
        return text;
    }
}
