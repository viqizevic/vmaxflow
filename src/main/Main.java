package main;

import java.util.HashMap;

import model.tool.maxflow.Arc;
import model.tool.maxflow.Graph;
import model.tool.maxflow.GraphReader;
import model.tool.maxflow.GraphUtil;
import model.tool.maxflow.PushRelabelAlgo;
import model.tool.maxflow.PushRelabelFifoAlgo;
import model.tool.maxflow.Vertex;
import util.Log;
import util.Timer;

/**
 * The Class Main.
 */
public class Main {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		int n = args.length;
		if (0 == n || n > 2) {
			Log.p("Usage: java -jar vmaxflow.jar <filename> <outputfilename>");
			return;
		}
		
		String fileName = args[0];
		String output = "maxflow.txt";
		if (2 == n) {
			output = args[1];
		}
		
		GraphReader gr = new GraphReader();
		gr.readFile(fileName);
		Graph g = gr.getGraph();
		Vertex s = gr.getSource();
		Vertex t = gr.getSink();
		Log.p(g.toString());
		
		String timer = Timer.startNewTimer();
		HashMap<Arc, Double> flow = runAlgo(g, s, t, PushRelabelAlgo.class.toString());
		Timer.stopTimerAndPrintLog(timer, "Algo HL");
		GraphUtil.writeOutputFile(output+".hl.txt", g, flow);
		
		String timer2 = Timer.startNewTimer();
		flow = runAlgo(g, s, t, PushRelabelFifoAlgo.class.toString());
		Timer.stopTimerAndPrintLog(timer2, "Algo FIFO");
		
		GraphUtil.writeOutputFile(output, g, flow);
	}
	
	private static HashMap<Arc, Double> runAlgo(Graph g, Vertex s, Vertex t, String algoClass) {
		HashMap<Arc, Double> flow = null;
		Log.turnOffPrintLog();
		if (algoClass.equals(PushRelabelAlgo.class.toString())) {
			PushRelabelAlgo algo = new PushRelabelAlgo(g, s, t);
			flow = algo.computeMaxFlow();
			Log.turnOnPrintLog();
			Log.ps("\nMax flow = " + algo.getMaxFlowValue());
		} else if (algoClass.equals(PushRelabelFifoAlgo.class.toString())) {
			PushRelabelFifoAlgo algo = new PushRelabelFifoAlgo(g, s, t);
			flow = algo.computeMaxFlow();
			Log.turnOnPrintLog();
			Log.ps("\nMax flow = " + algo.getMaxFlowValue());
		}
		return flow;
	}
	
}
