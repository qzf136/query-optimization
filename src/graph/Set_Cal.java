package graph;

import java.util.HashSet;
import java.util.Set;

public class Set_Cal {

	public static Set<Vertex> union(Set<Vertex> set1, Set<Vertex> set2) {
		Set<Vertex> set = new HashSet<>();
		set.addAll(set1);
		set.addAll(set2);
		return set;
	}
	
	public static Set<Vertex> diff(Set<Vertex> set1, Set<Vertex> set2) {
		Set<Vertex> set = new HashSet<>();
		set.addAll(set1);
		set.removeAll(set2);
		return set;
	}
	
	public static Set<Vertex> inter(Set<Vertex> set1, Set<Vertex> set2) {
		Set<Vertex> set = new HashSet<>();
		set.addAll(set1);
		set.removeAll(diff(set1, set2));
		return set;
	}
	
	public static void main(String[] args) {
		Set<Vertex> s1=  new HashSet<>();
		Set<Vertex> s2 = new HashSet<>();
		Vertex v1 = new Vertex(1, 10);
		Vertex v2 = new Vertex(2, 100);
		Vertex v3 = new Vertex(3, 100);
		s1.add(v1);
		s1.add(v2);
		s2.add(v3);
		s2.add(v2);
		System.out.println(Set_Cal.union(s1, s2));
		System.out.println(Set_Cal.diff(s1, s2));
		System.out.println(Set_Cal.inter(s1, s2));
	}
	
}
