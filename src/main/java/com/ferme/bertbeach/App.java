package com.ferme.bertbeach;

import com.ferme.bertbeach.controleurs.ParcellesListeController;
import com.ferme.bertbeach.domaine.GestionParcellesService;
import com.ferme.bertbeach.domaine.Parcelle;
import com.ferme.bertbeach.infrastructure.memoire.ParcelleMemoireRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée JavaFX. Cette classe prépare le service métier et injecte les données
 * initiales dans la vue pour permettre de démontrer les cas d'utilisation CU01 à CU03.
 */
public class App {

    private GestionParcellesService service;

    public static void main(String[] args) {
        launch(args);
    }

    private GestionParcellesService service;

    public static void main(String[] args) {
        // Appel explicite à l'utilitaire JavaFX pour lever l'ambiguïté sur la méthode statique
        Application.launch(App.class, args);
    }

    @Override
    @SuppressWarnings("unused") // Méthode appelée par le cycle de vie JavaFX
    public void start(Stage stage) throws Exception {
        // Couche domaine + dépôt en mémoire (remplaçable par une BD ultérieurement)
        this.service = new GestionParcellesService(new ParcelleMemoireRepository());
        chargerJeuDonneesDemonstration();

        // Chargement de l'interface FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ferme/bertbeach/vues/parcelles-liste.fxml"));
        Scene scene = new Scene(loader.load());

        // Injection du service métier dans le contrôleur JavaFX
        ParcellesListeController controller = loader.getController();
        controller.initialiserAvecService(service, stage);

        stage.setTitle("Gestion des parcelles - Ferme St-Bert-Beach");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Précharge quelques parcelles pour illustrer immédiatement les cas d'utilisation.
     * Ces données peuvent être remplacées par un import ou une base de données sans toucher
     * à la couche interface.
     */
    private void chargerJeuDonneesDemonstration() {
        service.ajouterOuModifierParcelle(new Parcelle(
                "Prairie Est", 3.2, "Limite Est", "Foin",
                "Drainage à vérifier après les pluies d'automne"));
        service.ajouterOuModifierParcelle(new Parcelle(
                "Champ Nord", 4.5, "Route départementale", "Maïs",
                "Rotation blé/maïs sur les 3 dernières années"));
        service.ajouterOuModifierParcelle(new Parcelle(
                "Champ Sud", 5.8, "Proche rivière", "Soja",
                "Historique de rendement variable selon météo"));
    }
}
