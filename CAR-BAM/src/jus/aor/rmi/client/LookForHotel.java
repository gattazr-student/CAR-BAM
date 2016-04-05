package jus.aor.rmi.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Annuaire;
import jus.aor.rmi.common._Chaine;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les
 * numéros de téléphone des hôtels répondant à son critère de choix.
 *
 * @author Morat
 */
public class LookForHotel {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Arguments expected : localisation");
			System.exit(1);
		}
		LookForHotel look = new LookForHotel(args[0]);

		long duration = look.call();
		System.out.println("Request proceding time: " + duration + " ms");
	}

	/** le critère de localisaton choisi */
	private String localisation;
	private int port = 1099;
	private int nbChaines = 4;
	private _Annuaire annuaire;
	private List<_Chaine> chainList = new ArrayList<_Chaine>();
	private List<Hotel> hotelList = new ArrayList<Hotel>();

	private HashMap<String, Numero> numList = new HashMap<String, Numero>();

	/**
	 * Définition de l'objet représentant l'interrogation.
	 *
	 * @param args
	 *            les arguments n'en comportant qu'un seul qui indique le
	 *            critère de localisation
	 */
	public LookForHotel(String local) {
		this.localisation = local;

		// Sécurity manager : charge certaines classes dynamiquement
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		Registry reg;

		// On récup d'abord toutes les chaines
		try {
			for (int i = 1; i <= this.nbChaines; i++) {
				reg = LocateRegistry.getRegistry(this.port + i);
				this.chainList.add((_Chaine) reg.lookup("chain" + i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Puis on récup l'annuaire
		try {
			reg = LocateRegistry.getRegistry(this.port + (this.nbChaines + 1));
			this.annuaire = (_Annuaire) reg.lookup("annuaire");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * réalise une intérrogation
	 *
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		long time = System.currentTimeMillis();
		List<Hotel> tmpList = new ArrayList<Hotel>();

		try {
			// interrogation successive des différents serveurs de chaines
			// d’hôtels pour obtenir l’ensemble des hôtels se trouvant dans la
			// localisation demandée
			for (int i = 0; i < this.chainList.size(); i++) {
				tmpList = this.chainList.get(i).get(this.localisation);
				this.hotelList.addAll(tmpList);
			}
			System.out.println("Le système à trouver " + this.hotelList.size()
					+ " hôtels à " + this.localisation);

			for (Hotel hotel : this.hotelList) {
				this.numList.put(hotel.name, this.annuaire.get(hotel.name));
			}

			System.out.println("Le système à trouver " + this.numList.size()
					+ " numéros de tel d'hôtels à " + this.localisation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (System.currentTimeMillis() - time);
	}

}
