package flow;

import graph.Arc;
import graph.Network;
import graph.NetworkUtil;
import graph.Vertex;

import java.util.HashMap;

import util.Log;

public class MaxFlow {
	
	private static Network network_;
	
	private static Vertex source_;
	
	private static HashMap<Arc, Double> preflow_;
	
	private static HashMap<Vertex, Integer> heights_;
	
	public static void computeFlow(Network network, Vertex s, Vertex t) {
		network_ = network;
		source_ = s;
		
		initialize();
		
		printNetwork();
	}
	
	private static void initialize() {
		/*
		 starts by creating a residual graph,
		 initializing the preflow values to zero
		 and performing a set of saturating push operations
		 on residual edges exiting the source, (s, v) where v in V \ {s}.
		 Similarly, the label heights are initialized
		 such that the height at the source is in the number of vertices in the graph, h(s) = |V|,
		 and all other vertices are given a height of zero.
		 */
		preflow_ = new HashMap<Arc, Double>();
		for (Arc a : network_.getAllArcs()) {
			preflow_.put(a, 0.0);
		}
		heights_ = new HashMap<Vertex, Integer>();
		for (Vertex v : network_.getAllVertices()) {
			int heightLevel = v.equals(source_) ? 1 : 0;
			heights_.put(v, heightLevel);
		}
		for (Arc sv : source_.getOutgoingArcs()) {
			push(sv);
		}
		heights_.put(source_, network_.getNumberOfVertices());
		/*
		 Once the initialization is complete,
		 the algorithm repeatedly performs either the push or relabel operations against active vertices
		 until no applicable operation can be performed.
		 */
	}
	
	/**
	 * Push.
	 *
	 * @param uv the uv
	 */
	private static void push(Arc uv) {
		Vertex u = uv.getStartVertex();
		Vertex v = uv.getEndVertex();
		Arc vu = network_.getConnectingArc(v, u);
		/*
		The push operation applies on an admissible out-edge (u, v) of an active vertex u in Gf.
		It moves min ( e(u), c_f(u,v) ) units of flow from u to v.
		push(u, v):
		    assert e[u] > 0 and h[u] == h[v] + 1
		    Δ = min(e[u], c[u][v] - f[u][v])
		    f[u][v] += Δ
		    f[v][u] -= Δ
		    e[u] -= Δ
		    e[v] += Δ
		    */
		double eu = getExcess(u);
		if (eu <= 0) {
			Log.e("Non-positive excess of " + u + ". Unable to push!");
			return;
		}
		if (!arcIsAdmissible(uv)) {
			Log.e("Arc is not admissible, " + uv + ". Unable to push!");
			return;
		}
		double fuv = preflow_.get(uv);
		double residualCapacity = uv.getCapacity() - fuv;
		double delta = Math.min(eu, residualCapacity);
		preflow_.put(uv, fuv + delta);
		preflow_.put(vu, preflow_.get(vu) - delta);
	}
	
	/**
	 * Arc is admissible.
	 *
	 * @param arc the arc
	 * @return true, if successful
	 */
	private static boolean arcIsAdmissible(Arc arc) {
		return heights_.get(arc.getStartVertex()) == heights_.get(arc.getEndVertex()) + 1;
	}
	
	/**
	 * Gets the excess.
	 *
	 * @param v the v
	 * @return the double
	 */
	private static double getExcess(Vertex v) {
		// TODO: consider to use array to save the excess values
		if (v.equals(source_)) {
			return Double.POSITIVE_INFINITY;
		}
		double in = NetworkUtil.totalFlow(v.getIngoingArcs(), preflow_);
		double out = NetworkUtil.totalFlow(v.getOutgoingArcs(), preflow_);
		return in - out;
	}
	
	private static void printNetwork() {
		Log.p(network_.getName());
		for (Vertex v : network_.getAllVertices()) {
			Log.ps("vertex %s: h=%d - e=%.1f", v.getName(), heights_.get(v), getExcess(v));
		}
		for (Arc a : network_.getAllArcs()) {
			Log.ps("arc %s: f=%.1f - c=%.1f", a.getName(), preflow_.get(a), a.getCapacity());
		}
	}

}
