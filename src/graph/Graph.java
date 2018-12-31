package graph; 

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

	private Set<Vertex> vertices = new HashSet<>();
	private Set<Edge> edges = new HashSet<>();
	
	public Set<Vertex> getVertices() {
		return vertices;
	}
	
	public Set<Edge> getEdges() {
		return edges;
	}
	
	public void addVertex(Vertex v) {
		if (!vertices.contains(v))
			vertices.add(v);
	}
	
	public void addEdge(Edge e) {
		if (!edges.contains(e))
			edges.add(e);
	}

	public Vertex getVertexByID(int id) {
		for (Vertex v:vertices) {
			if (v.getId() == id) {
				return v;
			}
		}
		return null;
	}
	
	public boolean isConnected(Vertex v1, Vertex v2) {
		for (Edge e : edges) {
			Set<Vertex> set = e.getEndPoints();
			if (set.contains(v1) && set.contains(v2))
				return true;
		}
		return false;
	}
	
	public boolean isConnected(Set<Vertex> s1, Set<Vertex> s2) {
		for (Edge e: edges) {
			Set<Vertex> set = e.getEndPoints();
			List<Vertex> list = new ArrayList<>(set);
			if ((s1.contains(list.get(0)) && s2.contains(list.get(1)))
			  ||(s1.contains(list.get(1)) && s2.contains(list.get(0))))
				return true;
		}
		return false;
	}
	
	public Set<Edge> getConnectedEdges(Set<Vertex> S1, Set<Vertex> S2) {
		Set<Edge> set = new HashSet<>();
		for (Edge e: edges) {
			Set<Vertex> s = e.getEndPoints();
			List<Vertex> list = new ArrayList<>(s);
			if ((S1.contains(list.get(0)) && S2.contains(list.get(1)))
			  ||(S1.contains(list.get(1)) && S2.contains(list.get(0))))
				set.add(e);
		}
		return set;
	}
	
	public Set<Vertex> getNeighborVertices(Vertex v, Set<Vertex> S) {
		Set<Vertex> set = new HashSet<>();
		for (Vertex vS : S) {
			if (isConnected(v, vS)) {
				set.add(vS);
			}
		}
		return set;
	}
	
	public Set<Edge> getNeighborEdges(Vertex v, Set<Edge> S) {
		Set<Edge> set = new HashSet<>();
		for (Edge e: S) {
			if (e.getEndPoints().contains(v))
				set.add(e);
		}
		return set;
	}
	
	
}
