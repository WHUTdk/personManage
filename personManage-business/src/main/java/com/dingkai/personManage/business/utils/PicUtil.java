package com.dingkai.personManage.business.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @Author dingkai
 * @Date 2019/10/29 11:01
 */
public class PicUtil {

    private static final int connectionTimeout = 5000;
    private static final int readTimeout = 30000;

    /**
     * 将本地图片文件转为base64编码
     */
    public static String picToBase64(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        return picToBase64(in);
    }

    public static String picToBase64(InputStream in) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) != -1) {
            data.write(bytes, 0, len);
        }
        // 关闭流
        in.close();
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码的字节数组字符串
        return encoder.encode(data.toByteArray());
    }

    /**
     * 图片base64转为图片文件,ImageIO.write会压缩图片
     */
    public static void base64ToPic(String base64, String filePath) throws IOException {
        if (StringUtils.isEmpty(base64)) {
            return;
        }
        // Base64解码，转为输入流
        byte[] b = new BASE64Decoder().decodeBuffer(base64);
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        File file = new File(filePath);
        FileUtils.copyInputStreamToFile(bis, file);
    }

    /**
     * 将http链接图片转为base64
     */
    public static String httpUrlToBase64(String httpURL) throws IOException {
        URL url = new URL(httpURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(readTimeout);
        conn.setConnectTimeout(connectionTimeout);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        InputStream in = conn.getInputStream();
        return picToBase64(in);
    }

    /**
     * http图片下载为本地图片
     */
    public static void httpUrlToPic(String httpUrl, String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils.copyURLToFile(new URL(httpUrl), file, connectionTimeout, readTimeout);
    }

    public static void main(String[] args) throws IOException {
        String url1 = "http://hiphotos.qianqian.com/ting/pic/item/c83d70cf3bc79f3d98ca8e36b8a1cd11728b2988.jpg";
        String base64 = httpUrlToBase64(url1);
        base64ToPic(base64, "C:/Users/86152/Downloads/test2.jpg");
        String url2 = "http://hiphotos.qianqian.com/ting/pic/item/8b13632762d0f7035cb3feda0afa513d2697c5b7.jpg";
        httpUrlToPic(url2, "C:/Users/86152/Downloads/test.jpg");
    }


}
