package model.tool.maxflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import util.FileOrganizer;
import util.Log;

/**
 * The Class GraphUtil.
 */
public class GraphUtil {
	
	/**
	 * Total flow.
	 *
	 * @param arcs the arcs
	 * @param flow the flow
	 * @return the double
	 */
	public static double totalFlow(Collection<Arc> arcs, HashMap<Arc, Double> flow) {
		double sum = 0.0;
		for (Arc a : arcs) {
			Double f = flow.get(a);
			if (null == f) {
				Log.w("Null preflow.. " + a.toString());
				continue;
			}
			sum += f;
		}
		return sum;
	}
	
	/**
	 * Write output file.
	 *
	 * @param outputFileName the output file name
	 * @param graph the graph
	 * @param flow the flow
	 */
	public static void writeOutputFile(String outputFileName, Graph graph, HashMap<Arc, Double> flow) {
		String content = "# Node u, Node v, Flow on arc uv, Capacity of arc uv\n";
		for (Arc arc : graph.getAllArcs()) {
			Double f = flow.get(arc);
			if (null == f) {
				Log.e("Cannot find flow value for arc " + arc.getName());
				f = 0.0;
			}
			Vertex u = arc.getStartVertex();
			Vertex v = arc.getEndVertex();
			content += String.format("%s, %s, %.3f, %.3f\n", u.getName(), v.getName(), f, arc.getCapacity());
		}
		FileOrganizer.writeFile(outputFileName, content);
	}
	
	/**
	 * Write the input file for push relabel algo.
	 *
	 * @param fileName the file name
	 * @param graph the graph
	 * @param source the source
	 * @param sink the sink
	 */
	public static void writeInputFileForPushRelabelAlgo(String fileName, Graph graph, Vertex source, Vertex sink) {
		String content = "# Node u, Node v, Capacity of uv, Name of uv\n";
		String format = "%3s, %3s, %12.5f, %s\n";
		for (Arc a : source.getOutgoingArcs()) {
			Vertex v = a.getEndVertex();
			if (v.equals(sink)) {
				// avoid double print (should print this later as ingoing arc to sink)
				continue;
			}
			content += String.format(format, source.getName(), v.getName(), a.getCapacity(), a.getName());
		}
		for (Arc a : graph.getAllArcs()) {
			Vertex u = a.getStartVertex();
			Vertex v = a.getEndVertex();
			if (u.equals(source) || v.equals(sink)) {
				continue;
			}
			content += String.format(format, u.getName(), v.getName(), a.getCapacity(), a.getName());
		}
		for (Arc a : sink.getIngoingArcs()) {
			Vertex u = a.getStartVertex();
			content += String.format(format, u.getName(), sink.getName(), a.getCapacity(), a.getName());
		}
		FileOrganizer.writeFile(fileName, content);
	}
	
	/**
	 * Gets the outgoing arcs.
	 *
	 * @param graph the graph
	 * @param set the set
	 * @return the outgoing arcs
	 */
	public static Collection<Arc> getOutgoingArcs(Graph graph, HashSet<String> set) {
		Vector<Arc> cut = new Vector<Arc>();
		for (String un : set) {
			Vertex u = graph.getVertex(un);
			for (Arc a : u.getOutgoingArcs()) {
				Vertex v = a.getEndVertex();
				if (!set.contains(v.getName())) {
					cut.add(a);
				}
			}
		}
		return cut;
	}
	
	/**
	 * Gets the ingoing arcs.
	 *
	 * @param graph the graph
	 * @param set the set
	 * @return the ingoing arcs
	 */
	public static Collection<Arc> getIngoingArcs(Graph graph, HashSet<String> set) {
		Vector<Arc> cut = new Vector<Arc>();
		for (String un : set) {
			Vertex v = graph.getVertex(un);
			for (Arc a : v.getIngoingArcs()) {
				Vertex u = a.getStartVertex();
				if (!set.contains(u.getName())) {
					cut.add(a);
				}
			}
		}
		return cut;
	}
	
	/**
	 * Gets the connecting arcs.
	 *
	 * @param graph the graph
	 * @param sourceSet the source set
	 * @param sinkSet the sink set
	 * @return the connecting arcs
	 */
	public static Collection<Arc> getConnectingArcs(Graph graph, HashSet<String> sourceSet, HashSet<String> sinkSet) {
		Vector<Arc> cut = new Vector<Arc>();
		for (String un : sourceSet) {
			Vertex u = graph.getVertex(un);
			for (Arc a : u.getOutgoingArcs()) {
				Vertex v = a.getEndVertex();
				if (sinkSet.contains(v.getName())) {
					cut.add(a);
				}
			}
		}
		return cut;
	}
	
	/**
	 * Find cut set.
	 *
	 * @param graph the graph
	 * @param flow the flow
	 * @param source the source
	 * @param sink the sink
	 * @return the collection
	 */
	public static Collection<Arc> findCutSet(Graph graph, HashMap<Arc, Double> flow, Vertex source, Vertex sink) {
		HashSet<String> setFromSource = findCutVerticesSetClosestToSource(graph, flow, source);
		HashSet<String> setFromSink = findCutVerticesSetClosestToSink(graph, flow, sink);
		return getConnectingArcs(graph, setFromSource, setFromSink);
	}
	
	/**
	 * Find cut vertices set closest to source.
	 *
	 * @param graph the graph
	 * @param flow the flow
	 * @param source the source
	 * @return the collection
	 */
	public static HashSet<String> findCutVerticesSetClosestToSource(Graph graph, HashMap<Arc, Double> flow, Vertex source) {
		Graph rg = graph.createResidualGraph("RES", flow);
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(rg.getVertex(source.getName()));
		HashSet<String> observedVertices = new HashSet<String>();
		while (!queue.isEmpty()) {
			Vertex u = queue.pop();
			observedVertices.add(u.getName());
			for (Arc uv : u.getOutgoingArcs()) {
				Vertex v = uv.getEndVertex();
				if (!observedVertices.contains(v.getName())) {
					queue.push(v);
				}
			}
		}
		return observedVertices;
	}
	
	/**
	 * Find cut vertices set closest to sink.
	 *
	 * @param graph the graph
	 * @param flow the flow
	 * @param sink the sink
	 * @return the hash set
	 */
	public static HashSet<String> findCutVerticesSetClosestToSink(Graph graph, HashMap<Arc, Double> flow, Vertex sink) {
		Graph rg = graph.createResidualGraph("RES", flow);
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(rg.getVertex(sink.getName()));
		HashSet<String> observedVertices = new HashSet<String>();
		while (!queue.isEmpty()) {
			Vertex v = queue.pop();
			observedVertices.add(v.getName());
			for (Arc uv : v.getIngoingArcs()) {
				Vertex u = uv.getStartVertex();
				if (!observedVertices.contains(u.getName())) {
					queue.push(u);
				}
			}
		}
		return observedVertices;
	}
	
}
