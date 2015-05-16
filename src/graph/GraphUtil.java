package graph;

import java.util.Collection;
import java.util.HashMap;

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
	
}
