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

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 3882387847447156013L;

	/**
	 * l'action à entreprendre sur les serveurs visités
	 */
	protected _Action doIt = new _Action() {
		/**
		 * Serial version UID
		 */
		private static final long serialVersionUID = -9129644307555501553L;

		@Override
		public void execute() {
			// TODO: Check how to get logger from Starter
			System.out.println("Hello Executing");
			Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Hello Executing");
		}
	};

	/**
	 * construction d'un agent de type hello.
	 *
	 * @param args
	 *            aucun argument n'est requis
	 */
	public Hello(Object... args) {
		System.err.println("EXITING");
		System.exit(0);
		// On ne fait rien à la construction de l'Agent
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jus.aor.mobilagent.kernel.Agent#retour() TODO: Check why it there
	 * was @Override
	 */
	protected _Action retour() {
		return this.doIt;
	}
}
