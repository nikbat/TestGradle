package com.nick.generic;

//import com.coremetrics.rb.query.util.SimpleBloomFilter;
//import com.google.common.hash.BloomFilter;


public class TestGuava {

	public static void main(String[] args) throws Exception{
		//new TestGuava().testBoom();
	}
	
	/*private void testBoom() throws Exception{
		String cookieId = "";
		int count = 0;
		SimpleBloomFilter<String> bl = new SimpleBloomFilter<String>(100000, 5);
		String filePath = "C:/tmp/campaign-results/1111111/user-output-00000-8563-33657.csv";
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));		
		while((cookieId = br.readLine()) != null){
			System.out.println(cookieId);
			bl.set(cookieId);
			count++;
		}
		System.out.println(count);
		br.close();
		
		
		System.out.println(bl.check("10963785311270709321295"));
		System.out.println(bl.check("11254198151271749369944"));
		System.out.println(bl.check("77741636824858497763799"));
		System.out.println(bl.check("42601689912277105571949"));		
		System.out.println(bl.check("42601689912277105571948"));
		
		DataOutputStream dos = null;
		ObjectOutputStream oos = null;
		
		try {
			String bloomFile = "c:/temp/bloom/bloomFile";
			//dos = new DataOutputStream(arg0)
			oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(new File(bloomFile))));
			dos = null;
			
			oos.writeObject(bl);
			
			oos.close();
			oos = null;
			
		} finally {
			if (dos != null)
				try { dos.close(); } catch (IOException e) { // ignore // }
			if (oos != null)
				try { oos.close(); } catch (IOException e) { // ignore // }
		}
		
		SimpleBloomFilter<String> filter = null;
		try {
			
			String bloomFile = "c:/temp/bloom/bloomFile";
			ObjectInputStream is = null;
			is = new ObjectInputStream(new GZIPInputStream(new FileInputStream(new File(bloomFile))));
						
			filter = (SimpleBloomFilter<String>)is.readObject();
			
			is.close();
			is = null;
			
		} finally {
			if (dos != null)
				try { dos.close(); } catch (IOException e) { // ignore // }
			if (oos != null)
				try { oos.close(); } catch (IOException e) { // ignore // }
		}
		
		
		System.out.println(bl.check("10963785311270709321295"));
		System.out.println(bl.check("11254198151271749369944"));
		System.out.println(bl.check("77741636824858497763799"));
		System.out.println(bl.check("42601689912277105571949"));
		System.out.println(bl.check("42601689912277105571949"));
		System.out.println(bl.check("42601689912277105571948"));
		System.out.println(bl.check("36311392928754172137986"));
		
		

		
		
		
	}*/

}
