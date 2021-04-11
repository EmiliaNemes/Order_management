package org.businessLayer;

import com.itextpdf.text.DocumentException;
import org.bll.validators.ClientValidator;
import org.bll.validators.ProductQuantityValidator;
import org.dataAccessLayer.ClientDAO;
import org.dataAccessLayer.OrderClientDAO;
import org.dataAccessLayer.OrderProductsDAO;
import org.dataAccessLayer.ProductDAO;
import org.model.Client;
import org.model.OrderClient;
import org.model.OrderProducts;
import org.model.Product;
import org.presentation.BillGenerator;
import org.presentation.ErrorGenerator;
import org.presentation.ReportGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OrderBLL {
    private int totalPrice;
    private int orderClientID;
    private List<Product> productList;
    private List<Integer> quantityList;
    private Client client;

    /**
     * Se cauta clientul cu numele din comanda.
     * Se face un OrderClient nou, si se insereaza in baza de date.
     * @param strings cuvintele comenzii
     */
    public void insertOrder(String[] strings) {
        ClientDAO clientDAO = new ClientDAO();
        client = clientDAO.findByName(strings[1] + " " + strings[2]).get(0);
        OrderClient orderClient = new OrderClient(0, client.getID(), totalPrice);
        orderClientID = new OrderClientDAO().insert(orderClient);
    }

    /**
     * Se face update OrderClientului cu totalPrice-ul calculat.
     */
    public void updateOrder() {
        new OrderClientDAO().update("totalPrice", totalPrice, orderClientID);
    }

    /**
     * Se parcurge lista de produse ce contine produsele comandate.
     * Daca cantitatea pe stoc a unui produs devine zero, produsul se sterge.
     * Altfel se updateaza la valoarea noua, cea de dupa livrarea comezii.
     * Se insereaza un OrderProduct nou in baza de date pentru fiecare
     * produs din comanda.
     * @param strings cuvintele comenzii
     */
    public void insertProductOrder(String[] strings){
        quantityList = new ArrayList<>();
        int i = 4;
        OrderProducts orderProducts;
        int selledQuantity;
        for (Product p : productList) {
            selledQuantity = Integer.parseInt(strings[i]);
            quantityList.add(selledQuantity);
            if(p.getQuantity() - selledQuantity == 0){
                new ProductDAO().delete(p.getName());
            } else {
                p.setQuantity(p.getQuantity() - selledQuantity);
                new ProductDAO().update("quantity", p.getQuantity(), p.getID());
            }
            orderProducts = new OrderProducts(0, orderClientID, p.getID(),Integer.parseInt(strings[i]));
            new OrderProductsDAO().insert(orderProducts);
            totalPrice += (Integer.parseInt(strings[i]) * p.getPrice());
            i += 2;
        }
    }

    /**
     * Se verifica daca clientul din comanda este valid (exista in baza de date),
     * si daca exista pe stoc cantitatea necesara din produsele comandate.
     * Daca nu se genereaza mesaje corespunzatoare, daca da se insereaza datele
     * orderului in tabelele corespunzatoare: orderclient si orderproducts, si
     * se genereaza factura pentru comanda.
     * @param strings cuvintele din comanda
     * @throws FileNotFoundException in caz daca nu gaseste fisierul
     * care afiseaza erorile sau genereaza factura
     * @throws DocumentException daca este ceva problema cu documentul
     * pdf care afiseaza erorile sau genereaza factura
     */
    public void makeOrder(String[] strings) throws FileNotFoundException, DocumentException {
        ClientValidator clientValidator = new ClientValidator();

        if (clientValidator.validate(strings)) {
            new ErrorGenerator().generateInvalidClientMessage(strings[1], strings[2]);
        } else {
            ProductQuantityValidator productQuantityValidator = new ProductQuantityValidator();
            if (productQuantityValidator.validate(strings)) {
                new ErrorGenerator().generateUnderStockMessage();
            } else {
                insertOrder(strings);
                productList = productQuantityValidator.getProductList();
                insertProductOrder(strings);
                updateOrder();
                new BillGenerator().generateBill(client.getName(), productList, quantityList, totalPrice);
            }
        }
    }

    /**
     * Se cauta toate comezile, si se apeleaza metoda reportOrder din clasa
     * ReportGenerator cu parametrul orderClientList, ce contine toti orderii.
     * @throws FileNotFoundException in caz daca nu gaseste fisierul
     * @throws DocumentException daca este ceva problema cu documentul pdf
     */
    public void reportOrders() throws FileNotFoundException, DocumentException {
        OrderClientDAO orderClientDAO = new OrderClientDAO();
        List<OrderClient> orderClientList = orderClientDAO.findAll();
        new ReportGenerator().reportOrder(orderClientList);
    }
}
