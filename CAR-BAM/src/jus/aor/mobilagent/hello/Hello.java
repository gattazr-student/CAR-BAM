package jus.aor.mobilagent.hello;

import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel.Agent;
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
			Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Action Hello");
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
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "I'm back");
			}
		};
	}
}
