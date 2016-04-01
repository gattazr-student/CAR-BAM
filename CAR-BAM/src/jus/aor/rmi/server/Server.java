package jus.aor.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	public static void main(String[] args) {

		int port = 1099;
		int nbChaines = 4;

		// Récupération des arguments
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Entrez le numéro de port en argument.");
				System.exit(1);
			}
		}

		// Sécurity manager : charge certaines classes dynamiquement
		// if (System.getSecurityManager() == null) {
		// System.setSecurityManager(new SecurityManager());
		// }

		try {

			// Déclaration du registry
			Registry reg;

			// Enregistrement des chaines dans le registry
			for (int i = 0; i < nbChaines; i++) {
				reg = LocateRegistry.createRegistry(port + i);
				// Bind de chaine dans registry
				// TODO Changer chemin
				Chaine c = new Chaine("src/jus/aor/rmi/DataStore/Hotels"
						+ (i + 1) + ".xml");
				reg.bind("chain" + i, c);
				System.out.println("Chemin " + i + " bind avec succès.");
			}

			// Enregistrement des entrées de l'annuaire
			reg = LocateRegistry.createRegistry(port + nbChaines + 1);
			// TODO Changer chemin annuaire
			Annuaire a = new Annuaire("src/jus/aor/rmi/DataStore/Annuaire.xml");
			reg.bind("Annuaire", a);
			System.out.println("Annuaire créé avec succès.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
