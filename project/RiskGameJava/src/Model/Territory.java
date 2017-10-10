package Model;

import java.io.Serializable;

/**
 * Az adott terulet adatait adja meg.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class Territory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int owner;
	private int units;
	private int id;

	@Override
	public void finalize() throws Throwable {

	}

	public Territory(int id) {
		this.id = id;
		owner = -1;
		units = -1;
	}

	public int getId() {
		return id;
	}

	public int getOwner() {
		return owner;
	}

	public int getUnits() {
		return units;
	}

	/**
	 * 
	 * @param newVal
	 *            newVal
	 */
	public void setOwner(int newVal) {
		owner = newVal;
	}

	/**
	 * 
	 * @param newVal
	 *            newVal
	 */
	public void setUnits(int newVal) {
		units = newVal;
	}

}