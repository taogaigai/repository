package cn.itcast.itercepto;

import com.google.common.base.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

public class ParseParameterInterceptor implements Interceptor {

    private static String separator = ",";

    @Override
    public void initialize() {
    }

    @Override
    public void close() {
    }


    //写你需要的ETL等逻辑
    @Override
    public Event intercept(Event event) {
        try {
            if (event == null) {
                return null;
            }
            //获取我们的一行数据
            String line = new String(event.getBody(), Charsets.UTF_8);
            String[] fields = line.split(separator);
            // index 也是我们从外部传入进来的，表示我们需要获取哪些下标的字段数据
            String[] index = {"1", "2"};

            String newLine = "";
            for (int i = 0; i < index.length; i++) {
                if (i == 0) {
                    //对第1个字段进行加密
                    newLine += DigestUtils.md5Hex(fields[0]);
                } else {
                    //其它字段不加密
                    newLine += fields[Integer.parseInt(index[i])];
                }
                //字段之间加上分隔符
                if (i != index.length - 1) {
                    newLine += separator;
                }
            }
            event.setBody(newLine.getBytes(Charsets.UTF_8));
            return event;
        } catch (Exception e) {
            return event;
        }
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> list = new ArrayList<Event>();
        for (Event event : events) {
            Event outEvent = intercept(event);
            if (outEvent != null) {
                list.add(outEvent);
            }
        }
        return list;
    }

    /**
     * 实现内部类接口
     */
    public static class Builder implements Interceptor.Builder {

        public void configure(Context context) {
        }

        /*
         * @see org.apache.flume.interceptor.Interceptor.Builder#build()
         */
        public Interceptor build() {
            return new ParseParameterInterceptor();
        }
    }

}
