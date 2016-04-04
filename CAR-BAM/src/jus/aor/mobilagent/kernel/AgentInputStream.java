package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * ObjectInputStream spécifique au bus à agents mobiles. Il permet d'utiliser le
 * loader de l'agent.
 *
 * @author Morat
 */
class AgentInputStream extends ObjectInputStream {
	/**
	 * le classLoader à utiliser
	 */
	BAMAgentClassLoader loader;

	AgentInputStream(InputStream is, BAMAgentClassLoader cl) throws IOException {
		super(is);
		this.loader = cl;
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass cl) throws IOException, ClassNotFoundException {
		return this.loader.loadClass(cl.getName());
	}
}
