package com.jh.net.processor;
/**
 * web请求处理结果
 * @author jhzhangnan1
 *
 * @param <T>
 */
public interface IWebResult<T> {
	/**
	 * 成功
	 * @param result
	 */
	public void success(T result);
	/**
	 * 失败
	 * @param responseCode
	 * @param exception
	 */
	public void failed(int responseCode,Exception exception);
}
