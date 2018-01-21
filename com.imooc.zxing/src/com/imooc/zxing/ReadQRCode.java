package com.imooc.zxing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ReadQRCode {

	public static void main(String[] args) {
		MultiFormatReader multiFormatReader=new MultiFormatReader();
		File file=new File("G:/code/宇信科技.png");
		try {
			BufferedImage image=ImageIO.read(file);
			BinaryBitmap binaryBitmap= 
					new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			
			HashMap hints=new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			
			Result result=multiFormatReader.decode(binaryBitmap,hints);
			
			System.out.println("解析结果："+result.toString());
			System.out.println("二维码格式类型："+result.getBarcodeFormat());
			System.out.println("二维码解析内容："+result.getText());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

}
