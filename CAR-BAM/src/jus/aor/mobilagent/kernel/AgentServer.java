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
import java.util.logging.Level;

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
	 * Retrieve _Agent from socket
	 *
	 * @param aSocket
	 * @return _Agent
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private _Agent getAgent(Socket aSocket) throws IOException, ClassNotFoundException {
		// Creation of an InputStream ObjectInputStream to retrieve Jar
		InputStream wInputStream = aSocket.getInputStream();
		ObjectInputStream wObjectInputStream = new ObjectInputStream(wInputStream);

		// Retrieve the Jar and create a BAMAgentClassLoader integrating the Jar
		Jar wJar = (Jar) wObjectInputStream.readObject();
		BAMAgentClassLoader wClassLoader = new BAMAgentClassLoader(this.getClass().getClassLoader());
		wClassLoader.integrateCode(wJar);

		// Retrieve the _Agent using an AgentInputStream and the
		// BAMAgentClassLoader
		AgentInputStream wAgentInputStream = new AgentInputStream(wInputStream, wClassLoader);

		_Agent wAgent = (_Agent) wAgentInputStream.readObject();

		wAgentInputStream.close();
		wObjectInputStream.close();
		wInputStream.close();

		return wAgent;
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
		boolean alive = true;
		try {
			// Try to create a socket Server
			ServerSocket wSocketServer = new ServerSocket(this.pPort);

			Starter.getLogger().log(Level.INFO, String.format("%s: started", this));
			while (alive) {
				Starter.getLogger().log(Level.INFO, String.format("%s: about to accept", this));
				// Accept on incoming Agents
				Socket wSocketClient = wSocketServer.accept();
				Starter.getLogger().log(Level.INFO, String.format("%s: accepted incoming agent", this));

				// load the repository and the agent
				_Agent wAgent = this.getAgent(wSocketClient);
				wAgent.reInit(this, this.pName);

				new Thread(wAgent).start();

				wSocketClient.close();
			}
			wSocketServer.close();
		} catch (IOException aException) {
			Starter.getLogger().log(Level.INFO, String.format("%s: An IO exception occured", this), aException);
		} catch (ClassNotFoundException aException) {
			Starter.getLogger().log(Level.INFO, String.format("%s: A class was not found", this), aException);
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
