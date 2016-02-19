/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.net.InetAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et
 * les fonctions permettant de déployer des services et des agents.
 *
 * @author Morat
 */
public final class Server {

	/** le nom logique du serveur */
	protected String name;
	/**
	 * le port où sera ataché le service du bus à agents mobiles. Pafr défaut on
	 * prendra le port 10140
	 */
	protected int port = 10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger = null;

	/**
	 * Démarre un serveur de type mobilagent
	 *
	 * @param port
	 *            le port d'écuote du serveur d'agent
	 * @param name
	 *            le nom du serveur
	 */
	public Server(final int port, final String name) {
		this.name = name;
		try {
			this.port = port;
			/* mise en place du logger pour tracer l'application */
			this.loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" + this.name;
			this.logger = Logger.getLogger(this.loggerName);
			/* démarrage du server d'agents mobiles attaché à cette machine */
			// A COMPLETER
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		} catch (Exception ex) {
			this.logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

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
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			// TODO: A COMPLETER
		} catch (Exception ex) {
			this.logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 *
	 * @param classeName
	 *            classe du service
	 * @param args
	 *            arguments de construction de l'agent
	 * @param codeBase
	 *            codebase du service
	 * @param etapeAddress
	 *            la liste des adresse des étapes
	 * @param etapeAction
	 *            la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress,
			List<String> etapeAction) {
		try {
			// TODO: A COMPLETER en terme de startAgent
		} catch (Exception ex) {
			this.logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son
	 * exécution immédiate.
	 *
	 * @param agent
	 *            l'agent devant être exécuté
	 * @param loader
	 *            le loader à utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		// TODO: A COMPLETER
	}
}
