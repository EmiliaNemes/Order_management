package org.bll.validators;

import org.dataAccessLayer.ProductDAO;
import org.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductQuantityValidator implements Validator {
    /**
     * Produsele din comanda se salveaza intr-o lista de produse.
     */
    private List<Product> productList;

    /**
     * Metoda suprascrie metoda validate din interfata Validator.
     * Se parcurg produsele enumerate in comanda, si se verifica
     * daca este pe stoc cantitatea ceruta de produs(e).
     * @param strings cuvintele din comanda
     * @return daca exista pe stoc cantitatea ceruta de produs(e)
     */
    @Override
    public boolean validate(String[] strings) {
        int numberOfProducts = (strings.length - 3) / 2;
        int i = 3;
        ProductDAO productDAO = new ProductDAO();
        productList = new ArrayList<>();
        while (numberOfProducts > 0) {
            numberOfProducts--;
            Product product = productDAO.findByName(strings[i]).get(0);
            if (product.getQuantity() < Integer.parseInt(strings[i + 1])) {
                return true;
            }
            productList.add(product);
            i+=2;
        }
        return false;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
