package com.nick.generic;

public class BuilderPattern {
	
	public static void  main(String[] args) {
		External e = new External.Builder().A("A").B("B").C("C").D("D").E("E").F("F").G("G").build();
		System.out.println(e);
	}
	
}

class External {
	
	String A;
	String B;
	String C;
	String D;
	String E;
	String F;
	String G;
	
	private External(Builder builder){
		this.A = builder.A;
		this.B = builder.B;
		this.C = builder.C;
		this.D = builder.D;
		this.E = builder.E;
		this.F = builder.F;
		this.G = builder.G;
	}
	
	@Override
	public String toString(){
		return (A+B+C+D+E+F+G);
	}
	
	public static class Builder{
		
		String A;
		String B;
		String C;
		String D;
		String E;
		String F;
		String G;
		
		public Builder A(String T){
			this.A = T;
			return this;
		}
		public Builder B(String T){
			this.B = T;
			return this;
		}
		public Builder C(String T){
			this.C = T;
			return this;
		}
		public Builder D(String T){
			this.D = T;
			return this;
		}
		public Builder E(String T){
			this.E = T;
			return this;
		}
		public Builder F(String T){
			this.F = T;
			return this;
		}
		public Builder G(String T){
			this.G = T;
			return this;
		}
		
		public External build (){
			return new External(this);
		}
		
	}
	
}
