package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarOutputStream;

public class BAMAgentClassLoader extends ClassLoader {

	/** Classes loaded from Jar files while executing integrateCode() */
	private Map<String, byte[]> pClasses;

	/**
	 *
	 * @param aParent
	 *            Parent classloader
	 */
	public BAMAgentClassLoader(ClassLoader aParent) {
		super(aParent);
		this.pClasses = new HashMap<String, byte[]>();
	}

	/**
	 * @param aParent
	 *            Parent classloader
	 * @throws IOException
	 * @throws JarException
	 */
	public BAMAgentClassLoader(String aJarFilePath, ClassLoader aParent) throws JarException, IOException {
		this(aParent);
		Jar wJar = new Jar(aJarFilePath);
		this.integrateCode(wJar);
	}

	/**
	 * Return the name of a class based on the given file path
	 *
	 * @param aClassFilePath
	 *            path to a file containing a class
	 * @return String
	 */
	private String className(String aClassFilePath) {
		return aClassFilePath.replace(".class", "").replace("/", ".");
	}

	/**
	 * Return a Jar object containing all the classes found in the
	 * BamAgentClassLoader
	 *
	 * To do this, a temporary Jar file is created.
	 *
	 * @return Jar containing all classes in the current BamAgentClassLoader
	 * @throws JarException
	 * @throws IOException
	 */
	public Jar extractCode() throws JarException, IOException {
		File wTmpJar = File.createTempFile("bamrepository", ".jar");

		// Try to create an OutputStream and JarOutputStream on the temporary
		// Jar file just created
		try (JarOutputStream wJarOutputStream = new JarOutputStream(new FileOutputStream(wTmpJar))) {
			for (String wClassName : this.pClasses.keySet()) {
				// Create an entry and write the byteCode of the class
				// for every class in pClasses
				wJarOutputStream.putNextEntry(new JarEntry(wClassName));
				wJarOutputStream.write(this.pClasses.get(wClassName));
			}
			wJarOutputStream.close();
		}
		// Create the Jar object using the Jar file
		return new Jar(wTmpJar.getPath());
	}

	/**
	 * Load all the classes in the Jar aJar and into the ClassLoader
	 *
	 * @param aJar
	 *            Jar to integrate
	 */
	public void integrateCode(Jar aJar) {
		for (Entry<String, byte[]> wEntry : aJar) {
			String wClassName = this.className(wEntry.getKey());
			this.pClasses.put(wClassName, wEntry.getValue());

			// Define and resolve the class to be able to use it
			Class<?> wClass = this.defineClass(wClassName, wEntry.getValue(), 0, wEntry.getValue().length);
			super.resolveClass(wClass);
		}
	}

}
