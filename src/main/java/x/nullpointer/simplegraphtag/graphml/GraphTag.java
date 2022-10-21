package x.nullpointer.simplegraphtag.graphml;

public class GraphTag {

	private String tagname = "N/A";
	private int priority = 0;

	public GraphTag(String tagname, int priority) {
		super();
		this.tagname = tagname;
		this.priority = priority;
	}
	
	public GraphTag(String tagname) {
		super();
		this.tagname = tagname;
		this.priority = 5;
	}

	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	
}
