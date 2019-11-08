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
import com.paic.crawler.util.PageCrawlerUtil;

public class XinWenGeTaskImpl extends AbstractCrawlerWlerTask {

	public static WebClient webClient = null;
	private static final Log log = LogFactory.getLog(XinWenGeTaskImpl.class);
	
	public final static String savetmpPath = "G:/data/xinwenge/" ;
	
	private int runType = 0 ; //1-wujian 2-wanjian
	
	private List<String> sourceList = new ArrayList<String>() ;
	
	public List<String> getSourceList() {
		return sourceList;
	}


	public void setSourceList(List<String> sourceList) {
		this.sourceList = sourceList;
	}

	public XinWenGeTaskImpl(int runType){
		this.runType = runType ;
	}
	

	public static void main(String[] args){
//		new XinWenGeTaskImpl().parseXinWenGeWebInfo("http://www.xinwenge.net/wujian/6101316b.html","day") ;
//		new XinWenGeTaskImpl().parseXinWenGeWebInfo("http://www.xinwenge.net/wanjian/c0ccdfa8.html","night") ;
		
		//午间
//		new XinWenGeTaskImpl().handlerNavigateInfo("http://www.xinwenge.net/","wujian","wujian_title.txt") ;
		
		//晚间
//		new XinWenGeTaskImpl().handlerNavigateInfo("http://www.xinwenge.net/","wanjian","wanjian_title.txt") ;
		
		for (int i = 0; i < 2; i++) {
			AbstractCrawlerWlerTask task = new XinWenGeTaskImpl(i + 1);
			if (i == 0) {
				task.setThreadName("XinWenGeTask_wujian_" + (i+1));
			} else if (i == 1) {
				task.setThreadName("XinWenGeTask_wanjian_" + (i+1));
			}
			task.start(); // 启动
		}
	}
	
	
	@Override
	public void run() {
		List<String> list = null ;
		if(runType == 1){
			list = new XinWenGeUrlMultipleManager("wujian_title.txt").getLatestUrlList("wujian") ;
			log.info(String.format("午间List大小为：%s 休息下3秒！", list.size())) ;
			PageCrawlerUtil.sleepSecord(3);
			setSourceList(list) ;
		}else if(runType == 2){
			list = new XinWenGeUrlMultipleManager("wanjian_title.txt").getLatestUrlList("wanjian") ;
			log.info(String.format("晚间List大小为：%s 休息下3秒", list.size())) ;
			PageCrawlerUtil.sleepSecord(3);
			setSourceList(list) ;
		}
		if(sourceList.size()>0){
			for(String url:sourceList){
				if(runType == 1){
					parseXinWenGeWebInfo(url,"wujian") ;
				}else if(runType == 2){
					parseXinWenGeWebInfo(url,"wanjian") ;
				}
			}
		}else{
			log.info(String.format("源文件大小为：%s", sourceList.size())) ;
		}
		log.info(String.format("当前线程%s结束~~~~", Thread.currentThread().getName())) ;
	}

	private void parseXinWenGeWebInfo(String url,String type){
		List<String> contentLists = new ArrayList<String>() ;
		Document doc = getJsoupPage(url) ;
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat folderFormat = new SimpleDateFormat("yyyy_MM_dd");
		
		String update = folderFormat.format(new Date()) ;
		//抓取更新日期
//		Element spanEle = doc.select("div[id=contrainer] ul[class=clearfix] li div[class=info] span").first() ;
		Element spanEle = doc.select("div[id=contrainer] div[class=meta txt-shadow] p span").first() ;
		if(null != spanEle){
			update = spanEle.text().replaceAll("时间：", "") ;
		}
		//递归创建文件夹
		String folder = update ; //更新日期
		String foldName = savetmpPath+type+"/"+folder+"/" ;
		newFolder(new File(foldName)) ;
		
		Element titleEle = doc.select("div[id=contrainer] div[class=article] h1[class=heading]").first() ;
//		Elements pEles = doc.select("div[id=Cnt-Main-Article-QQ] p") ;
		Elements pEles = doc.select("div[id=contrainer] div[class=article] div[class=artCon] p") ;
		log.info(String.format("标题是：%s", titleEle==null?"":titleEle.text())) ;
		contentLists.add(titleEle==null?"":titleEle.text()+"\n") ;
		
		for(Element pEle:pEles){
			Element img = pEle.select("img").first() ;
			if(null == img){
				log.info(String.format("内容是：%s", pEle.text())) ;
				contentLists.add(pEle.text()+"\n") ;
			}else{
				String imgSrc = img.attr("abs:src") ;
				try {
//					String preSrc = imgSrc.substring(imgSrc.lastIndexOf("/")+1,imgSrc.lastIndexOf(".")) ;
					String preSrc = imgSrc.substring(imgSrc.lastIndexOf("/")+1).replaceAll("\\.", "") ;
					String fileName = sFormat.format(new Date());
					fileName = fileName+"_"+preSrc+".jpg" ;
					log.info(String.format("图像地址是：%s 文件名是：%s", imgSrc,fileName)) ;
					DownImages.getImages(imgSrc, foldName+fileName) ;
					log.info("download success！") ;
					contentLists.add(fileName+"\n") ;
					if(contentLists.size()>20){
						writerSeedFile(contentLists,foldName+folder+"_desc.txt") ;
						contentLists.clear() ;
					}
				} catch (Exception e) {
					log.info(String.format("download fail！地址为：%s ",imgSrc)) ;
					e.printStackTrace();
					contentLists.add(imgSrc+"\n") ;
				}finally{
					//休息3秒
					log.info("休息3秒！") ;
					PageCrawlerUtil.sleepSecord(3);
				}
			}
		}
		writerSeedFile(contentLists,foldName+folder+"_desc.txt") ;
		contentLists.clear() ;
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
