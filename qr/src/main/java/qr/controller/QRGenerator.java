package qr.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import qr.QRGeneratorUtil;

@RestController
@RequestMapping(value = "/generateQR")
public class QRGenerator {
	
	@Autowired
	ServletContext context;

	@RequestMapping(value="/get")
	public String getPage(HttpServletRequest request,HttpServletResponse response) {
		return "qr";
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getCode/{qrCode}",produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> zxingQRCode(@PathVariable("qrCode") String qrCode) throws Exception {
		ResponseEntity<byte[]> respo= okResponse(QRGeneratorUtil.generateQRCodeImage(qrCode));
        return respo;
    }

	//for it to be a response
	private ResponseEntity<byte[]> okResponse(BufferedImage image) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		InputStream in = QRGeneratorUtil.convertImageToStream(image);
		byte[] media = IOUtils.toByteArray(in);
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		// responseHeaders.setLocation(location);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
		return  responseEntity;
	}
	
	
}
