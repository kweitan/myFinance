package com.sinjee.faces.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * url生成器管理器
 * <p>Title: CzvvUrlManager</p>
 * <p>Description:TODO</p>
 * @author EX-WANGWENTAO001
 * @date 2014-6-25 下午04:51:04
 */
public class XinWenGeUrlMultipleManager {

	private List<String> aList = null ;
	private Map<String,String> map = null ;
	private String savePath = "G:/data/xinwenge/"; //
	private static final Log log = LogFactory.getLog(XinWenGeUrlMultipleManager.class);
	
	public XinWenGeUrlMultipleManager(String fileName){
		if(null == aList){
			aList = new ArrayList<String>() ;
			map = new HashMap<String,String>() ;
		}
		savePath += fileName ;
	}
	
	//抓取最新URL-实现增量抓取
	public synchronized List<String> getLatestUrlList(String fileName){
		List<String> latestList = new ArrayList<String>() ;
		String tmpFilePath = "G:/data/xinwenge/"+fileName ;
		File file = new File(tmpFilePath) ;
		File[] folders = file.listFiles() ;
		for(File folder:folders){
			if(folder.isDirectory()){
				latestList.add(folder.getName());
			}
		}
		Collections.sort(latestList) ;
		String latestDate = "" ;
		if(latestList.size()>0){
			latestDate = latestList.get(latestList.size()-1) ;
		}
		log.info(String.format("最新日期是：%s", latestDate)) ;
		
		genereUrl() ;
		
		for(String s:latestList){
			map.remove(s) ;
		}
		
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry) it
					.next();
			log.info(String.format("日期是：%s 地址：%s", (String) entry.getKey(),
					(String) entry.getValue()));
			aList.add((String) entry.getValue());
		}
		log.info("URL集合大小："+aList.size()) ;
		return aList ;
	}
	
	private synchronized void genereUrl(){
		BufferedReader br = null ;
		String filePath = savePath ;
		try {
//			log.info("开始读取文件！begin") ;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-8")) ;
//			log.info("结束读取文件！end") ;
			String str = null ;
			while((str=br.readLine())!= null){
//				log.info("开始读取文件内容！begin") ;
//				log.info("内容是："+str) ;
				String[] content = str.split("\001") ;
				String dateTimeStr = content[5] ;
				String titleUrl = content[2] ;
				map.put(dateTimeStr, titleUrl) ;
//				log.info("结束读取文件内容！end") ;
			}
			log.info("初始化成功！") ;
		} catch (Exception e) {
			log.info(e.getMessage()) ;
		} finally{
			if(null != br){
				try {
					br.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args){
		
	}
	
}
