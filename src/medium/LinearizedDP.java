package medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Tree;
import graph.Vertex;

public class LinearizedDP {

	Graph graph;
	List<Vertex> lst = new ArrayList<>();
	int N;
	float[][] cost;
	float[][] M;
	
	public Graph generateGraph() {
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
		return graph;
	}
	
	public Tree construct(Graph graph) {
		this.graph = graph;
		int d = -1;
		N = graph.getVertices().size();
		cost = new float[N][N];
		M = new float[N][N];
		Tree[][] T = new Tree[N][N];
		List<Integer> O = new IKKBZ().construct(graph);
		for (Integer i : O) {
			lst.add(graph.getVertexByID(i));
		}
		for (int i = 0; i < N; i++) {
			Tree tree = new Tree();
			tree.setRoot(lst.get(i));
			T[i][i] = tree;
			cost[i][i] = 0;
			M[i][i] = lst.get(i).getNum();
		}
		for (int s = 2; s <= N; s++) {
			for (int i = 0; i <= N-s; i++) {
				for (int j = 1; j <= s-1; j++) {
					Tree lTree = T[i][i+j-1];
					Tree rTree = T[i+j][i+s-1];
					if (lTree != null && rTree != null) {
						Set<Vertex> lset = new HashSet<>(lst.subList(i, i+j));
						Set<Vertex> rset = new HashSet<>(lst.subList(i+j, i+s));
						if (graph.isConnected(lset, rset)) {
							Tree t = new Tree();
							Vertex v = new Vertex(d, -1);
							d--;
							t.setRoot(v);
							t.merge2Trees(lTree, rTree);
							float c = getM(i, i+j-1, i+j, i+s-1) + cost[i][i+j-1] + cost[i+j][i+s-1];
							if (cost[i][i+s-1] == 0 || c < cost[i][i+s-1]) {
								cost[i][i+s-1] = c;
								T[i][i+s-1] = t;
							}
						}
					}
					
				}
				
			}
		}
		return T[0][N-1];
	}
	
	public float getM(int a1, int a2, int a3, int a4) {
		float a = getf(a1, a2, a3, a4) * M[a1][a2] * M[a3][a4];
		M[a1][a4] = a;
		return a;
	}
	
	public float getf(int a1, int a2, int a3, int a4) {
		Set<Vertex> S1 = new HashSet<>(lst.subList(a1, a2+1));
		Set<Vertex> S2 = new HashSet<>(lst.subList(a3, a4+1));
		float a = 1;
		Set<Edge> set = graph.getConnectedEdges(S1, S2);
		for (Edge e : set) {
			a = a * e.getF();
		}
		return a;
	}
			
	public static void main(String[] args) {
		LinearizedDP lDp = new LinearizedDP();
		Graph graph = lDp.generateGraph();
		Tree tree = lDp.construct(graph);
		System.out.println(tree.getMap());
	}
}
