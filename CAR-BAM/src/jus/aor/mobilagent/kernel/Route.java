/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Définit la feuille de route que l'agent va suivre
 *
 * @author Morat
 */
class Route implements Iterable<Etape>, Serializable {

	private static final long serialVersionUID = 9081294824590167316L;
	/** la liste des étapes à parcourir autres que la dernière */
	protected List<Etape> route;

	/**
	 * la dernière étape de la feuille de route de l'agent qui désigne le
	 * serveur de départ.
	 */
	protected Etape retour;
	/** Indique si la feuille de route est épuisée ou non. */
	protected boolean hasNext;

	/**
	 * Construction d'une route.
	 *
	 * @param retour
	 *            le server initial et de retour.
	 */
	public Route(Etape retour) {
		this.route = new LinkedList<Etape>();
		this.retour = retour;
		this.hasNext = true;
	}

	/**
	 * Ajoute une étape en fin de route.
	 *
	 * @param e
	 *            l'étape à ajouter
	 */
	public void add(Etape e) {
		this.route.add(this.route.size(), e);
	}

	/**
	 * Restitue la prochaine étape ou la dernière qui est la base de départ.
	 *
	 * @return la prochaine étape.
	 */
	Etape get() throws NoSuchElementException {
		// TODO: A COMPLETER
		return null;
	}

	/**
	 * Il y a-t-il encore une étape à parcourir.
	 *
	 * @return vrai si une étape est possible.
	 */
	public boolean hasNext() {
		return this.hasNext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Etape> iterator() {
		return this.route.iterator();
	}

	/**
	 * Restitue la prochaine étape et élimine de la route ou la dernière qui est
	 * la base de départ.
	 *
	 * @return la prochaine étape.
	 */
	Etape next() throws NoSuchElementException {
		// TODO: A COMPLETER
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.route.toString().replaceAll(", ", "->");
	}
}
