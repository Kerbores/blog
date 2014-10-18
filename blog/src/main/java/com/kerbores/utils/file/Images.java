package com.kerbores.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.kerbores.utils.Base64Decoder;
import com.kerbores.utils.Base64Encoder;

/**
 * 图片base64处理
 * 
 * @author Kerbores
 * 
 */
public class Images {
	private static final Log log = Logs.get();
	/**
	 * 
	 * @param imgStr
	 *            图片数据
	 * @param outFile
	 *            输出文件
	 * @return
	 */
	public static File GenerateImage(String imgStr, File outFile) {
		if (imgStr == null) // 图像数据为空
			return null;
		try {
			// Base64解码
			byte[] b = Base64Decoder.decodeToBytes(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(outFile);
			out.write(b);
			out.flush();
			out.close();
			return outFile;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 根据base64串生成文件
	 * 
	 * @param imgStr
	 *            base64串
	 * @param outDir
	 *            输出目录
	 * @param suffix
	 *            扩展名
	 * @return
	 */
	public static File GenerateImage(String imgStr, File outDir, String suffix) {
		File file = new File(outDir.getPath() + File.separator + System.nanoTime() + "." + suffix);
		return GenerateImage(imgStr, file);
	}

	/**
	 * 生成png图片
	 * 
	 * @param imgStr
	 *            base64 image信息串
	 * @return 图片文件
	 */
	public static File GeneratePngImage(String imgStr) {// 对字节数组字符串进行Base64解码并生成图片
		return GenerateImage(imgStr, new File(System.nanoTime() + ".png"));
	}

	/**
	 * 图片转换成base64串
	 * 
	 * @param imgFile
	 *            图片文件
	 * @return base64 图片信息串
	 */
	public static String GetImageStr(File imgFile) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		// 对字节数组Base64编码
		return Base64Encoder.encode(data);
	}
}
