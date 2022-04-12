package com.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class Utils {
	
	public static String[] splitAvgVar(String avgVar) {
		String[] vars;
		avgVar = avgVar.toLowerCase();
		if(avgVar.startsWith("avg_")) {
			String attr = avgVar.split("_")[1];
			vars = new String[2];
			vars[0] = "sum_" + attr;
			vars[1] = "count";
		} else {
			vars = new String[1];
			vars[0] = avgVar;
		}
		
		return vars;
	}

	public static void exec(String cmd) {
		
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor(5, TimeUnit.SECONDS);
			if(p.exitValue() == 0) {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())); 
	                String line; 
	                while ((line = input.readLine()) != null) { 
	                    System.out.println(line); 
	                } 
	            System.out.println("Exec " + cmd + " is done successfully!!!");
			} else {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
                String line; 
                while ((line = input.readLine()) != null) { 
                    System.err.println(line); 
                } 
                System.err.println("Exec " + " failed !!!" + p.exitValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void execInDir(String cmd, String dir) {
		
		try {
			Process p = Runtime.getRuntime().exec(cmd, null, new File(dir));
			p.waitFor(5, TimeUnit.SECONDS);
			if(p.exitValue() == 0) {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())); 
	                String line; 
	                while ((line = input.readLine()) != null) { 
	                    System.out.println(line); 
	                } 
	            System.out.println("Exec " + cmd + " is done successfully!!!");
			} else {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
                String line; 
                while ((line = input.readLine()) != null) { 
                    System.err.println(line); 
                } 
                System.err.println("Exec " + " failed !!!" + p.exitValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy the content from source file to destination file
	 * 
	 * @param src:  File needs to be copied
	 * @param dest: File needs to be copied to
	 * @throws IOException
	 */
	public static void copy(File src, File dest) throws IOException {
//		dest.deleteOnExit();
//		
//		dest.createNewFile();
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(dest); // buffer size 1K
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0) {
				os.write(buf, 0, bytesRead);
			}
			System.out.println(String.format("File Copied from %s to %s", src.getAbsolutePath(), dest.getAbsolutePath()));
		} finally {
			is.close();
			os.close();
		}
	}

	public static void dump(String content, String filename) {
		File file = new File(filename);
		//if(file.exists())	file.renameTo(new File(filename + "_backup"));
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
			System.out.println("File Dump is successful for " + filename);
		} catch (Exception e) {
			System.err.println("Exception occurred while writing file " + filename);
			System.err.println(e);
		}
	}
}
