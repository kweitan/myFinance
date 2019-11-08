package com.sinjee.faces.demo;

import com.paic.crawler.seed.AbstractSeedProcess;

/**
 * 任务接口
 * @author c
 *
 */
public class AbstractCrawlerWlerTask extends Thread {

	protected AbstractSeedProcess seedProcess;
    
	protected boolean isSleep;
	
	/**
	 * 验证码字符串
	 */
	protected String checkCode;
	/**
	 * 是否正在输入验证码
	 */
	protected boolean inputCheckCodeing;
	/**
	 * 输入的验证码是否通过
	 */
	protected boolean inputCheckCodePass;
	/**
	 * 最近调用输入验证码的时间戳
	 */
	protected long callinputCheckCodeingTimeMillis;
	
	
	
	public boolean isInputCheckCodeing() {
		return inputCheckCodeing;
	}

	public void setInputCheckCodeing(boolean inputCheckCodeing) {
		this.inputCheckCodeing = inputCheckCodeing;
	}

	public boolean isInputCheckCodePass() {
		return inputCheckCodePass;
	}

	public void setInputCheckCodePass(boolean inputCheckCodePass) {
		this.inputCheckCodePass = inputCheckCodePass;
	}

	public long getCallinputCheckCodeingTimeMillis() {
		return callinputCheckCodeingTimeMillis;
	}

	public void setCallinputCheckCodeingTimeMillis(
			long callinputCheckCodeingTimeMillis) {
		this.callinputCheckCodeingTimeMillis = callinputCheckCodeingTimeMillis;
	}

	public String getCheckCode() {
		return checkCode;
	}

	/**
	 * 种子获取线程
	 * @param seedProcess
	 */
    public void setSeedProcess(AbstractSeedProcess seedProcess)
    {
    	this.seedProcess = seedProcess;
    }
    
    /**
     * 设置当前线程任务名称
     * @param threadName
     */
    public void setThreadName(String threadName)
    {
    	this.setName(threadName);
    }
   
    /**
     * 当前任务是否暂停
     * @return
     */
	public boolean isSleep()
	{
		return isSleep;
	}
	
	/**
	 * 设置任务的状态
	 * @param isSleep
	 */
	public void runTask(boolean run)
	{
		if(run)
		{
			this.notify();
			isSleep = false;
		}
		else
		{
			try {
				this.wait();
				isSleep = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}
