package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common._Chaine;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Chaine extends UnicastRemoteObject implements _Chaine {
	private List<Hotel> hotelList = new ArrayList<Hotel>();

	protected Chaine(String pathFile) throws ParserConfigurationException,
	SAXException, IOException {
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
			docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
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
			this.hotelList.add(new Hotel(name, localisation));
		}

	}

	@Override
	public List<Hotel> get(String localisation) {
		// TODO Auto-generated method stub
		List<Hotel> res = new ArrayList<Hotel>();

		for (Hotel hotel : this.hotelList) {
			if (localisation.equals(hotel.localisation)) {
				res.add(hotel);
			}
		}

		return res;
	}
}
