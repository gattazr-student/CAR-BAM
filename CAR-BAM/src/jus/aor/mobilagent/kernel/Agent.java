package jus.aor.mobilagent.kernel;

public class Agent implements _Agent, _Service<_Agent> {

	private static final long serialVersionUID = -1463619481727183159L;

	protected Route route;

	public Agent() {
		// TODO create constructor
	}

	@Override
	public void addEtape(Etape etape) {
		this.route.add(etape);
	}

	@Override
	public _Agent call(Object... params) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
