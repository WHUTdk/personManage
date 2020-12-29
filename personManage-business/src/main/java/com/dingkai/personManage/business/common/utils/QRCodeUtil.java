package com.dingkai.personManage.business.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @author dingkai1
 * @desc
 * @date 2020/12/29 13:54
 */
public class QRCodeUtil {

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // 定义二维码的参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        // 定义字符集编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置图片边距
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws IOException, WriterException {
        generateQRCodeImage("http://47.99.187.36:8080/images/%E9%B2%AE%E9%B2%A42020Q4%E5%9B%A2%E5%BB%BA.jpg", 1280, 1280, "C:\\Users\\dingkai1\\Downloads" + "\\QrCode.png");
    }

}
