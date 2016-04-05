package jus.aor.mobilagent.lookforhotel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel._Service;
import jus.aor.rmi.common.Hotel;

public class Chaine implements _Service<Collection<Hotel>> {

	private Collection<Hotel> hotels = new LinkedList<Hotel>();

	public Chaine(Object... args) {
		String pathFile = (String) args[0];
		/*
		 * récupération des hôtels de la chaîne dans le fichier xml passé en 1er
		 * argument
		 */
		DocumentBuilder docBuilder = null;
		Document doc = null;
		String name, localisation;
		NodeList hlist = null;
		NamedNodeMap attrs;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File(pathFile));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		hlist = doc.getElementsByTagName("Hotel");

		/* acquisition de toutes les entrées de la base d'hôtels */
		for (int i = 0; i < hlist.getLength(); i++) {
			attrs = hlist.item(i).getAttributes();
			name = attrs.getNamedItem("name").getNodeValue();
			localisation = attrs.getNamedItem("localisation").getNodeValue();
			this.hotels.add(new Hotel(name, localisation));
		}
	}

	@Override
	public Collection<Hotel> call(Object... params) throws IllegalArgumentException {
		if (params.length != 1)
			throw new IllegalArgumentException();

		String localisation = (String) params[0];
		Collection<Hotel> hotelsInLocalisation = new ArrayList<Hotel>();
		for (Hotel h : this.hotels)
			if (h.localisation.equals(localisation))
				hotelsInLocalisation.add(h);

		return hotelsInLocalisation;
	}
}
