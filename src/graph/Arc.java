package graph;

import util.Log;

/**
 * The Class Arc.
 */
public abstract class Arc {
	
	/** The default id. */
	protected static final int DEFAULT_ARC_ID = -1;
	
	/** The id. */
	private int id_;

	/** The name. */
	private String name_;
	
	/** The start vertex. */
	private Vertex startVertex_;
	
	/** The end vertex. */
	private Vertex endVertex_;
	
	/** The capacity. */
	private double capacity_;
	
	/**
	 * Instantiates a new arc.
	 *
	 * @param name the name
	 * @param startVertex the start vertex
	 * @param endVertex the end vertex
	 */
	public Arc(String name, Vertex startVertex, Vertex endVertex) {
		id_ = DEFAULT_ARC_ID;
		this.name_ = name;
		this.startVertex_ = startVertex;
		this.endVertex_ = endVertex;
		startVertex.addConnectingArc(this);
		endVertex.addConnectingArc(this);
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
		if (DEFAULT_ARC_ID != id_) {
			Log.e("ID changed already. Cannot change anymore. ID=" + id_);
			return false;
		}
		id_ = newId;
		return true;
	}

	/**
	 * Gets the start vertex.
	 *
	 * @return the start vertex
	 */
	public Vertex getStartVertex() {
		return startVertex_;
	}

	/**
	 * Gets the end vertex.
	 *
	 * @return the end vertex
	 */
	public Vertex getEndVertex() {
		return endVertex_;
	}

	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity_;
	}

	/**
	 * Sets the capacity.
	 *
	 * @param capacity the new capacity
	 */
	public void setCapacity(double capacity) {
		this.capacity_ = capacity;
	}

	/**
	 * Checks if is connecting.
	 *
	 * @param u the u
	 * @param v the v
	 * @return true, if is connecting
	 */
	public boolean isConnecting(Vertex u, Vertex v) {
		boolean b1 = startVertex_.equals(u) && endVertex_.equals(v);
		boolean b2 = startVertex_.equals(v) && endVertex_.equals(u);
		return b1 || b2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "ArcID:" + id_ + " - name:" + name_;
		if (startVertex_ != null && endVertex_ != null) {
			s += " - from: " + startVertex_.getName() + " - to:" + endVertex_.getName();
		}
		return s;
	}
}
