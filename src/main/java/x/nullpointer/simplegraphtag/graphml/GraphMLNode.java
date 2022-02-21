package x.nullpointer.simplegraphtag.graphml;

import x.nullpointer.simplegraphtag.utils.GraphMLUtils;

public class GraphMLNode {
	
	private String id; // unique identifier
	
	// varies
    private String displayname; // display name
    private String color = "blue"; // default

    public GraphMLNode() {
    }
    
	public GraphMLNode(String id) {
		super();
		this.id = id;
		this.displayname = id; // initial value
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String toGraphML() {
		return GraphMLUtils.create_graphml_from_GraphML_node(this);
	}

}