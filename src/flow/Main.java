package flow;

import util.Log;
import graph.Graph;
import graph.Vertex;

/**
 * The Class Main.
 */
public class Main {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		int n = args.length;
		if (1 != n) {
			Log.p("Usage: java -jar vmaxflow.jar <filename>");
			return;
		}
		
	}
	
}
