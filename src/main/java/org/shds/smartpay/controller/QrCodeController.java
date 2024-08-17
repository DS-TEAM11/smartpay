package org.shds.smartpay.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/qr")
@Log4j2
@CrossOrigin(origins = "http://192.168.45.137:3000") // React 애플리케이션의 URL
public class QrCodeController {

    /**
     * 판매자 사이트 QR생성
     * @return 판매자 사이트 url
     * @throws Exception
     */
    @GetMapping("/seller")
    public ResponseEntity<byte[]> sendSeller() throws Exception{

        String url = "http://192.168.45.137:3000/seller";

        BitMatrix encode = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, 200, 200);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(encode, "png", out);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());

        } catch (Exception e) {
            log.warn("QR Code OutputStream 중 Exception 발생");

            return null;
        }

    }


    /**
     * qr/{value} 값을 QR로 생성하는 방식
     *
     * @param barcode
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode)
            throws Exception {

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcode, BarcodeFormat.QR_CODE, 200, 200);

        return new ResponseEntity<>(MatrixToImageWriter.toBufferedImage(bitMatrix), HttpStatus.OK);
    }
}
