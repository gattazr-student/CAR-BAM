package jus.aor.rmi.common;

import java.io.Serializable;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Un numéro de téléphone
 */
public class Numero implements Serializable {
	/** le numéro de téléphone */
	public String numero;

	/**
	 * Construction d'un numéro de téléphone.
	 *
	 * @param numero
	 *            le numéro
	 */
	public Numero(String numero) {
		this.numero = numero;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.numero;
	}
}
