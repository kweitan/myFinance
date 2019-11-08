package com.sinjee.faces.demo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.paic.crawler.util.FileUtil;
import com.paic.crawler.util.PageCrawlerUtil;

public abstract class AbstractSeedProcess extends Thread implements SeedInfoInteface{
    /**
	 * 记录种子来源的map 0：本地  1：远程 2:本地SP（特别种子）
	 */
	private Map<String,Integer> seedSourceMap = new Hashtable<String,Integer>();
	
	private Vector<String> seedList =new Vector();
	private Vector<String> successSeedList=new Vector();
	
	/**
	 * 记录执行完毕的种子(包括成功和失败)
	 */
	private Vector<String> fileSuccessSeedList = new Vector();
	
	/**
	 * 只抓取seed_sp中的种子
	 */
	private String onlySpSeed = PageCrawlerUtil.getConfigValue("crawler.seed.onlysp");
	
	
	private  int maxSeedListCount=1;
	protected static final Log log = LogFactory.getLog(AbstractSeedProcess.class);
	private WebClient webClient=null;
	// 是否是本地文件的种子
	private boolean fileSeed;
	
	public AbstractSeedProcess(WebClient webClient) {
		webClient.getOptions().setJavaScriptEnabled(false);
		this.webClient =webClient;
		
//		try {
//			maxSeedListCount=Integer.parseInt(PageCrawlerUtil.getConfigValue("crawler.max.process.count"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.equals(e);
//		}
	}
	
	
	public WebClient getWebClient() {
		return webClient;
	}


	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}


	public int getMaxSeedListCount() {
		return maxSeedListCount;
	}

	public void setMaxSeedListCount(int maxSeedListCount) {
		this.maxSeedListCount = maxSeedListCount;
	}

	// 获取固定数量种子1个
	public synchronized List<String> getSeed() {
		List<String> resultList = new ArrayList<String>();
		if(seedList.size() == 0)
		{ 
			// 种子列表为空，已经处理完成，种子还未获取等待
			return resultList;
		}
		
		String seed = seedList.get(0);
		seedList.remove(seed);
		resultList.add(seed);
		
		log.info("AbstractSeedProcess缓存中种子数量:"+seedList.size()+"===========================================");
		
//		Iterator<String> it = seedList.iterator();
//		int count = 0;
//		while (it.hasNext()) {
//			resultList.add(it.next());
//			it.remove();
////			if (count++ > 5)
//				break;
//		}
		
		return resultList;
	}

	public int getSeedListSize() {
		return seedList.size();
	}

	public int saveSeedList(List<String> seedList) {
		if (seedList == null)
			return 0;
		if (seedList.size() > 0)
			successSeedList.addAll(seedList);
		return successSeedList.size();
	}
	
	/**
	 * 更新本地成功的种子文件
	 * @param seed
	 * @param toRun 是否马上更新
	 */
	public void saveLoactSeedList(String seed)
	{
		if(fileSuccessSeedList == null)
		{
			return;
		}
		fileSuccessSeedList.add(seed);
	}
	
	public Vector<String> getSeedList() {
		return seedList;
	}

	public Vector<String> getSuccessSeedList() {
		return successSeedList;
	}

	/**
	 * 线程进入休眠状态标识，用户不同时段资源分配
	 */
	private boolean isSleep=false;

	public boolean isSleep() {
		return isSleep;
	}

	public void setSleep(boolean isSleep) {
		this.isSleep = isSleep;
	}

	//取文件存放目录
	public abstract String getFileStoreRootPath()throws Exception;
	//从文件中获取种子
	public abstract List<String> getSeedFromFile()throws Exception; 
	// 获取特别的种子
	public abstract List<String> getSPSeedFromFile()throws Exception; 
	// 移除本地的缓冲种子记录
	public abstract void removeLocalSucessSeed(List<String> successSeedList) throws Exception;
	// 保存到本地缓存文件
	public abstract void saveLocalCacheSeed(List<String> seedList) throws Exception;
	//从远程服务器获取种子
	public abstract List<String> getSeedFromRemoteServer()throws Exception; //种子处理完后调用
	//保存每次获取的种子
	public abstract void addToListFile(List<String> seeds,int source)throws Exception;
	//保存处理成功的种子到服务器
	public abstract void saveSuccessFeed(List<String> successSeedList,boolean fileSeed)throws Exception;
	
	//每次获取多少个种子
	public abstract int getRemoteServiceSeedCountMaxNum()throws Exception;
	
	//保存处理成功的种子到服务器
	public void saveFailFeed(String seed)
	{
		synchronized (this) {
			String seedFailPath = "";
			try {
				
				String filePath = getFileStoreRootPath();
				// 如果是特别种子
				if (this.isSpSeed(seed)) {
					if(filePath.endsWith("/seed"))
					{
						filePath = filePath.replaceAll("/seed", "/seed_sp");
					}
					else
					{
						filePath = filePath.replaceAll("/seed/", "/seed_sp/");
					}	
				}

				seedFailPath = filePath + "/seed_fail.txt";
				String failseeds = FileUtil.readFiletoStr(seedFailPath);
				
				if(failseeds.indexOf(","+seed+",") == -1 || !failseeds.startsWith(seed+",")){
					log.info("保存失败种子["+seed+"]到 " + seedFailPath);
					PageCrawlerUtil.addToFile(seedFailPath, seed + ",");
				}
			} catch (Exception e) {
				log.info("保存失败种子["+seed+"]到"+seedFailPath+"失败！",e);
			}
		}
	}

	public void run() {
//		String fileStoreRootPath=getFileStoreRootPath();
		while (true) {
			try {
				// 移除本本地缓冲种子TODO: 命名不规范
				removeLocalSucessSeed(fileSuccessSeedList);
				// 移除记录种子来源的Map记录
				remvoeSeedSourceMapRecord(fileSuccessSeedList);
				// 保存一次清理一次
				fileSuccessSeedList.clear();
				
				if(isSleep){
					log.info("seed thread is sleep 种子线程进入休眠状态.");
					webClient.closeAllWindows(); //进入睡眠状态，连接关闭释放资源
				}
				
				while(isSleep){
					PageCrawlerUtil.sleepSecord(10);
				}
				
				if (seedList.size() <= 0) {
				// 取种子加入
					List seeds=null;
					fileSeed = true;
					
					// 从特别种子文件中获取种子
					seeds =  getSpSeedFromFile();
					
					// 非只抓取seed_sp种子
					if(!onlySpSeed.equals("true"))
					{
						if(null == seeds || seeds.size() ==0)
						{
							// 从本地文件获取种子		
							seeds = geSeedFromFile();
						}
						
						// 本地获取不到种子,从远程获取种子		
						if(null == seeds || seeds.size() ==0)
						{
							seeds = getSeedsFromRemoteServer();
						}
					}	
					
					if(seeds!=null&&seeds.size()>0){
						seedList.addAll(seeds);
					}
					else
					{
						log.info("无种子================================================");
					}
				}
				
				// 成功的种子，保存（文件或者服务器）
				if(successSeedList.size()>0){
					Iterator  it = successSeedList.iterator();  
					List<String> saveList=new ArrayList<String>();
					while(it.hasNext()){
						String seed=(String)it.next();
						saveList.add(seed);
						it.remove();
					}
					
					//调用远程接口，更新种子的状态
					saveSuccessFeed(saveList,fileSeed);
				}

			} catch (Exception e) {
				log.error(e);
			} finally {
				try {
					webClient.closeAllWindows();
					sleep(30000);
					log.info("休眠30秒");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage(), e);
					return;
				}
			}
		}
	}
	
	/**
	 * 获取特别种子
	 * @return
	 */
	private List getSpSeedFromFile()
	{
		List seeds = null;
		try {
			seeds=getSPSeedFromFile();
			// 登记种子来源:特别种子
			recordSeedSourceMap(seeds, 2);
			// 登记到本地种子记录文件中
			addToListFile(seeds,2);
		   } catch (Exception e) {
		}	
		
		int seedNum = (null == seeds)?0:seeds.size();
		log.info("获取SP种子数量："+seedNum+" ===============================");
	   return seeds;
	}
	
	/**
	 * 从本地文件获取种子
	 * @return
	 */
	private List geSeedFromFile()
	{
		List seeds = null;
		try {
			seeds=getSeedFromFile();
			// 登记种子来源:本地种子
			recordSeedSourceMap(seeds, 0);
			// 登记到本地种子记录文件中
			addToListFile(seeds,0);
			//seeds=manager.getLocalSeedList(PageCrawlerUtil.getConfigValue("crawler.seed.list.file.path.name"));
		} catch (Exception e) {
			//log.error(e);
		}	
		
		int seedNum = (null == seeds)?0:seeds.size();
		log.info("获取本地种子数量："+seedNum+" ===============================");
		
	   return seeds;
	}
	
	/**
	 * 从远程服务器获取种子
	 * @return
	 */
	private List getSeedsFromRemoteServer()
	{
		List seeds = null;
		fileSeed = false;
		try {
			seeds=getSeedFromRemoteServer();
		// 登记种子来源：远程种子
		recordSeedSourceMap(seeds, 1);
		// 登记到本地种子记录文件中
		addToListFile(seeds,1);
		// 先保存到本地缓存文件
		saveLocalCacheSeed(seeds);
		
		} catch (Exception e) {
			log.error("获取远程种子失败：",e);
		}
		
		int seedNum = (null == seeds)?0:seeds.size();
		log.info("获取远程种子数量："+seedNum+" ===============================");
		
		return seeds;
	}
	
	/**
	 * 记录种子来源
	 * @param seedList
	 * @param source
	 */
	private synchronized void recordSeedSourceMap(List<String> seedList, int source)
	{
		if(null == seedList)
		{
			return;
		}
		
		// 登记种子来源
		for(String seed : seedList)
		{
			seedSourceMap.put(seed, source);
		}
	}
	
	/**
	 * 移除种子来源的记录
	 * @param seed
	 */
	private synchronized void remvoeSeedSourceMapRecord(List<String> seedList)
	{
		if(seedList.size() == 0)
		{
			return;
		}
		
		int size = seedSourceMap.size();
		log.info("开始移除种子来源Map seedSourceMap.size:"+size+",需要移除种子数："+seedList.size()+"=================================================");
		for(String seed:seedList)
		{
			seedSourceMap.remove(seed);
		}
		
		log.info("移除种子来源Map完毕 seedSourceMap.size:"+seedSourceMap.size()+",共移除记录数:"+(size-seedSourceMap.size())+"=================================================");
	}
	
	@Override
	public boolean isSpSeed(String seed) {
		Integer source = seedSourceMap.get(seed);
		if(null != source)
		{
			return source == SEED_SOURCE_SP;
		}
		
		return false;
	}
}
