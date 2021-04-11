package org.dataAccessLayer;

import org.connection.ConnectionFactory;
import org.model.OrderProducts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class OrderProductsDAO extends AbstractDAO<OrderProducts> {
   private static final String deleteStatement = "DELETE FROM `orderproducts` WHERE orderID = ?";

    /**
     * Se face coenxiunea cu baza de date. Se executa statement-ul
     * cu valoarea orderID-ului primit ca parametru.
     * @param orderID este Id-ul orderClient-ului pe care vrem sa-l stergem
     */
    public void deleteOrderProduct(int orderID) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement(deleteStatement);
            statement.setInt(1, orderID);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "DAO: delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(dbConnection);
        }
    }
}
