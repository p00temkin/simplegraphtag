package x.nullpointer.simplegraphtag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import x.nullpointer.simplegraphtag.graph.FilteredGraph;
import x.nullpointer.simplegraphtag.graph.Graph;
import x.nullpointer.simplegraphtag.graph.GraphNodeGroup;
import x.nullpointer.simplegraphtag.utils.FilesUtils;
import x.nullpointer.simplegraphtag.utils.GraphMLUtils;

public class GraphTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(GraphTests.class);

	@Test
	public void testScenario1() {

		LOGGER.info("init");
		String scenarioName = "Scenario1";

		Graph g1 = new Graph();

		// node1 tags
		g1.addNodeWithTag("node1", "g_HIGH");

		// node2 tags (dup, should cause new tag to be created)
		g1.addNodeWithTag("node2", "g_HIGH");
		g1.addNodeWithTag("node2", "g_MEDIUM");

		// node3 tags
		g1.addNodeWithTag("node3", "g_MEDIUM");

		// node4 tags
		g1.addNodeWithTag("node4", "g_LOW");

		// node5 tags
		g1.addNodeWithTag("node5", "g_LOW");

		// Add the edges
		g1.addEdge("edge_n1n2", "node1", "node2");
		g1.addEdge("edge_n1n4", "node1", "node4");
		g1.addEdge("edge_n1n4", "node1", "node4");
		g1.addEdge("edge_n3n4", "node3", "node4");
		g1.addEdge("edge_n3n4_v2", "node3", "node4");
		g1.addEdge("edge_n1n5", "node1", "node5");

		// never remove any edges towards this group
		g1.addWhitelistOnTagMatchExists("g_HIGH"); 

		// remove nodes which match this name (no match)
		g1.addFilterOnExactTagComboMatch("MOJO");

		// tag filters, remove all nodes which has a "NOTAG" tag
		g1.addFilterOnTagMatchExists("NOTAG");

		// add some nodes which end up in the "NOTAG" group
		g1.addEdge("edge_n999n4", "node999", "node4"); // will be filtered due to addFilterOnTagMatchExists()
		g1.addEdge("edge_n998n1", "node998", "node1"); // will NOT be filtered due to addWhitelistOnTagMatchExists()

		// Filter the graph
		FilteredGraph fg1 = g1.filterGraph();

		// Count total nr nodes
		int nrNodes = 0;
		for (String nodeGroupName: fg1.getNodegroups().keySet()) {
			GraphNodeGroup nodeGroup = fg1.getNodegroups().get(nodeGroupName);
			nrNodes = nrNodes + nodeGroup.getNodes().size();
		}

		LOGGER.info(scenarioName + " nr KEPT nodegroups: " + fg1.getNodegroups().size());
		LOGGER.info(scenarioName + " nr KEPT nodes: " + nrNodes);
		LOGGER.info(scenarioName + " nr KEPT edges: " + fg1.getEdgesKeptCount());
		LOGGER.info(scenarioName + " nr FILTERED edges: " + fg1.getEdgesFilteredCount());

		// Asserts
		assertEquals(scenarioName + " nr KEPT nodegroups: ", 5, fg1.getNodegroups().size());
		assertEquals(scenarioName + " nr KEPT nodes: ", 6, nrNodes);
		assertEquals(scenarioName + " nr KEPT edges: ", 6, fg1.getEdgesKeptCount());
		assertEquals(scenarioName + " nr FILTERED edges: ", 1, fg1.getEdgesFilteredCount());

		// GraphML specific output
		boolean outputGraphMLFile = true;
		if (outputGraphMLFile) {
			FilesUtils.writeToFileUNIXNoException(GraphMLUtils.renderOutputFromFilteredGraph(fg1), "Scenario1.graphml");
		}

	}
}
