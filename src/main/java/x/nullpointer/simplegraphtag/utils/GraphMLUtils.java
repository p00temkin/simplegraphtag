package x.nullpointer.simplegraphtag.utils;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import x.nullpointer.simplegraphtag.graph.FilteredGraph;
import x.nullpointer.simplegraphtag.graph.GraphEdge;
import x.nullpointer.simplegraphtag.graph.GraphNode;
import x.nullpointer.simplegraphtag.graph.GraphNodeGroup;
import x.nullpointer.simplegraphtag.graphml.GraphMLNode;

public class GraphMLUtils {

    // Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphMLUtils.class);

    static 	public String toGraphML(GraphEdge e) {
        return create_graphml_from_edgename_nodes_and_color(e.getName(), e.getFrom(), e.getTo(), e.getColor());
    }

    public static String toGraphML(GraphNode n) {
        return GraphMLUtils.create_graphml_from_node(n);
    }

    public static String toGraphML(GraphNodeGroup g) {
        return create_graphml_from_groupname_and_nodes(g.getName(), g.getNodes());
    }

    static public String graphMLDocument_INIT() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + 
                "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns/graphml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/graphml http://www.yworks.com/xml/schema/graphml/1.0/ygraphml.xsd\">" + 
                "<key for=\"node\" id=\"d0\" yfiles.type=\"nodegraphics\"/>" + 
                "<key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d1\"/>" + 
                "<key for=\"edge\" id=\"d2\" yfiles.type=\"edgegraphics\"/>" + 
                "<key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d3\"/>" + 
                "<key for=\"graphml\" id=\"d4\" yfiles.type=\"resources\"/>" + 
                "<graph edgedefault=\"directed\" id=\"G\" parse.edges=\"1\" parse.nodes=\"2\" parse.order=\"free\">" + 
                "\n";
    }

    static public String graphMLDocument_FIN() {
        return "</graph>" + 
                "<data key=\"d4\">" + 
                "<y:Resources/>" + 
                "</data>" + 
                "</graphml>" + 
                "\n";
    }

    static public String define_group_FIN() {
        return "</graph>" + 
                "</node>" + 
                "\n";
    }

    static public String define_group_INIT(String name) {
        return define_group_INIT(name, name, name);
    }

    static public String define_group_INIT(String id, String open, String closed) {

        String init = "" +
                "<node id=\"" + id + "\" yfiles.foldertype=\"group\">" + 
                "<data key=\"d0\">" + 
                "<y:ProxyAutoBoundsNode>" + 
                "<y:Realizers active=\"0\">" + 
                "\n";

        String expanded = "" +
                "<y:GroupNode>" + 
                "<y:Geometry height=\"492.74896930650016\" width=\"1712.9889874443409\" x=\"-1081.415413376436\" y=\"-328.6931237142296\"/>" +
                "<y:Fill color=\"#FFFFFF\" transparent=\"false\"/>" + 
                "<y:BorderStyle color=\"#000000\" type=\"line\" width=\"2.0\"/>" + 
                "<y:NodeLabel alignment=\"center\" autoSizePolicy=\"node_width\" backgroundColor=\"#000000\" fontFamily=\"Dialog\" fontSize=\"50\" fontStyle=\"plain\" hasLineColor=\"false\" height=\"65.2548828125\" modelName=\"internal\" modelPosition=\"t\" textColor=\"#FFFFFF\" visible=\"true\" width=\"1712.9889874443409\" x=\"0.0\" y=\"0.0\">" + open + "</y:NodeLabel>" + 
                "<y:Shape type=\"roundrectangle\"/>" + 
                "<y:State closed=\"false\" innerGraphDisplayEnabled=\"false\"/>" + 
                "<y:Insets bottom=\"15\" left=\"15\" right=\"15\" top=\"15\"/>" + 
                "<y:BorderInsets bottom=\"0\" left=\"0\" right=\"0\" top=\"12\"/>" + 
                "</y:GroupNode>" + 
                "\n";

        String tight = "" + 
                "<y:GroupNode>" + 
                "<y:Geometry height=\"70.00045863274318\" width=\"214.76679115269712\" x=\"-1081.415413376436\" y=\"-328.6931237142296\"/>" + 
                "<y:Fill color=\"#FFFFFF\" transparent=\"false\"/>" + 
                "<y:BorderStyle color=\"#000000\" type=\"line\" width=\"2.0\"/>" + 
                "<y:NodeLabel alignment=\"center\" autoSizePolicy=\"node_width\" backgroundColor=\"#000000\" fontFamily=\"Dialog\" fontSize=\"30\" fontStyle=\"plain\" hasLineColor=\"false\" height=\"40.7529296875\" modelName=\"internal\" modelPosition=\"t\" textColor=\"#FFFFFF\" visible=\"true\" width=\"214.76679115269712\" x=\"0.0\" y=\"0.0\">" + closed + "</y:NodeLabel>" + 
                "<y:Shape type=\"roundrectangle\"/>" + 
                "<y:State closed=\"true\" innerGraphDisplayEnabled=\"false\"/>" + 
                "<y:Insets bottom=\"15\" left=\"15\" right=\"15\" top=\"15\"/>" + 
                "<y:BorderInsets bottom=\"0\" left=\"0\" right=\"0\" top=\"0\"/>" + 
                "</y:GroupNode>" + 
                "\n";

        String fin = "" +
                "</y:Realizers>" + 
                "</y:ProxyAutoBoundsNode>" + 
                "</data>" + 
                "\n";

        String datainit = "" + 
                "<data key=\"d1\"/>" + 
                "<graph edgedefault=\"directed\" id=\"" + id + ":\">" + 
                "\n";

        return init + expanded + tight + fin + datainit;

    }

    static public String create_graphml_from_GraphML_node(GraphMLNode node) {
        return node_text(node, 30, 18);
    }

    static public String create_graphml_from_node(GraphNode node) {
        return node_text(node, 30, 18);
    }

    static public String define_node(GraphNode node) {
        return node_text(node, 30, 18);
    }

    static public String node_text(GraphNode node, int nodesize, int fontsize) {
        String colorCode = "#CCCCFF"; // default blue
        return "" +
        "<node id=\"" + node.getId() + "\">" +
        "<data key=\"d0\">" +
        "<y:ShapeNode>" + 
        "<y:Geometry height=\"" + nodesize + "\" width=\"" + nodesize + "\" x=\"293.0\" y=\"279.0\"/>" + 
        "<y:Fill color=\"" + colorCode + "\" transparent=\"false\"/>" +
        "<y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>" +
        "<y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"" + fontsize + "\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"17.962890625\" modelName=\"sandwich\" modelPosition=\"s\" textColor=\"#000000\" visible=\"true\" width=\"56.0\" x=\"-13.0\" y=\"6.0185546875\">" + node.getDisplayname() + "</y:NodeLabel>" + 
        "<y:Shape type=\"ellipse\"/>" + 
        "</y:ShapeNode>" + 
        "</data>" + 
        "<data key=\"d1\"/>" +
        "</node>" +
        "\n";
    }

    static public String node_text(GraphMLNode node, int nodesize, int fontsize) {
        String colorCode = "#CCCCFF"; // default blue
        return "" +
        "<node id=\"" + node.getId() + "\">" +
        "<data key=\"d0\">" +
        "<y:ShapeNode>" + 
        "<y:Geometry height=\"" + nodesize + "\" width=\"" + nodesize + "\" x=\"293.0\" y=\"279.0\"/>" + 
        "<y:Fill color=\"" + colorCode + "\" transparent=\"false\"/>" +
        "<y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>" +
        "<y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"" + fontsize + "\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"17.962890625\" modelName=\"sandwich\" modelPosition=\"s\" textColor=\"#000000\" visible=\"true\" width=\"56.0\" x=\"-13.0\" y=\"6.0185546875\">" + node.getDisplayname() + "</y:NodeLabel>" + 
        "<y:Shape type=\"ellipse\"/>" + 
        "</y:ShapeNode>" + 
        "</data>" + 
        "<data key=\"d1\"/>" +
        "</node>" +
        "\n";
    }

    static public String create_graphml_from_edgename_nodes_and_color(String edgeName, String from, String to, String colorName) {
        return define_edge(from + "_" + to + "_" + edgeName, edgeName, from, to, colorName);
    }

    static public String define_edge(String id, String edgeName, String from, String to, String colorName) {
        String colorCode = "#000000"; // default to black
        String lineinfo = "<y:LineStyle color=\"" + colorCode + "\" type=\"line\" width=\"1.0\"/>";
        if ("none".equals(edgeName)) {
            return "" +
                    "<edge id=\"" + id + "\" source=\"" + from + "\" target=\"" + to + "\">" +
                    "<data key=\"d2\">" + 
                    "<y:PolyLineEdge>" + 
                    "<y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>" + 
                    lineinfo +
                    "<y:Arrows source=\"none\" target=\"delta\"/>" + 
                    "<y:BendStyle smoothed=\"false\"/>" + 
                    "</y:PolyLineEdge>" +
                    "</data>" + 
                    "<data key=\"d3\"/>" + 
                    "</edge>" + 
                    "\n";
        } else {
            return "" +
                    "<edge id=\"" + id + "\" source=\"" + from + "\" target=\"" + to + "\">" + 
                    "<data key=\"d2\">" + 
                    "<y:PolyLineEdge>" + 
                    "<y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>" + 
                    lineinfo +
                    "<y:Arrows source=\"none\" target=\"delta\"/>" + 
                    "<y:EdgeLabel alignment=\"center\" backgroundColor=\"#FFFFFF\" distance=\"2.0\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" height=\"17.962890625\" lineColor=\"#000000\" modelName=\"three_center\" modelPosition=\"center\" preferredPlacement=\"anywhere\" ratio=\"0.5\" textColor=\"#000000\" visible=\"true\" width=\"51.0\" x=\"44.17374127643819\" y=\"-118.76003087645796\">" + edgeName + "</y:EdgeLabel>" + 
                    "<y:BendStyle smoothed=\"false\"/>" + 
                    "</y:PolyLineEdge>" + 
                    "</data>" +
                    "<data key=\"d3\"/>" + 
                    "</edge>" + 
                    "\n";
        }

    }

    public static String create_graphml_from_groupname_and_GraphML_nodes(String groupName, ArrayList<GraphMLNode> nodes) {
        StringBuffer sb = new StringBuffer();
        sb.append(define_group_INIT(groupName));
        for (GraphMLNode node: nodes) {
            sb.append(node.toGraphML());
        }
        sb.append(define_group_FIN());
        return sb.toString();
    }

    public static String create_graphml_from_groupname_and_nodes(String groupName, ArrayList<GraphNode> nodes) {
        StringBuffer sb = new StringBuffer();
        sb.append(define_group_INIT(groupName));
        for (GraphNode node: nodes) {
            sb.append(GraphMLUtils.toGraphML(node));
        }
        sb.append(define_group_FIN());
        return sb.toString();
    }

    public static String renderOutputFromFilteredGraph(FilteredGraph fg) {
        StringBuffer sb = new StringBuffer();
        sb.append(GraphMLUtils.graphMLDocument_INIT());

        // Add all groups and nodes
        for (String groupName: fg.getNodegroups().keySet()) {
            GraphNodeGroup g = fg.getNodegroups().get(groupName);
            sb.append(GraphMLUtils.toGraphML(g));

            // Output group stats
            LOGGER.info("OUTPUT groupName: " + groupName + " nrNodes: " + g.getNodes().size());
        }

        // Add  edges
        for (String edgeKey: fg.getEdges().keySet()) {
            GraphEdge e = fg.getEdges().get(edgeKey);
            sb.append(GraphMLUtils.toGraphML(e));
        }

        sb.append(GraphMLUtils.graphMLDocument_FIN());
        return sb.toString();
    }

}
