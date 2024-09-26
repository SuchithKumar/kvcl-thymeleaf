//package org.vasaviyuvajanasangha.kvcl.language;
//
//import java.io.FileOutputStream;
//import java.io.*;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
////import com.itextpdf.text.Font;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.LineSeparator;
//import com.itextpdf.text.PageSize;
////import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.FontSelector;
//import java.awt.*;
//import java.awt.font.*;
//import java.awt.geom.*;
//import java.awt.image.*;
//import javax.imageio.ImageIO;
//
//public class FontTypes {
//
//    /** The resulting PDF file. */
//    public static String RESULT
//        = "C:/font_types.pdf";
//    /** Some text. */
//   public static String TEXT
//        = "উত্তরবঙ্গে নয়া ঘাঁটির জমি চায় সেনা";
//         public static String TEXT1
//        = "সীমান্তে ঘাড়ের কাছে নিঃশ্বাস ফেলছে চিন";
//
//
//
//
//    /**
//     * Creates a PDF document.
//     * @param filename the path to the new PDF document
//     * @throws    DocumentException
//     * @throws    IOException
//     */
//    public void createPdf(String filename) throws IOException, DocumentException,FontFormatException {
//        // step 1
//        Document document = new Document(PageSize.A4,20, 20, 20, 20);
//        // step 2
//        PdfWriter.getInstance(document, new FileOutputStream(filename));
//        // step 3
//        document.open();
//        // step 4
//     com.itextpdf.text.Image jpeg = com.itextpdf.text.Image.getInstance(textToImage(TEXT,20));
//       jpeg.scalePercent(50f);
//       document.add(jpeg);
//        com.itextpdf.text.Image jpeg1 = com.itextpdf.text.Image.getInstance(textToImage(TEXT1,24));
//         jpeg1.scalePercent(50f);
//        document.add(jpeg1);
//
//               // step 5
//        document.close();
//    }
//
//    /**
//     * Main method.
//     *
//     * @param    args    no arguments needed
//     * @throws DocumentException
//     * @throws IOException
//     */
//    public static void main(String[] args) throws IOException, DocumentException,FontFormatException {
//        try{
//        new FontTypes().createPdf(RESULT);
//        }catch(Exception e){
//        e.printStackTrace();
//        }
//    }
//
//
//
//
//public  byte[] textToImage(String text,  float size) throws IOException,FontFormatException{
//    //Derives font to new specified size, can be removed if not necessary.
//
//    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
//    try{
//        Graphics2D g2d = img.createGraphics();
//     //   Font font = new Font("Arial", Font.PLAIN, 48);
//        Font fnt=Font.createFont(Font.TRUETYPE_FONT, new File("c:/windows/fonts/SolaimanLipi.ttf"));
//        fnt = fnt.deriveFont(Font.PLAIN,size);
//        //System.out.println(fnt.getName());
//        //System.out.println(fnt.getNumGlyphs());
//        g2d.setFont(fnt);
//        FontMetrics fm = g2d.getFontMetrics();
//        int width = fm.stringWidth(text);
//        int height = fm.getHeight();
//        g2d.dispose();
//
//        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        g2d = img.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//
//        g2d.setFont(fnt);
//        fm = g2d.getFontMetrics();
//        g2d.setBackground(Color.WHITE);
//        g2d.setColor(Color.BLACK);
//        g2d.drawString(text, 0, fm.getAscent());
//        g2d.dispose();
//}catch(Exception e){
//e.printStackTrace();
//}
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//ImageIO.write( img, "png", baos );
//baos.flush();
//byte[] imageInByte = baos.toByteArray();
//baos.close();
//
//    return imageInByte;
//}
//
//}