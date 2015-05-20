package graph;

import util.Log;

public class TestGraph {
	
	public static Graph createComplexNetwork2(String sourceName, String sinkName, int size) {
		int k = size;
		String n = "N2k" + k;
		Graph g = new Graph(n);
		
		Vertex[] nodes = new Vertex[2*k+2];
		nodes[0] = new Vertex(sourceName);
		for (int i=1; i <= 2*k; i++) {
			nodes[i] = new Vertex(i+"");
		}
		nodes[2*k+1] = new Vertex(sinkName);
		for (Vertex v : nodes) {
			g.addVertex(v);
		}
		
		for (int i=0; i <= k; i++) {
			g.addArc(nodes[i], nodes[i+1], k+2-i);
		}
		for (int i=k+1; i <= 2*k; i++) {
			g.addArc(nodes[i], nodes[i+1], i-k+2);
		}
		for (int i=1; i <= k-1; i++) {
			g.addArc(nodes[i], nodes[2*k+1-i], 1.0);
		}
		
		Log.p(g.toString());
		
		return g;
	}

}
