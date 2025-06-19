package com.poly.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Base64;

import javax.imageio.ImageIO;

@Service
public class QRCodeServlet {

    public File generateQRCodeFile(String tien, String noidung) {
        try {
            String sTK = "6910915396";

            int soTienInt = (int) Double.parseDouble(tien);
            String tienFormatted = String.valueOf(soTienInt);

            int soTien = tienFormatted.length();
            int soNoiDung = noidung.length();

            String qrData = "00020101021238540010A000000727012400069704180110" + sTK +
                    "0208QRIBFTTA5303704540" + soTien + tienFormatted +
                    "5802VN62" + ((soNoiDung + 4) <= 9 ? "0" + (soNoiDung + 4) : (soNoiDung + 4)) +
                    "08" + (soNoiDung <= 9 ? "0" + soNoiDung : soNoiDung) + noidung + "6304E2E2";

            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            File qrFile = new File("qr_code.png");
            ImageIO.write(qrImage, "png", qrFile);
            return qrFile;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateQRCodeBase64(String tien, String noidung) {
        try {
            String sTK = "6910915396";
            int soTienInt = (int) Double.parseDouble(tien);
            String tienFormatted = String.valueOf(soTienInt);

            int soTien = tienFormatted.length();
            int soNoiDung = noidung.length();

            String qrData = "00020101021238540010A000000727012400069704180110" + sTK +
                    "0208QRIBFTTA5303704540" + soTien + tienFormatted +
                    "5802VN62" + ((soNoiDung + 4) <= 9 ? "0" + (soNoiDung + 4) : (soNoiDung + 4)) +
                    "08" + (soNoiDung <= 9 ? "0" + soNoiDung : soNoiDung) + noidung + "6304E2E2";

            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 300, 300, hints);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            String base64QRCode = Base64.getEncoder().encodeToString(imageBytes);

            return "data:image/png;base64," + base64QRCode;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
