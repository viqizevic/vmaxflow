package flow;

import graph.Arc;
import graph.Network;
import graph.Vertex;

import java.util.Collection;
import java.util.HashMap;

import util.Log;

public class MaxFlow {
	
	private static Network network_;
	
	private static Vertex source_;
	
	private static Vertex sink_;
	
	private static HashMap<Arc, Double> preflow_;
	
	private static HashMap<Vertex, Double> excess_;
	
	private static HashMap<Vertex, Integer> heights_;
	
	public static void computeFlow(Network network, Vertex s, Vertex t) {
		network_ = network;
		source_ = s;
		sink_ = t;
		
		initialize();
		
		printNetwork();
	}
	
	private static void initialize() {
		preflow_ = new HashMap<Arc, Double>();
		for (Arc a : network_.getAllArcs()) {
			preflow_.put(a, 0.0);
		}
		excess_ = new HashMap<Vertex, Double>();
		heights_ = new HashMap<Vertex, Integer>();
		for (Vertex v : network_.getAllVertices()) {
			excess_.put(v, 0.0);
			heights_.put(v, 0); // TODO: set h(v) to be the smaller of n and the distance from v to t
		}
		heights_.put(source_, 1);
		// Excess of s is set to a number that exceeds the potential flow value
		// e.g., sum of capacities of all outgoing arcs of s plus one
		for (Arc sv : source_.getOutgoingArcs()) {
			excess_.put(source_, excess_.get(source_) + sv.getCapacity());
		}
		excess_.put(source_, excess_.get(source_) + 1);
		for (Arc sv : source_.getOutgoingArcs()) {
			push(sv);
		}
		heights_.put(source_, network_.getNumberOfVertices());
	}
	
	/**
	 * Discharge.
	 *
	 * @param v the v
	 */
	private static void discharge(Vertex v) {
		if (!vertexIsActive(v)) {
			Log.e("Non-active " + v + ". Unable to discharge!");
			return;
		}
		Collection<Arc> list = v.getOutgoingArcs();
		Arc vw = list.iterator().next();
		do {
			if (arcIsAdmissible(vw)) {
				push(vw);
			} else {
				list.remove(vw);
				if (!list.isEmpty()) {
					vw = list.iterator().next();
				}
			}
		} while (0.0 != excess_.get(v) && !list.isEmpty());
		if (list.isEmpty()) {
			relabel(v);
		}
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
		if (!vertexIsActive(u)) {
			Log.e("Non-active " + u + ". Unable to push!");
			return;
		}
		if (!arcIsAdmissible(uv)) {
			Log.e("Arc is not admissible, " + uv + ". Unable to push!");
			return;
		}
		double fuv = preflow_.get(uv);
		double residualCapacity = uv.getCapacity() - fuv;
		double delta = Math.min(excess_.get(u), residualCapacity);
		preflow_.put(uv, fuv + delta);
		preflow_.put(vu, preflow_.get(vu) - delta);
		excess_.put(u, excess_.get(u) - delta);
		excess_.put(v, excess_.get(v) + delta);
	}
	
	/**
	 * Relabel.
	 *
	 * @param u the vertex u
	 */
	private static void relabel(Vertex u) {
		/*
		The relabel operation applies on an active vertex u without any admissible out-edges in G.
		It modifies h(u) to the minimum value such that an admissible out-edge is created.
		Note that this always increases h(u) and never creates a steep edge,
		which is an edge (u, v) such that c_f(u, v) > 0, and h(u) > h(v) + 1.
		relabel(u):
    		assert e[u] > 0 and h[u] <= h[v] for all v such that f[u][v] < c[u][v]
    		h[u] = min( h[v] for all v such that f[u][v] < c[u][v] ) + 1
    		if such node v not exists, then set h[u] = n
		 */
		if (!vertexIsActive(u)) {
			Log.e("Non-active " + u + ". Unable to relabel!");
			return;
		}
		for (Arc uv : u.getOutgoingArcs()) {
			if (arcIsAdmissible(uv)) {
				Log.e("Found admissible ougoing arc " + uv + ". Unable to relabel!");
				return;
			}
		}
		int minHeight = Integer.MAX_VALUE;
		for (Arc uv : u.getOutgoingArcs()) {
			if (preflow_.get(uv) < uv.getCapacity()) {
				Vertex v = uv.getEndVertex();
				minHeight = heights_.get(v);
			}
		}
		if (minHeight < Integer.MAX_VALUE) {
			heights_.put(u, minHeight + 1);
		} else {
			heights_.put(u, network_.getNumberOfVertices());
		}
	}
	
	/**
	 * Vertex is active.
	 *
	 * @param v the v
	 * @return true, if successful
	 */
	private static boolean vertexIsActive(Vertex v) {
//		if (v.equals(source_) || v.equals(sink_)) {
//			return false;
//		} // TODO: check if we really need this
		if (heights_.get(v) >= network_.getNumberOfVertices()) {
			return false;
		}
		if (excess_.get(v) <= 0) {
			return false;
		}
		return true;
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
	
	private static void printNetwork() {
		Log.p(network_.getName());
		for (Vertex v : network_.getAllVertices()) {
			String active = vertexIsActive(v) ? "active" : "passive";
			Log.ps("vertex %s: h=%d - e=%.1f - %s", v.getName(), heights_.get(v), excess_.get(v), active);
		}
		for (Arc a : network_.getAllArcs()) {
			Log.ps("arc %s: f=%.1f - c=%.1f", a.getName(), preflow_.get(a), a.getCapacity());
		}
	}

}
