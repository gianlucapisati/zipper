package org.apache.cordova.extractzip;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
			final String destinationFolder = args.getString(1);
			final CallbackContext callbackContextFinal = callbackContext;
			cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					unzip(filename, destinationFolder, callbackContextFinal);
				}
			});
			return true;
		}
		return false;
	}
	private void unzip(String filename, String destinationFolder, CallbackContext callbackContext){
		try  {
            
			File file = new File(filename);
			String[] dirToSplit = filename.split(File.separator);
			String dirToInsert = "";
			for (int i = 3; i < dirToSplit.length-1; i++) {
				dirToInsert += dirToSplit[i] + File.separator;
			}
			dirToInsert = File.separator + dirToInsert;
			String zipPath = dirToInsert;
            dirToInsert += (destinationFolder+File.separator);
			zipPath+=dirToSplit[dirToSplit.length-1];
			FileInputStream fin = new FileInputStream(zipPath);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;
			
			byte[] buffer = new byte[1024];
			while ((ze = zin.getNextEntry()) != null) {
                
				FileOutputStream fout = new FileOutputStream(dirToInsert + ze.getName());
				int len;
				while((len = zin.read(buffer)) > 0){
					fout.write(buffer,0,len);
				}
                
				zin.closeEntry();
				fout.close();
			}
            
			zin.close();
		} catch(Exception e) {
			System.out.println(e);
		} 
        
		callbackContext.success(filename);
	}  
}