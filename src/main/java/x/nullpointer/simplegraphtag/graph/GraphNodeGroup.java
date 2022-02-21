package x.nullpointer.simplegraphtag.graph;

import java.util.ArrayList;

public class GraphNodeGroup {
    private String name;
    private ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();

    public GraphNodeGroup() {
    }
    
	public GraphNodeGroup(String name) {
		super();
		this.name = name;
	}
	
	public GraphNodeGroup(String name, ArrayList<GraphNode> nodes) {
		super();
		this.name = name;
		this.nodes = nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<GraphNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GraphNode> nodes) {
		this.nodes = nodes;
	}

	public void addNode(GraphNode n) {
		this.nodes.add(n);
	}

}