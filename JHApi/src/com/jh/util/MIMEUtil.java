package com.jh.util;

import java.io.File;

public class MIMEUtil {

	 
	    public static String getMIMEType(File f)   
	    {   
	      String type="";  
	      String fName=f.getName();  
	      
	      String end=fName.substring(fName.lastIndexOf(".")  
	      +1,fName.length()).toLowerCase();   
	        
	     
	      if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
	      end.equals("xmf")||end.equals("ogg")||end.equals("wav"))  
	      {  
	        type = "audio";   
	      }  
	      else if(end.equals("3gp")||end.equals("mp4"))  
	      {  
	        type = "video";  
	      }  
	      else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
	      end.equals("jpeg")||end.equals("bmp"))  
	      {  
	        type = "image";  
	      }  
	      else if(end.equals("apk"))   
	      {   
	        /* android.permission.INSTALL_PACKAGES */   
	        type = "application/vnd.android.package-archive";   
	      }   
	      else  
	      {  
	        type="*";  
	      }  
	      
	      if(end.equals("apk"))   
	      {   
	      }   
	      else   
	      {   
	        type += "/*";    
	      }   
	      return type;    
	    }
}
