package org.businessLayer;

import com.itextpdf.text.DocumentException;
import org.dataAccessLayer.ProductDAO;
import org.model.Product;
import org.presentation.ReportGenerator;

import java.io.FileNotFoundException;
import java.util.List;

public class ProductBLL {

    /**
     * Se instantiaza un obiect nou de tip Produs, pe baza proprietatilor
     * enumerate in comanda. Se cauta produsul cu numele respectiv in baza de date.
     * Daca nu exista produs cu numele acesta se insereaza un produs nou,
     * iar daca exist deja se updateaza produsul: cantitatea veche se aduna cu
     * cantitatea din comanda.
     * @param strings cuvintele comenzii
     */
    public void insertOrUpdateProduct(String[] strings) {
        Product product = new Product(0, strings[2], Integer.parseInt(strings[3]), Float.parseFloat(strings[4]));
        ProductDAO productDAO = new ProductDAO();

        if (productDAO.findByName(strings[2]) == null) {
            productDAO.insert(product);
        } else { // daca exista deja produsul respevtic updatam produsul
            Product product1 = productDAO.findByName(strings[2]).get(0);
            product1.setQuantity(product1.getQuantity() + product.getQuantity());
            productDAO.update("quantity", product1.getQuantity(), product1.getID());
        }
    }

    /**
     * Se cauta toate produsele. Se trimit ca parametru metodei reportProduct.
     * Exceptiile sunt aruncate din metoda reportProduct, si aruncate mai
     * departe de metoda curenta.
     * @throws FileNotFoundException in caz daca nu gaseste fisierul
     * care afiseaza produsele
     * @throws DocumentException daca este ceva problema cu documentul
     * pdf care afiseaza produsele
     */
    public void reportProducts() throws FileNotFoundException, DocumentException {
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.findAll();
        new ReportGenerator().reportProduct(productList);
    }

    /**
     * Metoda sterge produsul cu numele specificat in comanda
     * @param strings contine comanda de stergere
     */
    public void deleteProduct(String[] strings) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.delete(strings[2]);
    }
}
