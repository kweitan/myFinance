package com.sinjee.faces.demo;


public enum CrawlerWlerTaskEnum {
 
	/**
	 * 获取商品数据
	 */
	ITEMS(1,"商品","获取商品评论","com.paic.crawler.task.impl.ItemsTaskImpl"),
	/**
	 * 获取商铺数据
	 */
	SELLER(2,"店铺","获取天猫店铺","com.paic.crawler.task.impl.SellTaskImpl"),
	/**
	 * 获取用户数据
	 */
	USER(3,"用户","获取用户信息","com.paic.crawler.parser.taobao.TBUserTaskImpl"),
	/**
	 * 搜索前十商品
	 */
	TOP10(4,"前十商品","获取前十商品信息","com.paic.crawler.parser.taobao.TBGoodsSearchTaskImpl");
	
	/**
	 * 任务Id
	 */
	private int taksId;
	
	/**
	 * 任务名称
	 */
	private String taskName;
	
	/**
	 * 任务描述
	 */
	private String taskDesc;
	/**
	 * 实现类全名称
	 */
	private String className;
	/**
	 * 搜索关键字
	 */
    private String searchKey;
	
	
	CrawlerWlerTaskEnum(int taskId,String taskName,String taskDesc,String className)
	{
		this.taksId = taskId;
		this.taskName = taskName;
		this.taskDesc = taskDesc;
		this.className = className;
	}

	public int getTaksId() {
		return taksId;
	}

	public void setTaksId(int taksId) {
		this.taksId = taksId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
}
