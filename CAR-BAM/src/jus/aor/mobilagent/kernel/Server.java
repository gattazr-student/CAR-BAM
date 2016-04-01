/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et
 * les fonctions permettant de déployer des services et des agents.
 *
 * @author Morat
 */
public final class Server implements _Server {

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
			this.agentServer = new AgentServer(this.port, this.name);
			this.logger.log(Level.INFO, String.format("%s: Starting AgentServer", this));
			new Thread(this.agentServer).start();
			/* temporisation de mise en place du ServerAgents */
			Thread.sleep(1000);
		} catch (Exception ex) {
			this.logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
		this.logger.log(Level.FINE, String.format("%s: End of creation of Server", this));
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
	@Override
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			//
			BAMServerClassLoader wClassLoader = new BAMServerClassLoader(new URL[] {},
					this.getClass().getClassLoader());
			wClassLoader.addURL(new URL(codeBase));

			// Récupère la classe contenant le service
			Class<?> wClassService = Class.forName(classeName, true, wClassLoader);

			// Récupère le constructeur de cette classe
			Constructor<?> wConstructor = wClassService.getConstructor(Object[].class);
			// Instantie l'object
			_Service<?> wService = (_Service<?>) wConstructor.newInstance(new Object[] { args });

			// Ajoute le setvice sur l'AgentServer
			this.agentServer.addService(name, wService);

		} catch (Exception ex) {
			System.out.println("WOW");
			this.logger.log(Level.FINE, " erreur pendant l'ajout des services" + this, ex);
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
	@Override
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress,
			List<String> etapeAction) {
		_Agent wAgent = null;
		try {
			BAMAgentClassLoader wClassLoader = new BAMAgentClassLoader(new URI(codeBase).getPath(),
					this.getClass().getClassLoader());

			// Récupère la classe héritant _Agent
			Class<?> wClassAgent = Class.forName(classeName, true, wClassLoader);

			// Récupère le constructeur de cette classe
			Constructor<?> wConstructor = wClassAgent.getConstructor(Object[].class);
			// Instantie l'object
			// TODO: Fix problem here
			Object wLol = wConstructor.newInstance(new Object[] { args });
			wAgent = (_Agent) wLol;
			// Initialise l'Agent
			wAgent.init(this.agentServer, this.name);
			if (etapeAction.size() != etapeAction.size()) {
				this.logger.log(Level.INFO,
						" ERREUR : Problème de cohérence, le nombre d'action de d'adresse sont différents");
			} else {
				int wSize = etapeAction.size();
				for (int i = 0; i < wSize; i++) {
					// Ajoute une étape dans l'agent pour chaque étape dans les
					// listes
					// Récupère les champs de la classe
					Field wChamp = wClassAgent.getDeclaredField(etapeAction.get(i));
					// Assure que le champs soit accessible
					wChamp.setAccessible(true);
					// Récupère l'action
					_Action wAction = (_Action) wChamp.get(wAgent);
					// Ajout de l'étape
					wAgent.addEtape(new Etape(new URI(etapeAddress.get(i)), wAction));
				}
				// Démarre l'Agent
				this.startAgent(wAgent, wClassLoader);
				new Thread(wAgent).start();
			}

		} catch (Exception ex) {
			this.logger.log(Level.FINE,
					String.format("%s: erreur durant le déploiement de l'agent %s. %s", this, wAgent, ex.getMessage()),
					ex);
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

		URI wAgentServerSite = this.agentServer.site();

		Socket wSock = new Socket(wAgentServerSite.getHost(), wAgentServerSite.getPort());

		// Creation of a Stream and a ObjectOutputStream to destination
		OutputStream wOutputStream = wSock.getOutputStream();
		ObjectOutputStream wObjectOutputStream = new ObjectOutputStream(wOutputStream);
		// TODO: Find out why wObjectOutputStream2 is needed
		ObjectOutputStream wObjectOutputStream2 = new ObjectOutputStream(wOutputStream);

		// Retrieve byte code to send
		BAMAgentClassLoader wAgentClassLoader = (BAMAgentClassLoader) this.getClass().getClassLoader();
		Jar wBaseCode = wAgentClassLoader.extractCode();

		// Send Jar in BAMAgentClassLoader
		wObjectOutputStream.writeObject(wBaseCode);
		// Send Agent (this)
		wObjectOutputStream2.writeObject(this);

		// Close the sockets
		wObjectOutputStream2.close();
		wObjectOutputStream.close();
		wSock.close();

	}

	@Override
	public String toString() {
		return String.format("[Server: name=%s, port=%d]", this.name, this.port);
	}
}
