package com.ootb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FiletoBytes {
	public static byte [] fileToBytes(File file) throws NullPointerException,IOException{
		if(file == null) throw new NullPointerException("Not Found");
		
		// byte array 
		byte[] bytes=new byte[(int)file.length()];
					
		
		InputStream input=new FileInputStream(file);
		
		//read the bytes
		int iter = 0 , block = 0;
		while ( iter < bytes.length) {
			block = input.read(bytes, iter , bytes.length-iter);
			if ( block >= 0 ) {
				iter += block;
			} else {
				break;					
			}
		}
		
		input.close();
		
		return bytes;
	}
}
