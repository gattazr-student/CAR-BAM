/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

/**
 * Définition d'un service pouvant intégrer un serveur. Le constructeur d'un
 * service devra avoir la signature suivante : <bold>public
 * XXXX(Object...)</bold>
 * 
 * @author Morat
 * @param <T>
 *            le type d'information délivrée par le service
 */
public interface _Service<T> {
	/**
	 * Appel générique du service.
	 * 
	 * @param params
	 *            les paramètres du services
	 * @return la valeur retournée par le service.
	 * @throws IllegalArgumentException
	 */
	public T call(Object... params) throws IllegalArgumentException;
}
