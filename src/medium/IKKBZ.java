package medium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Set_Cal;
import graph.Tree;
import graph.Vertex;

public class IKKBZ {
	
	public Tree	tree;
	public Graph graph;
	public Map<Vertex, Float> ch;
	public Map<Vertex, Float> rank;
	
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
	
	public void precedenceGraph(Vertex root) {
		Set<Vertex> vertices = graph.getVertices();
		tree = new Tree();
		ch = new HashMap<>();
		rank = new HashMap<>();
		tree.setRoot(root);
		while (tree.getNodes().size() < vertices.size()) {
			for (Vertex v_add: Set_Cal.diff(vertices, tree.getNodes())) {
				Set<Vertex> fathers = graph.getNeighborVertices(v_add, tree.getNodes());
				for (Vertex father: fathers) {
					tree.addEdge(father, v_add);
					break;
				}
			}
		}
		cal_rank();
	}
	
	public void cal_rank() {
		List<Vertex> list = tree.getList();
		for (Vertex v: list) {
			if (tree.getRoot().equals(v))
				ch.put(v, (float) 0);
			else
				ch.put(v, getsi(v)*getni(v));
		}
		for (Vertex ver : list) {
			if (ver.equals(tree.getRoot()))
				rank.put(ver, (float) -1);
			else {
				rank.put(ver, (getT(ver)-1) / ch.get(ver));
			}
		}
		
	}
	
	public float getsi(Vertex vertex) {
		Vertex v = tree.getFather(vertex);
		if (v == null)	return 1;
		else {
			for (Edge e: graph.getEdges()) {
				if (e.getEndPoints().contains(vertex) && e.getEndPoints().contains(v)) {
					return e.getF();
				}
			}
		}
		return 1;
	}
	
	public float getni(Vertex vertex) {
		return vertex.getNum();
	}
	
	public float CH(Vertex v1, Vertex v2) {
		return ch.get(v1) + getT(v1) * ch.get(v2);
	}
	
	public float ch_tree() {
		List<Integer> list = denormalization();
		List<Vertex> vers = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			vers.add(graph.getVertexByID(list.get(i)));
		}
		float sum = 0;
		for (int i = 1; i < list.size(); i++) {
			float v = 1;
			for (int j = 0; j <= i; j++) {
				v = v * getsi(vers.get(j)) * getni(vers.get(j));
			}
			sum += v;
		}
		return sum;
	}
	
	public float getT(Vertex s) {
		float a = 1;
		List<Integer> list = s.getMix();
		for (int i = 0; i < list.size(); i++) {
			Vertex v = graph.getVertexByID(list.get(i));
			a = a * getsi(v) * getni(v);
		}
		return a;
	}
	
	public void IKKBZ_Sub() {
		while (!tree.isChain()) {
			List<Vertex> list = tree.getList();
			for (int i = list.size()-1; i >= 0; i--) {
				if (!tree.getSon(list.get(i)).isEmpty()) {
					IKKBZ_normalize(list.get(i));
				}
			}
		}
	}
	
	public void IKKBZ_normalize(Vertex root) {
		Tree R = getSubtree(root);
		if (R.isChain()) {
			List<Vertex> list = R.getList();
			for (int i = list.size()-1; i > 0; i--) {
				Vertex v1 = list.get(i-1);
				Vertex v2 = list.get(i);
				if (rank.get(v1) > rank.get(v2)) {
					ch.put(v1, CH(v1, v2));
					rank.put(v1, (getT(v1)*getT(v2)-1)/ch.get(v1));
					R.merge(v1, v2);
					tree.merge(v1, v2);
				}
			}
		}
		else {
			List<Float> ra = new ArrayList<>();
			Map<Float, Vertex> rank_r = new HashMap<>();
			for (Vertex v : rank.keySet()) {
				if (R.getList().contains(v) && !root.equals(v)) {
					rank_r.put(rank.get(v), v);
					ra.add(rank.get(v));
				}
			}
			Collections.sort(ra);
			List<Vertex> list = new ArrayList<>();
			for (float val:ra) {
				list.add(rank_r.get(val));
			}
			Tree t = new Tree();
			t.setRoot(root);
			for (int i = 0; i < list.size()-1; i++) {
				t.addEdge(list.get(i), list.get(i+1));
			}
			t.addEdge(root, list.get(0));
			tree.mergeTree(t);
			IKKBZ_normalize(root);
		}
	}
	
	public Tree getSubtree(Vertex node) {
		Tree subtree = new Tree();
		subtree.setRoot(node);
		LinkedList<Vertex> list = new LinkedList<>();
		list.add(node);
		while (!list.isEmpty()) {
			Set<Vertex> set = tree.getSon(list.get(0));
			for (Vertex v : set) {
				subtree.addEdge(list.get(0), v);
				list.add(v);
			}
			list.poll();
		}
		return subtree;
	}
	
	public List<Integer> denormalization() {
		List<Integer> list = new ArrayList<>();
		Vertex vertex  = tree.getRoot();
		while (!tree.getSon(vertex).isEmpty()) {
			list.addAll(vertex.getMix());
			vertex = (new ArrayList<>(tree.getSon(vertex))).get(0);
		}
		list.add(vertex.getId());
		return list;
	}
	
	public List<Integer> getOrder(int i) {
		precedenceGraph(graph.getVertexByID(i));
		IKKBZ_Sub();
		List<Integer> list = denormalization();
		Set<Vertex> set = graph.getVertices();
		for (Vertex v : set) {
			v.clearMix();
		}
		return list;
	}
	
	public List<Integer> construct(Graph graph) {
		float c = -1;
		this.graph = graph;
		int N = graph.getVertices().size();
		List<Integer> res = new ArrayList<>();
		for (int i = 1; i <= N; i++) {
			List<Integer> list = getOrder(i);
			float cost = ch_tree();
			if (cost < c || c < 0) {
				c = cost;
				res = list;
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
		IKKBZ a = new IKKBZ();
		Graph graph = a.generateGraph();
		List<Integer> list = a.construct(graph);
		System.out.println(list);
	}
	
}
