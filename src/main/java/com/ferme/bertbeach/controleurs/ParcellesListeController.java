package com.ferme.bertbeach.controleurs;

import com.ferme.bertbeach.modele.Parcelle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Contrôleur de l'écran principal listant les parcelles.
 * Cet écran sera le point d'entrée pour les CU :
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
    private TableColumn<Parcelle, String> colonneTypeSol;

    @FXML
    private TableColumn<Parcelle, String> colonneCulture;

    @FXML
    private Button boutonAjouter;

    @FXML
    private Button boutonModifier;

    @FXML
    private Button boutonSupprimer;

    @FXML
    private Button boutonConsulter;

    // --- Données affichées dans la table ---

    private final ObservableList<Parcelle> parcelles = FXCollections.observableArrayList();

    /**
     * Méthode appelée automatiquement par JavaFX après le chargement du FXML.
     * On y initialise les colonnes et on charge des données de test (en attendant la BD).
     */
    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés de la classe Parcelle
        colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficieHa"));
        colonneTypeSol.setCellValueFactory(new PropertyValueFactory<>("typeSol"));
        colonneCulture.setCellValueFactory(new PropertyValueFactory<>("cultureActuelle"));

        // Pour l'instant : données de démonstration (en attendant le DAO SQLite)
        parcelles.addAll(
                new Parcelle(1, "Champ Nord", 4.5, "Limoneux", "Maïs", "Parcelle proche de la route"),
                new Parcelle(2, "Prairie Est", 3.2, "Argileux", "Foin", "Drainage à vérifier"),
                new Parcelle(3, "Champ Sud", 5.8, "Sableux", "Soja", "Historique de rendement variable")
        );

        tableParcelles.setItems(parcelles);
    }

    // --- Gestion des actions des boutons ---
    // Pour l'instant : simple affichage de messages. On branchera les vraies logiques CU01–02–03 ensuite.

    @FXML
    private void onAjouterParcelle(ActionEvent event) {
        afficherInfo("Ajouter", "Ouverture du formulaire d'ajout (CU01) – à implémenter.");
    }

    @FXML
    private void onModifierParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }
        afficherInfo("Modifier", "Ouverture du formulaire de modification pour : " + selection.getNom());
    }

    @FXML
    private void onSupprimerParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }

        // Pour l'instant, on supprime directement dans la liste.
        // Plus tard, on branchera sur le DAO (CU02).
        parcelles.remove(selection);
        afficherInfo("Supprimer", "Parcelle supprimée (simulation, pas encore de BD) : " + selection.getNom());
    }

    @FXML
    private void onConsulterParcelle(ActionEvent event) {
        Parcelle selection = tableParcelles.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherErreurSelection();
            return;
        }

        // Plus tard : on ouvrira une fenêtre de consultation détaillée (historique, etc.)
        afficherInfo(
                "Consulter",
                "Consultation de la parcelle :\n" +
                        "- Nom : " + selection.getNom() + "\n" +
                        "- Superficie : " + selection.getSuperficieHa() + " ha\n" +
                        "- Type de sol : " + selection.getTypeSol() + "\n" +
                        "- Culture : " + selection.getCultureActuelle()
        );
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
}
