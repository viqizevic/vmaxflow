package graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import util.Log;

/**
 * The Class GraphReader.
 */
public class GraphReader {
	
	private static String sourceName_;
	private static String sinkName_;
	
	/**
	 * Read file.
	 *
	 * @param fileName the file name
	 * @return the graph
	 */
	public static Graph readFile(String fileName) {
		Log.p("Read file " + fileName);
		Graph g = new Graph(fileName);
		sourceName_ = null;
		sinkName_ = null;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) { // comment line
					continue;
				}
				String[] s = line.split(",");
				if (s.length != 3) {
					Log.e("Unexpected line: " + line + "\nThree elements separated by two commas were expected.");
					continue;
				}
				
				// retrieve capacity
				double cap = Double.parseDouble(s[2]);
				if (cap <= 0) {
					Log.e("Unexpected non-positive capacity found in line: " + line + "\nArc won\'t be added.");
					continue;
				}
				
				// add vertices if needed
				String u = s[0].trim();
				if (!g.vertexExists(u)) {
					g.addVertex(new Vertex(u));
					if (null == sourceName_) {
						sourceName_ = u; // update source name
					}
				}
				String v = s[1].trim();
				if (!g.vertexExists(v)) {
					g.addVertex(new Vertex(v));
				}
				sinkName_ = v; // update sink name
				
				// add new arc
				g.addArc(g.getVertex(u), g.getVertex(v), cap);
			}
			br.close();
		} catch (Exception e) {
			Log.e("Cannot read file " + fileName, e);
			return null;
		}
		
		return g;
	}
	
	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public static String getSourceName() {
		return sourceName_;
	}
	
	/**
	 * Gets the sink name.
	 *
	 * @return the sink name
	 */
	public static String getSinkName() {
		return sinkName_;
	}

}
