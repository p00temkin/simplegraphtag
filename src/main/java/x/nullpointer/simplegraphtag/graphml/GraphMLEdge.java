package x.nullpointer.simplegraphtag.graphml;

import x.nullpointer.simplegraphtag.utils.GraphMLUtils;

public class GraphMLEdge {
    private String name;
    private String from;
    private String to;
    private String color = "black";

    public GraphMLEdge() {
    }
    
	public GraphMLEdge(String name, String from, String to) {
		super();
		this.name = name;
		this.from = from;
		this.to = to;
	}

	public GraphMLEdge(String name, String from, String to, String color) {
		super();
		this.name = name;
		this.from = from;
		this.to = to;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String toGraphML() {
		return GraphMLUtils.create_graphml_from_edgename_nodes_and_color(this.getName(), this.getFrom(), this.getTo(), this.getColor());
	}

}