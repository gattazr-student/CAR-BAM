/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Morat
 */
public class Starter {

	/** le logger pour ce code */
	protected static Logger logger;

	/**
	 * Retrieve the Logger
	 *
	 * @return Logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	private static Iterable<Node> iterable(final Node racine, final String element) {
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator() {
				return new Iterator<Node>() {
					NodeList nodelist;
					int current = 0, length;

					{ // init
						try {
							this.nodelist = ((Document) racine).getElementsByTagName(element);
						} catch (ClassCastException e) {
							this.nodelist = ((Element) racine).getElementsByTagName(element);
						}
						this.length = this.nodelist.getLength();
					}

					@Override
					public boolean hasNext() {
						return this.current < this.length;
					}

					@Override
					public Node next() {
						return this.nodelist.item(this.current++);
					}

					@Override
					public void remove() {
					}
				};
			}
		};
	}

	/**
	 * Application starter
	 *
	 * @param args
	 */
	public static void main(String... args) {
		// if (System.getSecurityManager() == null) {
		// System.setSecurityManager(new RMISecurityManager());
		// }
		new Starter(args);
	}

	/** le document xml en cours */
	protected Document doc;

	/** le server associé à ce starter */
	protected _Server server;
	/** le Loader utilisé */
	protected BAMServerClassLoader loader;

	/** la classe du server : jus.aor.mobilagent.kernel.Server */
	protected Class<Server> classe;

	/**
	 *
	 * @param args
	 */
	public Starter(String... args) {
		// récupération du niveau de log
		java.util.logging.Level level;
		try {
			level = Level.parse(System.getProperty("LEVEL"));
		} catch (NullPointerException e) {
			level = java.util.logging.Level.OFF;
		} catch (IllegalArgumentException e) {
			level = java.util.logging.Level.SEVERE;
		}
		try {
			/* Mise en place du logger pour tracer l'application */
			String loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" + args[1];
			logger = Logger.getLogger(loggerName);
			// logger.setUseParentHandlers(false);
			logger.addHandler(new IOHandler());
			logger.setLevel(level);
			/* Récupération d'informations de configuration */
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.doc = docBuilder.parse(new File(args[0]));
			int port = Integer.parseInt(
					this.doc.getElementsByTagName("port").item(0).getAttributes().getNamedItem("value").getNodeValue());
			// Création du serveur
			this.createServer(port, args[1]);
			// ajout des services
			this.addServices();
			// déploiement d'agents
			this.deployAgents();
		} catch (Exception ex) {
			logger.log(Level.FINE, "Ce programme nécessite un argument : <conf file> <name server>", ex);
			return;
		}
	}

	/**
	 * Ajoute un service
	 *
	 * @param name
	 *            le nom du service
	 * @param classeName
	 *            la classe du service
	 * @param codeBase
	 *            le code du service
	 * @param args
	 *            les arguments de la construction du service
	 */
	protected void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			this.server.addService(name, classeName, codeBase, args);
		} catch (Exception e) {
			logger.log(Level.FINE, " erreur durant l'ajout d'un service", e);
		}
	}

	/**
	 * Ajoute les services définis dans le fichier de configuration
	 */
	protected void addServices() {
		NamedNodeMap attrs;
		Object[] args;
		String codeBase, classeName, name;
		for (Node item : iterable(this.doc, "service")) {
			attrs = item.getAttributes();
			codeBase = attrs.getNamedItem("codebase").getNodeValue();
			classeName = attrs.getNamedItem("class").getNodeValue();
			args = attrs.getNamedItem("args").getNodeValue().split(" ");
			name = attrs.getNamedItem("name").getNodeValue();
			this.addService(name, classeName, codeBase, args);
		}
	}

	@SuppressWarnings("unchecked")
	protected void createServer(int port, String name)
			throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.loader = new BAMServerClassLoader(new URL[] { new URL("file://codebase/mobilagentServer.jar") },
				this.getClass().getClassLoader());
		this.classe = (Class<Server>) Class.forName("jus.aor.mobilagent.kernel.Server", true, this.loader);
		this.server = this.classe.getConstructor(int.class, String.class).newInstance(port, name);
		logger.log(Level.FINE, "Successfull creation of Server");
	}

	/**
	 * Déploie un agent
	 *
	 * @param classeName
	 *            la classe de l'agent
	 * @param args
	 *            les arguments de la construction de l'agent
	 * @param codeBase
	 *            le code de l'agent
	 * @param serverAddress
	 *            la liste des serveurs des étapes
	 * @param serverAction
	 *            la liste des actions des étapes
	 */
	protected void deployAgent(String classeName, Object[] args, String codeBase, List<String> serverAddress,
			List<String> serverAction) {
		try {
			this.server.deployAgent(classeName, args, codeBase, serverAddress, serverAction);
		} catch (Exception e) {
			logger.log(Level.FINE, " erreur durant le déploiement de l'agent", e);
		}
	}

	/**
	 * Déploiement les agents définis dans le fichier de configuration
	 */
	protected void deployAgents() {
		NamedNodeMap attrsAgent, attrsEtape;
		Object[] args = null;
		String codeBase;
		String classeName;
		List<String> serverAddress = new LinkedList<String>(), serverAction = new LinkedList<String>();
		for (Node item1 : iterable(this.doc, "agent")) {
			attrsAgent = item1.getAttributes();
			codeBase = attrsAgent.getNamedItem("codebase").getNodeValue();
			classeName = attrsAgent.getNamedItem("class").getNodeValue();
			args = attrsAgent.getNamedItem("args").getNodeValue().split(" ");
			for (Node item2 : iterable(item1, "etape")) {
				attrsEtape = item2.getAttributes();
				serverAction.add(attrsEtape.getNamedItem("action").getNodeValue());
				serverAddress.add(attrsEtape.getNamedItem("server").getNodeValue());
			}

			this.deployAgent(classeName, args, codeBase, serverAddress, serverAction);
		}
	}
}
