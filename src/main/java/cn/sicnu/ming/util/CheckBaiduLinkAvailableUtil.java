package cn.sicnu.ming.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author frank ming
 * @createTime 20200619 1:42
 * @description 检查百度的连接是否有效的工具类
 */
public class CheckBaiduLinkAvailableUtil {
    static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static boolean check(String link) throws IOException {
        HttpGet httpGet = new HttpGet(link.trim());
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");  //设置请求头消息
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();       //获取返回实体
        String result = EntityUtils.toString(entity,"UTF-8");
//        System.out.println(result);
        if(result.contains("请输入提取码")||result.contains("永久有效")) {
            return true;
        }else {
            return false;
        }
    }

//    public static void main(String[] args) throws IOException {
//        System.out.println(CheckBaiduLinkAvailableUtil.check(" https://pan.baidu.com/s/1ZmhGcXm_ggEz6j2mN5hCYQ"));
//    }
}
