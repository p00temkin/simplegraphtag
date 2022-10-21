package x.nullpointer.simplegraphtag.graphml;

import java.util.ArrayList;

import x.nullpointer.simplegraphtag.utils.GraphMLUtils;

public class GraphMLGroup {
    private String name;
    private ArrayList<GraphMLNode> nodes = new ArrayList<GraphMLNode>();

    public GraphMLGroup() {
    }
    
	public GraphMLGroup(String name) {
		super();
		this.name = name;
	}
	
	public GraphMLGroup(String name, ArrayList<GraphMLNode> nodes) {
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

	public ArrayList<GraphMLNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GraphMLNode> nodes) {
		this.nodes = nodes;
	}

	public String toGraphML() {
		return GraphMLUtils.create_graphml_from_groupname_and_GraphML_nodes(this.getName(), this.getNodes());
	}

	public void addNode(GraphMLNode n) {
		this.nodes.add(n);
	}

}