package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.AdministratorDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class LogInController implements Initializable {

    @FXML 
    private TextField tfKorisnickoIme; 

    @FXML 
    private TextField tfLozinka; 

    @FXML 
    private Button bPotvrda; 

    public static String tipKorisnika = "";

    @FXML
    private Label lKorisnik;

    @FXML
    private Label lSifra;

    public static Integer idLogovanog=null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lKorisnik.setGraphic(new ImageView(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/rsz_korisnik.png"))));
        lSifra.setGraphic(new ImageView(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/rsz_sifra.png"))));
    }

    private boolean provjeraAutentifikacije(String username, String password) {
        String passwordHash = hashSHA256(password);
        String userType = postojiUBazi(username, passwordHash);
        if ("".equals(userType)) {
            return false;
        }
        return true;
    }

    private String postojiUBazi(String username, String passwordHash) {
        boolean postoji = false;
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call provjeraLogovanja(?,?,?)}");
            callableStatement.setString(1, username);
            callableStatement.setString(2, passwordHash);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.executeQuery();
            postoji = callableStatement.getBoolean(3);
            if (postoji) {
                callableStatement = connection.prepareCall("{call provjeraLozinkeIKorisnickogImena(?,?)}");
                callableStatement.setString(1, username);
                callableStatement.setString(2, passwordHash);

                resultSet = callableStatement.executeQuery();
                if (resultSet.next()) {
                    tipKorisnika = resultSet.getString("tipKorisnika");
                }
            } else {
                upozorenjeLogovanje();
            }
            return tipKorisnika;
        } catch (SQLException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionPool.getInstance().checkIn(connection);
        }
        return "";
    }

    private String hashSHA256(String value) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(encodedhash);
        return hash;
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @FXML
    void potvrdaAction(ActionEvent event) throws IOException {
        if (provjeraAutentifikacije(tfKorisnickoIme.getText(), tfLozinka.getText())) {
            idLogovanog=AdministratorDAO.vratiId(tfKorisnickoIme.getText());
            if ("Administrator".equals(tipKorisnika)) {
                try {
                    Parent pozoristeController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

                    Scene pozScene = new Scene(pozoristeController);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    window.setTitle("Administrator");
                    window.setResizable(false);

                    window.setScene(pozScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("Biletar".equals(tipKorisnika)) {
                try {
                    Parent pozoristeController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRepertoara.fxml"));
                    Scene pozScene = new Scene(pozoristeController);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setTitle("Biletar");
                    window.setResizable(false);
                    window.setScene(pozScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void upozorenjeLogovanje() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa korisnickog imena ili lozinke!");
        alert.setHeaderText(null);
        alert.setContentText("Pogresno korisnicko ime ili lozinka!");
        alert.showAndWait();
        return;
    }

    private void upozorenjeKorisnik() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa korisnickog imena!");
        alert.setHeaderText(null);
        alert.setContentText("Pogresno korisnicko ime!");
        alert.showAndWait();
    }

    private void upozorenjeLozinka() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa lozinke!");
        alert.setHeaderText(null);
        alert.setContentText("Netacna lozinka");
        alert.showAndWait();
    }

}
