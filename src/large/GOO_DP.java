package large;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Tree;
import graph.Vertex;
import small.DPccp;

public class GOO_DP {

	private Graph graph;
	private Set<Vertex> vertices = new HashSet<>();
	private Set<Edge> edges = new HashSet<>();
	private int d = -1;
	private Tree tree = new Tree();
	
	private Set<Vertex> allVs = new HashSet<>();
	private Set<Edge> allEs = new HashSet<>();
	
	private Map<Set<Vertex>, Boolean> flags = new HashMap<>();
//	private Map<Set<Vertex>, Float> cost = new HashMap<>();
	
	public void goo_dp(Graph graph, DPccp ccp, int k) {
		this.graph = graph;
		this.vertices.addAll(graph.getVertices());
		this.edges.addAll(graph.getEdges());
		this.allVs.addAll(graph.getVertices());
		this.allEs.addAll(graph.getEdges());
		greedy();
		System.out.println("greedy:" + tree.getMap());
		reduce(ccp, k);
		System.out.println("reduce:" + tree.getMap());
	}
	
	public void greedy() {
		while (vertices.size() > 1) {
			minCost();
		}
	}
	
	public void minCost() {
		float min = 0;
		Edge min_edge = null;
		for (Edge e : edges) {
			List<Vertex> lst = new ArrayList<>(e.getEndPoints());
			float cost = lst.get(0).getNum() * lst.get(1).getNum() * e.getF() + lst.get(0).getNum() + lst.get(1).getNum();
			if (min == 0 || cost < min) {
				min = cost;
				min_edge = e;
			}
		}
		replace(min_edge, min);
	}
	
	public void replace(Edge e, float cost) {
		List<Vertex> list = new ArrayList<>(e.getEndPoints());
		Vertex v1 = list.get(0);
		Vertex v2 = list.get(1);
		Vertex vertex = new Vertex(d, (int) cost);
		vertex.addComb(v1);
		vertex.addComb(v2);
		d--;
		vertices.remove(v1);
		vertices.remove(v2);
		vertices.add(vertex);
		edges.remove(e);
		List<Edge> r1 = new ArrayList<>();
		List<Edge> r2 = new ArrayList<>();
		for (Edge edge : edges) {
			if (edge.getEndPoints().contains(v1)) {
				r1.add(edge);
				Edge edge2 = edge.replace(v1, vertex);
				r2.add(edge2);
			}
			else if (edge.getEndPoints().contains(v2)) {
				r1.add(edge);
				Edge edge2 = edge.replace(v2, vertex);
				r2.add(edge2);
			}
		}
		edges.removeAll(r1);
		edges.addAll(r2);
		tree.setRoot(vertex);
		tree.addEdge(vertex, v1);
		tree.addEdge(vertex, v2);
	}
	
	public void reduce(DPccp ccp, int k) {
		for (int i = 0; i < 5; i++) {
			List<Vertex> list = new ArrayList<>(tree.getList());
			for (Vertex v : list) {
				if (v.getId() < 0) {
					Tree t = tree.getSubtree(v);
					if (t.getList().size() <= k && tree.getSubtree(tree.getFather(v)).getList().size() > k) {
						Set<Vertex> set = getVfromTree(t);
						if (flags.get(set) == null) {
							flags.put(set, true);
							Tree tr = ccp.ccp(Tree2Graph(t), d);
							if (getCost(tr) < getCost(t)) {
								tree.replaceSubtree(v, tr);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public Set<Vertex> getVfromTree(Tree tree) {
		List<Vertex> list = tree.getList();
		Set<Vertex> set = new HashSet<>();
		for (Vertex v: list) {
			if (v.getId() > 0)
				set.add(v);
		}
		return set;
	}
	
	public Graph Tree2Graph(Tree tree) {
		List<Vertex> list = tree.getList();
		Set<Vertex> set = new HashSet<>();
		Graph graph = new Graph();
		for (Vertex v : allVs) {
			if (list.contains(v)) {
				graph.addVertex(v);
				set.add(v);
			}
		}
		for (Edge e : allEs) {
			if (set.containsAll(e.getEndPoints())) {
				graph.addEdge(e);
			}
		}
		return graph;
	}
	
	public float getCost(Tree tree) {
		tree.getRoot();
		if (tree.getSon(tree.getRoot()).isEmpty())	return 0;
		else {
			List<Vertex> l = new ArrayList<>(tree.getSon(tree.getRoot()));
			Tree t1 = tree.getSubtree(l.get(0));
			Tree t2 = tree.getSubtree(l.get(1));
			return getT(tree) + getCost(t1) + getCost(t2);
		}
	}
	
	public float getT(Tree tree) {
		if (tree.getSon(tree.getRoot()).isEmpty())	return 0;
		else {
			List<Vertex> l = new ArrayList<>(tree.getSon(tree.getRoot()));
			Tree t1 = tree.getSubtree(l.get(0));
			Tree t2 = tree.getSubtree(l.get(1));
			Set<Vertex> s1 = getVfromTree(t1);
			Set<Vertex> s2=  getVfromTree(t2);
			Set<Edge> set = graph.getConnectedEdges(s1, s2);
			float a = 1;
			for (Edge e: set) {
				a *= e.getF();
			}
			return a * getT(t1) * getT(t2);
		}
	}
	
	public static void main(String[] args) {
		Graph graph = new Graph();
		Vertex v1 = new Vertex(1, 10);
		Vertex v2 = new Vertex(2, 100);
		Vertex v3 = new Vertex(3, 100);
		Vertex v4 = new Vertex(4, 100);
		Vertex v5 = new Vertex(5, 18);
		Vertex v6 = new Vertex(6, 10);
		Vertex v7 = new Vertex(7, 20);
		Edge e1 = new Edge(v1, v2, 0.5f);
		Edge e2 = new Edge(v1, v3, 0.25f);
		Edge e3 = new Edge(v1, v4, 0.2f);
		Edge e4 = new Edge(v4, v5, 0.3333f);
		Edge e5 = new Edge(v4, v6, 0.5f);
		Edge e6 = new Edge(v6, v7, 0.1f);
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addVertex(v7);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		graph.addEdge(e6);
		GOO_DP dp = new GOO_DP();
		dp.goo_dp(graph, new DPccp(), 4);
	}
	
}
