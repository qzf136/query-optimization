package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Edge {

	private Set<Vertex> vertices = new HashSet<>();
	private float f;
	
	public Edge(Vertex v1, Vertex v2, float f) {
		vertices.add(v1);
		vertices.add(v2);
		this.f = f;
	}
	
	public float getF() {
		return f;
	}
	
	public Set<Vertex> getEndPoints() {
		return vertices;
	}
	
	public Edge replace(Vertex v1, Vertex v2) {
		List<Vertex> l = new ArrayList<>(vertices);
		Edge edge = new Edge(l.get(0), l.get(1), f);
		edge.getEndPoints().remove(v1);
		edge.getEndPoints().add(v2);
		return edge;
	}
	
	@Override
	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		if (edge.getEndPoints().equals(vertices))
			return true;
		else 
			return false;
 	}
	
	@Override
	public String toString() {
		return vertices.toString();
	}
	
}
