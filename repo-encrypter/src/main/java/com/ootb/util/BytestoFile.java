package com.ootb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BytestoFile {
	public static File bytesToFile (byte[] bytes,String path) throws NullPointerException, IOException{
		if(bytes == null) throw new NullPointerException();
		if(path == null) throw new NullPointerException();
		
		File file=new File(path);
		FileOutputStream fout;
		fout = new FileOutputStream(file);

		for (int i = 0; i < bytes.length; i++)
			fout.write(bytes[i]);

		fout.close();
		
		return file;
	}
}
