package example;

import java.util.HashMap;

import model.tool.maxflow.Arc;
import model.tool.maxflow.Graph;
import model.tool.maxflow.PushRelabelAlgo;
import model.tool.maxflow.Vertex;
import util.Log;

/**
 * The Class SimpleExample.
 */
public class SimpleExample {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Log.turnOffPrintLog();
		
		// create new empty graph
		Graph g = new Graph("SimpleExample");
		
		// add vertices
		Vertex s = new Vertex("s");
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		Vertex t = new Vertex("t");
		g.addVertex(s);
		g.addVertex(a);
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(t);
		
		// add arcs
		g.addArc(s, a, 15);
		g.addArc(s, c, 4);
		g.addArc(a, b, 12);
		g.addArc(b, c, 3);
		g.addArc(c, d, 10);
		g.addArc(d, a, 5);
		g.addArc(b, t, 7);
		g.addArc(d, t, 10);
		
		// run algo
		PushRelabelAlgo algo = new PushRelabelAlgo(g, s, t);
		HashMap<Arc, Double> flow = algo.computeMaxFlow();
		
		// print graph and flow
		Log.turnOnPrintLog();
		Log.p(g.toString());
		
		Log.ps("\nMax flow = " + algo.getMaxFlowValue());
		for (Arc arc : flow.keySet()) {
			Log.ps("f[%s] = %.2f", arc.getName(), flow.get(arc));
		}
		
		Log.p("\nMin Cut:");
		for (Arc arc : algo.getMinCut()) {
			Log.p(arc.toString());
		}
	}
	
}
