package flow;

import util.Log;
import graph.Graph;
import graph.Vertex;

public class Main {
	
	public static void main(String[] args) {
		
		int n = 5;
		Graph g = createTestGraph(n);
		Log.p(g.toString());
		
		MaxFlow.computeFlow(g, g.getVertex(1+""), g.getVertex(n+""));
		
	}
	
	public static Graph createTestGraph(int n) {
		Graph g = new Graph("Test");
		for (int i=1; i <= n; i++) {
			g.addVertex(new Vertex(i+""));
			if (i > 1) {
				g.addDoubleArcs(g.getVertex((i-1)+""), g.getVertex(i+""), 1);
			}
		}
		return g;
	}

}
