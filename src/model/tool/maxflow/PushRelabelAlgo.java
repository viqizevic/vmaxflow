package model.tool.maxflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import util.Log;

public class PushRelabelAlgo {
	
	private static final String RES_ = "RESIDUAL_";
	
	private Graph graph_;
	
	private Graph residualGraph_;
	
	private Vertex source_;
	
	private Vertex sink_;
	
	private HashMap<String, Double> preflow_;
	
	private HashMap<Vertex, Double> excess_;
	
	private HashMap<Vertex, Integer> distances_;
	
	private HashMap<Integer, LinkedList<Vertex>> activeVertices_;
	
	private int largestDistance_;
	
	/**
	 * Instantiates a new push relabel algo.
	 *
	 * @param graph the graph
	 * @param s the s
	 * @param t the t
	 */
	public PushRelabelAlgo(Graph graph, Vertex s, Vertex t) {
		initialize(graph, s, t);
	}
	
	/**
	 * Compute max flow.
	 *
	 * @return the hash map
	 */
	public HashMap<Arc, Double> computeMaxFlow() {
		printResidualGraph();
		
		while(!noMoreActiveVertexAvailable()) {
			Vertex v = retrieveActiveVertex();
			discharge(v);
			if (vertexIsActive(v)) {
				addToActiveVerticesList(v);
			}
			Log.p("");
		}
		
		return getFlow();
	}
	
	/**
	 * Gets the flow.
	 *
	 * @return the flow
	 */
	public HashMap<Arc, Double> getFlow() {
		HashMap<Arc, Double> flow = new HashMap<Arc, Double>();
		for (String a : preflow_.keySet()) {
			if (graph_.arcExists(a)) {
				flow.put(graph_.getArc(a), preflow_.get(a));
				if (preflow_.get(a) > 0) {
					Log.p("Flow " + a + " " + preflow_.get(a));
				}
			}
		}
		return flow;
	}
	
	/**
	 * Gets the min cut set close to source.
	 *
	 * @return the min cut set close to source
	 */
	public HashSet<Vertex> getMinCutSetCloseToSource() {
		HashSet<Vertex> set = new HashSet<Vertex>();
		int n = graph_.getNumberOfVertices();
		if (null == distances_) {
			Log.w("Cannot get min cut. Please call the method to compute max flow first.");
			return null;
		}
		for (Vertex v : distances_.keySet()) {
			if (distances_.get(v) >= n) {
				set.add(graph_.getVertex(v.getName()));
			}
		}
		return set;
	}
	
	/**
	 * Gets the min cut.
	 *
	 * @return the min cut
	 */
	public Collection<Arc> getMinCut() {
		return Graph.getOutgoingArcs(getMinCutSetCloseToSource());
	}
	
	/**
	 * Gets the max flow value.
	 *
	 * @return the max flow value
	 */
	public double getMaxFlowValue() {
		double f = 0.0;
		for (Arc a : graph_.getVertex(source_.getName()).getOutgoingArcs()) {
			f += preflow_.get(a.getName());
		}
		return f;
	}
	
	private void initialize(Graph graph, Vertex s, Vertex t) {
		graph_ = graph;
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
		activeVertices_ = new HashMap<Integer, LinkedList<Vertex>>();
		largestDistance_ = 0;
		addToActiveVerticesList(source_);
	}
	
	/**
	 * Sets the initial distances.
	 */
	private void setInitialDistances() {
		distances_ = new HashMap<Vertex, Integer>();
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(sink_);
		distances_.put(sink_, 0);
		while (!queue.isEmpty()) {
			Vertex v = queue.pop();
			int dv = distances_.get(v);
			for (Arc uv : v.getIngoingArcs()) {
				Vertex u = uv.getStartVertex();
				int du = dv + 1;
				if (du > residualGraph_.getNumberOfVertices()) {
					du = residualGraph_.getNumberOfVertices();
				}
				if (!distances_.containsKey(u)) {
					queue.add(u);
					distances_.put(u, du);
				}
			}
		}
		if (distances_.size() < residualGraph_.getNumberOfVertices()) {
			for (Vertex v : residualGraph_.getAllVertices()) {
				if (!distances_.containsKey(v)) {
					distances_.put(v, residualGraph_.getNumberOfVertices());
				}
			}
		}
	}
	
