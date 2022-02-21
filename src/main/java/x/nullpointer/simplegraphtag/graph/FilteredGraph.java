package x.nullpointer.simplegraphtag.graph;

import java.util.HashMap;

public class FilteredGraph {

	private HashMap<String,GraphNodeGroup> nodegroups = new HashMap<String,GraphNodeGroup>();
	private HashMap<String, GraphEdge> edges = new HashMap<String, GraphEdge>();
	
	private int edgesKeptCount = 0;
	private int edgesFilteredCount = 0;
	
	public FilteredGraph() {
		super();
	}

	public FilteredGraph(HashMap<String, GraphNodeGroup> nodegroups, HashMap<String, GraphEdge> edges, final int edgesKeptCount, final int edgesFilteredCount) {
		super();
		this.nodegroups = nodegroups;
		this.edges = edges;
		this.edgesKeptCount = edgesKeptCount;
		this.edgesFilteredCount = edgesFilteredCount;
	}

	public HashMap<String, GraphNodeGroup> getNodegroups() {
		return nodegroups;
	}

	public void setNodegroups(HashMap<String, GraphNodeGroup> nodegroups) {
		this.nodegroups = nodegroups;
	}

	public HashMap<String, GraphEdge> getEdges() {
		return edges;
	}

	public void setEdges(HashMap<String, GraphEdge> edges) {
		this.edges = edges;
	}

	public int getEdgesKeptCount() {
		return edgesKeptCount;
	}

	public void setEdgesKeptCount(int edgesKeptCount) {
		this.edgesKeptCount = edgesKeptCount;
	}

	public int getEdgesFilteredCount() {
		return edgesFilteredCount;
	}

	public void setEdgesFilteredCount(int edgesFilteredCount) {
		this.edgesFilteredCount = edgesFilteredCount;
	}
	
}
