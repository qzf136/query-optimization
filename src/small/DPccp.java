package small;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Set_Cal;
import graph.Tree;
import graph.Vertex;

public class DPccp {

	private int d;
	private int N;
	private Graph graph;
	private List<Vertex> vertices;
	private Map<Set<Vertex>, Tree> B = new HashMap<>();
	private Set<Set<Vertex>> enums = new HashSet<>();
	private List<List<Set<Vertex>>> pairs = new ArrayList<>();
	private Map<Set<Vertex>, Float> cost = new HashMap<>();
	private Map<Set<Vertex>, Float> T = new HashMap<>();
	
	public void initializeB() {
		for (int i = 0; i <= Math.pow(2, N); i++) {
			Set<Vertex> sub = new HashSet<>();
			int index = i;
			for (int j = 0; j < N; j++) {
				if ((index & 1) == 1)	sub.add(vertices.get(j));
				index >>= 1;
			}
			B.put(sub, null);
		}
	}
	
	public Set<Set<Vertex>> getAllSubs(Set<Vertex> S) {
		List<Vertex> L = new ArrayList<>(S);
		Set<Set<Vertex>> all_subs = new HashSet<>();
		int n = (int) Math.pow(2, S.size());
		for (int i = 0; i <= n; i++) {
			Set<Vertex> sub = new HashSet<>();
			int index = i;
			for (int j = 0; j < L.size(); j++) {
				if ((index & 1) == 1)	sub.add(L.get(j));
				index >>= 1;
			}
			all_subs.add(sub);
		}
		return all_subs;
	}
	
	public Tree ccp(Graph graph, int d) {
		this.d = d;
		this.graph = graph;
		vertices = new ArrayList<>(graph.getVertices());
		this.N = vertices.size();
		graph.getEdges();
		Collections.sort(vertices, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex o1, Vertex o2) {
				return new Integer(o2.getId()).compareTo(new Integer(o1.getId()));
			}
		});
		initializeB();
		EnumarateCsg();
		getAllPairs();
		for (Vertex v : vertices) {
			Set<Vertex> set = new HashSet<>();
			set.add(v);
			Tree tree = new Tree();
			tree.setRoot(v);
			B.put(set, tree);
			cost.put(set, (float) 0);
			T.put(set, (float) v.getNum());
		}
		for (int i = 0; i < pairs.size(); i++) {
			Set<Vertex> s1 = pairs.get(i).get(0);
			Set<Vertex> s2 = pairs.get(i).get(1);
			Set<Vertex> S = Set_Cal.union(s1, s2);
			Tree t1 = B.get(s1);
			Tree t2 = B.get(s2);
			Tree P = createJoinTree(t1, t2);
			float c = getT(s1, s2) + cost.get(s1) + cost.get(s2);
			if (B.get(S) == null || c < cost.get(S)) {
				cost.put(S, c);
				B.put(S, P);
			}
		}
		return B.get(graph.getVertices());
	}
	
	public float getf_product(Set<Vertex> S1, Set<Vertex> S2) {
		float a = 1;
		Set<Edge> set = graph.getConnectedEdges(S1, S2);
		for (Edge e : set) {
			a = a * e.getF();
		}
		return a;
	}
	
	public float getT(Set<Vertex> S1, Set<Vertex> S2) {
		float a = getf_product(S1, S2) * T.get(S1) * T.get(S2);
		T.put(Set_Cal.union(S1, S2), a);
		return a;
	}
	
	public Tree createJoinTree(Tree t1, Tree t2) {
		Tree tree = new Tree();
		Vertex temp = new Vertex(d, -1);
		d--;
		tree.setRoot(temp);
		tree.merge2Trees(t1, t2);
		return tree;
	}
	
	public void getAllPairs() {
		EnumarateCsg();
		List<Set<Vertex>> list = new ArrayList<>(enums);
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				if (Set_Cal.inter(list.get(i), list.get(j)).isEmpty()) {
					if (graph.isConnected(list.get(i), list.get(j))) {
						List<Set<Vertex>> l = new ArrayList<>();
						l.add(list.get(i));
						l.add(list.get(j));
						pairs.add(l);
					}
				}
			}
		}
		Collections.sort(pairs, new Comparator<List<Set<Vertex>>>() {
			public int compare(List<Set<Vertex>> o1, List<Set<Vertex>> o2) {
				int n1 = o1.get(0).size() + o1.get(1).size();
				int n2 = o2.get(0).size() + o2.get(1).size();
				return new Integer(n1).compareTo(new Integer(n2));
			};
		});
	}
	
	public void EnumarateCsg() {
		for (Vertex v : vertices) {
			Set<Vertex> set = new HashSet<>();
			set.add(v);
			enums.add(set);
			Set<Vertex> bi = new HashSet<>();
			for (Vertex ver : vertices) {
				if (ver.getId() <= v.getId())
					bi.add(ver);
			}
			EnumarateCsgRec(set, bi);
		}
	}
	
	public void EnumarateCsgRec(Set<Vertex> S, Set<Vertex> X) {
		Set<Vertex> vers = graph.getVertices();
		Set<Vertex> NS = new HashSet<>();
		for (Vertex v : S) {
			NS = Set_Cal.union(NS, graph.getNeighborVertices(v, vers));
		}
		Set<Vertex> N = Set_Cal.diff(NS, X);
		Set<Set<Vertex>> all_subs = getAllSubs(N);
		for (Set<Vertex> s : all_subs) {
			if (!s.isEmpty())
				enums.add(Set_Cal.union(S,s));
		}
		for (Set<Vertex> s : all_subs) {
			if (!s.isEmpty())
				EnumarateCsgRec(Set_Cal.union(S, s), Set_Cal.union(X, N));
		}
		
	}
	
	public float getCost(Set<Vertex> set) {
		return cost.get(set);
	}

	
	public static void main(String[] args) {
		Graph graph = new Graph();
		Vertex v1 = new Vertex(1, 10);
		Vertex v2 = new Vertex(2, 100);
		Vertex v3 = new Vertex(3, 100);
		Vertex v4 = new Vertex(4, 100);
		Vertex v5 = new Vertex(5, 18);
		
		Edge e1 = new Edge(v1, v2, 0.5f);
		Edge e2 = new Edge(v2, v3, 0.25f);
		Edge e3 = new Edge(v2, v4, 0.2f);
		Edge e4 = new Edge(v2, v5, 0.3333f);
		Edge e5 = new Edge(v1, v4, 0.5f);
		Edge e6 = new Edge(v4, v3, 0.1f);
		Edge e7 = new Edge(v3, v5, 0.5f);
		Edge e8 = new Edge(v1, v5, 0.5f);
		
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		graph.addEdge(e6);
		graph.addEdge(e7);
		graph.addEdge(e8);
		DPccp c = new DPccp();
		Tree tree = c.ccp(graph, -1);
		System.out.println(tree.getMap());
		float cost = c.getCost(graph.getVertices());
		System.out.println(cost);
	}
	
}
