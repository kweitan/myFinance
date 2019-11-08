package com.sinjee.faces.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.paic.crawler.task.AbstractCrawlerWlerTask;
import com.paic.crawler.util.DateUtil;
import com.paic.crawler.util.PageCrawlerUtil;

public class XinWenGeMainTaskImpl extends AbstractCrawlerWlerTask {

	public static WebClient webClient = null;
	private static final Log log = LogFactory.getLog(XinWenGeMainTaskImpl.class);
	
	public final static String savetmpPath = "G:/data/xinwenge/" ;
	
	private int runType = 1 ; //1-wujian 2-wanjian
	
	private String postFileName = null ;
	
	private String latestDateTime = null ;
	
	public XinWenGeMainTaskImpl(int type){
//		this.postFileName = fileName ;
//		this.latestDateTime = latestDateTime ;
		this.runType = type ;
	}

	public static void main(String[] args){
		
		for(int i=0;i<2;i++){
			AbstractCrawlerWlerTask xwgTask = new XinWenGeMainTaskImpl(i+1) ;
			if(i == 0){
				xwgTask.setThreadName("XinWenGeTask_wujian_"+(i+1)) ;
			}else if(i == 1){
				xwgTask.setThreadName("XinWenGeTask_wanjian_"+(i+1)) ;
				
			}
			xwgTask.start() ;
		}
	}
	
	
	@Override
	public void run() {
		if(runType == 1){
			String latestDateTime = new XinWenGeDateSingleManager("wujian_title.txt").getLatestDateTime() ;
			this.latestDateTime = latestDateTime ;
			this.postFileName = "wujian_title.txt" ;
			handlerNavigateInfo("http://www.xinwenge.net/","wujian",this.postFileName) ;
		}else if(runType == 2){
			String latestDateTime = new XinWenGeDateSingleManager("wanjian_title.txt").getLatestDateTime() ;
			this.latestDateTime = latestDateTime ;
			this.postFileName = "wanjian_title.txt" ;
			handlerNavigateInfo("http://www.xinwenge.net/","wanjian",this.postFileName) ;
		}
		
	}


	/**
	 * 
	 * 功能说明
	 *
	 * EX-WANGWENTAO001 2014-7-15 
	 * 
	 * @param loadUrl
	 * @param wujian|wanjian
	 * @return void
	 * @throws
	 */
	private void handlerNavigateInfo(String loadUrl,String type,String fileName){
		List<String> navList = new ArrayList<String>() ;
		int i = 1 ;
		Document doc = null ;
		String strUrl = "" ;
		String postFileName = fileName ; //文件后缀
		boolean flag = true ;
		while(true){
			strUrl = loadUrl+type+"/?p="+i ;
			doc = getJsoupPage(strUrl) ;
			if(null != doc){
				Elements liEles = doc.select("div[id=contrainer] ul[class=clearfix] li") ;
				if(liEles.size()>0 && flag){
					if(liEles.size()<8){
						flag = false ;
					}
					for (Element liEle:liEles) {
						DescInfo desc = new DescInfo() ;
						//id
						desc.setId(UUIDHexGeneratorManager.getInstance().getUUID()) ;
						//创建日期
						desc.setCreateDate(DateUtil.getCurrentTimeString()) ;
						//1.标注Callout 更新日期
						Element divEle = liEle.select("div[class=info] span").first() ;
						String callout = "" ;
						if(null != divEle){
							callout = DateUtil.replaceKeyTab(divEle.text()) ;
							log.info(String.format("当前最新日期是：%s", callout)) ;
							if(callout.equals(latestDateTime)){
								//跳出
								log.info(String.format("当前最新日期是：%s 历史文件的最新日期是：%s 相等跳出！", callout,latestDateTime)) ;
								flag = false ;
								break ;
							}else{
								desc.setCallout(DateUtil.changeDateStr1(callout)) ;
							}
						}
						
						//2.标题title
						String titleUrl = "" ;
						String titleName = "" ;
						Element aEle = liEle.select("h2[class=heading] a").first() ;
						if(null != aEle){
							titleName = aEle.text() ;
							titleUrl = aEle.attr("abs:href") ;
							desc.setTitleName(titleName) ;
							desc.setTitleUrl(titleUrl) ;
						}
						
						//3.概要Summary
						Element summaryEle = liEle.select("div[class=main row-fluid] div[class=desc pull-left] p").first() ;
						String summary = "" ;
						if(null != summaryEle){
							summary = summaryEle.text() ;
							desc.setSummary(summary) ;
						}
						//4.图片pic
						Element picEle = liEle.select("div[class=main row-fluid] div[class=thumbnail pull-left] img").first() ;
						String pic = "" ;
						if(null != picEle){
							pic = picEle.attr("abs:src") ;
							desc.setPic(pic) ;
						}
						log.info(String.format("第 %s 页  内容是：%s 更新时间：%s ", i,titleName,callout)) ;
						navList.add(desc.toString()) ;
						if(navList.size()>30){
							writerSeedFile(navList,savetmpPath+postFileName) ;
							navList.clear() ;
						}
					}
					if(!flag){
						//跳出while
						log.info("结束while！") ;
						break ;
					}
				}else{
					log.info(String.format("不存在URL：%s", strUrl)) ;
					break ;
				}
				log.info(String.format("休息3秒！URL:%s",strUrl)) ;
				PageCrawlerUtil.sleepSecord(3);
			}else{
				log.info(String.format("不存在：%s", loadUrl)) ;
				PageCrawlerUtil.sleepSecord(3);
			}
			i++ ; //
		}
		
		writerSeedFile(navList,savetmpPath+postFileName) ;
		navList.clear() ;
		log.info(String.format("当前线程%s结束~~~~", Thread.currentThread().getName())) ;
	}
	
	
	private String getUsedTime(long starttime) {
		long endtime = System.currentTimeMillis();
		long usedsecond = (endtime - starttime) / 1000;
		return (usedsecond / 60) + "分" + (usedsecond % 60) + "秒";
	}
	
