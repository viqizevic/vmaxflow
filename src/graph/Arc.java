package graph;

import util.Log;

/**
 * The Class Arc.
 */
public abstract class Arc {
	
	/** The default id. */
	private final int defaultId_ = -1;
	
	/** The id. */
	private int id_;

	/** The name. */
	private String name_;
	
	/** The start vertex. */
	private Vertex startVertex_;
	
	/** The end vertex. */
	private Vertex endVertex_;
	
	/** The establishment cost. */
	private double establishmentCost_;
	
	/**
	 * Instantiates a new arc.
	 *
	 * @param name the name
	 * @param startVertex the start vertex
	 * @param endVertex the end vertex
	 */
	public Arc(String name, Vertex startVertex, Vertex endVertex) {
		id_ = defaultId_;
		this.name_ = name;
		setStartVertex(startVertex);
		setEndVertex(endVertex);
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
		if (defaultId_ != id_) {
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
	 * Sets the start vertex.
	 *
	 * @param startVertex the new start vertex
	 */
	public void setStartVertex(Vertex startVertex) {
		this.startVertex_ = startVertex;
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
	 * Sets the end vertex.
	 *
	 * @param endVertex the new end vertex
	 */
	public void setEndVertex(Vertex endVertex) {
		this.endVertex_ = endVertex;
	}

	/**
	 * Gets the establishment cost.
	 *
	 * @return the establishment cost
	 */
	public double getEstablishmentCost() {
		return establishmentCost_;
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
