package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader {

	public BAMServerClassLoader(URL[] urls, ClassLoader c) {
		super(urls);
	}

	@Override
	protected void addURL(URL url) {
		super.addURL(url);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
