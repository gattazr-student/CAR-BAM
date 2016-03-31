package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AgentServer implements Runnable {

	/** Return name of AgentServer */
	private String pName;
	/** Port on which the AgentServer is running */
	private int pPort;
	/** List of services in the AgentService */
	private Map<String, _Service<?>> pServices;

	/**
	 *
	 * @param port
	 *            port on which the Agent is running
	 * @param name
	 *            name of the AgentServer
	 */
	protected AgentServer(int port, String name) {
		this.pPort = port;
		this.pName = name;
		this.pServices = new HashMap<String, _Service<?>>();
	}

	/**
	 * Add a service to the AgentServer
	 *
	 * @param s
	 *            name of the service
	 * @param service
	 *            service
	 */
	protected void addService(String s, _Service<?> service) {
		this.pServices.put(s, service);
	}

	/**
	 * Retrieve a service in the AgentServer
	 *
	 * @param s
	 *            name of the service
	 */
	protected _Service<?> getService(String s) {
		return this.pServices.get(s);
	}

	@Override
	public void run() {
		// TODO: Make function
		try {
			ServerSocket wSocketServer = new ServerSocket(this.pPort);

			while (true) {
				Socket wSocketClient = wSocketServer.accept();

				InputStream wClientInputStream = wSocketClient.getInputStream();
				ObjectInputStream wObjectInputStream = new ObjectInputStream(wClientInputStream);

				// - Reçois le jar Construction du BAMAgentClassLoader
				// - Utlise cet object pour charge les classes qu'il contient en
				// mémoire
				// - Reçois l'Agent (avec AgentInputStream crée avec
				// BamAgentClassLoader)
				// - Reinit l'agent
				// - Redémarrer (Thread.start())

			}
		} catch (IOException e) {
			// TODO: log
		}
	}

	/**
	 * Retourne l'adresse de l'AgentServer
	 *
	 * @return URI de l'AgentServer
	 */
	public URI site() {
		try {
			return new URI("//localhost:" + this.pPort);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("[AgentServer: name=%s, port=%d]", this.pName, this.pPort);
	}

}
