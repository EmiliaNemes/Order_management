package org.bll.validators;

import org.dataAccessLayer.ClientDAO;

public class ClientValidator implements Validator{
    /**
     * Metoda suprascrie metoda validate din interfata Validator.
     * Se apeleaza din ea metoda findByName, pentru a verifica
     * daca exista clientul cu numele din string.
     * @param strings cuvintele din comanda
     * @return daca exista sau nu in baza de date clientul cu numele din comanda
     */
    @Override
    public boolean validate(String[] strings) {
        ClientDAO clientDAO = new ClientDAO();
        return clientDAO.findByName(strings[1] + " " + strings[2]) == null;
    }
}
