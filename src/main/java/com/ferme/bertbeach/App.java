package com.ferme.bertbeach;

import com.ferme.bertbeach.domaine.GestionParcellesService;
import com.ferme.bertbeach.domaine.Parcelle;
import com.ferme.bertbeach.infrastructure.memoire.ParcelleMemoireRepository;

import java.util.Optional;
import java.util.Scanner;

/**
 * Point d'entrée JavaFX déclaré dans Gradle. Ici nous n'utilisons que la partie console
 * pour illustrer les cas d'utilisation. Il serait possible de connecter cette couche
 * à une interface graphique par la suite sans changer le domaine.
 */
public class App {

    private final GestionParcellesService service;
    private final Scanner scanner = new Scanner(System.in);

    public App() {
        this.service = new GestionParcellesService(new ParcelleMemoireRepository());
        // Jeu de données minimal pour que le CU03 (consultation) dispose d'informations.
        service.ajouterOuModifierParcelle(new Parcelle(
                "Parcelle du Nord", 2.3, "Champ Nord", "Blé",
                "Rotation blé/maïs sur les 3 dernières années"));
    }

    public static void main(String[] args) {
        new App().bouclerMenu();
    }

    /**
     * Menu textuel simplifié : l'accent est mis sur la lisibilité du flot d'exécution
     * par rapport aux cas d'utilisation fournis dans le document.
     */
    private void bouclerMenu() {
        System.out.println("\n=== Gestion des parcelles (CU01, CU02, CU03) ===");
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n1) Ajouter ou modifier une parcelle (CU01)" +
                    "\n2) Supprimer une parcelle (CU02)" +
                    "\n3) Consulter une parcelle (CU03)" +
                    "\n4) Lister toutes les parcelles" +
                    "\n0) Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> executerCu01();
                case "2" -> executerCu02();
                case "3" -> executerCu03();
                case "4" -> afficherListeParcelles();
                case "0" -> continuer = false;
                default -> System.out.println("Choix non reconnu.");
            }
        }
        System.out.println("Au revoir !");
    }

    private void executerCu01() {
        System.out.println("\n--- CU01 : Ajouter ou modifier une parcelle ---");
        System.out.print("Identifiant (laisser vide pour création) : ");
        String id = scanner.nextLine().trim();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Superficie (hectares) : ");
        double superficie = Double.parseDouble(scanner.nextLine());
        System.out.print("Localisation : ");
        String localisation = scanner.nextLine();
        System.out.print("Culture active : ");
        String culture = scanner.nextLine();
        System.out.print("Historique cultural : ");
        String historique = scanner.nextLine();

        Parcelle parcelle;
        if (id.isBlank()) {
            // Création d'une nouvelle parcelle (déclencheur du CU01).
            parcelle = new Parcelle(nom, superficie, localisation, culture, historique);
        } else {
            // Modification : on récupère la parcelle existante ou on en crée une nouvelle avec l'id fourni.
            parcelle = service.consulterParcelle(id)
                    .map(p -> {
                        p.mettreAJour(nom, superficie, localisation, culture, historique);
                        return p;
                    })
                    .orElse(new Parcelle(id, nom, superficie, localisation, culture, historique, java.time.LocalDate.now()));
        }

        Parcelle sauvegardee = service.ajouterOuModifierParcelle(parcelle);
        System.out.println("Parcelle enregistrée : " + sauvegardee);
    }

    private void executerCu02() {
        System.out.println("\n--- CU02 : Supprimer une parcelle ---");
        System.out.print("Identifiant de la parcelle à supprimer : ");
        String id = scanner.nextLine();
        boolean supprimee = service.supprimerParcelle(id);
        if (supprimee) {
            System.out.println("Parcelle supprimée. (Postcondition CU02)");
        } else {
            System.out.println("Aucune parcelle trouvée avec cet identifiant.");
        }
    }

    private void executerCu03() {
        System.out.println("\n--- CU03 : Consulter les informations d’une parcelle ---");
        afficherListeParcelles();
        System.out.print("Identifiant à consulter : ");
        String id = scanner.nextLine();
        Optional<Parcelle> parcelle = service.consulterParcelle(id);
        if (parcelle.isPresent()) {
            System.out.println("Détails de la parcelle : " + parcelle.get());
            System.out.println("(Lecture seule, aucune modification n’est effectuée)");
        } else {
            System.out.println("Aucune parcelle disponible pour cet identifiant.");
        }
    }

    private void afficherListeParcelles() {
        System.out.println("\nParcelles enregistrées :");
        service.listerParcelles().forEach(p ->
                System.out.println("- " + p.getIdentifiant() + " | " + p.getNom() + " | " +
                        p.getSuperficieHectares() + " ha | " + p.getCultureActive()));
    }
}
