package Model;

public enum Phase {
	Reinforcement,
	Battle,
	WaitForUnitCount, // Terulet elfoglalasa utan meg kell varni az egysegek szamat, amiket az ujonnan elfoglalt teruletre akarunk mozgatni.
	Transfer,
	Preparation,
	PlayerRegistration
}
