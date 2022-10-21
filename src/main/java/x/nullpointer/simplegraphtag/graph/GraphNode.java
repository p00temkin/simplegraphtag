package x.nullpointer.simplegraphtag.graph;

public class GraphNode {
	
	private String id; // unique identifier
    private String displayname; // display name
    private String color = "blue"; // default

    public GraphNode() {
    }
    
	public GraphNode(String id) {
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

}