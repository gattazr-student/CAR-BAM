package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.logging.Level;

public class Agent implements _Agent {

	private static final long serialVersionUID = -1463619481727183159L;

	protected Route pRoute;
	private transient String pServerName;
	protected transient AgentServer pAgentServer;

	@Override
	public void addEtape(Etape etape) {
		this.pRoute.add(etape);
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		this.pAgentServer = agentServer;
		this.pServerName = serverName;
		this.pRoute = new Route(new Etape(this.pAgentServer.site(), _Action.NIHIL));

		// The first Etape to do by an Agent is NIHL
		this.pRoute.add(new Etape(this.pAgentServer.site(), _Action.NIHIL));
		// Starter.getLogger().log(Level.INFO, String.format("Agent initialized
		// %s", this));
	}

	private void move() {
		this.move(this.pRoute.get().server);
	}

	protected void move(URI destination) {
		Starter.getLogger().log(Level.FINE,
				String.format("Agent %s moving to %s:%d ", this, destination.getHost(), destination.getPort()));

		try {
			// Creation of a socket to destination
			Socket wDestSocket = new Socket(destination.getHost(), destination.getPort());

			// Creation of a Stream and a ObjectOutputStream to destination
			OutputStream wOutputStream = wDestSocket.getOutputStream();
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

			// Close the socket
			wObjectOutputStream2.close();
			wObjectOutputStream.close();
			wDestSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void reInit(AgentServer agentServer, String serverName) {
		this.pAgentServer = agentServer;
		this.pServerName = serverName;
	}

	@Override
	public void run() {
		Starter.getLogger().log(Level.INFO, String.format("Agent %s starting execution", this));

		// If there is something to do
		if (this.pRoute.hasNext()) {
			Etape wNextStep = this.pRoute.next();
			Starter.getLogger().log(Level.FINE, String.format("Agent %s running step %s", this, wNextStep));
			wNextStep.action.execute();

			if (this.pRoute.hasNext()) {
				// Send Agent to next AgentServer
				this.move();
			} else {
				// There is nothing left to do, Agent has finished
				Starter.getLogger().log(Level.FINE, String.format("Agent %s has finished its route", this));
			}
		} else {
			// If this is reached, it means that the Agent had nothing to do. So
			// just log it ended
			Starter.getLogger().log(Level.INFO, String.format("Agent %s had already finished", this));
		}
	}

	@Override
	public String toString() {
		return String.format("[Agent: route=%s, retour=%s, location=%s]", this.pRoute, this.pRoute, this.pServerName);
	}

}
