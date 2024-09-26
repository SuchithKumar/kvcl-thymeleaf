package org.vasaviyuvajanasangha.kvcl.pdf;

import java.io.FileOutputStream;

import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class DocumentGenerator {

	public ByteArrayOutputStream htmlToPdf(String processedHtml) {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		try {
			
			PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);
			
			DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, true);
			
			ConverterProperties converterProperties = new ConverterProperties();
			
			converterProperties.setFontProvider(defaultFont);
			try {
				HtmlConverter.convertToPdf(processedHtml, pdfwriter, converterProperties);
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}
//			FileOutputStream fout = new FileOutputStream("registration-form.pdf");
			
//			byteArrayOutputStream.writeTo(fout);
//			byteArrayOutputStream.close();
			
//			byteArrayOutputStream.flush();
//			fout.close();
			
			return byteArrayOutputStream;
			
		} catch(Exception ex) {
//			ex.printStackTrace();
		}
		
		return null;
	}
}