package org.vasaviyuvajanasangha.kvcl;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

public class TestPdf {
	
	public static void main(String[] args) throws IOException {
		
		TestPdf pdf = new TestPdf();
		pdf.createpdf();
				
	}
	
	public void createpdf()  throws IOException {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		
		document.addPage(page);
		
        Path path = Paths.get("src/main/resources/static/images/tournament-announcement.png");
        Resource resource = new UrlResource(path.toUri());
		
        File file = resource.getFile();
		byte[] fileContent = Files.readAllBytes(file.toPath());
		
		
		PDImageXObject image = PDImageXObject.createFromByteArray(document, fileContent , "tournament-announcement");
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		

        // Build the table
        Table myTable = Table.builder()
                .addColumnsOfWidth(200, 200)
                .padding(2)
                .addRow(Row.builder()
                        .add(TextCell.builder().text("One One").borderWidth(4).borderColorLeft(Color.MAGENTA).backgroundColor(Color.WHITE).build())
                        .add(TextCell.builder().text("One Two").borderWidth(0).backgroundColor(Color.YELLOW).build())
                        .build())
                .addRow(Row.builder()
                        .padding(10)
                        .add(TextCell.builder().text("Two One").textColor(Color.RED).build())
                        .add(TextCell.builder().text("Two Two")
                                .borderWidthRight(1f)
                                .borderStyleRight(BorderStyle.DOTTED)
                                .horizontalAlignment(HorizontalAlignment.RIGHT)
                                .build())
                        .build())
                .build();

        // Set up the drawer
        TableDrawer tableDrawer = TableDrawer.builder()
                .contentStream(contentStream)
                .startX(20f)
                .startY(page.getMediaBox().getUpperRightY() - 20f)
                .table(myTable)
                .build();
        
        tableDrawer.draw();
        
        contentStream.close();
        document.save("test2.pdf");
		document.close();
		System.out.println("completed");
		
        
		
//		
//		contentStream.drawImage(image, 0, (page.getTrimBox().getHeight()-160),620,160);
//		
//		contentStream.beginText();
////		contentStream.setFont(PD, 18f);
//		contentStream.setLeading(16.0f);
//		
//		contentStream.newLineAtOffset(25, (page.getTrimBox().getHeight()-180));
//		
//		contentStream.showText("This is Line 1");
//		contentStream.newLine();
//		
//		contentStream.showText("This is Line 2");
//		contentStream.newLine();
//		
//		contentStream.showText("This is Line 3");
//		contentStream.newLine();
//		contentStream.endText();
//		
//		contentStream.close();
		
		
	}

}
