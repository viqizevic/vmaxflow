package graph;

import java.util.Collection;
import java.util.HashMap;

import util.Log;
import util.Util;

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
			content += String.format("%s, %s, %.0f, %.0f\n", u.getName(), v.getName(), flow.get(arc), arc.getCapacity());
		}
		Util.writeFile(outputFileName, content);
	}
	
}
