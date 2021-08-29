package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import sample.model.InvoiceModel;
import sample.model.ProductModel;
import sample.model.VatModel;
import java.io.*;


public class AddPDF{

    private Font bigFontBold = FontFactory.getFont(FontFactory.TIMES_ROMAN,"Cp1250",true,18,Font.BOLD);
    private Font middleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,"Cp1250",true,16);
    private Font middleFontBold = FontFactory.getFont(FontFactory.TIMES_ROMAN,"Cp1250",true,16,Font.BOLD);
    private Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,"Cp1250",true,10);
    private Font smallFontBold = FontFactory.getFont(FontFactory.TIMES_ROMAN,"Cp1250",true,10, Font.BOLD);


    public void createPdf(InvoiceModel invoiceModel) throws IOException, DocumentException {
        String[] factoringSerial = invoiceModel.getInvoiceSerial().split("/");
        String invoiceName = invoiceModel.getInvoiceType()+" "+factoringSerial[0]+"."+factoringSerial[1]+"."+factoringSerial[2]+".pdf";
        //File file = new File("C:/Users/Bartek/Desktop/inz/FakturowanieApp/faktury/"+invoiceName);
        //file.getParentFile().mkdirs();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Bartek/Desktop/inz/FakturowanieApp/faktury/"+invoiceName));
        document.open();
        PdfContentByte cb = writer.getDirectContent();


        Paragraph title = new Paragraph(invoiceModel.getInvoiceType(), bigFontBold);
        title .setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        title  = new Paragraph("Nr: "+ invoiceModel.getInvoiceSerial(), bigFontBold);
        title .setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // dane wystawienia
        Paragraph daneWystawienia;
        daneWystawienia = new Paragraph("Miejsce wystawienia: " + invoiceModel.getInvoiceCity(),smallFont);
        daneWystawienia.setAlignment(Element.ALIGN_RIGHT);
        document.add(daneWystawienia);
        daneWystawienia = new Paragraph("Data wystawienia: " + invoiceModel.getInvoiceDate(), smallFont);
        daneWystawienia.setAlignment(Element.ALIGN_RIGHT);
        document.add(daneWystawienia);
        daneWystawienia = new Paragraph("Data sprzedaży: "+ invoiceModel.getInvoiceTransactionDate(), smallFont);
        daneWystawienia.setAlignment(Element.ALIGN_RIGHT);
        document.add(daneWystawienia);

        Phrase sprzedawca = new Phrase("Sprzedawca", middleFontBold);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawca, 35,650,0);
        Phrase sprzedawcaFirma = new Phrase(invoiceModel.getSellerFirmName(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaFirma, 35,625,0);
        Phrase sprzedawcaNip = new Phrase("NIP: " + invoiceModel.getSellerNip(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaNip, 35,613,0);
        Phrase sprzedawcaUlica = new Phrase("ul. "+ invoiceModel.getSellerStreet(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaUlica, 35,601,0);
        Phrase sprzedawcaAdres = new Phrase(invoiceModel.getSellerPostalCode()+" "+invoiceModel.getSellerCity(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaAdres, 35,589,0);
        Phrase sprzedawcaNumerTelefonu = new Phrase("tel.: "+invoiceModel.getSellerPhoneNumber(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaNumerTelefonu, 35,577,0);
        Phrase sprzedawcaEmail = new Phrase("e-mail: "+invoiceModel.getSellerPhoneNumber(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaEmail, 35,565,0);
        Phrase sprzedawcaNumerKonta = new Phrase("Nr konta: "+ invoiceModel.getSellerBankNumber(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, sprzedawcaNumerKonta, 35,553,0);
        Phrase nabywca = new Phrase("Nabywca", middleFontBold);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywca, 300,650,0);
        Phrase nabywcaFirma = new Phrase(invoiceModel.getBuyerFirmName(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaFirma, 300,625,0);
        Phrase nabywcaNip = new Phrase("NIP: "+ invoiceModel.getBuyerNip(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaNip, 300,613,0);
        Phrase nabywcaUlica = new Phrase("ul. "+ invoiceModel.getBuyerStreet(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaUlica, 300,601,0);
        Phrase nabywcaAdres = new Phrase(invoiceModel.getBuyerPostalCode()+" "+invoiceModel.getBuyerCity(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaAdres, 300,589,0);
        Phrase nabywcaNumerTelefonu = new Phrase("tel.: "+ invoiceModel.getBuyerPhoneNumber(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaNumerTelefonu, 300,577,0);
        Phrase nabywcaEmail = new Phrase("e-mail: "+ invoiceModel.getBuyerEmail(),smallFont);
        ColumnText.showTextAligned(cb, PdfContentByte.ALIGN_LEFT, nabywcaEmail, 300,565,0);
        addEmptyLine(new Paragraph(),5);

        PdfPTable mainTable = new PdfPTable(8);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{5,1,1,2,1,2,2,3});
        mainTable.setSpacingBefore(200f);
        mainTable.setSpacingAfter(25f);
        PdfPTable productTable = new PdfPTable(1);
        PdfPTable quantityTable = new PdfPTable(1);
        PdfPTable unitTable = new PdfPTable(1);
        PdfPTable nettoTable = new PdfPTable(1);
        PdfPTable vatTable = new PdfPTable(1);
        PdfPTable nettoPriceTable = new PdfPTable(1);
        PdfPTable vatValueTable = new PdfPTable(1);
        PdfPTable bruttoPriceTable = new PdfPTable(1);

        PdfPCell cell = new PdfPCell(new Phrase("Produkt/usługa", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        productTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Ilość", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        quantityTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Jm", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        unitTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Cena netto", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        nettoTable.addCell(cell);
        cell = new PdfPCell(new Phrase("VAT", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        vatTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Kwota netto", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        nettoPriceTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Kwota VAT", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        vatValueTable.addCell(cell);
        cell = new PdfPCell(new Phrase("Kwota brutto", smallFontBold));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(2);
        bruttoPriceTable.addCell(cell);

        // wypelnianie tabeli produktami z modelu
        for (ProductModel productModel : invoiceModel.getProductModelList()) {
            productTable.addCell(createProductCell(productModel.getProductName()));
            quantityTable.addCell(createProductCell(productModel.getProductQuantity()));
            unitTable.addCell(createProductCell(productModel.getProductUnit()));
            nettoTable.addCell(createProductCell(productModel.getProductNetto()));
            vatTable.addCell(createProductCell(productModel.getProductVat()));
            nettoPriceTable.addCell(createProductCell(productModel.getProductNettoValue()));
            vatValueTable.addCell(createProductCell(productModel.getProductVatValue()));
            bruttoPriceTable.addCell(createProductCell(productModel.getProductBruttoValue()));
        }
        mainTable.addCell(productTable);
        mainTable.addCell(quantityTable);
        mainTable.addCell(unitTable);
        mainTable.addCell(nettoTable);
        mainTable.addCell(vatTable);
        mainTable.addCell(nettoPriceTable);
        mainTable.addCell(vatValueTable);
        mainTable.addCell(bruttoPriceTable);

        PdfPTable secondaryTable = new PdfPTable(4);
        secondaryTable.setWidthPercentage(65);
        secondaryTable.setSpacingBefore(30f);
        secondaryTable.setSpacingAfter(10);
        secondaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        secondaryTable.setWidths(new float[]{1,1,1,1});

        PdfPTable vatSummaryTable = new PdfPTable(1);
        PdfPTable nettoValueSummaryTable = new PdfPTable(1);
        PdfPTable vatValueSummaryTable = new PdfPTable(1);
        PdfPTable bruttoValueTable = new PdfPTable(1);

        PdfPCell cell2 = new PdfPCell(new Phrase("Stawka VAT", smallFontBold));
        cell2.setVerticalAlignment(Element.ALIGN_LEFT);
        cell2.setBorder(2);
        vatSummaryTable.addCell(cell2);
        cell2 = new PdfPCell(new Phrase("Kwota netto", smallFontBold));
        cell2.setVerticalAlignment(Element.ALIGN_LEFT);
        cell2.setBorder(2);
        nettoValueSummaryTable.addCell(cell2);
        cell2 = new PdfPCell(new Phrase("Kwota VAT", smallFontBold));
        cell2.setVerticalAlignment(Element.ALIGN_LEFT);
        cell2.setBorder(2);
        vatValueSummaryTable.addCell(cell2);
        cell2 = new PdfPCell(new Phrase("Kwota brutto", smallFontBold));
        cell2.setVerticalAlignment(Element.ALIGN_LEFT);
        cell2.setBorder(2);
        bruttoValueTable.addCell(cell2);

        for (VatModel vatModel : invoiceModel.getVatModelList()) {
            vatSummaryTable.addCell(createVatCell(vatModel.getStawkaVat()));
            nettoValueSummaryTable.addCell(createVatCell(vatModel.getKwotaNetto()));
            vatValueSummaryTable.addCell(createVatCell(vatModel.getKwotaVat()));
            bruttoValueTable.addCell(createVatCell(vatModel.getKwotaBrutto()));
        }


        vatSummaryTable.addCell(createProductCellRazem("Razem"));
        nettoValueSummaryTable.addCell(createProductCellRazem(String.valueOf(invoiceModel.getVatModelList().stream()
                .map(VatModel::getKwotaNetto).mapToDouble(Double::valueOf).sum())));
        vatValueSummaryTable.addCell(createProductCellRazem(String.valueOf(invoiceModel.getVatModelList().stream()
                .map(VatModel::getKwotaVat).mapToDouble(Double::valueOf).sum())));
        bruttoValueTable.addCell(createProductCellRazem(invoiceModel.getInvoiceBruttoPrice()));

        secondaryTable.addCell(vatSummaryTable);
        secondaryTable.addCell(nettoValueSummaryTable);
        secondaryTable.addCell(vatValueSummaryTable);
        secondaryTable.addCell(bruttoValueTable);


        document.add(mainTable);
        document.add(secondaryTable);
        document.add(createPodsumowanieElemnt("Sposob zaplaty: " + invoiceModel.getInvoicePaymentType()));
        document.add(createPodsumowanieElemnt("Termin zaplaty: "  + invoiceModel.getInvoicePaymentDate()));
        document.add(createDoZaplaty("Do zaplaty: " + invoiceModel.getInvoiceBruttoPrice() + " PLN"));

        document.close();

        //generateImageFromPDF("test", "JPG");
    }

    private PdfPCell createProductCell(String name) {
        PdfPCell cell = new PdfPCell(new Phrase(name, smallFont));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell createProductCellRazem(String name) {
        PdfPCell cell = new PdfPCell(new Phrase(name, smallFont));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.TOP);
        return cell;
    }

    private PdfPCell createVatCell(String name) {
        PdfPCell cell = new PdfPCell(new Phrase(name, smallFont));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        return cell;
    }

    private PdfPTable createPodsumowanieElemnt(String name) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(name, smallFont));
        pdfPCell.setBorder(0);
        PdfPTable pdfPTable = new PdfPTable(1);
        pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(pdfPCell);
        pdfPTable.setWidthPercentage(30);
        return pdfPTable;
    }

    private PdfPTable createDoZaplaty(String name) {
        Font font = middleFont;
        font.setStyle(Font.BOLD);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(name, font));
        pdfPCell.setBorder(0);
        PdfPTable pdfPTable = new PdfPTable(1);
        pdfPTable.setSpacingBefore(10);
        pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(pdfPCell);
        pdfPTable.setWidthPercentage(35);
        return pdfPTable;
    }

//    private void generateImageFromPDF(String filename, String extension) throws IOException {
//        PDDocument document = PDDocument.load(new File(DEST));
//        PDFRenderer pdfRenderer = new PDFRenderer(document);
//        for (int page = 0; page < document.getNumberOfPages(); ++page) {
//            BufferedImage bim = pdfRenderer.renderImageWithDPI(
//                    page, 300, ImageType.RGB);
//            ImageIOUtil.writeImage(
//                    bim, String.format("C:/Users/Bartek/Desktop/inz/FakturowanieApp/faktury/faktura.jpg", page + 1, extension), 300);
 //       }
//        document.close();
//
//    }
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
