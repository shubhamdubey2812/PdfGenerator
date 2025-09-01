package com.pdf.generation;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.pdfbox.io.IOUtils;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.RGBColor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class GeneratePdf implements CommandLineRunner{
	public void addImageWithCaption(Document document, String imageName, String captionText) throws Exception {
	    InputStream chartStream = GeneratePdf.class.getClassLoader().getResourceAsStream(imageName);
	    
	    if (chartStream != null) {
	        Image chartImg = Image.getInstance(IOUtils.toByteArray(chartStream));
	        
	        PdfPTable imageWithCaption = new PdfPTable(1);
	        imageWithCaption.setWidthPercentage(100);
	        imageWithCaption.setSpacingBefore(10);
	        imageWithCaption.setSpacingAfter(10);
	        imageWithCaption.setKeepTogether(true);

	        // Image cell
	        PdfPCell imageCell = new PdfPCell(chartImg, true);
	        imageCell.setBorder(Rectangle.NO_BORDER);
	        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        imageCell.setPaddingBottom(5);
	        imageWithCaption.addCell(imageCell);

	        // Caption cell
	        Font captionFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, RGBColor.DARK_GRAY);
	        Paragraph caption = new Paragraph(captionText, captionFont);
	        caption.setAlignment(Element.ALIGN_CENTER);

	        PdfPCell captionCell = new PdfPCell(caption);
	        captionCell.setBorder(Rectangle.NO_BORDER);
	        captionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        imageWithCaption.addCell(captionCell);

	        document.add(imageWithCaption);
	    } else {
	        System.err.println("Image not found: " + imageName);
	    }
	}
	@Override
	public void run(String... args) throws Exception {
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < 100; i++) {
	        sb.append("Line number " + i + System.lineSeparator());
	    }

	    // Set top and bottom margins to make space for header and footer
	    Document document = new Document(new Rectangle(595, 842), 50, 50, 100, 70); // left, right, top, bottom margins
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Truchip.pdf"));

	    // Set page event BEFORE opening document
	    writer.setPageEvent(new HeaderFooterPageEvent());

	    document.open();

	    // Body Paragraph
	    Font bodyFont = FontFactory.getFont(FontFactory.COURIER, 12, RGBColor.DARK_GRAY);
	    Paragraph paragraph = new Paragraph(sb.toString(), bodyFont);
	    paragraph.setAlignment(Element.ALIGN_LEFT);
	    paragraph.setSpacingAfter(10);
	    document.add(paragraph);

	    // Chart Image with Border and Caption
	    String[] imageFiles = { "StackedLine.png", "BarChart.png", "PieChart.png" };

	    for (int i = 0; i < imageFiles.length; i++) {
	        String caption = "Figure " + (i + 1) + ": Description of " + imageFiles[i];
	        addImageWithCaption(document, imageFiles[i], caption);
	    }
	    //Page Break
	    document.newPage();
	    //Add Table 
	    addTable(document);

	    document.close();
	    System.out.println("PDF Created Successfully");
	}
	private void addTable(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(3);
//		table.setWidths(new float[] {100f, 50f, 150f});  // widths in points
		//Header With Color
		PdfPCell cell1=new PdfPCell(new Phrase("Id"));
		cell1.setBackgroundColor(RGBColor.GRAY);
		table.addCell(cell1);
		PdfPCell cell2=new PdfPCell(new Phrase("Company Name"));
		cell2.setBackgroundColor(RGBColor.GRAY);
		table.addCell(cell2);
		PdfPCell cell3=new PdfPCell(new Phrase("Company Email"));
		cell3.setBackgroundColor(RGBColor.GRAY);
		table.addCell(cell3);
		
		// Two colors to alternate
        RGBColor color1 = new RGBColor(230, 240, 255); // light blue
        RGBColor color2 = new RGBColor(255, 255, 255);
        
        // Rows with alternating colors
        for (int i = 1; i <= 10; i++) {
            RGBColor bgColor = (i % 2 == 0) ?  color1 : color2;

            PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(i)));
            c1.setBackgroundColor(bgColor);
            table.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase("Company " + i));
            c2.setBackgroundColor(bgColor);
            table.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("company"+i+"@"+"company"+i+".net"));
            c3.setBackgroundColor(bgColor);
            table.addCell(c3);
        }
	        document.add(table);
		
	}


}
