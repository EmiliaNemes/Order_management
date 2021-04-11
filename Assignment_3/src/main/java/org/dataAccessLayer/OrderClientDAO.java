package org.dataAccessLayer;

import org.connection.ConnectionFactory;
import org.model.OrderClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class OrderClientDAO extends AbstractDAO<OrderClient>{
    private static final String deleteStatement = "DELETE FROM `orderclient` WHERE ID = ?";

    /**
     * Se face coenxiunea cu baza de date. Se executa statement-ul
     * cu valoarea ID-ului primit ca parametru.
     * @param ID Id-ul orderClient-ului pe care vrem sa-l stergem
     */
    public void deleteOrderClient(int ID) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement(deleteStatement);
            statement.setInt(1, ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "DAO: delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(dbConnection);
        }
    }
}
