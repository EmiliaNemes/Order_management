package org.businessLayer;

import com.itextpdf.text.DocumentException;
import org.dataAccessLayer.ClientDAO;
import org.dataAccessLayer.OrderClientDAO;
import org.dataAccessLayer.OrderProductsDAO;
import org.model.Client;
import org.model.OrderClient;
import org.presentation.ReportGenerator;

import java.io.FileNotFoundException;
import java.util.List;

public class ClientBLL {

    /**
     * Se instantiaza un obiect nou de tip Client, pe baza proprietatilor
     * enumerate in comanda. Se apeleaza metoda insert, din clasa ClientDAO.
     * @param strings cuvintele comenzii
     */
    public void insertClient(String[] strings) {
        Client client = new Client(0, strings[2] + " " + strings[3], strings[4]);
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.insert(client);
    }

    /**
     * Se cauta toti clientii. Si se trimit ca parametru metodei reportClient.
     * Exceptiile sunt aruncate din metoda reportClient, si aruncate mai
     * departe de metoda curenta.
     * @throws FileNotFoundException in caz daca nu gaseste fisierul
     * care afiseaza clientii
     * @throws DocumentException daca este ceva problema cu documentul
     * pdf care afiseaza clientii
     */
    public void reportClients() throws FileNotFoundException, DocumentException {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clientList = clientDAO.findAll();
        new ReportGenerator().reportClient(clientList);
    }

    /**
     * Se sterg nu numai clientii primite ca parametru, ci si order-ele
     * facute de aceste clienti.
     * Se cauta item-urile din orderClient care au clientID-ul egal cu
     * ID-ul clientului pe care vrem sa-l stergem.
     * Se cauta item-urile din orderProducts care au orderID-ul egal cu
     * ID-ul orderClient-urilor gasite anterior. Se sterg orderProducts-urile
     * gasite. Dupa care se sterg si orderClient-urile gasite.
     * In final se sterge si clientul care are numele din comanda.
     * @param strings cuvintele comenzii
     */
    public void deleteClient(String[] strings) {
        ClientDAO clientDAO = new ClientDAO();

        OrderClientDAO orderClientDAO = new OrderClientDAO();
        Client clientToBeDeleted = clientDAO.findByName(strings[2] + " " + strings[3]).get(0);
        List<OrderClient> orderClientList = orderClientDAO.findByAttributeID("clientID", clientToBeDeleted.getID());
        OrderProductsDAO orderProductsDAO = new OrderProductsDAO();

        for(OrderClient ordCl: orderClientList){
            orderProductsDAO.deleteOrderProduct(ordCl.getID());
        }

        for(OrderClient ordCl: orderClientList){
            orderClientDAO.deleteOrderClient(ordCl.getID());
        }

        clientDAO.delete(strings[2] + " " + strings[3]);
    }

}
