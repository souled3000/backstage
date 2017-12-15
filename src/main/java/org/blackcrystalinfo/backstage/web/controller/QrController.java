package org.blackcrystalinfo.backstage.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.blackcrystalinfo.backstage.bo.QrBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

@Controller
public class QrController {
	private static final Logger log = LoggerFactory.getLogger(QrController.class);

	@RequestMapping(value = "/qr", method = RequestMethod.GET)
	public String a() {
		return "qr";
	}

	@RequestMapping(value = "/qr/genImg", method = RequestMethod.GET)
	public void genImg(QrBo bo, HttpServletResponse response) throws Exception {
		response.setContentType("image/png");
		String ctn = bo.gen();
		int width = 300;
		int height = 300;
		String format = "png";
		Hashtable<EncodeHintType, String> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(ctn, BarcodeFormat.QR_CODE, width, height, hints);
		OutputStream stream = response.getOutputStream();
		MatrixToImageWriter.writeToStream(matrix, format, stream);
	}

	@RequestMapping(value = "/qr/batch", method = RequestMethod.GET)
	public void batch(QrBo bo, HttpServletResponse response) throws Exception {
		response.setContentType("application/csv;charset=UTF-8");
		bo.batchGen(response.getOutputStream());
	}
}

class MatrixToImageWriter {
	private static final int BLACK = -16777216;
	private static final int WHITE = -1;

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, 1);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				image.setRGB(x, y, (matrix.get(x, y)) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!(ImageIO.write(image, format, file)))
			throw new IOException("Could not write an image of format " + format + " to " + file);
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!(ImageIO.write(image, format, stream)))
			throw new IOException("Could not write an image of format " + format);
	}
}