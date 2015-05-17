package flow;

import graph.Arc;
import graph.Graph;
import graph.Vertex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import util.Log;

public class PushRelabelAlgo {
	
	private static final String RES_ = "Residual";
	
	private static Graph residualGraph_;
	
	private static Vertex source_;
	
	private static Vertex sink_;
	
	private static HashMap<String, Double> preflow_;
	
	private static HashMap<Vertex, Double> excess_;
	
	private static HashMap<Vertex, Integer> distances_;
	
	private static LinkedList<Vertex> activeVertices_;
	
	public static HashMap<Arc, Double> computeMaxFlow(Graph graph, Vertex s, Vertex t) {

		initialize(graph, s, t);
		
		printResidualGraph();
		
		while(!activeVertices_.isEmpty()) {
			Vertex v = activeVertices_.pop();
			discharge(v);
			if (vertexIsActive(v)) {
				activeVertices_.add(v);
			}
			printResidualGraph();
			Log.p("");
		}
		
		HashMap<Arc, Double> flow = new HashMap<Arc, Double>();
		for (String a : preflow_.keySet()) {
			if (graph.arcExists(a)) {
				flow.put(graph.getArc(a), preflow_.get(a));
				Log.p("Flow " + a + " " + preflow_.get(a));
			}
		}
		return flow;
	}
	
	private static void initialize(Graph graph, Vertex s, Vertex t) {
		residualGraph_ = new Graph(getResidualName(graph.getName()));
		for (Vertex v : graph.getAllVertices()) {
			Vertex u = new Vertex(v.getName());
			u.setId(v.getId());
			residualGraph_.addVertex(u);
		}
		source_ = residualGraph_.getVertex(s.getName());
		sink_ = residualGraph_.getVertex(t.getName());
		for (Arc a : graph.getAllArcs()) {
			Vertex u = residualGraph_.getVertex(a.getStartVertex().getName());
			Vertex v = residualGraph_.getVertex(a.getEndVertex().getName());
			Arc uv = new Arc(a.getName(), u, v);
			uv.setId(a.getId());
			uv.setCapacity(a.getCapacity());
			residualGraph_.addArc(uv);
		}
		// zero preflow
		preflow_ = new HashMap<String, Double>();
		for (Arc a : residualGraph_.getAllArcs()) {
			preflow_.put(a.getName(), 0.0);
		}
		// initial excesses
		excess_ = new HashMap<Vertex, Double>();
		for (Vertex v : residualGraph_.getAllVertices()) {
			// zero excess
			excess_.put(v, 0.0);
		}
		// Excess of s is set to a number that exceeds the potential flow value
		// e.g., sum of capacities of all outgoing arcs of s plus one
		for (Arc sv : source_.getOutgoingArcs()) {
			excess_.put(source_, excess_.get(source_) + sv.getCapacity());
		}
		excess_.put(source_, excess_.get(source_) + 1);
		// set distances
		setInitialDistances();
		activeVertices_ = new LinkedList<Vertex>();
		activeVertices_.add(source_);
	}
	
	/**
	 * Sets the initial distances.
	 */
	private static void setInitialDistances() {
		distances_ = new HashMap<Vertex, Integer>();
		for (Vertex v : residualGraph_.getAllVertices()) {
			distances_.put(v, 1);
		}
		distances_.put(sink_, 0);
		distances_.put(source_, 2);
		if (2 == residualGraph_.getNumberOfVertices()) {
			distances_.put(source_, 1); // since no other node exists with distance 1
		}
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
		Log.p("Discharge vertex " + v.getName());
		Vector<Arc> list = new Vector<Arc>();
		list.addAll(v.getOutgoingArcs());
		if (!list.isEmpty()) {
			do {
				Arc vw = list.remove(0);
				if (arcIsAdmissible(vw)) {
					push(vw);
				}
			} while (0.0001 <= excess_.get(v) && !list.isEmpty());
		}
		if (vertexIsActive(v)) {
			relabel(v);
		}
	}
	
	/**
	 * Gets the residual name.
	 *
	 * @param name the name
	 * @return the residual name
	 */
	private static String getResidualName(String name) {
		if (name.startsWith(RES_)) {
			return name.substring(RES_.length());
		}
		return RES_ + name;
	}
	
