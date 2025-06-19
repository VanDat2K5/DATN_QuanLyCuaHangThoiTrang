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
import java.awt.Graphics2D;

@Service
public class QRCodeServlet {

    private String generateQRData(String tien, String noidung) {
        String sTK = "6910915396";
        int soTienInt = (int) Double.parseDouble(tien);
        String tienFormatted = String.valueOf(soTienInt);

        int soTien = tienFormatted.length();
        int soNoiDung = noidung.length();

        String qrData = "00020101021238540010A000000727012400069704180110" + sTK +
                "0208QRIBFTTA5303704540" + soTien + tienFormatted +
                "5802VN62" + ((soNoiDung + 4) <= 9 ? "0" + (soNoiDung + 4) : (soNoiDung + 4)) +
                "08" + (soNoiDung <= 9 ? "0" + soNoiDung : soNoiDung) + noidung + "6304";

        String crc = calculateCRC16(qrData);
        return qrData + crc;
    }

    public File generateQRCodeFile(String tien, String noidung) {
        try {
            String qrData = generateQRData(tien, noidung);
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
            String qrData = generateQRData(tien, noidung);
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

    public String generateQRCodeWithLogoBase64(String tien, String noidung) {
        try {
            String qrData = generateQRData(tien, noidung);
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 300, 300, hints);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Chèn logo vào giữa QR code
            BufferedImage logo = ImageIO.read(new File("src/main/resources/static/img/logoST.png"));
            int logoWidth = qrImage.getWidth() / 5;
            int logoHeight = qrImage.getHeight() / 5;
            int logoX = (qrImage.getWidth() - logoWidth) / 2;
            int logoY = (qrImage.getHeight() - logoHeight) / 2;

            Graphics2D g = qrImage.createGraphics();
            g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
            g.dispose();

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

    private String calculateCRC16(String data) {
        int[] table = new int[256];
        int polynomial = 0x1021; // CRC-16-CCITT polynomial

        // Generate CRC table
        for (int i = 0; i < 256; i++) {
            int crc = i << 8;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc = crc << 1;
                }
            }
            table[i] = crc & 0xFFFF;
        }

        // Calculate CRC
        int crc = 0xFFFF;
        for (byte b : data.getBytes()) {
            crc = ((crc << 8) ^ table[((crc >> 8) ^ (b & 0xFF)) & 0xFF]) & 0xFFFF;
        }
        // System.out.println("CRC: " + crc);
        // System.out.println(String.format("%04X", crc));
        return String.format("%04X", crc);
    }

}
