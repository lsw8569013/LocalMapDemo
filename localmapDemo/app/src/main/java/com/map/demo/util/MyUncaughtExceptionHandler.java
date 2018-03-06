package com.map.demo.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by wb-lsw350290 on 2017/12/23.
 */

//捕获到程序未处理的异常信息，并记录到Log日志中，Log日志是自己定义的一个路径的文件；

public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    boolean canLog = false;
    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        if(!canLog){
            canLog = true;
            try {
                long time = System.currentTimeMillis();
                Date date = new Date(time);
                String timestr = date.toGMTString();
                StringBuffer sb = new StringBuffer();
                sb.append(timestr);
                sb.append("\n");
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File folder = new File(absolutePath+"/olympic");
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File log = new File(folder.getAbsolutePath() +"/"+"passport_log_"+time+".txt");
                LogUtils.e("========="+log.getAbsolutePath());
//            Field[] fields = Build.class.getDeclaredFields();
//            for (Field field : fields) {
//                String name = field.getName();
//                String values = field.get(null).toString();
//                sb.append(name + ":" + values);
//                sb.append("\n");
//            }

                StringWriter sw = new StringWriter();
                PrintWriter writer = new PrintWriter(sw);
                arg1.printStackTrace(writer);

                if(!log.exists()) {
                    log.createNewFile();
                }

                String errorlog = sw.toString();
                FileOutputStream fos = new FileOutputStream(log);
//          fos.write(sb.toString().getBytes());
                fos.write(errorlog.getBytes());
                fos.flush();
                fos.close();
                canLog = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