	/**
	 * Adds the to active vertices list.
	 *
	 * @param v the v
	 */
	private void addToActiveVerticesList(Vertex v) {
		Integer dv = distances_.get(v);
		if (!activeVertices_.containsKey(dv)) {
			activeVertices_.put(dv, new LinkedList<Vertex>());
		}
		activeVertices_.get(dv).add(v);
		if (largestDistance_ < dv) {
			largestDistance_ = dv;
		}
	}
	
	/**
	 * Retrieve active vertex.
	 *
	 * @return the vertex
	 */
	private Vertex retrieveActiveVertex() {
		if (!activeVertices_.containsKey(largestDistance_)) {
			Log.e("Cannot retrieve active vertex from the level " + largestDistance_);
		}
		Vertex v = activeVertices_.get(largestDistance_).pop();
		if (activeVertices_.get(largestDistance_).isEmpty()) {
			activeVertices_.remove(largestDistance_);
		}
		while (!activeVertices_.containsKey(largestDistance_) && largestDistance_ > 0) {
			largestDistance_--;
		}
		return v;
	}
	
	/**
	 * No more active vertex available.
	 *
	 * @return true, if successful
	 */
	private boolean noMoreActiveVertexAvailable() {
		return activeVertices_.isEmpty();
	}
	
	/**
	 * Discharge.
	 *
	 * @param v the v
	 */
	private void discharge(Vertex v) {
		if (!vertexIsActive(v)) {
			Log.w("Non-active " + v + ". Unable to discharge!");
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
			} while (0.0 <= excess_.get(v) && !list.isEmpty());
		}
		if (vertexIsActive(v)) {
			relabel(v);
		}
	}
	
	/**
	 * Checks if is residual name.
	 *
	 * @param name the name
	 * @return true, if is residual name
	 */
	private boolean isResidualName(String name) {
		return name.startsWith(RES_);
	}
	
	/**
	 * Gets the residual name.
	 *
	 * @param name the name
	 * @return the residual name
	 */
	private String getResidualName(String name) {
		if (isResidualName(name)) {
			return name.substring(RES_.length());
		}
		return RES_ + name;
	}
	
	/**
	 * Push.
	 *
	 * @param uv the uv
	 */
	private void push(Arc uv) {
		Vertex u = uv.getStartVertex();
		Vertex v = uv.getEndVertex();
		if (!vertexIsActive(u)) {
			Log.w("Non-active " + u + ". Unable to push!");
			return;
		}
		if (!arcIsAdmissible(uv)) {
			Log.w("Arc is not admissible, " + uv + ". Unable to push!");
			return;
		}
		double delta = Math.min(excess_.get(u), uv.getCapacity());
		if (0.0 == delta) {
			Log.w("Delta is zero: " + delta);
		}
		Log.ps("Push %.2f over arc %s", delta, uv.getName());
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
			addToActiveVerticesList(v);
		}
	}
	
	/**
	 * Adds to preflow.
	 *
	 * @param uv the uv
	 * @param delta the delta
	 */
	private void addToPreflow(Arc uv, double delta) {
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
	private void relabel(Vertex u) {
		if (!vertexIsActive(u)) {
			Log.w("Non-active " + u + ". Unable to relabel!");
			return;
		}
		for (Arc uv : u.getOutgoingArcs()) {
			if (arcIsAdmissible(uv)) {
				Log.w("Found admissible outgoing arc " + uv + ". Unable to relabel!");
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
	private boolean vertexIsActive(Vertex v) {
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
		if (excess_.get(v) <= 0.0) {
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
	private boolean arcIsAdmissible(Arc arc) {
		Vertex u = arc.getStartVertex();
		Vertex v = arc.getEndVertex();
		boolean correctDistance = (distances_.get(u) >= distances_.get(v) + 1);
		return correctDistance;
	}
	
	/**
	 * Prints the residual graph.
	 */
	private void printResidualGraph() {
		Log.p(residualGraph_.getName());
		for (Vertex v : residualGraph_.getAllVertices()) {
			String active = vertexIsActive(v) ? "active" : "passive";
			Log.ps("vertex %s: d=%d - e=%.2f - %s", v.getName(), distances_.get(v), excess_.get(v), active);
		}
		for (Arc a : residualGraph_.getAllArcs()) {
			Log.ps("arc %s: f=%.2f - rc=%.2f", a.getName(), preflow_.get(a.getName()), a.getCapacity());
		}
	}

}
