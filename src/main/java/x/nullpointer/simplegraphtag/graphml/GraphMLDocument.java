package x.nullpointer.simplegraphtag.graphml;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import x.nullpointer.simplegraphtag.utils.GraphMLUtils;
import x.nullpointer.simplegraphtag.utils.SystemUtils;

public class GraphMLDocument {

	private static final Logger LOGGER = LoggerFactory.getLogger(GraphMLDocument.class);

	// nodeIDstring, GraphMLNode
	private HashMap<String,GraphMLNode> nodeIndex = new HashMap<String,GraphMLNode>();

	// tagstring, GraphTag
	private HashMap<String,GraphTag> tagIndex = new HashMap<String,GraphTag>();

	// nodeIDstring, <tagString, true>
	private HashMap<String,HashMap<String,Boolean>> nodeTags = new HashMap<String, HashMap<String,Boolean>>();

	// tagstring, <nodeString, true>
	private HashMap<String,HashMap<String,Boolean>> tagNodes = new HashMap<String, HashMap<String,Boolean>>();

	// internal var
	private HashMap<String,Boolean> nodeFiltered = new HashMap<String,Boolean>();

	// edgestring, GraphMLEdge
	private HashMap<String, GraphMLEdge> edges = new HashMap<String, GraphMLEdge>();

	// tag/tagcombo filters/whitelists
	private HashMap<String, Boolean> filterOnExactTagComboMatch = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> filterOnTagMatchExists = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> whitelistOnTagMatchExists = new HashMap<String, Boolean>();

	// nodeIDstring, <nodeIDstring, true>
	private HashMap<String, HashMap<String, Boolean>> outBoundNodesForNode = new HashMap<String, HashMap<String, Boolean>>();
	private HashMap<String, HashMap<String, Boolean>> inBoundNodesForNode = new HashMap<String, HashMap<String, Boolean>>();

	public GraphMLDocument() {
	}

	public String toGraphML() {
		return toGraphML(false);
	}

	@SuppressWarnings("unused")
	public String toGraphML(boolean onlyIncludeDirectlyWhitelisted) {
		StringBuffer sb = new StringBuffer();
		sb.append(GraphMLUtils.graphMLDocument_INIT());

		// Determine the nodegroups
		HashMap<String,GraphMLGroup> nodegroups = new HashMap<String,GraphMLGroup>();

		// Generate all unique tag combos for nodes and create associated groups
		int nrNodes = nodeIndex.keySet().size();
		int currentCounter = 1;
		for (String nodeID: nodeIndex.keySet()) {
			GraphMLNode node = nodeIndex.get(nodeID);

			LOGGER.debug("Processing node: " + currentCounter + "/" + nrNodes);
			currentCounter++;

			HashMap<String, Boolean> tags = nodeTags.get(nodeID);
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

			// Apply filter unless whitelisted
			boolean filtered = false;
			if (!exactTagWhitelistMatch) {
				if (false ||
						(null != filterOnExactTagComboMatch.get(tagsCombo)) ||
						exactTagFilterMatch ||
						onlyIncludeDirectlyWhitelisted ||
						false) {
					
					// we have a filter candidate

					// Walk through all edges to make sure we should filter
					boolean whitelisted = false;
					String whiteNode = "N/A";
					String whiteTag = "N/A";

					// outBoundNodesForNode
					HashMap<String, Boolean> outboundnodes = outBoundNodesForNode.get(nodeID);
					if (null != outboundnodes) {
						for (String outboundnode: outboundnodes.keySet()) {
							// evaluate if any target nodes should whitelist this one
							HashMap<String, Boolean> targetTags = nodeTags.get(outboundnode);
							for (String targetTag: targetTags.keySet()) {
								if (null != whitelistOnTagMatchExists.get(targetTag)) {
									whiteNode = outboundnode;
									whiteTag = targetTag;
									whitelisted = true;
								}
							}
						}
					}

					// inBoundNodesForNode
					HashMap<String, Boolean> inboundnodes = inBoundNodesForNode.get(nodeID);
					if (null != inboundnodes) {
						for (String inboundnode: inboundnodes.keySet()) {
							// evaluate if any source nodes should whitelist this one
							HashMap<String, Boolean> sourceTags = nodeTags.get(inboundnode);
							for (String sourceTag: sourceTags.keySet()) {
								if (null != whitelistOnTagMatchExists.get(sourceTag)) {
									whiteNode = inboundnode;
									whiteTag = sourceTag;
									whitelisted = true;
								}
							}
						}
					}

					if (whitelisted) {
						LOGGER.debug("Was about to filter " + nodeID + " (tagsCombo: " + tagsCombo + "), but communication with " + whiteNode + " changed my mind, since it has tag " + whiteTag);
						filtered = false;
					} else {
						filtered = true;
					}
				}
			}

			if (filtered) {
				nodeFiltered.put(nodeID, true);
			} else {
				GraphMLGroup gmlGroup = nodegroups.get(tagsCombo);
				if (null == gmlGroup) gmlGroup = new GraphMLGroup(tagsCombo);
				gmlGroup.addNode(node);
				nodegroups.put(tagsCombo, gmlGroup);
			}

		}

		// Add all groups and nodes
		for (String groupName: nodegroups.keySet()) {
			GraphMLGroup g = nodegroups.get(groupName);
			sb.append(g.toGraphML());

			// Output group stats
			System.out.println("groupName: " + groupName + " nrNodes: " + g.getNodes().size());
		}

		// Add all non-filtered edges
		int edgesFilteredCount = 0;
		int edgesKeptCount = 0;
		int nrEdges = edges.keySet().size();
		for (String edgeKey: edges.keySet()) {
			GraphMLEdge e = edges.get(edgeKey);

			if (true &&
					(null == nodeFiltered.get(e.getFrom()) && // src should not be filtered
					(null == nodeFiltered.get(e.getTo()))) && // dest should not be filtered
					true) {
				sb.append(e.toGraphML());
				edgesKeptCount++;
				LOGGER.debug("Added an EDGE from: " + e.getFrom() + " to: " + e.getTo());
			}  else {
				LOGGER.debug("Edge filtered from: " + e.getFrom() + " to: " + e.getTo());
				edgesFilteredCount++;
			}
		}
		LOGGER.info("edgesKeptCount: " + edgesKeptCount);
		LOGGER.info("edgesFilteredCount: " + edgesFilteredCount);

		sb.append(GraphMLUtils.graphMLDocument_FIN());

		return sb.toString();
	}

