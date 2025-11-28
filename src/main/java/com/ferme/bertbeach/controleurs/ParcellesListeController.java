package com.ferme.bertbeach.controleurs;

import com.ferme.bertbeach.domaine.GestionParcellesService;
import com.ferme.bertbeach.domaine.Parcelle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Contrôleur de l'écran principal listant les parcelles.
 * L'interface sert de point d'entrée aux cas d'utilisation :
 *  - CU01 : Ajouter / Modifier une parcelle
 *  - CU02 : Supprimer une parcelle
 *  - CU03 : Consulter une parcelle
 */
public class ParcellesListeController {

    // --- Composants FXML ---

    @FXML
    private TableView<Parcelle> tableParcelles;

    @FXML
    private TableColumn<Parcelle, String> colonneNom;

    @FXML
    private TableColumn<Parcelle, Double> colonneSuperficie;

    @FXML
    private TableColumn<Parcelle, String> colonneLocalisation;

    @FXML
    private TableColumn<Parcelle, String> colonneCulture;

    // --- Données affichées dans la table ---

    private final ObservableList<Parcelle> parcelles = FXCollections.observableArrayList();

    // --- Services métiers et contexte ---
    private GestionParcellesService service;
    private Stage scenePrincipale;

    /**
     * Méthode appelée par l'application (voir App.java) une fois le service disponible.
     */
    public void initialiserAvecService(GestionParcellesService service, Stage stage) {
        this.service = service;
        this.scenePrincipale = stage;
        configurerColonnes();
        rafraichirTable();
    }

    /**
     * Prépare l'association entre les colonnes du tableau et les propriétés de la classe métier.
     */
    private void configurerColonnes() {
        colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficieHectares"));
        colonneLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colonneCulture.setCellValueFactory(new PropertyValueFactory<>("cultureActive"));
        tableParcelles.setItems(parcelles);
    }

    // --- Gestion des actions des boutons ---

    @FXML
    private void onAjouterParcelle(ActionEvent event) {
        afficherFormulaireEdition(null);
    }

    @FXML
    private void onModifierParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }
        afficherFormulaireEdition(selection);
    }

    @FXML
    private void onSupprimerParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Suppression de parcelle (CU02)");
        confirmation.setHeaderText("Confirmer la suppression");
        confirmation.setContentText("Supprimer définitivement la parcelle \"" + selection.getNom() + "\" ?");
        Optional<ButtonType> choix = confirmation.showAndWait();
        if (choix.isPresent() && choix.get() == ButtonType.OK) {
            service.supprimerParcelle(selection.getIdentifiant());
            rafraichirTable();
            afficherInfo("Parcelle supprimée", "La parcelle a été retirée du référentiel.");
        }
    }

    @FXML
    private void onConsulterParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }

        Alert details = new Alert(Alert.AlertType.INFORMATION);
        details.setTitle("Consultation d'une parcelle (CU03)");
        details.setHeaderText(selection.getNom());
        details.setContentText(
                "Identifiant : " + selection.getIdentifiant() + "\n" +
                        "Superficie : " + selection.getSuperficieHectares() + " ha\n" +
                        "Localisation : " + selection.getLocalisation() + "\n" +
                        "Culture active : " + selection.getCultureActive() + "\n" +
                        "Historique : " + selection.getHistoriqueCulture());
        details.showAndWait();
    }

    // --- Méthodes utilitaires privées ---

    private void afficherErreurSelection() {
        Alert alerte = new Alert(Alert.AlertType.WARNING);
        alerte.setTitle("Aucune parcelle sélectionnée");
        alerte.setHeaderText(null);
        alerte.setContentText("Veuillez sélectionner une parcelle dans la liste.");
        alerte.showAndWait();
    }

    private void afficherInfo(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    /**
     * Ouvre un petit formulaire pour créer ou mettre à jour une parcelle (CU01).
     */
    private void afficherFormulaireEdition(Parcelle parcelleExistante) {
        Alert dialogue = new Alert(Alert.AlertType.NONE);
        dialogue.initOwner(scenePrincipale);
        dialogue.setTitle(parcelleExistante == null ? "Ajouter une parcelle" : "Modifier une parcelle");
        dialogue.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField champNom = new TextField(parcelleExistante != null ? parcelleExistante.getNom() : "");
        TextField champSuperficie = new TextField(parcelleExistante != null ? String.valueOf(parcelleExistante.getSuperficieHectares()) : "");
        TextField champLocalisation = new TextField(parcelleExistante != null ? parcelleExistante.getLocalisation() : "");
        TextField champCulture = new TextField(parcelleExistante != null ? parcelleExistante.getCultureActive() : "");
        TextArea champHistorique = new TextArea(parcelleExistante != null ? parcelleExistante.getHistoriqueCulture() : "");
        champHistorique.setPromptText("Historique cultural (rotations, événements notables...)");
        champHistorique.setWrapText(true);

        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);
        grille.addRow(0, new javafx.scene.control.Label("Nom"), champNom);
        grille.addRow(1, new javafx.scene.control.Label("Superficie (ha)"), champSuperficie);
        grille.addRow(2, new javafx.scene.control.Label("Localisation"), champLocalisation);
        grille.addRow(3, new javafx.scene.control.Label("Culture active"), champCulture);
        grille.addRow(4, new javafx.scene.control.Label("Historique"), champHistorique);
        dialogue.getDialogPane().setContent(grille);

        Optional<ButtonType> resultat = dialogue.showAndWait();
        if (resultat.isEmpty() || resultat.get() == ButtonType.CANCEL) {
            return; // l'utilisateur annule, aucune action.
        }

        try {
            double superficie = Double.parseDouble(champSuperficie.getText());
            if (parcelleExistante == null) {
                // Création
                Parcelle nouvelle = new Parcelle(
                        champNom.getText(),
                        superficie,
                        champLocalisation.getText(),
                        champCulture.getText(),
                        champHistorique.getText()
                );
                service.ajouterOuModifierParcelle(nouvelle);
            } else {
                // Mise à jour
                parcelleExistante.mettreAJour(
                        champNom.getText(),
                        superficie,
                        champLocalisation.getText(),
                        champCulture.getText(),
                        champHistorique.getText()
                );
                service.ajouterOuModifierParcelle(parcelleExistante);
            }
            rafraichirTable();
            afficherInfo("Parcelle sauvegardée", "Les informations ont été enregistrées.");
        } catch (NumberFormatException e) {
            afficherInfo("Valeur incorrecte", "La superficie doit être un nombre (ex : 4.5).");
        } catch (IllegalArgumentException e) {
            afficherInfo("Validation", e.getMessage());
        }
    }

    /**
     * Recharge les données depuis le service applicatif.
     */
    private void rafraichirTable() {
        parcelles.setAll(service.listerParcelles());
    }
}
