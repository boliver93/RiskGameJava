package Model;


/**
 * Az adott terulet adatait adja meg.
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class Territory {

	private int owner;
	private int units;



	public void finalize() throws Throwable {

	}

	public Territory(){

	}

	public int getOwner(){
		return owner;
	}

	public int getUnits(){
		return units;
	}

	/**
	 * 
	 * @param newVal    newVal
	 */
	public void setOwner(int newVal){
		owner = newVal;
	}

	/**
	 * 
	 * @param newVal    newVal
	 */
	public void setUnits(int newVal){
		units = newVal;
	}

}