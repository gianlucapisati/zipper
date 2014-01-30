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

public class CreateZipFilePlugin extends CordovaPlugin
{
	List<String> fileList = new ArrayList<String>();
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
		if (action.equals("zip")){
			final String folderToZip = args.getString(0);
			final CallbackContext callbackContextFinal = callbackContext;
			cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					zip(folderToZip, callbackContextFinal);
				}
			});
			return true;
		}
		return false;
	}
	private void zip(String folderToZip, CallbackContext callbackContext){
		try  {
			byte[] buffer = new byte[1024];
			
			try{
		    	FileOutputStream fos = new FileOutputStream(folderToZip.concat("/dump.zip"));
		    	ZipOutputStream zos = new ZipOutputStream(fos);
                
		    	this.generateFileList(new File(folderToZip));
		    	
		    	for(String file : fileList){
                    
		    		ZipEntry ze= new ZipEntry(file);
		        	zos.putNextEntry(ze);
                    
		        	FileInputStream in = new FileInputStream(folderToZip + File.separator + file);
                    
		        	int len;
		        	while ((len = in.read(buffer)) > 0) {
		        		zos.write(buffer, 0, len);
		        	}
                    
		        	in.close();
		    	}
		    	
		    	zos.closeEntry();
		    	zos.close();
		    	
			}catch(Exception e){
				e.printStackTrace();
			}
            
            
		} catch(Exception e) {
			System.out.println(e);
		}
        
		callbackContext.success(folderToZip);
	}
    
	public void generateFileList(File node){
        
		//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
        
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
        
	}
    
    private String generateZipEntry(String file){
    	return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}