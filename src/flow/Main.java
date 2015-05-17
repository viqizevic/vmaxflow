package flow;

import java.util.HashMap;

import util.Log;
import graph.Arc;
import graph.Graph;
import graph.GraphReader;
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
		
		String fileName = args[0];
		Graph g = GraphReader.readFile(fileName);
		Vertex s = g.getVertex(GraphReader.getSourceName());
		Vertex t = g.getVertex(GraphReader.getSinkName());
		Log.p(g.toString());
		
		HashMap<Arc, Double> flow = PushRelabelAlgo.computeMaxFlow(g, s, t);
		for (Arc arc : flow.keySet()) {
//			Log.ps("f[%s] = %.2f", arc.getName(), flow.get(arc));
			
			Vertex u = arc.getStartVertex();
			Vertex v = arc.getEndVertex();
			Log.ps("%s, %s, %.0f, %.0f", u.getName(), v.getName(), flow.get(arc), arc.getCapacity());
		}
	}
	
}
