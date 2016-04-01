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
	 * Si la route à un élément suivant, retourne le prochaine élement. Si elle
	 * est finie, c'est l'adresse retour qui est retournée. Sinon, l'exception
	 * NoSuchElementException est levée
	 *
	 * @return la prochaine étape.
	 *
	 * @throws NoSuchElementException
	 */
	public Etape get() throws NoSuchElementException {
		if (this.hasNext) {
			if (this.route.size() > 0) {
				return this.route.get(0);
			} else {
				return this.retour;
			}
		} else {
			throw new NoSuchElementException();
		}
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
	public Etape next() throws NoSuchElementException {
		Etape wStep;
		if (this.hasNext) {
			if (this.route.size() > 0) {
				wStep = this.route.get(0);
				this.route.remove(0);
			} else {
				wStep = this.retour;
				this.hasNext = false;
			}
		} else {
			throw new NoSuchElementException();
		}

		return wStep;
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
