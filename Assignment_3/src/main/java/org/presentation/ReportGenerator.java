package org.presentation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.dataAccessLayer.ClientDAO;
import org.dataAccessLayer.OrderProductsDAO;
import org.dataAccessLayer.ProductDAO;
import org.model.Client;
import org.model.OrderClient;
import org.model.OrderProducts;
import org.model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ReportGenerator {
    private static int clientContor = 1;
    private static int productContor = 1;
    private static int orderContor = 1;

    /**
     * Se face un document pdf nou, in care se insereaza o tabela cu toti clientii inregistrati in baza de date.
     * @param clientList lista cu clientii care trebuie afisate
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza mesajul de eroare
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza mesajul de eroare
     */
    public void reportClient(List<Client> clientList) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ReportClients" + clientContor + ".pdf"));
        clientContor++;
        document.open();

        PdfPTable table = new PdfPTable(2);
        table.addCell("Name");
        table.addCell("Address");

        for (Client c : clientList) {
            table.addCell(c.getName());
            table.addCell(c.getAddress());
        }

        document.add(table);
        document.close();
    }

    /**
     * Se face un document pdf nou, in care se insereaza o tabela cu toate produsele inregistrate in baza de date.
     * @param productList lista cu produsele care trebuie afisate
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza mesajul de eroare
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza mesajul de eroare
     */
    public void reportProduct(List<Product> productList) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ReportProducts" + productContor + ".pdf"));
        productContor++;
        document.open();

        PdfPTable table = new PdfPTable(3);
        table.addCell("Name");
        table.addCell("Quantity");
        table.addCell("Price");

        for (Product p : productList) {
            table.addCell(p.getName());
            table.addCell(p.getQuantity() + "");
            table.addCell(p.getPrice() + "");
        }

        document.add(table);
        document.close();
    }

    /**
     * Se face un document pdf nou, in care se insereaza o tabela cu toti orderii inregistrati in baza de date.
     * @param orderClientList lista cu comenzile care trebuie afisate
     * @throws FileNotFoundException in caz daca nu gaseste fisierul care afiseaza mesajul de eroare
     * @throws DocumentException daca este ceva problema cu documentul pdf care afiseaza mesajul de eroare
     */
    public void reportOrder(List<OrderClient> orderClientList) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ReportOrders" + orderContor + ".pdf"));
        orderContor++;
        document.open();
        PdfPTable table = new PdfPTable(4);
        table.addCell("Client");
        table.addCell("Product");
        table.addCell("Ordered Quantity");
        table.addCell("Total Price");

        for (OrderClient oc : orderClientList) {
            Client client = new ClientDAO().findById(oc.getClientID());
            table.addCell(client.getName()); // clientName
            OrderProductsDAO orderProductsDAO = new OrderProductsDAO();
            List<OrderProducts> orderProductsList = orderProductsDAO.findByAttributeID("orderID",oc.getID());
            StringBuilder productsString = new StringBuilder();
            StringBuilder quantitiesString = new StringBuilder();

            for(OrderProducts op: orderProductsList){
                Product product = new ProductDAO().findById(op.getProductID());
                productsString.append(product.getName()).append("\n");
                quantitiesString.append(op.getQuantity()).append("\n");
            }
            table.addCell(String.valueOf(productsString)); // productName
            table.addCell(String.valueOf(quantitiesString)); // Ordered Quantities
            table.addCell(oc.getTotalPrice()+"");
        }
        document.add(table);
        document.close();
    }
}
