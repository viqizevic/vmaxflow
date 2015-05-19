package flow;

import java.util.HashMap;

import util.Log;
import graph.Arc;
import graph.Graph;
import graph.GraphReader;
import graph.GraphUtil;
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
		if (0 == n || n > 2) {
			Log.p("Usage: java -jar vmaxflow.jar <filename> <outputfilename>");
			return;
		}
		
		String fileName = args[0];
		String output = "maxflow.txt";
		if (2 == n) {
			output = args[1];
		}
		
		GraphReader gr = new GraphReader();
		gr.readFile(fileName);
		Graph g = gr.getGraph();
		Vertex s = gr.getSource();
		Vertex t = gr.getSink();
		Log.p(g.toString());
		
		HashMap<Arc, Double> flow = PushRelabelAlgo.computeMaxFlow(g, s, t);
		
		GraphUtil.writeOutputFile(output, g, flow);
	}
	
}
