package com.nick.test8;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static test8.Dish.menu;

public class Test8 {

	public static void main(String[] args) {
		
		Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
		
		List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300), 
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),	
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );	
	
		
		List<Dish> testList = menu.stream().collect(Collectors.toList());
		//System.out.println(testList);
		
		List<Dish> testList1 = menu.stream().sorted((d1,d2) ->  d1.getCalories() - d2.getCalories()).collect(Collectors.toList());
		//System.out.println(testList1);
		
		List<Dish> testList2 = menu.stream().sorted(comparing(Dish::getCalories)).collect(Collectors.toList());
		//System.out.println(testList2);
		
		List<String> cities = transactions.stream().map(t -> t.getTrader().getCity()).distinct().collect(Collectors.toList());
		//System.out.println(cities);
		
		List<String> names = transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge")).map(t -> t.getTrader().getName()).distinct().sorted((s1,s2) -> s1.compareTo(s2)).collect(Collectors.toList());
		//System.out.println(names);
		
		List<String> names1 = transactions.stream().map(t -> t.getTrader().getName()).distinct().sorted(comparing(String::toString)).collect(Collectors.toList());
		//System.out.println(names1);
		
		String joined = transactions.stream().map(t -> t.getTrader().getName()).collect(Collectors.joining(","));
		//System.out.println(joined);
		
		String reduceJoin = transactions.stream().map(t -> {
			return t.getTrader().getName(); 
			}).reduce("", (a,b) -> {
				if(a.equals("")){
					return a+b;
				}else{
					return a+","+b;
				}
			} ); 
		
	
		//System.out.println(reduceJoin);
		
		IntSummaryStatistics test = menu.stream().collect(Collectors.summarizingInt( (Dish d) -> d.getCalories() ));
		IntSummaryStatistics test1 = menu.stream().collect(Collectors.summarizingInt( Dish::getCalories ));
		
		menu.stream().collect(Collectors.reducing(0, d -> d.getCalories(), (i,j) -> i+j ));
		menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i,j) -> i+j ));
		

		int mcalories = menu.stream().collect(Collectors.reducing(0, d -> d.getCalories(), (i,j) -> i>j ? i:j));
		int mcalories1 = menu.stream().collect(Collectors.reducing(0, d -> d.getCalories(), (i,j) -> 
		{
			if(i > j)
				return i;
			else
				return j;
		}));
		
		System.out.println(mcalories);
	}

}
