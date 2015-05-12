package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.Log;

/**
 * The Class Vertex.
 */
public class Vertex {
	
	/** The default id. */
	private final int defaultId_ = -1;
	
	/** The id. */
	private int id_;
	
	/** The name. */
	private String name_;
	
	/** The arcs connecting to this vertex. */
	private HashMap<String, Arc> arcs_;
	

	/**
	 * Instantiates a new vertex.
	 *
	 * @param name the name
	 */
	public Vertex(String name) {
		id_ = defaultId_;
		this.name_ = name;
		arcs_ = new HashMap<String, Arc>();
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id_;
	}

	/**
	 * Sets the id.
	 *
	 * @param newId the new id
	 * @return true, if successful
	 */
	public boolean setId(int newId) {
		if (defaultId_ != id_) {
			Log.e("ID changed already. Cannot change anymore. ID=" + id_);
			return false;
		}
		id_ = newId;
		return true;
	}

	/**
	 * Adds the connecting arc.
	 *
	 * @param arc the arc
	 */
	public void addConnectingArc(Arc arc) {
		if (arc == null) {
			return;
		}
		arcs_.put(arc.getName(), arc);
	}
	
	/**
	 * Gets the arcs.
	 *
	 * @return the arcs
	 */
	public Collection<Arc> getArcs() {
		return arcs_.values();
	}
	
	/**
	 * Removes the arc.
	 *
	 * @param name the name
	 * @return the arc
	 */
	public Arc removeArc(String name) {
		return arcs_.remove(name);
	}
	
	/**
	 * Removes the arcs.
	 *
	 * @return the collection
	 */
	public Collection<Arc> removeArcs() {
		Vector<Arc> removed = new Vector<Arc>();
		for (Arc t : arcs_.values()) {
			removed.add(t);
		}
		for (Arc t : removed) {
			removeArc(t.getName());
		}
		return removed;
	}

	/**
	 * Gets the neighbors.
	 *
	 * @return the neighbors
	 */
	public Collection<Vertex> getNeighbors() {
		Vector<Vertex> v = new Vector<Vertex>();
		for (Arc t : arcs_.values()) {
			v.add(getConnectedVertex(t));
		}
		return v;
	}
	
	/**
	 * Checks if is connected to.
	 *
	 * @param otherVertex the other vertex
	 * @return true, if is connected to
	 */
	public boolean isConnectedTo(Vertex otherVertex) {
		return this.getNeighbors().contains(otherVertex);
	}
	
	/**
	 * Gets the connected vertex.
	 *
	 * @param connectingArc the connecting arc
	 * @return the connected vertex
	 */
	public Vertex getConnectedVertex(Arc connectingArc) {
		Vertex other = connectingArc.getStartVertex();
		if (other.equals(this)) {
			other = connectingArc.getEndVertex();
		}
		if (!connectingArc.isConnecting(this, other)) {
			Log.e("The arc given is not connected to this vertex,"
					+ " cannot retrieve other connected vertex over this arc");
			return null;
		}
		return other;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "VertexID:" + id_ + " - name:" + name_;
		return s;
	}
	
	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean equals(Vertex other) {
		if (null == other) return false;
		boolean sameClass = this.getClass().equals(other.getClass());
		boolean sameId = this.getId() == other.getId() && this.getName().equals(other.getName());
		return sameClass && sameId;
	}
	
}
