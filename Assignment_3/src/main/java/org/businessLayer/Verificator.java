package org.businessLayer;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

public class Verificator {

    /**
     * Se verifica primul cuvant al comenzii, pentru a detecta ce operatie
     * sa fie efectuata.
     * @param strings contine comanda ce are toate cuvintele ca string-uri separate
     * @throws FileNotFoundException exceptie ce provine din metodele apelate, in caz
     * in care nu se gaseste fisierul in care se scrie
     * @throws DocumentException exceptie ce provine din metodele apelate, in caz
     *  in care exista probleme cu fisierul in care se scrie
     */
    public void detectCommand(String[] strings) throws FileNotFoundException, DocumentException {
        if (strings[0].equalsIgnoreCase("Insert")) {
            makeInsert(strings);
        }

        if (strings[0].equalsIgnoreCase("Order")) {
            OrderBLL orderBLL = new OrderBLL();
            orderBLL.makeOrder(strings);
        }

        if(strings[0].equalsIgnoreCase("Report")){
            makeReport(strings);
        }

        if (strings[0].equalsIgnoreCase("Delete")) {
            makeDelete(strings);
        }
    }

    /**
     * Se verifica daca se cere inserarea unui client sau a unui produs,
     * ci se apeleaza metoda corespunzatoare.
     * @param strings comanda de insert
     */
    private void makeInsert(String[] strings) {
        if (strings[1].equalsIgnoreCase("client")) {
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.insertClient(strings);
        } else {
            if (strings[1].equalsIgnoreCase("product")) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.insertOrUpdateProduct(strings);
            }
        }
    }

    /**
     * Se verifica ce trebuie afisata: clientii, produsele sau comenzile,
     * si se apeleaza metoda corespunzatoare.
     * @param strings comanda de report
     * @throws FileNotFoundException exceptie aruncata din metodele de report
     * @throws DocumentException exceptie aruncata din metodele de report
     */
    private void makeReport(String[] strings) throws FileNotFoundException, DocumentException {
        if (strings[1].equalsIgnoreCase("client")) {
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.reportClients();
        } else {
            if (strings[1].equalsIgnoreCase("product")) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.reportProducts();
            } else {
                if (strings[1].equalsIgnoreCase("order")) {
                    OrderBLL orderBLL = new OrderBLL();
                    orderBLL.reportOrders();
                }
            }
        }
    }

    /**
     * Se verifica daca se cere stergerea unui client sau a unui produs,
     * ci se apeleaza metoda corespunzatoare.
     * @param strings comanda de stergere
     */
    private void makeDelete(String[] strings) {
        if (strings[1].equalsIgnoreCase("client")) {
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.deleteClient(strings);
        } else {
            if (strings[1].equalsIgnoreCase("product")) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.deleteProduct(strings);
            }
        }
    }
}
