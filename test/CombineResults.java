package test;

import java.io.*;
import java.util.*;

public class CombineResults {
	
	public static void main(String[] args) throws IOException{
		new CombineResults().start();
	}
	
	public void start(){
		File f = new File("");
		String path = f.getAbsolutePath();
		File dir = new File(path+File.separator+"results");
		dir.mkdir();
		
		String[] resultFolders = dir.list();
		List<Thread> mergers = new ArrayList<Thread>();

		for(String s: resultFolders){
			//enter each graph size folder and print out the graph files in it
			File dir2 = new File(dir.getAbsolutePath()+File.separator+s);
			if(dir2.isDirectory()){
				mergers.add(new Thread(new Merger(dir,dir2,s)));
			}
		}
		
		//start the merging threads
		for(Thread t: mergers)
			t.start();
		
		//wait for threads to complete
		for(Thread t: mergers){	
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Merging completed");
	}
	
	public static class Merger implements Runnable{
		
		File dir, dir2;
		String s;
		
		Merger(File dir, File dir2, String s){
			this.dir = dir;
			this.dir2 = dir2;
			this.s = s;
		}
		public void run(){
			System.out.println("Combining files in "+ dir2.getAbsolutePath());
			String finalResultName = dir.getAbsolutePath()+File.separator+s+"_combined.txt";
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			String[] resultFiles = dir2.list();
			for(String t: resultFiles){
				String resultFileName = dir2.getAbsolutePath()+File.separator+t;
				//open each file
				
				try {
					Scanner scan = new Scanner(new FileReader(resultFileName));
					int i=0;
					while(scan.hasNextLine()){
						if(i==0){
							scan.nextLine();
							i++;
						}else{
							String result = scan.nextLine();
							output += String.format("%s%n", result);
						}
					}
					scan.close();
					PrintWriter writer = new PrintWriter(finalResultName);
					writer.println(output);
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
