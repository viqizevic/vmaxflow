package graph;

import java.util.Collection;
import java.util.HashMap;

import util.Log;
import util.Util;

/**
 * The Class Network.
 */
public class Network {
	
	/** The name. */
	private String name_;
	
	/** The vertex id counter_. */
	private int vertexIdCounter_;
	
	/** The arc id counter_. */
	private int arcIdCounter_;
	
	/** The vertices. */
	private HashMap<String, Vertex> vertices_;
	
	/** The vertex names. */
	private HashMap<Integer, String> vertexNames_;
	
	/** The arcs. */
	private HashMap<String, Arc> arcs_;
	
	/** The arc names_. */
	private HashMap<Integer, String> arcNames_;
	
	/**
	 * Instantiates a new network.
	 *
	 * @param name the name
	 */
	public Network(String name) {
		name_ = name;
		vertexIdCounter_ = 1;
		arcIdCounter_ = 1;
		vertices_ = new HashMap<String, Vertex>();
		vertexNames_ = new HashMap<Integer, String>();
		arcs_ = new HashMap<String, Arc>();
		arcNames_ = new HashMap<Integer, String>();
		addInitialStyles();
	}
	
	/**
	 * Adds the initial styles.
	 */
	private void addInitialStyles() {
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name_;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		name_ = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		int nrLoc = vertices_.size();
		int nrTr = arcs_.size();
		String s = "Network " + name_ + "\n";
		s += Util.nText("%d vertex", "%d vertices", nrLoc) + "\n";
		s += Util.nText("%d arc", nrTr) + "\n";
		if (nrLoc + nrTr > 25) {
			return s;
		}
		for (String n : vertices_.keySet()) {
			s += vertices_.get(n).toString() + "\n";
		}
		for (Arc t : arcs_.values()) {
			s += t.toString() + "\n";
		}
		return s.trim();
	}
	
	/**
	 * Adds the vertex.
	 *
	 * @param vertex the vertex
	 * @return true, if successful
	 */
	public boolean addVertex(Vertex vertex) {
		if (vertex == null) {
			Log.e("Unable to add a null vertex!");
			return false;
		}
		if (vertexExists(vertex.getName())) {
			Log.e("Cannot replace existing vertex: " + getVertex(vertex.getName()));
			return false;
		}
		int vertexId = vertexIdCounter_;
		if (Vertex.DEFAULT_VERTEX_ID == vertex.getId()) {
			vertex.setId(vertexIdCounter_);
			vertexIdCounter_++;
		} else {
			vertexId = vertex.getId();
		}
		if (vertexNames_.containsKey(vertexId)) {
			Log.w("Cannot replace existing vertex ID:  " + vertexId);
			return false;
		}
		vertexNames_.put(vertexId, vertex.getName());
		vertices_.put(vertex.getName(), vertex);
		return true;
	}
	
	/**
	 * Adds the vertices.
	 *
	 * @param vertices the vertices
	 */
	public void addVertices(Collection<Vertex> vertices) {
		for (Vertex v : vertices) {
			addVertex(v);
		}
	}
	
	/**
	 * Gets the all vertices names.
	 *
	 * @return the all vertices names
	 */
	public Collection<String> getAllVerticesNames() {
		return vertices_.keySet();
	}
	
	/**
	 * Gets the all vertices.
	 *
	 * @return the all vertices
	 */
	public Collection<Vertex> getAllVertices() {
		return vertices_.values();
	}
	
	/**
	 * Gets the number of vertices.
	 *
	 * @return the number of vertices
	 */
	public int getNumberOfVertices() {
		return vertices_.size();
	}
	
