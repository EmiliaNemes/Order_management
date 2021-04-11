package org.presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class BillGenerator {
    private static int contor = 1;

    /**
     * Se creeaza un document pdf nou. Se afiseaza in el numele
     * clientului care a facut comanda, produsele comandate cu
     * cantitatea comandata, si pretul total al comenzii.
     * Se inchide documentul creat.
     * @param clientName numele clientului care a facut comanda
     * @param productList lista cu produsele comandate
     * @param quantityList cantitatea comandata din fiecare produs
     * @param totalPrice pretul total al comenzii
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza factura
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza factura
     */
    public void generateBill(String clientName, List<Product> productList, List<Integer> quantityList, int totalPrice) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Bill" + contor + ".pdf"));
        contor++;
        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 12);
        Paragraph paragraph = new Paragraph("The client named " + clientName + " ordered the following product(s): \n", font);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        Chunk chunk;

        int i = 0;
        for(Product p: productList){
            chunk = new Chunk(quantityList.get(i) + " pieces of " + p.getName() + "\n", font);
            document.add(chunk);
            i++;
        }

        document.add(Chunk.NEWLINE);
        paragraph = new Paragraph("The total price of the order is: " + totalPrice + "\n", font);
        document.add(paragraph);
        document.close();
    }
}
