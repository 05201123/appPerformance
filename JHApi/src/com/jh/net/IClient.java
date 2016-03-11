/*
 * @project testcursor
 * @package com.testcursor.util
 * @file HttpClient.java
 * @version  1.0
 * @author  yourname
 * @time  2012-1-31 下午01:21:47
 * CopyRight:北京金和软件信息技术有限公司 2012-1-31
 */
package com.jh.net;

import com.jh.exception.JHException;


public interface IClient {
	/** Comment for <code>number</code> */

	public String request(String url,String req) throws JHException;
	public byte[] requestByte(String url,String req) throws JHException;
}