	/**
	 * Push.
	 *
	 * @param uv the uv
	 */
	private static void push(Arc uv) {
		Vertex u = uv.getStartVertex();
		Vertex v = uv.getEndVertex();
		if (!vertexIsActive(u)) {
			Log.e("Non-active " + u + ". Unable to push!");
			return;
		}
		if (!arcIsAdmissible(uv)) {
			Log.e("Arc is not admissible, " + uv + ". Unable to push!");
			return;
		}
		double delta = Math.min(excess_.get(u), uv.getCapacity());
		if (0.0 == delta) {
			Log.w("Delta is zero: " + delta);
		}
		Log.ps("Push %.1f over arc %s", delta, uv.getName());
		addToPreflow(uv, delta);
		Arc vu = residualGraph_.getArc(getResidualName(uv.getName()));
		if (null == vu) {
			vu = new Arc(getResidualName(uv.getName()), v, u);
			vu.setCapacity(delta);
			residualGraph_.addArc(vu);
			if (preflow_.containsKey(vu.getName())) {
				preflow_.put(vu.getName(), preflow_.get(vu.getName()) - delta);
			} else {
				preflow_.put(vu.getName(), -delta);
			}
		} else {
			addToPreflow(vu, -delta);
		}
		excess_.put(u, excess_.get(u) - delta);
		excess_.put(v, excess_.get(v) + delta);
		if (vertexIsActive(v)) {
			activeVertices_.add(v);
		}
	}
	
	/**
	 * Adds to preflow.
	 *
	 * @param uv the uv
	 * @param delta the delta
	 */
	private static void addToPreflow(Arc uv, double delta) {
		preflow_.put(uv.getName(), preflow_.get(uv.getName()) + delta);
		uv.setCapacity(uv.getCapacity() - delta);
		if (0.0 == uv.getCapacity()) {
			residualGraph_.removeArc(uv.getName());
		}
	}
	
	/**
	 * Relabel.
	 *
	 * @param u the vertex u
	 */
	private static void relabel(Vertex u) {
		if (!vertexIsActive(u)) {
			Log.e("Non-active " + u + ". Unable to relabel!");
			return;
		}
		for (Arc uv : u.getOutgoingArcs()) {
			if (arcIsAdmissible(uv)) {
				Log.e("Found admissible outgoing arc " + uv + ". Unable to relabel!");
				return;
			}
		}
		int minDistance = Integer.MAX_VALUE;
		for (Arc uv : u.getOutgoingArcs()) {
			Vertex v = uv.getEndVertex();
			int dv = distances_.get(v);
			if (minDistance > dv) {
				minDistance = dv;
			}
		}
		if (minDistance < Integer.MAX_VALUE) {
			distances_.put(u, minDistance + 1);
		} else {
			distances_.put(u, residualGraph_.getNumberOfVertices());
		}
		Log.ps("Relabel node %s to level %d", u.getName(), distances_.get(u));
	}
	
	/**
	 * Vertex is active.
	 *
	 * @param v the v
	 * @return true, if successful
	 */
	private static boolean vertexIsActive(Vertex v) {
		if (v.equals(sink_)) {
			return false;
		}
		int dv = distances_.get(v);
		if (0 > dv) {
			return false;
		}
		if (dv >= residualGraph_.getNumberOfVertices()) {
			if (v.equals(source_)) {
				return false;
			}
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
		Vertex u = arc.getStartVertex();
		Vertex v = arc.getEndVertex();
		boolean correctDistance = (distances_.get(u) >= distances_.get(v) + 1);
		return correctDistance;
	}
	
	/**
	 * Prints the residual graph.
	 */
	private static void printResidualGraph() {
		Log.p(residualGraph_.getName());
		for (Vertex v : residualGraph_.getAllVertices()) {
			String active = vertexIsActive(v) ? "active" : "passive";
			Log.ps("vertex %s: d=%d - e=%.1f - %s", v.getName(), distances_.get(v), excess_.get(v), active);
		}
		for (Arc a : residualGraph_.getAllArcs()) {
			Log.ps("arc %s: f=%.1f - rc=%.1f", a.getName(), preflow_.get(a.getName()), a.getCapacity());
		}
	}

}
