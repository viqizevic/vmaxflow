package vmaxflow;

import static org.junit.Assert.*;

import java.util.HashMap;

import flow.PushRelabelAlgo;
import graph.Arc;
import graph.Graph;
import graph.Vertex;

import org.junit.Before;
import org.junit.Test;

public class TestPushRelabelAlgo {
	
	@Before
	public void before() {
	}
	
	@Test
	public void testComputedMaxFlowOfValueOne() {
		int n = 10;
		Graph g = new Graph("Test");
		for (int i=1; i <= n; i++) {
			g.addVertex(new Vertex(i+""));
			if (i > 1) {
				g.addArc(g.getVertex((i-1)+""), g.getVertex(i+""), n+1-i);
			}
		}
		Vertex s = g.getVertex("1");
		Vertex t = g.getVertex(n+"");
		
		HashMap<Arc, Double> f = PushRelabelAlgo.computeMaxFlow(g, s, t);
		assertNotNull(f);
		assertFalse(f.isEmpty());
		for (Arc a : g.getAllArcs()) {
			assertNotNull(f.get(a));
			assertEquals(1.0, (double) f.get(a), 0.001);
		}
	}

}
