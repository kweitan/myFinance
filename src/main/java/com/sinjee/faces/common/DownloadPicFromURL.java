package com.sinjee.faces.common;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 小小极客
 * 时间 2019/11/9 0:13
 * @ClassName DownloadPicFromURL
 * 描述 TODO
 **/
public class DownloadPicFromURL {
    //链接url下载图片
    public static synchronized void downloadPicture(String urlList,String path) {
        URL url = null;
        DataInputStream dataInputStream = null ;
        FileOutputStream fileOutputStream = null ;
        ByteArrayOutputStream output = null ;
        try {
            url = new URL(urlList);
            dataInputStream = new DataInputStream(url.openStream());

            fileOutputStream = new FileOutputStream(new File(path));
            output = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(dataInputStream);
            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(output);
        }
    }

}
