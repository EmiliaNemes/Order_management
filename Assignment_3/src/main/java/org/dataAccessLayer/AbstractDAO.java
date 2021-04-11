package org.dataAccessLayer;

import org.connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unhecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Se concateneaza cuvintele statementului. Field-ul dupa care se conditioneaza cautarea este dat ca parametru. Tabela in care se cauta coincide cu numele clasei, a carei instanta este obiectul cu care se apeleaza metoda.
     * @param field field-ul dupa care se face cautarea
     * @return statementul selectiei
     */
    String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append("`").append(type.getSimpleName()).append("`");
        sb.append(" WHERE ").append(field).append("=?");
        return sb.toString();
    }

    /**
     * Se concateneaza cuvintele statementului. Tabela in care se cauta coincide cu numele clasei carei instanta este obiectul cu care se apeleaza metoda.
     * @return statementul selectiei
     */
    String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append("`").append(type.getSimpleName()).append("`");
        return sb.toString();
    }

    /**
     * Se concateneaza cuvintele statementului. Tabela din care se sterge coincide cu numele clase corespunzatoare obiectului cu care se apeleaza metoda iar conditionarea se face dupa field-ul 'name'.
     * @return statementul stergerii
     */
    private String createDeleteQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append(" FROM ");
        sb.append("`").append(type.getSimpleName()).append("`");
        sb.append(" WHERE ").append(" name ").append("=?");
        return sb.toString();
    }

    /**
     * Se concateneaza cuvintele statementului.Tabela in care se face update coincide cu numele clasei, iar conditionarea se face dupa field-ul 'id'.
     * @param field numele field-ului care trebuie setat
     * @return statementul update-ului
     */
    private String createUpdateQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append("`").append(type.getSimpleName()).append("`");
        sb.append(" SET ").append(field).append("=?");
        sb.append(" WHERE ").append(" id ").append("=?");
        return sb.toString();
    }

    /**
     * Se concateneaza cuvintele statementului. Tabela in care se insereaza coincide cu numele clasei al instantei. Se parcurg toate field-urile clasei si numele lor se pune in statement.Dupa care se pune un numar egal cu numarul field-urilor de semnul intrebarii dupa VALUES.
     * @return statementul insert-ului
     */
    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT ");
        sb.append(" INTO ");
        sb.append("`").append(type.getSimpleName()).append("`");
        sb.append(" (");
        int i = 0;
        for (Field field : type.getDeclaredFields()) {
            if (!field.getName().equals("ID")) {
                sb.append(field.getName()).append(",");
                i++;
            }
        }
        sb.deleteCharAt(sb.length() - 1); // sa stergem ultima virgula
        sb.append(") ");
        sb.append(" VALUES(");
        for (int k = 0; k < i; k++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1); // sa stergem ultima virgula
        sb.append(");");

        return sb.toString();
    }

    /**
     * Se face conexiunea cu baza de date. Se foloseste statementul de create, cu optiunea sa se genereze si valorile returnate, adica valorile inserate in tabela. Field-urile cu semnul intrebarii se seteaza la valorile atributelor ale obiectului.
     * @param object obiectul care trebuie inserat in tabela care coincide
     *               cu numele clasei a carui instanta este obiectul
     * @return se returneaza ID-ul ce primeste elementul in tabela
     */
    public int insert(Object object) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        int insertedID = 0;

        try {
            statement = dbConnection.prepareStatement(createInsertQuery(), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value;
                try {
                    if (!field.getName().equals("ID")) {
                        value = field.get(object);
                        statement.setString(i, value.toString());
                        i++;
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                insertedID = rs.getInt(1); // to return the inserted row's ID
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "DAO: insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedID;
    }

    /**
     * Se face conexiunea cu baza de date. Se introduc valorile parametrilor in locul semnelor de intrebare. Se executa statement-ul.
     * @param fieldName field-ul care se updateaza
     * @param value valoarea la care se updateaza field-ul
     * @param ID ID-ul elementului care se updateaza
     */
    public void update(String fieldName, int value, int ID) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement(createUpdateQuery(fieldName));
            statement.setInt(1, value);
            statement.setInt(2, ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "AbstractDAO: update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(dbConnection);
        }
    }

    /**
     * Se face conexiunea cu baza de date. Se introduce valoarea parametrului 'name' in locul semnului de intrebare din statement. Se executa statement-ul.
     * @param name numele elementului care se sterge
     */
    public void delete(String name) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            String query = createDeleteQuery();
            statement = dbConnection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "DAO: delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(dbConnection);
        }
    }

    /**
     * Se face conexiunea cu baza de date. Se introduce valoarea parametrului 'name' in locul semnului de intrebare din statement. Se executa statement-ul. Se creeaza obiectele cu proprietatea ceruta.
     * @param name numele dupa care se face cautarea
     * @return lista de obiecte gasite cu numele specificat
     */
    public List<T> findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<T> toReturn = null;
        String query = createSelectQuery("name");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                resultSet.beforeFirst();
                toReturn = createObjects(resultSet);
            } else {
                return null ; //new ArrayList<T>();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO: findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return toReturn;
    }

    /**
     * Se face conexiunea cu baza de date. Se introduce valoarea parametrului 'id' in locul semnului de intrebare din statement. Se executa statement-ul. Se creeaza obiectul cu proprietatea ceruta.
     * @param id id-ul dupa care se face cautarea
     * @return obiectul gasit cu id-ul specificat, id-urile sunt unice deci va fi un singur element
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findById" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Se face conexiunea cu baza de date. Se executa statement-ul, care returneaza toate elementele dintr-o tabela. Se creeaza o lista cu elementele.
     * @return lista de obiecte cu toate elementele tabelei
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findAll" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
       return null;
    }

    /**
     * Se face conexiunea cu baza de date. Se introduce valoarea parametrului 'ID' in locul semnului de intrebare din statement. Se executa statement-ul. Se creeaza o lista cu obiectele returnate.
     * @param fieldName numele campului dupa care se face cautarea
     * @param ID id-ul orderului dupa care se face cautarea
     * @return lista de obiecte cu specificatia ceruta
     */
    public List<T> findByAttributeID(String fieldName, int ID) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(fieldName);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, ID);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findById" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Se face o instanta noua de tipul clasei al obiectului cu care se apeleaza metoda care apeleaza metoda aceasta. Field-urile lui se populeaza cu datele returnate de BD, stocate in parametrul resultSet.
     * @param resultSet variabila returnata de baza de date cu rezultatele statement-elor
     * @return o lista de obiecte creata pe baza resultSet-ului
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor((String)field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | IntrospectionException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }
}
