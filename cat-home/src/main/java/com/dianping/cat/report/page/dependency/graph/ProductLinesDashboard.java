package com.dianping.cat.report.page.dependency.graph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dianping.cat.home.dependency.graph.entity.TopologyEdge;
import com.dianping.cat.home.dependency.graph.entity.TopologyNode;
import com.dianping.cat.report.page.JsonBuilder;

public class ProductLinesDashboard {

	private Map<String, List<TopologyNode>> m_productLines = new LinkedHashMap<String, List<TopologyNode>>();

	private List<TopologyEdge> m_edges = new ArrayList<TopologyEdge>();

	private transient Map<String, TopologyNode> m_nodes = new LinkedHashMap<String, TopologyNode>();

	public ProductLinesDashboard addEdge(TopologyEdge edge) {
		m_edges.add(edge);
		return this;
	}

	public ProductLinesDashboard addNode(String productLine, TopologyNode node) {
		List<TopologyNode> nodeList = m_productLines.get(productLine);

		if (nodeList == null) {
			nodeList = new ArrayList<TopologyNode>();
			m_productLines.put(productLine, nodeList);
		}

		nodeList.add(node);
		return this;
	}

	public boolean exsit(TopologyNode node) {
		return m_nodes.containsKey(node.getId());
	}

	public List<TopologyEdge> getEdges() {
		return m_edges;
	}

	public Map<String, List<TopologyNode>> getNodes() {
		return m_productLines;
	}

	public String toJson() {
		String str = new JsonBuilder().toJson(this);

		return str;
	}

}
