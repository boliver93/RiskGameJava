package Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * A ResourceManager feladata a szerializ�lt adat beolvas�sa �s ki�r�sa
 */
public class ResourceManager {

	/*
	 * Mentes met�dus, univerz�lis
	 * 
	 * @param data,fileName
	 */
	public static void save(Serializable data, String fileName) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
			oos.writeObject(data);
		}
	}

	/*
	 * Beolvas�s met�dus, univerz�lis
	 * 
	 * @param fileName
	 */
	public static Object load(String fileName) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
			return ois.readObject();
		}
	}
}