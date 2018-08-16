package com.nick.com.nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class NLP {
	
	public static List<String> igonoreWords = new ArrayList<String>();
	
	//build initial reporsitory of words to be ignore
	static{
		igonoreWords.add("if");
		igonoreWords.add("the");
	}
	
	private Map<String,Integer> reporsitory = new HashMap<String, Integer>();
	private long totalNumberOfDocuments = 0;

	public static void main(String[] args) throws Exception{
		new NLP().startProcessing();
	}
	
	private void startProcessing() throws Exception{
		String inputFile = "C:/temp/thefind_pip.idf/thefind_pip.idf/electronics_prod.sample200ul";
		System.out.println("Please input file location e.g. "+inputFile);
		
		
		Scanner scan =  new Scanner(System.in);
		inputFile = scan.nextLine();	
		
		readInputFile(inputFile);
	}
	
	
	private void readInputFile(String inputFile) {
		BufferedReader br = null;
		String line = null;
		
		try{
			br = new BufferedReader(new FileReader(new File(inputFile)));
			int documentId = 0;
			while((line = br.readLine()) != null){
				line = updateLine(line);
				documentId++;
				String[] words = line.split(" ");
				buildRepository(words, documentId);
				
			}
			totalNumberOfDocuments = documentId - 1;
			printIDFWords(inputFile);
			
		}catch(Exception e){
			System.err.println("Error while reading file ");
			e.printStackTrace();
		}finally{
			try{	
				br.close();
			}catch(Exception e){
				System.err.println("Error closing input file ");
				e.printStackTrace();
			}
		}
	}
	
	private void printIDFWords(String inputFile) {
		BufferedReader br = null;
		String line = null;
		
		try{
			br = new BufferedReader(new FileReader(new File(inputFile)));
			
			while((line = br.readLine()) != null){
				line = updateLine(line);
				String[] words = line.split(" ");
				printWord(words);
				
			}
			
		}catch(Exception e){
			System.err.println("Error while reading file ");
			e.printStackTrace();
		}finally{
			try{	
				br.close();
			}catch(Exception e){
				System.err.println("Error closing input file ");
				e.printStackTrace();
			}
		}
	}
	
	private void printWord(String[] words) {
		
		List<String> alreadyPrinted = new ArrayList<String>(); 
		IDFWord idfWord = null;
		List<IDFWord> wordList = new ArrayList<IDFWord>();
		StringBuilder br = new StringBuilder();
		boolean print = false;
		for(String word : words){
			if(reporsitory.containsKey(word)){
				if((idfWord = findIDF(word)) != null && !alreadyPrinted.contains(word)){
					alreadyPrinted.add(word);
					print = true;
					wordList.add(idfWord);				
					
				}
			}
		}		
		alreadyPrinted.clear();
		if(print){
			Collections.sort(wordList);
			int i = 0;
			for(IDFWord tempWord : wordList){
				if(i >=5){
					break;
				}
				br.append(tempWord.word);
				br.append(" ");
				i++;
			}
			System.out.println(br);
		}
	}
	
	private IDFWord findIDF(String word){
		double quotient = totalNumberOfDocuments/reporsitory.get(word);
		double logbaseten = Math.log10(quotient);
		if(logbaseten >= 2 && logbaseten <= 3.5){
			return new IDFWord(word, logbaseten);
		}else{
			return null;
		}
			
	}
	
	private void buildRepository(String[] words, int documentId) {
		//temp list to maintain word count in a document, assuming that a word will be only counted 1 even if it occors twice in a document
		List<String> tempList = new ArrayList<String>();
		for(String word : words){
			//check if a word is a valid word
			if(!igonoreWords.contains(word) && isValid(word) && !tempList.contains(word)){
				//add this word in a reposirtory
				addToARepository(word);
				tempList.add(word);
				
			}
		}
		tempList.clear();
	}
	
	private void addToARepository(String word){
		if(reporsitory.containsKey(word)){
			reporsitory.put(word,reporsitory.get(word)+1);
		}else{
			reporsitory.put(word, 1);
		}
	}
	
	private boolean isValid(String word){
		// a word is called a valid word if it has 3 or more chars, Note: I may have done this with regex
		int count = 0;
		char[] wc = word.toCharArray();
		for(char c : wc){
			if(Character.isLetter(c)){
				count++;				
			}else{
				return false;
			}
		}
		
		if(count >= 3 ){
			return true;
		}else{
			return false;
		}
	}
	
	private String updateLine(String line){
		if(line.indexOf('.') > 0){
			if(line.endsWith(".")){
				line = line.substring(0, line.length()-1);
			}
			
			line=line.replace('.', ' ');
			
		}
		return line;
	}

	
	class IDFWord implements Comparable<IDFWord>{
		
		String word;
		double idf;
		

		public IDFWord(String word, double idf) {
			super();
			this.word = word;
			this.idf = idf;
		}


		@Override
		public int compareTo(IDFWord arg0) {
			// TODO Auto-generated method stub
			double temp = this.idf - arg0.idf;
			if(temp > 0){
				return -1;
			}else if(temp < 0){
				return 1;
			}else{
				return 0;
			}
		}
		
		@Override
		public String toString() {
			return word+":"+idf;
		}
		
		
	}

}
