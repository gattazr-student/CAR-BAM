package jus.aor.mobilagent.kernel;

import java.net.UnknownHostException;

/**
 * Description d'un agent du modèle de bus à agents mobiles "BAM". Le
 * constructeur d'un agent devra avoir la signature suivante : <bold>public
 * XXXX(Object...)</bold>
 * 
 * @author P.Morat
 */
public interface _Agent extends java.io.Serializable, Runnable {
	/**
	 * ajoute une étape en fin de la feuille de route de l'agent.
	 * 
	 * @param etape
	 *            l'étape à ajouter
	 */
	public void addEtape(Etape etape);

	/**
	 * Initialise l'agent lors de son déploiement initial dans le bus à agents
	 * mobiles.
	 * 
	 * @param agentServer
	 *            le serveur hébergeant initialement l'agent.
	 * @param serverName
	 *            le nom logique du serveur d'agent
	 */
	public void init(AgentServer agentServer, String serverName);

	/**
	 * Initialise l'agent lors de son déploiement sur un des serveurs du bus.
	 * 
	 * @param server
	 *            le server actuel pour cet agent
	 * @param serverName
	 *            le nom logique du serveur d'agent
	 * @throws UnknownHostException
	 */
	public void reInit(AgentServer server, String serverName);
}