	/**
	 * Vertex exists.
	 *
	 * @param vertexName the vertex name
	 * @return true, if vertex with this name exists
	 */
	public boolean vertexExists(String vertexName) {
		return vertices_.containsKey(vertexName);
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param name the name
	 * @return the vertex
	 */
	public Vertex getVertex(String name) {
		if (!vertexExists(name)) {
			Log.w("Try to retrieve non-existent vertex.. " + name);
		}
		return vertices_.get(name);
	}
	
	/**
	 * Gets the vertex.
	 *
	 * @param id the id
	 * @return the vertex
	 */
	public Vertex getVertex(int id) {
		return getVertex(getVertexName(id));
	}
	
	/**
	 * Removes the vertex.
	 *
	 * @param name the name
	 * @return the abstract vertex
	 */
	public Vertex removeVertex(String name) {
		if (!vertexExists(name)) {
			Log.p("Try to remove a non-existent vertex: " + name);
		}
		Vertex loc = vertices_.get(name);
		if (!loc.getIngoingArcs().isEmpty()) {
			Log.p("Try to remove a vertex which still connected to a arc: " + name);
		}
		vertexNames_.remove(loc.getId());
		return vertices_.remove(name);
	}
	
	
	/**
	 * Gets the vertex name.
	 *
	 * @param id the id
	 * @return the vertex name
	 */
	public String getVertexName(int id) {
		if (!vertexNames_.containsKey(id)) {
			Log.w("Try to retrieve non-existent vertex name.. Id:" + id);
		}
		return vertexNames_.get(id);
	}
	
	/**
	 * Adds the arc.
	 *
	 * @param start the start
	 * @param end the end
	 * @param capacity the capacity
	 * @return true, if successful
	 */
	public boolean addArc(Vertex start, Vertex end, double capacity) {
		Arc a = new Arc(start.getName()+"to"+end.getName(), start, end);
		a.setCapacity(capacity);
		return addArc(a);
	}
	
	/**
	 * Adds the double arcs.
	 *
	 * @param u the u
	 * @param v the v
	 * @return true, if successful
	 */
	public boolean addDoubleArcs(Vertex u, Vertex v, double capacity) {
		boolean b = true;
		b = b && addArc(u, v, capacity);
		b = b && addArc(v, u, capacity);
		return b;
	}
	
	/**
	 * Adds the arc.
	 *
	 * @param arc the arc
	 * @return true, if successful
	 */
	public boolean addArc(Arc arc) {
		if (arc == null) {
			Log.e("Unable to add a null arc!");
			return false;
		}
		if (arc.getStartVertex().equals(arc.getEndVertex())) {
			Log.w("Cannot add a circle arc connecting two same vertices!");
			return false;
		}
		if (arcExists(arc.getName())) {
			Log.w("Cannot replace existing arc with the same name: " + arc.getName());
			return false;
		}
		if (!vertexExists(arc.getStartVertex().getName()) || !vertexExists(arc.getEndVertex().getName())) {
			Log.w("Cannot add arc " + arc.getName() + ", since one of the nodes is not in network!");
			return false;
		}
		int arcId = arcIdCounter_;
		if (Arc.DEFAULT_ARC_ID == arc.getId()) {
			arc.setId(arcIdCounter_);
			arcIdCounter_++;
		} else {
			arcId = arc.getId();
		}
		arcs_.put(arc.getName(), arc);
		if (arcNames_.containsKey(arcId)) {
			Log.w("Override existing arc with the same id!");
		}
		arcNames_.put(arc.getId(), arc.getName());
		return true;
	}

	/**
	 * Gets the all arcs names.
	 *
	 * @return the all arcs names
	 */
	public Collection<String> getAllArcsNames() {
		return arcs_.keySet();
	}
	
	/**
	 * Gets the all arcs.
	 *
	 * @return the all arcs
	 */
	public Collection<Arc> getAllArcs() {
		return arcs_.values();
	}
	
	/**
	 * Arc exists.
	 *
	 * @param name the name
	 * @return true, if arc with given name exists
	 */
	public boolean arcExists(String name) {
		return arcs_.containsKey(name);
	}
	
	/**
	 * Gets the arc.
	 *
	 * @param name the name
	 * @return the arc
	 */
	public Arc getArc(String name) {
		return arcs_.get(name);
	}
	
	/**
	 * Gets the arc.
	 *
	 * @param id the id
	 * @return the arc
	 */
	public Arc getArc(int id) {
		return arcs_.get(arcNames_.get(id));
	}
	
	/**
	 * Gets the connecting arc.
	 *
	 * @param u the start vertex
	 * @param v the end vertex
	 * @return the connecting arc if exists, otherwise null
	 */
	public Arc getConnectingArc(Vertex u, Vertex v) {
		if (u.isConnectedTo(v)) {
			for (Arc t : u.getIngoingArcs()) {
				if (t.isConnecting(u, v)) {
					return t;
				}
			}
		}
		return null;
	}
	
	/**
	 * Removes the arc.
	 *
	 * @param name the name
	 * @return the arc
	 */
	public Arc removeArc(String name) {
		if (!arcExists(name)) {
			Log.e("Try to remove a arc which not exists.. " + name);
			return null;
		}
		Arc r = arcs_.remove(name);
		arcNames_.remove(r.getId());
		r.getStartVertex().removeArc(name);
		r.getEndVertex().removeArc(name);
		return r;
	}
	
	/**
	 * Removes the arcs.
	 *
	 * @param arcs the arcs
	 */
	public void removeArcs(Collection<Arc> arcs) {
		for (Object l : arcs.toArray()) {
			removeArc(((Arc) l).getName());
		}
	}
	
	/**
	 * Gets the number of arcs.
	 *
	 * @return the number of arcs
	 */
	public int getNumberOfArcs() {
		return arcs_.size();
	}
	
	/**
	 * Checks if network is empty.
	 *
	 * @return true, if network is empty
	 */
	public boolean isEmpty() {
		 return vertices_.isEmpty();
	}
	
	/**
	 * Checks if network is consistent.
	 *
	 * @return true, if network is consistent
	 */
	public boolean checkConsistency() {
		boolean res = true;
		for (String lName : getAllVerticesNames()) {
			Vertex loc = getVertex(lName);
			if (loc.getId() <= -1) {
				Log.w("Bad number is used as id for a vertex: " + loc.toString());
				res = false;
			}
			if (!lName.equals(loc.getName())) {
				Log.w("Name is not used as the key for the vertices hash map: " + loc.getName());
				res = false;
			}
			if (loc.getIngoingArcs().isEmpty()) {
				Log.w("Found a vertex without arc: " + loc.getName());
				res = false;
			}
			for (Arc t : loc.getIngoingArcs()) {
				if (!arcExists(t.getName())) {
					Log.w("A arc connected to a vertex is not in hash map: " + t.getName());
					res = false;
				}
			}
		}
		for (String tName : getAllArcsNames()) {
			Arc tr = getArc(tName);
			if (tr.getId() <= -1) {
				Log.w("Bad number is used as id for a arc: " + tr.toString());
				res = false;
			}
			if (!tName.equals(tr.getName())) {
				Log.w("Name is not used as the key for the arcs hash map: " + tr.getName());
				res = false;
			}
			if (!vertexExists(tr.getStartVertex().getName()) || !vertexExists(tr.getEndVertex().getName())) {
				if (!vertexExists(tr.getStartVertex().getName())) {
					Log.w("A vertex connected to arc is not in hash map: " + tr.getStartVertex());
				}
				if (!vertexExists(tr.getEndVertex().getName())) {
					Log.w("A vertex connected to arc is not in hash map: " + tr.getEndVertex());
				}
				res = false;
			}
		}
		if (true == res) {
			Log.p("Network " + this.name_ + " is consistent.");
		} else {
			Log.w("Network " + this.name_ + " is inconsistent");
		}
		return res;
	}
	
}
