package fi.sprint0;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

/* Project template for sprint one
 * 
 * 
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	downloadAndInstall("1");
           
            }
        });
        
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	downloadAndInstall("2");
            }
        });
        
        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	downloadAndInstall("3");
            }
        });

		
		RootTools.debugMode = true; //ON
		
		//pop-up tells if the device is rooted or not
		
		
		if (isRooted()){
			Toast.makeText(getApplicationContext(), "This device is rooted!", Toast.LENGTH_SHORT).show();
			//downloadAndInstall("1");
		} else {
			Toast.makeText(getApplicationContext(), "This device is not rooted!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//test if the device is rooted 
	private boolean isRooted(){
		return RootTools.isRootAvailable();
	}
	
	public void downloadAndInstall(String versionname) {
		Toast.makeText(getApplicationContext(),
				"Starting downloading intent..", Toast.LENGTH_SHORT).show();
		
		DownloadFile dLoad = new DownloadFile();
		dLoad.execute("http://www.cs.helsinki.fi/u/jkekalai/tellyver"+ versionname +".apk", versionname);
	
	}

	
	//install APK via shell command (incomplete)
	private void installAPK(String version){
	    Process process;
	    try {
	    	Log.i("tellyver","Version "+ version +" Installation Start...");
	        process = Runtime.getRuntime().exec(new String[] {"su", "-c", "pm", "install", "/sdcard/tellyver."+ version +".apk"});
	        
	        //"Softinstall"
	        //process = Runtime.getRuntime().exec(new String[] {"su", "-c", "adb install tellyver1.1111.apk"});
	        
	        //reinstall shell command
	        //adb install -l tellyver1.1111.apk
	        
	        //reinstall shell command, keep the app data
	        //adb install -r  tellyver1.1111.apk
	        
	        //Or install as a system app. Move to system folder and reboot
	        //process = Runtime.getRuntime().exec(new String[] {"su", "-c", "adb push file.apk /system/app/"});
	        //chmod 644 /system/app/tellyver1.1111.apk
	        //reboot somehow
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("tellyver","Version "+ version +" Installation Failure...");
	        return;
	    }
	    
	    Log.i("tellyver","Version "+ version +" Installation Should be Completed...");
	}
	
	//uninstall APK via shell command (incomplete)
		private void uninstallAPK(){
		    Process process;
		    try {
		    	Log.i("tellyver"," UnInstallation Start...");
		
		        process = Runtime.getRuntime().exec(new String[] {"adb", "shell", "am", "start", "-a", "android.intent.action.DELETE", "-d", "com.sprint.tellyver1"});

		    } catch (IOException e) {
		   
		        e.printStackTrace();

		        Log.i("tellyver"," Uninstall Failure...");
		        return;
		    }
		}
	
/*
	//lists all folders from shell command "ls"
	private void exampleShellCommand(){
		
		 try {
             //Process proc = Runtime.getRuntime().exec("ls");
			 Process proc = Runtime.getRuntime().exec("pm");
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
             String line;
             while((line = in.readLine()) != null) {
            	 Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
             }
             
         } catch (IOException e) {
             e.printStackTrace();
         }
		
	}

	*/

	private class DownloadFile extends AsyncTask<String, Integer, String> {
	    @Override
	    protected String doInBackground(String... sUrl) {
	    	String version = sUrl[1];
	        try {	        	
	        	File file = new File(getExternalCacheDir(), "/sdcard/tellyver."+version+".apk" );
	        	if (file.exists()) {
	        		 file.delete();
	        		 Log.i("tellyver","Old version deleted...");
	        	} 
	        	
	            URL url = new URL(sUrl[0]);
	            URLConnection connection = url.openConnection();
	            connection.connect();

	            int fileLength = connection.getContentLength();

	            InputStream input = new BufferedInputStream(
	                    connection.getInputStream());
	            // Create db
	            OutputStream output = new FileOutputStream("/sdcard/tellyver."+version+".apk");

	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	            }

	            output.flush();
	            output.close();
	            input.close();
	            
	        

	        } catch (Exception e) {
	        }
	        
	        installAPK(version);
	        
	        return null;
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	    }

	}
	

}
