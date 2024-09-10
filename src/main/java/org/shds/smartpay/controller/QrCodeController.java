package org.shds.smartpay.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// java.awt.image.BufferedImage: 이미지를 메모리에 버퍼링하기 위한 클래스로,
// QR 코드를 이미지로 변환하기 위해 사용.
import java.awt.image.BufferedImage;

// java.io.ByteArrayOutputStream: 바이트 배열 출력 스트림으로,
// 데이터를 메모리에 바이트 배열로 기록하기 위해 사용.
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/qr")
@Log4j2
@CrossOrigin(origins = "http://192.168.0.30:3000") // React 애플리케이션의 URL
public class QrCodeController {
    @Value("${front.url}")
    private String front_url;
    /**
     * 판매자 사이트 QR생성
     * @return 판매자 사이트 url
     * @throws Exception
     */
    @GetMapping("/seller")
    public ResponseEntity<byte[]> sendSeller(@RequestParam String memberNo) throws Exception{
        //쿼리스트링으로 memberNo 받아서 처리하게 수정
        String url = "http://ec2-3-36-35-102.ap-northeast-2.compute.amazonaws.com:3000seller?memberNo=" + memberNo;

        // URL을 200x200 크기의 QR 코드로 인코딩하여 BitMatrix 객체로 반환
        BitMatrix encode = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, 200, 200);

        try {
            // QR 코드 이미지를 메모리에 저장할 바이트 배열 출력 스트림을 생성
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // MatrixToImageWriter를 사용하여 BitMatrix를 PNG 이미지로 변환하고,
            // 이를 ByteArrayOutputStream에 기록
            MatrixToImageWriter.writeToStream(encode, "png", out);

            return ResponseEntity.ok() // HTTP 200 OK 상태를 가진 응답을 생성
                    .contentType(MediaType.IMAGE_PNG) // 응답의 Content-Type을 image/png로 설정
                    .body(out.toByteArray()); // PNG 이미지 데이터를 바이트 배열로 변환하여 응답 본문에 포함

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
