 package org.apache.cordova.extractzipfile;  
 import java.io.BufferedInputStream;  
 import java.io.BufferedOutputStream;  
 import java.io.File;  
 import java.io.FileOutputStream;  
 import java.io.IOException;  
 import java.util.Enumeration;  
 import java.util.zip.ZipEntry;  
 import java.util.zip.ZipException;  
 import java.util.zip.ZipFile;  
 import org.json.JSONArray;  
 import org.json.JSONException;  
 import org.apache.cordova.CallbackContext;  
 import org.apache.cordova.CordovaPlugin;  

 public class ExtractZipFilePlugin extends CordovaPlugin  
 {  
  @Override  
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{  
   if (action.equals("unzip") || action.equals("extractFile")){  
     final String filename = args.getString(0);   
     final CallbackContext callbackContextFinal = callbackContext;  
     cordova.getThreadPool().execute(new Runnable() {  
      public void run() {  
       unzip(filename, callbackContextFinal);  
      }  
     });  
     return true;  
   }  
   return false;  
  }  
  private void unzip(String filename, CallbackContext callbackContext){  
   File file = new File(filename);  
   String[] dirToSplit = filename.split(File.separator);  
   String dirToInsert = "";  
   for (int i = 0; i < dirToSplit.length - 1; i++) {  
    dirToInsert += dirToSplit[i] + File.separator;  
   }  
   // Put extracted files into a folder  
   String [] folders = filename.split(File.separator);  
   String folderName = folders[folders.length-1].split("\\.")[0];  
   File newFolder = new File(dirToInsert + folderName);  
   boolean result = newFolder.mkdirs();  
   dirToInsert += folderName + File.separator;  
   try {  
    ZipFile zipfile = new ZipFile(file);  
    Enumeration<? extends ZipEntry> e = zipfile.entries();  
    while (e.hasMoreElements()) {  
     ZipEntry entry = (ZipEntry) e.nextElement();  
     BufferedInputStream is = new BufferedInputStream(zipfile.getInputStream(entry),  
       8192);  
     int count;  
     byte data[] = new byte[102222];  
     String fileName = dirToInsert + entry.getName();  
     File outFile = new File(fileName);  
     if (entry.isDirectory()) {  
      outFile.mkdirs();  
     } else {  
      FileOutputStream fos = new FileOutputStream(outFile);  
      BufferedOutputStream dest = new BufferedOutputStream(fos, 102222);  
      while ((count = is.read(data, 0, 102222)) != -1) {  
       dest.write(data, 0, count);  
      }  
      dest.flush();  
      dest.close();  
      is.close();  
     }  
    }  
   } catch (ZipException e1) {  
    System.out.println(e1);  
    return;  
   } catch (IOException e1) {  
    System.out.println(e1);  
    return;  
   }
   callbackContext.success(filename);
  }  
 }  