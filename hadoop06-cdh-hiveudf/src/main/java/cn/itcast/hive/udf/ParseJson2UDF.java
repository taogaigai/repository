package cn.itcast.hive.udf;

import com.google.gson.Gson;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.Map;

public class ParseJson2UDF extends UDF {

    public Text evaluate(final Text text, final Text key) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(text.toString(), Map.class);
        text.set(map.get(key.toString()).toString());
        return text;
    }
}
