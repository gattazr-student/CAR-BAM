/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.util.List;

/**
 * @author morat
 */
public interface _Server {
	/**
	 * Ajoute le service caractérisé par les arguments
	 * 
	 * @param name
	 *            nom du service
	 * @param classeName
	 *            classe du service
	 * @param codeBase
	 *            codebase du service
	 * @param args
	 *            arguments de construction du service
	 */
	public abstract void addService(String name, String classeName, String codeBase, Object... args);

	/**
	 * déploie l'agent caractérisé par les arguments sur le serveur
	 * 
	 * @param classeName
	 *            classe de l'agent
	 * @param args
	 *            arguments de construction de l'agent
	 * @param codeBase
	 *            codebase de l'agent
	 * @param etapeAddress
	 *            la liste des adresse des étapes
	 * @param etapeAction
	 *            la liste ds actions des étapes
	 */
	public abstract void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress,
			List<String> etapeAction);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
}