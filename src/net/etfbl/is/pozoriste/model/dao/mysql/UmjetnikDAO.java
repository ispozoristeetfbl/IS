/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;

/**
 *
 * @author djord
 */
public class UmjetnikDAO {

    public static void dodajUmjetnika(Umjetnik umjetnik) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeUmjetnika(?,?,?,?,?,?)}");
            callableStatement.setString(1, umjetnik.getIme());
            callableStatement.setString(2, umjetnik.getPrezime());
            callableStatement.setString(3, umjetnik.getJmb());
            callableStatement.setString(4, umjetnik.getOpisPosla());
            callableStatement.setString(5, umjetnik.getKontakt());
            callableStatement.setString(6, umjetnik.getBiografija());

            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void ubaciUTabeluRadnik() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Umjetnik umjetnik;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from umjetnici_info");
            while (rs.next()) {

                umjetnik = new Umjetnik(rs.getString("Ime"), rs.getString("Prezime"), rs.getString("OpisPosla"), rs.getString("JMB"), rs.getBoolean("StatusRadnika"), rs.getString("Kontakt"), rs.getString("Biografija"));
                if (!PregledRadnikaController.radniciObservaleList.contains(umjetnik)) {
                    PregledRadnikaController.radniciObservaleList.add(umjetnik);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
