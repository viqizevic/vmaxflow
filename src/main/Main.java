package main;

import java.util.HashMap;

import model.tool.maxflow.Arc;
import model.tool.maxflow.Graph;
import model.tool.maxflow.GraphUtil;
import model.tool.maxflow.PushRelabelAlgo;
import model.tool.maxflow.Vertex;
import util.GraphReader;
import util.Log;

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
		
		PushRelabelAlgo algo = new PushRelabelAlgo(g, s, t);
		HashMap<Arc, Double> flow = algo.computeMaxFlow();
		Log.ps("\nMax flow = " + algo.getMaxFlowValue());
		
		Log.p("");
		GraphUtil.writeOutputFile(output, g, flow);
	}
	
}
