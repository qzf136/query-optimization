package graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

	private int id;
	private int num;
	private List<Integer> mix = new ArrayList<>();
	private List<Vertex> comb = new ArrayList<>();
	
	public Vertex(Integer id, int num) {
		this.id = id;
		this.num = num;
		this.mix.add(id);
	}
	
	public int getId() {
		return id;
	}
	
	public int getNum() {
		return num;
	}
	
	public void addmix(int id) {
		mix.add(id);
	}
	
	public List<Integer> getMix() {
		return mix;
	}
	
	public void clearMix() {
		mix = new ArrayList<>();
		mix.add(id);
	}
	
	public List<Vertex> getComb() {
		return comb;
	}
	
	public void addComb(Vertex v) {
		comb.add(v);
	}
	
	@Override
	public boolean equals(Object obj) {
		Vertex vertex = (Vertex) obj;
		if (vertex.getId() != this.id)
			return false;
		else
			return true;
	}
	
	@Override
	public String toString() {
		return new String(id+"");
	}
	
}
