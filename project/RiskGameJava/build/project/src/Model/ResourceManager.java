package Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * A ResourceManager feladata a szerializált adat beolvasása és kiírása
 */
public class ResourceManager {

	/*
	 * Mentes metódus, univerzális
	 * 
	 * @param data,fileName
	 */
	public static void save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
			oos.writeObject(data);
		}
	}

	/*
	 * Beolvasás metódus, univerzális
	 * 
	 * @param fileName
	 */
	public static Object load(String fileName) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
			return ois.readObject();
		}
	}
}