	public Document getJsoupPage(String url) {
		int count = 0;
		Document doc = null;
		while (count < 5) {
			count++;
			try {
				String referrer = "http://news.qq.com/";
				doc = Jsoup
						.connect(url)
						.userAgent(
								"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
						.referrer(referrer).timeout(1000 * 60 * 5).get();

				PageCrawlerUtil.sleepSecord((long) (Math.random() * 5) + 5);
				return doc;
			} catch (Exception e) {
				String message = e.getMessage().trim();
				log.error("========================: 第 " + count
						+ " 次，webClient访问网络连接异常,访问URL:" + url + "，原因："
						+ message);

				if (message.startsWith("403")) {// IP被禁止访问
					log.info("本机IP被禁止访问，休眠30分钟...");
					PageCrawlerUtil.sleepSecord(60 * 30);
				}

				PageCrawlerUtil.sleepSecord(20);
			}
		}
		return Jsoup.parse("<div></div>");
	}

	public static HtmlPage getHtmlPage(String url) {
		int count = 0;
		while (count < 5) {
			count++;
			try {
				HtmlPage page = webClient.getPage(url);
				webClient.waitForBackgroundJavaScript(3000);
				int statuscode = page.getWebResponse().getStatusCode();
				PageCrawlerUtil.sleepSecord((long) (Math.random() * 3) + 3);
				if (statuscode == 200) {
					return page;
				} else {
					log.info("========================: 第 " + count
							+ " 次，webClient访问网络连接异常,访问URL:" + url
							+ "，statuscode：" + statuscode);
				}
			} catch (Exception e) {
				String message = e.getMessage().trim();
				log.error("========================: 第 " + count
						+ " 次，webClient访问网络连接异常,访问URL:" + url + "，原因："
						+ message);

				if (message.startsWith("403")) {// IP被禁止访问
					log.info("本机IP被阿里巴巴禁止访问，休眠30分钟...");
					PageCrawlerUtil.sleepSecord(60 * 30);
				}

				PageCrawlerUtil.sleepSecord(20);
			}
		}
		return null;
	}

	public static Document getPage(String url) {
		HtmlPage page = getHtmlPage(url);
		if (page != null) {
			return Jsoup.parse(page.asXml());
		} else {
			return Jsoup.parse("<div></div>");
		}
	}

	public static WebClient creatWebClient() {
		BrowserVersion bv = BrowserVersion.FIREFOX_17;
		WebClient webClient = new WebClient(bv);
		WebClientOptions option = webClient.getOptions();
		option.setCssEnabled(false);
		option.setThrowExceptionOnScriptError(true);

		option.setRedirectEnabled(true);
		option.setPopupBlockerEnabled(true);
		option.setActiveXNative(true);

		option.setJavaScriptEnabled(true);
		option.setRedirectEnabled(true);
		option.setActiveXNative(true);
		option.setTimeout(1000 * 60 * 2);
		webClient.waitForBackgroundJavaScriptStartingBefore(20000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setJavaScriptTimeout(1000 * 60);
		// 设置最大缓存数为0 默认为25--控制CPU使用率

		// ProxyConfig proxyConfig = new ProxyConfig("111.161.126.88", 8080);
		// webClient.setProxyConfig(proxyConfig);
		webClient.getCache().setMaxSize(0);
		return webClient;
	}
	
	private synchronized void writerSeedFile(List<String> expandSeedList,String fileName){
		FileOutputStream fos = null;
		OutputStreamWriter os = null;
		BufferedWriter bw =null;
		try {
			 fos = new FileOutputStream(fileName,true);
			 os = new OutputStreamWriter(fos, "UTF-8");
			 bw = new BufferedWriter(os);
			for (String expandSeedResult : expandSeedList)
			{
				bw.write(expandSeedResult);
				bw.newLine();
			}
		} catch (IOException e) {
			log.error("BufferedWriter 写入传众本地种子扩展文件时发生异常！",e);
		}
		finally
		{
			if(null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} 
	
	/**
	 * 递归创建文件夹
	 * @param file 
	 */
	 public static void newFolder(File file){
		 
		try {
			if (file.getParentFile().exists()) {
				file.mkdir();
			} else {
				newFolder(file.getParentFile());
				file.mkdir();
			}
		} catch (Exception e) {
			log.info("新建目录操作出错");
			e.printStackTrace();
		}
	 }
}
