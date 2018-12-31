package graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tree {

	private Vertex root;
	private Map<Vertex, Set<Vertex>> sonMap = new HashMap<>();
	private List<Vertex> list = new ArrayList<>();
	private Map<Vertex, Vertex> fatherMap = new HashMap<>();
	
	public void setRoot(Vertex root) {
		this.root = root;
		sonMap.put(root, new HashSet<>());
		list.add(root);
	}
	
	public Vertex getRoot() {
		return root;
	}
	
	public Set<Vertex> getNodes() {
		return sonMap.keySet();
	}
	
	public Set<Vertex> getSon(Vertex father) {
		return sonMap.get(father);
	}
	
	public Vertex getFather(Vertex son) {
		return fatherMap.get(son);
	}
	
	public Map<Vertex, Set<Vertex>> getMap() {
		return sonMap;
	}
	
	public Map<Vertex, Vertex> getFatherMap() {
		return fatherMap;
	}
	
	public List<Vertex> getList() {
		return list;
	}
	
	public void addEdge(Vertex father, Vertex son) {
		if (!sonMap.containsKey(father)) {
			sonMap.put(father, new HashSet<>());
			list.add(father);
		}
		sonMap.get(father).add(son);
		if (!sonMap.containsKey(son)) {
			sonMap.put(son, new HashSet<>());
			list.add(son);
		}
		fatherMap.put(son, father);
	}
	
	public Tree getSubtree(Vertex vertex) {
		Tree t = new Tree();
		t.setRoot(vertex);
		LinkedList<Vertex> l = new LinkedList<>();
		l.add(vertex);
		while (!l.isEmpty()) {
			t.getList().addAll(sonMap.get(l.get(0)));
			l.addAll(sonMap.get(l.get(0)));
			l.poll();
		}
		for (Vertex v : t.getList()) {
			t.sonMap.put(v, sonMap.get(v));
			t.fatherMap.put(v, fatherMap.get(v));
		}
		return t;
	}
	
	public void removeSubtree(Vertex vertex) {
		LinkedList<Vertex> l = new LinkedList<>();
		List<Vertex> lst = new ArrayList<>();
		lst.add(vertex);
		l.add(vertex);
		while (!l.isEmpty()) {
			Vertex v = l.getFirst();
			l.addAll(sonMap.get(v));
			lst.addAll(sonMap.get(v));
			l.poll();
		}
		for (Vertex v : lst) {
			list.remove(v);
			sonMap.remove(v);
			fatherMap.remove(v);
		}
	}
	
	public void replaceSubtree(Vertex v1, Tree t2) {
		Vertex v = getFather(v1);
		sonMap.get(v).remove(v1);
		sonMap.get(v).add(t2.getRoot());
		fatherMap.put(t2.getRoot(), v);
		removeSubtree(v1);
		list.addAll(t2.getList());
		sonMap.putAll(t2.getMap());
		fatherMap.putAll(t2.getFatherMap());
	}
	
	public boolean isChain() {
		for (Vertex v:sonMap.keySet()) {
			if (sonMap.get(v).size() > 1)
				return false;
		}
		return true;
	}
	
	public void merge(Vertex v1, Vertex v2) {
		for (Integer i : v2.getMix()) {
			if (!v1.getMix().contains(i))
				v1.addmix(i);
		}
		for (Vertex v: sonMap.get(v2))
			fatherMap.put(v, v1);
		sonMap.get(v1).addAll(sonMap.get(v2));
		sonMap.remove(v2);
		sonMap.get(v1).remove(v2);
		list.remove(v2);
	}
	
	public void mergeTree(Tree t) {
		List<Vertex> newlst = t.getList();
		for (Vertex v : newlst) {
			sonMap.remove(v);
			if (!v.equals(t.getRoot()))
				fatherMap.remove(v);
		}
		sonMap.putAll(t.getMap());
		fatherMap.putAll(t.fatherMap);
	}
	
	public void merge2Trees(Tree t1, Tree t2) {
		sonMap.putAll(t1.getMap());
		sonMap.putAll(t2.getMap());
		addEdge(root, t1.getRoot());
		addEdge(root, t2.getRoot());
	}
	
	@Override
	public String toString() {
		return sonMap.toString();
	}

}
