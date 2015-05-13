package flow;

import util.Log;
import graph.Network;
import graph.Vertex;

public class Main {
	
	public static void main(String[] args) {
		
		int n = 5;
		Network g = createTestNetwork(n);
		Log.p(g.toString());
		
		MaxFlow.computeFlow(g, g.getVertex(1+""), g.getVertex(n+""));
		
	}
	
	public static Network createTestNetwork(int n) {
		Network g = new Network("Test");
		for (int i=1; i <= n; i++) {
			g.addVertex(new Vertex(i+""));
			if (i > 1) {
				g.addDoubleArcs(g.getVertex((i-1)+""), g.getVertex(i+""), 1);
			}
		}
		return g;
	}

}
