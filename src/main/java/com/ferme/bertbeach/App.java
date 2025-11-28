package com.ferme.bertbeach;

import com.ferme.bertbeach.controleurs.ParcellesListeController;
import com.ferme.bertbeach.domaine.GestionParcellesService;
import com.ferme.bertbeach.domaine.Parcelle;
import com.ferme.bertbeach.infrastructure.memoire.ParcelleMemoireRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Point d'entrée graphique basé sur JavaFX. L'application charge une interface
 * utilisateur en français permettant d'exécuter les cas d'utilisation :
 *  - CU01 : Ajouter ou modifier une parcelle via un formulaire
 *  - CU02 : Supprimer une parcelle
 *  - CU03 : Consulter les informations d'une parcelle
 *
 * Le reste du code métier reste identique à la version console. Seul l'usage
 * change : l'utilisateur remplit désormais un formulaire plutôt que la console.
 */
public class App {

    private GestionParcellesService service;

    /**
     * Méthode standard JavaFX appelée après le lancement. On charge la vue FXML,
     * on injecte le service métier et on présente la fenêtre principale.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Initialisation du domaine avec un dépôt en mémoire et une donnée de démonstration.
        service = new GestionParcellesService(new ParcelleMemoireRepository());
        service.ajouterOuModifierParcelle(new Parcelle(
                "Parcelle du Nord", 2.3, "Champ Nord", "Blé",
                "Rotation blé/maïs sur les 3 dernières années"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ferme/bertbeach/vues/parcelles-liste.fxml"));
        Parent racine = loader.load();

        // On transmet le service au contrôleur afin qu'il puisse manipuler les données.
        ParcellesListeController controller = loader.getController();
        controller.initialiserAvecService(service, stage);

        Scene scene = new Scene(racine, 800, 600);
        stage.setTitle("Gestion des parcelles - Ferme St-Bert-Beach");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Lance le cycle de vie JavaFX. "App" étant la classe Application, JavaFX
        // retrouvera automatiquement la méthode start(Stage).
        Application.launch(App.class, args);
    }
}
