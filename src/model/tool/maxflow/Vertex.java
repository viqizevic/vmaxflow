package model.tool.maxflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.Log;

/**
 * The Class Vertex.
 */
public class Vertex {
	
	/** The default id. */
	protected static final int DEFAULT_VERTEX_ID = -1;
	
	/** The id. */
	private int id_;
	
	/** The name. */
	private String name_;
	
	/** The ingoing arcs_. */
	private HashMap<String, Arc> ingoingArcs_;

	/** The outgoing arcs_. */
	private HashMap<String, Arc> outgoingArcs_;
	
	/**
	 * Instantiates a new vertex.
	 *
	 * @param name the name
	 */
	public Vertex(String name) {
		id_ = DEFAULT_VERTEX_ID;
		this.name_ = name;
		ingoingArcs_ = new HashMap<String, Arc>();
		outgoingArcs_ = new HashMap<String, Arc>();
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
		if (DEFAULT_VERTEX_ID != id_) {
			Log.e("ID changed already. Cannot change anymore. ID=" + id_);
			return false;
		}
		id_ = newId;
		return true;
	}

	/**
	 * Adds the ingoing arc.
	 *
	 * @param arc the arc
	 */
	public void addIngoingArc(Arc arc) {
		if (arc == null) {
			return;
		}
		ingoingArcs_.put(arc.getName(), arc);
	}
	
	/**
	 * Adds the outgoing arc.
	 *
	 * @param arc the arc
	 */
	public void addOutgoingArc(Arc arc) {
		if (arc == null) {
			return;
		}
		outgoingArcs_.put(arc.getName(), arc);
	}
	
	/**
	 * Gets the ingoing arcs.
	 *
	 * @return the arcs
	 */
	public Collection<Arc> getIngoingArcs() {
		return ingoingArcs_.values();
	}
	
	/**
	 * Gets the outgoing arcs.
	 *
	 * @return the outgoing arcs
	 */
	public Collection<Arc> getOutgoingArcs() {
		return outgoingArcs_.values();
	}
	
	/**
	 * Removes the arc.
	 *
	 * @param name the name
	 */
	public void removeArc(String name) {
		removeIngoingArc(name);
		removeOutgoingArc(name);
	}
	
	/**
	 * Removes the ingoing arc.
	 *
	 * @param name the name
	 * @return the arc
	 */
	public Arc removeIngoingArc(String name) {
		return ingoingArcs_.remove(name);
	}
	
	/**
	 * Removes the outgoing arc.
	 *
	 * @param name the name
	 * @return the arc
	 */
	public Arc removeOutgoingArc(String name) {
		return outgoingArcs_.remove(name);
	}
	
	/**
	 * Removes the arcs.
	 *
	 * @return the collection
	 */
	public Collection<Arc> removeArcs() {
		Vector<Arc> removed = new Vector<Arc>();
		removed.addAll(getIngoingArcs());
		removed.addAll(getOutgoingArcs());
		for (Arc t : removed) {
			removeArc(t.getName());
		}
		return removed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "Vertex - id:" + id_ + " - name:" + name_;
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
