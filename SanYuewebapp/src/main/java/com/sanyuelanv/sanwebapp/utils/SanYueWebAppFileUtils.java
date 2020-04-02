package com.sanyuelanv.sanwebapp.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.sanyuelanv.sanwebapp.SanYueWebApp;
import com.sanyuelanv.sanwebapp.bean.SanYueAppMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create By songhang in 2020/2/29
 */
public class SanYueWebAppFileUtils {
    public static String rootPath = "sanyueWeb";
    public static String configPath = "appConfig.json";
    // data
    public static String getDataFromFile(File file){
        String result = "";
        try {
            InputStream inStream = new FileInputStream(file);
            if (inStream != null) {
                InputStreamReader inputReader = new InputStreamReader(inStream, "UTF-8");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line = "";
                while ((line = buffReader.readLine()) != null) {  result += line;  }
                inStream.close();
            }
        }
        catch (java.io.FileNotFoundException e) {
        } catch (IOException e) {
        }
        return  result;
    }
    public static boolean setJsonToFile(String content,File file) throws IOException {
        if (file.exists() && file.isFile()){
            file.delete();
        }
        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(file.length());
        raf.write(content.getBytes());
        raf.close();
        return  false;
    }
    public  static String getAssetsData(Context context,String name) {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = context.getAssets().open(name);
            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    // file
    public static void checkNeedDown(final String url, Context context,final  onCheckNeedLoadListener checkNeedLoadListener){
        String[] arr = url.split("\\?");
        final String urlName =  arr[0];
        final String urlPara = arr.length > 1 ? arr[1] : "";
        // URL 分割 ？ 获取前半段 hash 化查找对应路径是否存在，存在则寻找路径下config.json 文件，读出version ,是否与 ？ 后半段对应
        File rootFile = new File(context.getCacheDir(), rootPath);
        File appFileDir = null;
        File configFile = null;
        // 判断 root path 是否存在
        boolean hasAppFile = false;
        boolean needDownLoad = true;
        String appName = urlName.hashCode() + "";
        //Log.d("SanYue_APP_NAME",appName);
        if (rootFile.exists() && rootFile.isDirectory()){
            appFileDir = new File(rootFile,appName);
            if (appFileDir.exists() && appFileDir.isDirectory()){
                hasAppFile = true;
                configFile = new File(appFileDir, configPath);
            }
            else {  appFileDir.mkdir();  }
        }
        else {
            rootFile.mkdir();
            appFileDir = new File(rootFile,appName);
            appFileDir.mkdir();
        }
        if (hasAppFile && configFile != null){
            // 读取 configFile 里面的
            String jsonStr = getDataFromFile(configFile);
            JSONObject json = null;
            if (!jsonStr.equals("")){
                try {
                    json = new JSONObject(jsonStr);
                }
                catch (JSONException e){
                }
            }
            try {
                if (json != null){
                    String cache =  json.getString("cache");
                    if (cache.equals(urlPara)){
                        // 不需要下载，直接打开
                        String date = json.getString("date");
                        SanYueAppMessage sanYueAppMessage = new SanYueAppMessage(url,date,cache,appFileDir.getAbsolutePath());
                        needDownLoad = false;
                        checkNeedLoadListener.onSuccess(sanYueAppMessage,1);
                    }
                }
            }
            catch (Exception e){
                //checkNeedLoadListener.onFailed(e);
            }
        }
        if (needDownLoad){
            if (hasAppFile){
                removeFile(appFileDir);
                appFileDir.mkdir();
            }
            // 下载 & 解压
            final String finalAppFileDir = appFileDir.getAbsolutePath();
            downLoadAndUnZip(urlName, appFileDir,urlPara, new OnDownloadListener() {
                @Override
                public void onDownloadSuccess() { }

                @Override
                public void onUnZipSuccess(String date) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    final SanYueAppMessage sanYueAppMessage = new SanYueAppMessage(url,date,urlPara, finalAppFileDir);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            checkNeedLoadListener.onSuccess(sanYueAppMessage,0);
                        }
                    });
                }

                @Override
                public void onDownloading(int progress) {

                }

                @Override
                public void onDownloadFailed(final Exception e) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            checkNeedLoadListener.onFailed(e);
                        }
                    });
                }
            });

        }
    }
    public static void downLoadAndUnZip(final String url,final File downloadDir,final  String version, final OnDownloadListener listener){
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout( SanYueWebApp.getInstance().getTimeout(),TimeUnit.SECONDS).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listener.onDownloadFailed(e);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    // 文件名
                    File file = new File(downloadDir, "app.zip");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                    // 解压
                    ZipInputStream inZip = new ZipInputStream(new FileInputStream(file.getAbsoluteFile()));
                    ZipEntry zipEntry;
                    String  szName = "";
                    while ((zipEntry = inZip.getNextEntry()) != null) {
                        szName = zipEntry.getName();
                        if (zipEntry.isDirectory()) {
                            //获取部件的文件夹名
                            szName = szName.substring(0, szName.length() - 1);
                            File folder = new File(downloadDir,szName);
                            folder.mkdirs();
                        }
                        else {
                            File newFile = new File(downloadDir, szName);
                            if (!newFile.exists()){
                                newFile.getParentFile().mkdirs();
                                newFile.createNewFile();
                            }
                            // 获取文件的输出流
                            FileOutputStream out = new FileOutputStream(newFile);
                            int length;
                            byte[] buffer = new byte[1024];
                            // 读取（字节）字节到缓冲区
                            while ((length = inZip.read(buffer)) != -1) {
                                // 从缓冲区（0）位置写入（字节）字节
                                out.write(buffer, 0, length);
                                out.flush();
                            }
                            out.close();
                        }
                    }
                    inZip.close();
                    // 删除 app.zip
                    file.delete();
                    // 写入 JSON  date & cache
                    JSONObject data = new JSONObject();
                    String now = getCurrentTime();
                    data.put("date",now);
                    data.put("cache",version);
                    setJsonToFile(data.toString(),new File(downloadDir,configPath));
                    listener.onUnZipSuccess(now);
                }
                catch (Exception e) {  listener.onDownloadFailed(e);  }
                finally {
                    try { if (is != null)  is.close();  }  catch (IOException e) {  }
                    try { if (fos != null)  fos.close(); }  catch (IOException e) { }
                }
            }
        });
    }
    public static void removeFile(File file){
        //如果是文件直接删除
        if(file.isFile()){
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                removeFile(f);
            }
            file.delete();
        }
    }
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }
    public static long getDirSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) size = size + getDirSize(fileList[i]);
                else size = size + fileList[i].length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    public static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileSize==0){
            return wrongSize;
        }
        if (fileSize < 1024){
            fileSizeString = df.format((double) fileSize) + "B";
        }
        else if (fileSize < 1048576){
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        }
        else if (fileSize < 1073741824){
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public interface OnDownloadListener {
        void onDownloadSuccess();
        void onUnZipSuccess(String date);
        void onDownloading(int progress);
        void onDownloadFailed(Exception e);
    }
    public  interface  onCheckNeedLoadListener{
        void onFailed(Exception e);
        void onSuccess(SanYueAppMessage sanYueAppMessage, int type);
    }
}