	public void addNodeWithTag(String nodeID, String tagName) {
		this.addNodeWithTag(new GraphMLNode(nodeID), new GraphTag(tagName));
	}

	public void addNodeWithTag(GraphMLNode node, GraphTag t) {

		LOGGER.debug("Adding node " + node.getId() + " with tag " + t.getTagname());
		if ("N/A".contentEquals(t.getTagname())) {
			LOGGER.error("Invalid tag: " + t.getTagname());
			SystemUtils.halt();
		}

		// Update the tag index
		GraphTag gt = this.tagIndex.get(t.getTagname());
		if (null == gt) {
			this.tagIndex.put(t.getTagname(), t);
		}

		// Assign the tag with the node id
		HashMap<String, Boolean> existingTags = this.nodeTags.get(node.getId());
		if (null == existingTags) existingTags = new HashMap<String, Boolean>();
		existingTags.put(t.getTagname(), true);
		this.nodeTags.put(node.getId(), existingTags);
		LOGGER.debug("just associated .. node.getId(): " + node.getId() + " with t.getTagname(): " + t.getTagname());

		// Assign the nodeid with the tag
		HashMap<String, Boolean> existingNodes = this.tagNodes.get(t.getTagname());
		if (null == existingNodes) existingNodes = new HashMap<String, Boolean>();
		existingNodes.put(node.getId(), true);
		this.tagNodes.put(t.getTagname(), existingNodes);
		LOGGER.debug("just associated .. node.getId(): " + node.getId() + " with t.getTagname(): " + t.getTagname());

		// Add the node
		GraphMLNode existingNode = this.nodeIndex.get(node.getId());
		if (null == existingNode) {
			this.nodeIndex.put(node.getId(), node);
		}

	}

	public void addEdge(String name, String from, String to) {
		GraphMLEdge e = new GraphMLEdge(name, from, to);

		// Make sure src exists
		if (!hasNodeWithTag(e.getFrom())) {
			addNodeWithTag(new GraphMLNode(e.getFrom()), new GraphTag("NOTAG"));
		}

		// Make sure dst exists
		if (!hasNodeWithTag(e.getTo())) {
			addNodeWithTag(new GraphMLNode(e.getTo()), new GraphTag("NOTAG"));
		}

		// All edges
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