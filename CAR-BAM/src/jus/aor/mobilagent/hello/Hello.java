package jus.aor.mobilagent.hello;

import java.util.logging.Level;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Starter;
import jus.aor.mobilagent.kernel._Action;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 *
 * @author Morat
 */
public class Hello extends Agent {

	private static final long serialVersionUID = 3882387847447156013L;
	/**
	 * Action à entreprendre sur les serveurs visités
	 */
	protected _Action doIt = new _Action() {

		private static final long serialVersionUID = -9129644307555501553L;

		@Override
		public void execute() {
			Starter.getLogger().log(Level.INFO, "Executing action Hello");
		}

		@Override
		public String toString() {
			return new String("HELLO");
		}
	};

	/**
	 * construction d'un agent de type hello.
	 *
	 * @param args
	 *            aucun argument n'est requis
	 */
	public Hello(Object... args) {
	}

	protected _Action retour() {
		return new _Action() {

			private static final long serialVersionUID = 8112403583439231794L;

			@Override
			public void execute() {
				Starter.getLogger().log(Level.INFO, "Executing action Retour");
			}

			@Override
			public String toString() {
				return new String("BACK");
			}
		};
	}
}
