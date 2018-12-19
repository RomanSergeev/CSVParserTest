import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParallelFileReader {
	public static String CSV_DELIMITER = ";";
	
	private Map<String, Set<String>> map;
	
	public ParallelFileReader() {
		this.map = new HashMap<>();
	}
	
	public void readFile(String filename) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String titleLine = br.readLine();
			if (titleLine == null) {
				br.close();
				return;
			}
			String line;
			String[] keys = titleLine.split(CSV_DELIMITER);
			synchronized(map) {
				for (String key : keys) {
					if (!map.containsKey(key)) {
						map.put(key, new HashSet<String>());
					}
				}
				while((line = br.readLine()) != null) {
					String[] values = line.split(CSV_DELIMITER);
					for (int j = 0; j < values.length; j++) {
						map.get(keys[j]).add(values[j]);
					}
				}
				br.close();
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("File " + filename + " was not found.");
		}
		catch(IOException ex) {
			System.out.println("Error occurred while reading file " + filename + ".");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void printResults() {
		for (String key : map.keySet()) {
			System.out.println(key + ":");
			for (String value : map.get(key)) {
				System.out.println(value);
			}
		}
	}
}
