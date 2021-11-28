package com.cofano.coffeecorner.data;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class Database {

    private static final String DB = "CofanoCoffeeCorner";
    private static final String SCHEMA = "public";
    private static final String URL = "jdbc:postgresql:" + DB + "?currentSchema=" + SCHEMA;
    private static final String USER = "db-access-coffeecorner";
    private static final String PASS = "RbY!ztM-DvvmSn8\n";

    public ResultSet resultSet;
    public Statement statement;
    public Connection connection;
    
    public Database() { open(URL, USER, PASS); }

    public Database(String URL, String USER, String PASS) {
        open(URL, USER, PASS);
    }

    public Database(String JNDI) {
        open(JNDI);
    }

    private void open(String JDNI) {

        try {

            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(JDNI);
            connection = ds.getConnection();

        } catch (SQLException | NamingException e) { e.printStackTrace(); }

    }

    private void open(String URL, String USERNAME, String PASSWORD) {

        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }

    }

    public void close() {

        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) { e.printStackTrace(); }

    }

    public ResultSet execute(String query) {
        
        try {

            if (!query.contains("RETURNING")
            		&& (query.startsWith("INSERT")
                    || query.startsWith("UPDATE")
                    || query.startsWith("ALTER")
                    || query.startsWith("DELETE"))) {

                connection.createStatement().executeUpdate(query);

            } else return connection.createStatement().executeQuery(query);

        } catch (SQLException e) { System.out.println(e); }

        return null;

    }
    
    public ResultSet execute(PreparedStatement preparedStatement) {

    	try {

			preparedStatement.execute();
			return preparedStatement.getResultSet();

		} catch (SQLException e) { e.printStackTrace(); }

    	return null;

    }
    
    public int getHighestColumnValue(String column, String table) {

        try {
        
            String query = "SELECT MAX(" + column +") AS max "
                         + "FROM " + table + ";";
            ResultSet r = execute(query);

            while (r.next()) return r.getInt("max");
        
        } catch (SQLException e) { e.printStackTrace(); }

        close();
        return -1;

    }

    public void resetAIValueToHighest(String table) {

        String query = "ALTER TABLE `" + table + "` "
                + "AUTO_INCREMENT=" + getHighestColumnValue("id", table) + ";";

        execute(query);
        close();

    }
    
}