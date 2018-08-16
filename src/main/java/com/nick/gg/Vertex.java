package com.nick.gg;

public class Vertex implements Comparable<Vertex>{
	
	String name;
	double minDistance = Double.POSITIVE_INFINITY;
	public Edge[] adjacencies;
	public Vertex previous;
	
	public Vertex(String name){
		this.name = name;
	}	
	
	@Override
	public String toString() {
		return "N:"+name;
	}

	@Override
	public int compareTo(Vertex o) {
		return Double.compare(minDistance, o.minDistance);
	}
	
	
	
	
	

	

}
