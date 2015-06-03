package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LayoutGraph {
	public List<Node> nodes;
	public List<Edge> edges;
	
	public LayoutGraph() {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}
	
	public Node addNode(int x, int y) {
		Node node = new Node(x,y);
		node.parent = this;
		this.nodes.add(node);
		return node;
	}
	
	public Edge addEdge(Node node1, Node node2) {
		if (!this.nodes.contains(node1) || !this.nodes.contains(node2)) {
			return null;
		}
		
		Edge edge = new Edge(node1, node2);
		this.edges.add(edge);
		return edge;
	}
	
	public void removeNode(Node node) {
		if (!this.nodes.contains(node)) {
			return;
		}
		
		for (Edge e : node.edges.values()) {
			//this.removeEdge(e);
			
			if (e.node1 == node) {
				e.node2.edges.remove(node);
			} else {
				e.node1.edges.remove(node);
			}
			this.edges.remove(e);
		}
		node.edges.clear();
		
		this.nodes.remove(node);
	}
	
	public void removeEdge(Edge edge) {
		if (!this.edges.contains(edge)) {
			return;
		}
		
		this.edges.remove(edge);
		edge.node1.edges.remove(edge.node2);
		edge.node2.edges.remove(edge.node1);
	}
	
	public void tree(Node start, Random rand) {
		if (!this.nodes.contains(start)) {
			return;
		}
		
		for (Node n : this.nodes) {
			n.visited = false;
			n.depth = 0;
		}
		for (Edge e : this.edges) {
			e.active = false;
		}
		
		List<Node> search = new ArrayList<Node>();
		search.add(start);
		start.visited = true;
		
		while(!search.isEmpty()) {
			Node node = search.remove(rand.nextInt(search.size()));
			for (Node n : node.neighbours) {
				if (!n.visited) {
					n.visited = true;
					search.add(n);
					node.edges.get(n).active = true;
					n.depth = node.depth +1;
				}
			}
		}
	}
	
	public void repopulateEdges(double fraction, int maxdepthdiff, Random rand) {
		for (Edge e : this.edges) {
			if (e.node1.visited && e.node2.visited) {
				int diff = Math.abs(e.node1.depth - e.node2.depth);
				
				if (diff <= maxdepthdiff && rand.nextDouble() <= fraction) {
					e.active = true;
				}
			}
		}
	}
	
	public static class Node {
		LayoutGraph parent;
		Map<Node, Edge> edges;
		List<Node> neighbours;
		int x;
		int y;
		boolean visited = false;
		int depth = 0;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
			
			this.edges = new HashMap<Node, Edge>();
			this.neighbours = new ArrayList<Node>();
		}
	}
	
	public static class Edge {
		Node node1;
		Node node2;
		boolean active = true;
		
		public Edge(Node node1, Node node2) {
			this.node1 = node1;
			this.node2 = node2;
			
			this.node1.edges.put(node2, this);
			this.node2.edges.put(node1, this);
			this.node1.neighbours.add(this.node2);
			this.node2.neighbours.add(this.node1);
		}
	}
}
