package flow;

import java.util.HashMap;

import util.Log;
import graph.Arc;
import graph.Network;
import graph.Vertex;

public class MaxFlow {
	
	private static Network residual_;
	
	private static HashMap<Arc, Double> preflow_;
	
	private static HashMap<Vertex, Integer> heights_;
	
	public static void computeFlow(Network network, Vertex s, Vertex t) {
		
		initialize(network, s);
	}
	
	private static void initialize(Network network, Vertex s) {
		/*
		 starts by creating a residual graph,
		 initializing the preflow values to zero
		 and performing a set of saturating push operations
		 on residual edges exiting the source, (s, v) where v in V \ {s}.
		 Similarly, the label heights are initialized
		 such that the height at the source is in the number of vertices in the graph, h(s) = |V|,
		 and all other vertices are given a height of zero.
		 */
		residual_ = new Network("Residual");
		for (Vertex v : network.getAllVertices()) {
			Vertex w = new Vertex(v.getName());
			w.setId(v.getId());
			residual_.addVertex(w);
		}
		for (Arc a : network.getAllArcs()) {
			Arc b = new Arc(a.getName(), a.getStartVertex(), a.getEndVertex());
			b.setId(a.getId());
			b.setCapacity(a.getCapacity());
			residual_.addArc(b);
		}
		Log.p(residual_.toString());
		
		preflow_ = new HashMap<Arc, Double>();
		for (Arc a : network.getAllArcs()) {
			preflow_.put(a, 0.0);
		}
		
		heights_ = new HashMap<Vertex, Integer>();
		for (Vertex v : network.getAllVertices()) {
			int heightLevel = v.equals(s) ? network.getNumberOfVertices() : 0;
			heights_.put(v, heightLevel);
		}
		/*
		 Once the initialization is complete,
		 the algorithm repeatedly performs either the push or relabel operations against active vertices
		 until no applicable operation can be performed.
		 */
	}

}
