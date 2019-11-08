package com.sinjee.faces.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
public class XinWenGeDateSingleManager {

	private Set<String> setList = null ;
	private List<String> aList = null ;
	private List<String> resultList = null ;
	// (/aaaa/xinwenge/wujian_title.txt) -> 午间版
	private String savePath = "G:/data/xinwenge/"; //
	private static final Log log = LogFactory.getLog(XinWenGeDateSingleManager.class);
	
	public XinWenGeDateSingleManager(String fileName){
		if(null == setList){
			setList = new HashSet<String>() ;
			aList = new ArrayList<String>() ;
			resultList = new ArrayList<String>() ;
		}
		savePath += fileName;
		genereUrl() ;
		if (aList.size() > 0) {
			Iterator iter = aList.iterator();
			while (iter.hasNext()) {
				setList.add((String) iter.next());
			}
			aList.clear() ;
		}
		if(setList.size()>0){
			Iterator iter = setList.iterator();
			while (iter.hasNext()) {
				resultList.add((String) iter.next());
			}
			setList.clear() ;
		}
		Collections.sort(resultList) ;
		log.info("URL集合大小："+resultList.size()) ;
	}
	
	/**
	 * 
	 * 获取最新日期
	 *
	 * EX-WANGWENTAO001 2014-7-17 
	 * 
	 * @return
	 * @return String
	 * @throws
	 */
	public String getLatestDateTime(){
		String latestDateTime = null ;
		if(resultList.size() > 0){
			latestDateTime = (String)resultList.remove(resultList.size()-1) ;
		}
		resultList.clear() ; //
		return latestDateTime ;
	}
	
	public synchronized String getUrl(){
		String url = "" ;
		if(resultList.size() > 0){
			url = (String)resultList.remove(0) ;
		}
		return url ;
	}
	
	public synchronized List<String> getUrlList(){
		return resultList ;
	}
	
	private void genereUrl(){
		
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
				String titleDate = content[5] ;
				aList.add(DateUtil.changeDateStr1(titleDate)) ;
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
		//
		
	}
	
}
