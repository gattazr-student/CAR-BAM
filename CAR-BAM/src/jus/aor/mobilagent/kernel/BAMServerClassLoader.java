package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader {
	/* TODO: Complete */

	public BAMServerClassLoader(URL[] urls, ClassLoader c) {
		super(urls);
		// TODO: Complete
	}

	@Override
	protected void addURL(URL url) {
		super.addURL(url);
		// TODO: Complete
	}

	@Override
	public String toString() {
		return super.toString();
		// TODO: Complete
	}

}
