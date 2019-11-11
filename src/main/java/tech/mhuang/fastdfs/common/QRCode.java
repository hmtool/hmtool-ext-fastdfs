package tech.mhuang.fastdfs.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Hashtable;

public class QRCode {

	private static final int WIDTH=300;      //图片的宽度
    private static final int HEIGHT=300;     //图片的高度
    public static final String FORMAT= "png";    //图片的格式


    public static byte[] generate(String content){
    	return generate(content, WIDTH, HEIGHT, FORMAT);
    }
    
    public static byte[] generate(String content,int width,int height,String format){
    	File tmpFile = null;
        byte[] buff = null;
    	/**
         * 生成二维码
         */
        try {
        	tmpFile = File.createTempFile("temp", "." + format);
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            MatrixToImageWriter.writeToPath(bitMatrix, format, tmpFile.toPath());
            buff = FileUtils.readFileToByteArray(tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
		} finally {
			if(tmpFile != null){
				tmpFile.deleteOnExit();
			}
		}
        return buff;
    }
}
