package vmaxflow;

import static org.junit.Assert.*;

import java.util.HashMap;

import model.tool.maxflow.Arc;
import model.tool.maxflow.Graph;
import model.tool.maxflow.GraphUtil;
import model.tool.maxflow.PushRelabelAlgo;
import model.tool.maxflow.Vertex;

import org.junit.Before;
import org.junit.Test;

import util.Log;
import util.TestGraph;

public class TestPushRelabelAlgo {
	
	@Before
	public void before() {
		Log.turnOffPrintLog();
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
		
		PushRelabelAlgo algo = new PushRelabelAlgo(g, s, t);
		HashMap<Arc, Double> f = algo.computeMaxFlow();
		// Correct flow
		assertNotNull(f);
		assertFalse(f.isEmpty());
		for (Arc a : g.getAllArcs()) {
			assertNotNull(f.get(a));
			assertEquals(1.0, (double) f.get(a), 0.001);
		}
		
		// Correct min cut value
		assertEquals(algo.getMaxFlowValue(), GraphUtil.totalFlow(algo.getMinCut(), f), 0.001);
	}
	
	@Test
	public void testComputedMaxFlowOfComplexNetwork2() {
		String source = "s";
		String sink = "t";
		int k = 2;
		Graph g = TestGraph.createComplexNetwork2(source, sink, k);
		Vertex s = g.getVertex(source);
		Vertex t = g.getVertex(sink);
		assertEquals(1, s.getOutgoingArcs().size());
		assertEquals(1, t.getIngoingArcs().size());
		
		PushRelabelAlgo algo = new PushRelabelAlgo(g, s, t);
		HashMap<Arc, Double> f = algo.computeMaxFlow();
		
		Arc su = s.getOutgoingArcs().iterator().next();
		Arc vt = t.getIngoingArcs().iterator().next();
		assertEquals(f.get(su), f.get(vt));
		assertEquals(k+1, (Double) f.get(su), 0.001);
		
		assertEquals(algo.getMaxFlowValue(), GraphUtil.totalFlow(algo.getMinCut(), f), 0.001);
	}

}
