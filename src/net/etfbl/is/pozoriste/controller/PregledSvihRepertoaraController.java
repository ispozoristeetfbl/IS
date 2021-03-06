/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.BIletarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dto.Radnik;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class PregledSvihRepertoaraController implements Initializable {

    @FXML
    private Button bNazad;

    @FXML
    private TableColumn<Radnik, Date> datumColumn;

    public static ObservableList<Repertoar> repertoariObservableList = FXCollections.observableArrayList();

    @FXML
    private Button bDodajRepertoar;

    @FXML
    private TableView sviRepertoariTableView;
    @FXML
    private Button bIzmjeniRepertoar;

    public static boolean izmjenaRepertoara = false;

    public static Repertoar izabraniRepertoar = null;

    @FXML
    void IzmjeniRepertoarAction(ActionEvent event) {
        izmjenaRepertoara = true;
        ObservableList<Repertoar> izabranaVrsta, repertoariObservaleList;
        repertoariObservaleList = sviRepertoariTableView.getItems();
        izabranaVrsta = sviRepertoariTableView.getSelectionModel().getSelectedItems();
        izabraniRepertoar = (Repertoar) izabranaVrsta.get(0);
        if (izabraniRepertoar != null) {

            try {
                Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRepertoar.fxml"));

                Scene dodajRadnikaScene = new Scene(adminController);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(dodajRadnikaScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            upozorenjeRepertoar();
            return;
        }
    }

    private void ubaciKoloneUTabeluRadnik(ObservableList repertoari) {
        datumColumn = new TableColumn("Pregled svih repertoara");
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("mjesecIGodina"));

        sviRepertoariTableView.setItems(repertoariObservableList);
        sviRepertoariTableView.getColumns().addAll(datumColumn);

        sviRepertoariTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && e.getButton().compareTo(MouseButton.PRIMARY) == 0) {
                final Repertoar zaPrikaz = (Repertoar) sviRepertoariTableView.getSelectionModel().getSelectedItem();
                try {
                    Optional<Repertoar> dobaviSvjeze = RepertoarDAO.repertoars().stream().filter(r -> r.getId() == zaPrikaz.getId()).findAny();
                    if (dobaviSvjeze.isPresent()) {
                        PregledRepertoaraController.incijalizacija(dobaviSvjeze.get());
                    }
                    Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRepertoara.fxml"));
                    Scene pregledRepertoara = new Scene(adminController);
                    Stage window = (Stage) sviRepertoariTableView.getScene().getWindow();
                    window.setScene(pregledRepertoara);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(PregledSvihRepertoaraController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    void dodajRepertoaraAction(ActionEvent event) {
        izmjenaRepertoara = false;
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRepertoar.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void nazadNaAdminFormu(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        repertoariObservableList.removeAll(repertoariObservableList);
        repertoariObservableList.addAll(RepertoarDAO.repertoars());
        ubaciKoloneUTabeluRadnik(repertoariObservableList);
        datumColumn.prefWidthProperty().bind(sviRepertoariTableView.widthProperty().divide(1));
    }

    private void upozorenjeRepertoar() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom izbora repertoara !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite repertoar");
        alert.showAndWait();
    }

}
