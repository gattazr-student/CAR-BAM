/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * Définit une action à exécuter par un agent.
 *
 * @author Morat
 */
public interface _Action extends Serializable {
	/** l'action vide */
	public static final _Action NIHIL = new _Action() {

		/**
		 *
		 */
		private static final long serialVersionUID = 9211608560914435550L;

		@Override
		public void execute() {
			Starter.getLogger().log(Level.INFO, "Executing action NIHIL");
		}

		@Override
		public String toString() {
			return new String("NIHIL");
		}
	};

	/**
	 * Exécute l'action
	 */
	public void execute();
}
