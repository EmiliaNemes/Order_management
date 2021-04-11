package org.presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ErrorGenerator {

    /**
     * Se face un document pdf nou cu mesajul ca nu exista client cu numele specificat in comanda.
     * @param firstName prenumele clientului
     * @param lastName numele clientului
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza mesajul de eroare
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza mesajul de eroare
     */
    public void generateInvalidClientMessage(String firstName, String lastName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("OrderError_Client.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER);
        Chunk chunk = new Chunk("There is no client with name: " + firstName + " " + lastName, font);
        document.add(chunk);
        document.close();
    }

    /**
     * Se face un document pdf nou cu mesajul ca nu exista pe stoc cantitatea ceruta in comanda.
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza mesajul de eroare
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza mesajul de eroare
     */
    public void generateUnderStockMessage() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("OrderError_Product.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER);
        Chunk chunk = new Chunk("There is no enough product on stock", font);
        document.add(chunk);
        document.close();
    }
}
