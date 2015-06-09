package model.tool.maxflow;

import java.util.Collection;
import java.util.HashMap;

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
		String content = "# Node u, Node v, Capacity of uv\n";
		String format = "%3s, %3s, %7.3f\n";
		for (Arc a : source.getOutgoingArcs()) {
			Vertex v = a.getEndVertex();
			content += String.format(format, source.getName(), v.getName(), a.getCapacity());
		}
		for (Arc a : graph.getAllArcs()) {
			Vertex u = a.getStartVertex();
			Vertex v = a.getEndVertex();
			if (u.equals(source) || v.equals(sink)) {
				continue;
			}
			content += String.format(format, u.getName(), v.getName(), a.getCapacity());
		}
		for (Arc a : sink.getIngoingArcs()) {
			Vertex u = a.getStartVertex();
			if (u.equals(source)) {
				// avoid double print (already printed before as outgoing arc from source)
				continue;
			}
			content += String.format(format, u.getName(), sink.getName(), a.getCapacity());
		}
		FileOrganizer.writeFile(fileName, content);
	}
	
}
