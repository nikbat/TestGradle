package com.nick.generic;

public enum MyEnum {
	
	PLUS ("PLUS") { 
		
		public double apply(double x, double y){
			return x+y;
		}
	},
	MINUS ("MINUS"){
		public double apply(double x, double y){
			return x-y;
		}
	},
		
	MULTIPLY ("MULTIPLY"){
		public double apply(double x, double y){
			return x*y;
		}
	},
	DIVIDE ("DIVIDE"){
		public double apply(double x, double y){
			return x/y;
		}
	};
	
	String name;
	MyEnum(String name){
		this.name = name;
	}
	
	abstract public double apply(double x, double y);
	
}
