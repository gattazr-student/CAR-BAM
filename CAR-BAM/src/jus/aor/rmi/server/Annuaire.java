package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Annuaire;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	private static final long serialVersionUID = -8828635014943186320L;
	private HashMap<String, Numero> annuaire = new HashMap<String, Numero>();

	protected Annuaire(String pathFile) throws RemoteException, IOException,
			SAXException {
		// Init
		DocumentBuilder documentBuilder = null;
		Document document = null;
		String name = "";
		String num = "";
		NodeList telList = null;
		NamedNodeMap attributes = null;

		// Récup info depuis xml
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			document = documentBuilder.parse(new File(pathFile));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		telList = document.getElementsByTagName("telephone");

		for (int i = 0; i < telList.getLength(); i++) {
			attributes = telList.item(i).getAttributes();
			name = attributes.getNamedItem("name").getNodeName();
			num = attributes.getNamedItem("numero").getNodeValue();
			this.annuaire.put(name, new Numero(num));
		}

	}

	@Override
	public Numero get(String abonne) {
		return this.annuaire.get(abonne);
	}

}
