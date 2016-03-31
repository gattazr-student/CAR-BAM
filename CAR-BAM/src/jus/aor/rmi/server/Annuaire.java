package jus.aor.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Annuaire;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	private static final long serialVersionUID = -8828635014943186320L;

	protected Annuaire() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Numero get(String abonne) {
		// TODO Auto-generated method stub
		return null;
	}

}
