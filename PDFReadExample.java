import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * This class is used to split an existing pdf file using iText jar.
 * 
 * @author NikantChaudhary
 */
public class PDFReadExample {

public static final String PDF_FILE_LOCATION = "C:\\Test\\F04_en-merged.pdf";

private static void splitPdfFile(InputStream inputPdf) throws IOException, DocumentException {
 
 
 int startPage = 0;
 // Create PdfReader instance.
 PdfReader pdfReader = new PdfReader(PDF_FILE_LOCATION);

 // Get the number of pages in pdf.
 int pages = pdfReader.getNumberOfPages();
 
 //listOfPageNumber is used to hold last page number of a pdf file
 ArrayList<Integer> listOfEndPageNumbers = new ArrayList<>();
 
 // Iterate the pdf through pages.
 for (int i = 1; i <= pages; i++) {
  // Extract the page content using PdfTextExtractor.
  String pageContent = PdfTextExtractor.getTextFromPage(pdfReader, i);
  if (pageContent.contains("QA AUDIT PROOFS")) {
   if(i!=1){
    listOfEndPageNumbers.add(i-1);
   }
  }
 }
 
 //Splitting First PDF file
 splitDocuments(new FileInputStream(PDF_FILE_LOCATION) ,1, listOfEndPageNumbers.get(0));
 startPage = listOfEndPageNumbers.get(0)+1;
 
 //Splitting Intermediate PDF files
 for(int j=1; j < listOfEndPageNumbers.size(); j++) {
  splitDocuments(new FileInputStream(PDF_FILE_LOCATION) ,startPage, listOfEndPageNumbers.get(j));
  startPage = listOfEndPageNumbers.get(j)+1;
 }
 
 //Splitting last PDF file
 splitDocuments(new FileInputStream(PDF_FILE_LOCATION) ,startPage, pages);

 // Close the PdfReader.
 pdfReader.close();

 
}


private static void splitDocuments(InputStream inputPdf, int startPage, int endPage) throws IOException, DocumentException {
 PdfReader pdfReader = new PdfReader(inputPdf);
 Document document = new Document();
 
 OutputStream outputStream = new FileOutputStream(
   "C:\\Test\\SplitFile" + startPage +"-"+ endPage+".pdf");
 PdfWriter writer = PdfWriter.getInstance(document, outputStream);

 // Open document
 document.open();

 // Contain the pdf data.
 PdfContentByte pdfContentByte = writer.getDirectContent();
 PdfImportedPage page;

 while (startPage <= endPage) {
  document.newPage();
  page = writer.getImportedPage(pdfReader, startPage);
  pdfContentByte.addTemplate(page, 0, 0);
  startPage++;
 }

 // Close document and outputStream.
 outputStream.flush();
 document.close();
 outputStream.close();

 
}



public static void main(String args[]) {
 try {
  // call method to split pdf file.
  splitPdfFile(new FileInputStream(PDF_FILE_LOCATION));

  System.out.println("Pdf file splitted successfully.");
 } catch (Exception e) {
  e.printStackTrace();
 }
}




}
