package small;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class Small_Subgraph_Num {
	
	public Graph generateGraph() {
		Graph graph = new Graph();
		Vertex v1 = new Vertex(1, 10);
		Vertex v2 = new Vertex(2, 10);
		Vertex v3 = new Vertex(3, 10);
		Vertex v4 = new Vertex(4, 10);
		Vertex v5 = new Vertex(5, 10);
		Vertex v6 = new Vertex(6, 10);
		Edge e1 = new Edge(v1, v2, 0.1f);
		Edge e2 = new Edge(v2, v3, 0.1f);
		Edge e3 = new Edge(v3, v4, 0.1f);
		Edge e4 = new Edge(v2, v5, 0.1f);
		Edge e5 = new Edge(v5, v6, 0.1f);
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		return graph;
	}
	
	public Set<Set<Vertex>> getSubSet(Set<Vertex> set) {
		Set<Set<Vertex>> result = new HashSet<>();
		List<Vertex> list = new ArrayList<>(set);
		int length = set.size();
		int num = (length == 0 ? 0 : 1 << length);
		for (int i = 0; i < num; i++) {
			Set<Vertex> sub = new HashSet<>();
			int index = i;
			for (int j = 0; j < length; j++) {
				if ((index & 1) == 1) {
					sub.add(list.get(j));
				}
				index >>= 1;
			}
			result.add(sub);
		}
		return result;
	}
	
	public int countCC(Graph graph, int budget) {
		Set<Vertex> V = graph.getVertices();
		int c = 0;
		for (Vertex vertex : V) {
			if ((c = c+1) > budget)	return c;
			Set<Vertex> B = new HashSet<>();
			for (Vertex vertex2 : V) {
				if (vertex2.getNum() < vertex.getNum())	B.add(vertex2);
			}
			Set<Vertex> S = new HashSet<>();
			S.add(vertex);
			c = countCCRec(graph, S, B, c, budget);
		}
		return c;
	}
	
	public int countCCRec(Graph graph, Set<Vertex> S, Set<Vertex> X, int c, int budget) {
		Set<Vertex> V = graph.getVertices();		
		Set<Vertex> NS = new HashSet<>();
		for (Vertex vertex : V) {
			if (!X.contains(vertex) && !graph.getNeighborVertices(vertex, S).isEmpty()) {
				NS.add(vertex);
			}
		}
		Set<Set<Vertex>> subs = getSubSet(NS);
		for (Set<Vertex> set : subs) {
			if ((c = c + 1) > budget)	return c;
			Set<Vertex> S2 = new HashSet<>(S);
			S2.addAll(set);
			Set<Vertex> X2 = new HashSet<>(X);
			X2.addAll(NS);
			c = countCCRec(graph, S2, X2, c, budget);
		}
		return c;
	}
	
	public static void main(String[] args) {
		Small_Subgraph_Num small = new Small_Subgraph_Num();
		Graph graph = small.generateGraph();
		int c = small.countCC(graph, 1000);
		System.out.println(c);
	}
	
}
