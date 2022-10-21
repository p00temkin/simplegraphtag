package x.nullpointer.simplegraphtag.graph;

import java.util.HashMap;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

	private HashMap<String,GraphNode> nodeIndex = new HashMap<String,GraphNode>();
	private HashMap<String,GraphTag> tagIndex = new HashMap<String,GraphTag>();
	private HashMap<String,HashMap<String,Boolean>> nodeTags = new HashMap<String, HashMap<String,Boolean>>();
	private HashMap<String,HashMap<String,Boolean>> tagNodes = new HashMap<String, HashMap<String,Boolean>>();
	private HashMap<String,Boolean> nodeFiltered = new HashMap<String,Boolean>();
	private HashMap<String, GraphEdge> edges = new HashMap<String, GraphEdge>();

	// tag/tagcombo filters/whitelists
	private HashMap<String, Boolean> filterOnExactTagComboMatch = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> filterOnTagMatchExists = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> whitelistOnTagMatchExists = new HashMap<String, Boolean>();

	private HashMap<String, HashMap<String, Boolean>> outBoundNodesForNode = new HashMap<String, HashMap<String, Boolean>>();
	private HashMap<String, HashMap<String, Boolean>> inBoundNodesForNode = new HashMap<String, HashMap<String, Boolean>>();

	public Graph() {
	}

	public FilteredGraph filterGraph() {
		return filterGraph(false);
	}

	public FilteredGraph filterGraph(boolean onlyIncludeDirectlyWhitelisted) {

		// Determine the nodegroups
		HashMap<String,GraphNodeGroup> nodegroups = new HashMap<String,GraphNodeGroup>();
		HashMap<String, GraphEdge> edges = new HashMap<String, GraphEdge>();

		// Generate all unique tag combos for nodes and create associated groups
		for (String nodeID: this.nodeIndex.keySet()) {
			GraphNode node = this.nodeIndex.get(nodeID);

			HashMap<String, Boolean> tags = this.nodeTags.get(nodeID);
			String tagsCombo = Joiner.on("_").skipNulls().join(tags.keySet());

			// Look for exact tag filter match
			boolean exactTagFilterMatch = false;
			for (String tag: tags.keySet()) {
				if (null != this.filterOnTagMatchExists.get(tag)) exactTagFilterMatch = true;
			}

			// Look for exact tag whitelist match
			boolean exactTagWhitelistMatch = false;
			for (String tag: tags.keySet()) {
				if (null != this.whitelistOnTagMatchExists.get(tag)) exactTagWhitelistMatch = true;
			}

			// Apply filter
			boolean filtered = false;
			if (!exactTagWhitelistMatch) {
				if (false ||
						(null != this.filterOnExactTagComboMatch.get(tagsCombo)) ||
						exactTagFilterMatch ||
						onlyIncludeDirectlyWhitelisted ||
						false) {
				    
					// Walk through all edges to check if we should filter
					boolean whitelisted = false;
					String whiteNode = "N/A";
					String whiteTag = "N/A";

					// outBoundNodesForNode
					HashMap<String, Boolean> outboundnodes = this.outBoundNodesForNode.get(nodeID);
					if (null != outboundnodes) {
						for (String outboundnode: outboundnodes.keySet()) {
							// evaluate if any target nodes should whitelist this one
							HashMap<String, Boolean> targetTags = nodeTags.get(outboundnode);
							for (String targetTag: targetTags.keySet()) {
								if (null != this.whitelistOnTagMatchExists.get(targetTag)) {
									whiteNode = outboundnode;
									whiteTag = targetTag;
									whitelisted = true;
								}
							}
						}
					}

					// inBoundNodesForNode
					HashMap<String, Boolean> inboundnodes = this.inBoundNodesForNode.get(nodeID);
					if (null != inboundnodes) {
						for (String inboundnode: inboundnodes.keySet()) {
							// evaluate if any source nodes should whitelist this one
							HashMap<String, Boolean> sourceTags = this.nodeTags.get(inboundnode);
							for (String sourceTag: sourceTags.keySet()) {
								if (null != this.whitelistOnTagMatchExists.get(sourceTag)) {
									whiteNode = inboundnode;
									whiteTag = sourceTag;
									whitelisted = true;
								}
							}
						}
					}

					if (whitelisted) {
						System.out.println("Was about to filter " + nodeID + " (tagsCombo: " + tagsCombo + "), but links with " + whiteNode + " changed my mind, since it has tag " + whiteTag);
						filtered = false;
					} else {
						filtered = true;
					}
				} 
			}

			if (filtered) {
				this.nodeFiltered.put(nodeID, true);
			} else {
				GraphNodeGroup gmlGroup = nodegroups.get(tagsCombo);
				if (null == gmlGroup) gmlGroup = new GraphNodeGroup(tagsCombo);
				gmlGroup.addNode(node);
				nodegroups.put(tagsCombo, gmlGroup);
			}

		}
		
		int edgesFilteredCount = 0;
		int edgesKeptCount = 0;
		for (String edgeKey: this.edges.keySet()) {
			GraphEdge e = this.edges.get(edgeKey);
			if (true &&
					(null == this.nodeFiltered.get(e.getFrom()) && // src should not be filtered
					(null == this.nodeFiltered.get(e.getTo()))) && // dest should not be filtered
					true) {

				edges.put(edgeKey, e);
				edgesKeptCount++;
			}  else {
				edgesFilteredCount++;
			}
		}
		
		return new FilteredGraph(nodegroups, edges, edgesKeptCount, edgesFilteredCount);
	}

	public boolean addNodeWithTag(String nodeID, String tagName) {
		return addNodeWithTag(new GraphNode(nodeID), new GraphTag(tagName));
	}

	public boolean addNodeWithTag(GraphNode node, GraphTag t) {

		boolean isNewNode = false;

		// Update the tag index
		GraphTag gt = tagIndex.get(t.getTagname());
		if (null == gt) {
			tagIndex.put(t.getTagname(), t);
		}

		// Assign the tag with the node id
		HashMap<String, Boolean> existingTags = nodeTags.get(node.getId());
		if (null == existingTags) {
			existingTags = new HashMap<String, Boolean>();
			existingTags.put(t.getTagname(), true);
			nodeTags.put(node.getId(), existingTags);
			isNewNode = true;
		} else {
			existingTags.put(t.getTagname(), true);
			nodeTags.put(node.getId(), existingTags);
		}
		
		// Assign the nodeid with the tag
		HashMap<String, Boolean> existingNodes = this.tagNodes.get(t.getTagname());
		if (null == existingNodes) {
			existingNodes = new HashMap<String, Boolean>();
			existingNodes.put(node.getId(), true);
			this.tagNodes.put(t.getTagname(), existingNodes);
		} else {
			existingNodes.put(node.getId(), true);
			this.tagNodes.put(t.getTagname(), existingNodes);
		}

		// Add the node
		GraphNode existingNode = this.nodeIndex.get(node.getId());
		if (null == existingNode) {
			this.nodeIndex.put(node.getId(), node);
		}
		
		return isNewNode;

	}

	public boolean addEdge(String name, String from, String to) {
		GraphEdge e = new GraphEdge(name, from, to);

		boolean isNewEdge = false;
		
		// Make sure src exists
		if (!hasNodeWithTag(e.getFrom())) {
			addNodeWithTag(new GraphNode(e.getFrom()), new GraphTag("NOTAG"));
		}

		// Make sure dst exists
		if (!hasNodeWithTag(e.getTo())) {
			addNodeWithTag(new GraphNode(e.getTo()), new GraphTag("NOTAG"));
		}

		// Check if edge is new
		if (null == this.edges.get(e.getFrom() + ":::" + e.getTo() + ":::" + e.getName())) {
			isNewEdge = true;
		}
		this.edges.put(e.getFrom() + ":::" + e.getTo() + ":::" + e.getName(), e);

		// outBoundNodesForNode 
		HashMap<String, Boolean> outboundNodes = this.outBoundNodesForNode.get(from);
		if (null == outboundNodes) {
			outboundNodes = new HashMap<String, Boolean>();
		}
		outboundNodes.put(to, true);
		this.outBoundNodesForNode.put(from, outboundNodes);

		// inBoundNodesForNode
		HashMap<String, Boolean> inboundNodes = this.inBoundNodesForNode.get(to);
		if (null == inboundNodes) {
			inboundNodes = new HashMap<String, Boolean>();
		}
		inboundNodes.put(from, true);
		this.inBoundNodesForNode.put(to, inboundNodes);

		return isNewEdge;
	}

	public boolean hasNodeWithTag(String id) {
		if (null != this.nodeIndex.get(id)) return true;
		return false;
	}

	public String getCurrentTagComboForNode(String nodeID) {
		if (null == this.nodeIndex.get(nodeID)) return "UNKNOWN";

		HashMap<String, Boolean> tags = nodeTags.get(nodeID);
		String tagsCombo = Joiner.on("_").skipNulls().join(tags.keySet());

		return tagsCombo;
	}

	public void addFilterOnExactTagComboMatch(String tagCombo) {
		this.filterOnExactTagComboMatch.put(tagCombo, true);
	}

	public void addFilterOnTagMatchExists(String tag) {
		this.filterOnTagMatchExists.put(tag, true);
	}

	public void addWhitelistOnTagMatchExists(String tag) {
		this.whitelistOnTagMatchExists.put(tag, true);
	}
	
	public int getNrNodesWithTag(String tag) {
		HashMap<String, Boolean> existingNodes = this.tagNodes.get(tag);
		if (null == existingNodes) {
			return 0;
		} else {
			return existingNodes.size();
		}
	}

}