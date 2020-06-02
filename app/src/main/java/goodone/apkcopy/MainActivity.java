package goodone.apkcopy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] perms={android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v("permission","Permission is granted");
                copy();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, perms,1);
            }
        }else{
            copy();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            copy();
        }
    }

    void copy(){
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);
        int z = 0;
        for (int i=0;i<=5;i++) {

            ResolveInfo info = (ResolveInfo) pkgAppsList.get(i);

            File f1 =new File( info.activityInfo.applicationInfo.publicSourceDir);

            Log.v("file--", " "+f1.getName()+"----"+info.loadLabel(getPackageManager()));
            try{

                String file_name = info.loadLabel(getPackageManager()).toString();
                Log.d("file_name--", " "+file_name);

                // File f2 = new File(Environment.getExternalStorageDirectory().toString()+"/Folder/"+file_name+".apk");
                // f2.createNewFile();

                File f2 = new File(Environment.getExternalStorageDirectory().toString()+"/Copied Apk");
                f2.mkdirs();
                f2 = new File(f2.getPath()+"/"+file_name+".apk");
                f2.createNewFile();

                InputStream in = new FileInputStream(f1);

                OutputStream out = new FileOutputStream(f2);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0){
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                System.out.println("File copied.");
            }
            catch(FileNotFoundException ex){
                System.out.println(ex.getMessage() + " in the specified directory.");
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
