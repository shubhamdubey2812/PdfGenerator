package com.pdf.generation;

import java.io.InputStream;
import java.time.LocalDate;

import org.apache.pdfbox.io.IOUtils;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.ColumnText;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.RGBColor;
import org.springframework.stereotype.Component;



@Component
public class HeaderFooterPageEvent extends PdfPageEventHelper {
	Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, RGBColor.BLACK);
	 Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, RGBColor.DARK_GRAY);

	 @Override
	 public void onEndPage(PdfWriter writer, Document document) {
	     PdfContentByte cb = writer.getDirectContent();
	     Rectangle pageSize = document.getPageSize();

	     float left = document.left();
	     float right = document.right();
	     float top = pageSize.getTop() - 36;
	     float bottom = document.bottom();
	     float centerX = (left + right) / 2;

	  // === HEADER ===
	     addHeader(writer,document,cb,pageSize,left,right,top,bottom,centerX);
	     // === FOOTER ===
	     addFooter(writer,document,cb,pageSize,left,right,top,bottom,centerX);
	 }

	 private void addFooter(PdfWriter writer, Document document, PdfContentByte cb, Rectangle pageSize, float left,
			float right, float top, float bottom, float centerX) {
		 float footerY = bottom - 40;
	     // Draw line above footer
	     cb.setLineWidth(1f);
	     cb.moveTo(left, footerY + 20);
	     cb.lineTo(right, footerY + 20);
	     cb.stroke();

	     // Footer Text - Center
	     Phrase footerText = new Phrase("© " + LocalDate.now().getYear() + " Truechip Solutions Pvt. Ltd. All rights reserved.", footerFont);
	     ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footerText, centerX, footerY, 0);

	     // Page Number - Right
	     Phrase pageNumber = new Phrase("Page " + writer.getPageNumber(), footerFont);
	     ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pageNumber, right, footerY, 0);
	}

	 private void addHeader(PdfWriter writer, Document document, PdfContentByte cb, Rectangle pageSize, float left,
			float right, float top, float bottom, float centerX) {
		// 1. Logo (Left-aligned)
	     try (InputStream is = GeneratePdf.class.getClassLoader().getResourceAsStream("logotruechip.png")) {
	         if (is != null) {
	             Image logo = Image.getInstance(IOUtils.toByteArray(is));
	             logo.scaleAbsolute(100, 50);

	             float logoX = left;
	             float logoY = pageSize.getTop() - 70; 
	             logo.setAbsolutePosition(logoX, logoY);
	             writer.getDirectContent().addImage(logo);

	             // Border around logo
//	             cb.setColorStroke(BaseColor.LIGHT_GRAY);
//	             cb.rectangle(logoX, logoY, logo.getScaledWidth(), logo.getScaledHeight());
//	             cb.stroke();
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	     }

	     // 2. Company Name (Center-aligned)
	     RGBColor customBlue = new RGBColor(0, 107, 181); // RGB for dark blue
	     Font nameHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, customBlue);
	     Phrase headerPhrase = new Phrase("Truechip Solutions Pvt Ltd", nameHeaderFont);
	     ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, headerPhrase, centerX, top - 20, 0);

	  // 3. Timestamp (Right-aligned)
	     Font timeFont = FontFactory.getFont(FontFactory.HELVETICA, 10, RGBColor.DARK_GRAY);

	     // Format the timestamp
	     String timestamp = java.time.LocalDateTime.now()
	         .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));

	     // Create Phrase and place it at the right side of the header
	     Phrase timePhrase = new Phrase(timestamp, timeFont);
	     ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, timePhrase, right, pageSize.getTop() - 15, 0);

	     
	     // 4. Header Separator Line
	     cb.setLineWidth(1);
	     cb.setColorStroke(RGBColor.BLACK);
	     cb.moveTo(left, pageSize.getTop() - 80);
	     cb.lineTo(right, pageSize.getTop() - 80);
	     cb.stroke();
	 }

}
