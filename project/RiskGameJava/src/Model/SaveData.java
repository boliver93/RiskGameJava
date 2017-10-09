package Model;

import java.util.List;

public class SaveData implements java.io.Serializable {

	private static final long serialVersionUID = 12345678L;

	public Phase phase;
	public Deck deck;
	public List<Player> playersList;
	public boolean capturedThisTurn;
	public Map map;
	public int currentPlayer;
	public Territory[] waitForUnitsTemp;
}