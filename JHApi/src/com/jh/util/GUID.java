/*
 * @project POA_Android_2
 * @package com.jh.iyou.util
 * @file GUID.java
 * @version  1.0
 * @author  yourname
 * @time  2011-8-31 上午10:51:29
 * CopyRight:北京金和软件信息技术有限公司 2011-8-31
 */
package com.jh.util;

import java.util.UUID;


public class GUID {
	/*
	 *
	 * guid类.
	 *
	 * @class GUID
	 * @version  1.0
	 * @author  张楠
	 * @time  2011-8-31 上午10:51:29
	 */
	/**
	 * <code>getGUID</code>
	 * @description: 获取GUID
	 * @return 返回唯一标识id
	 * @since   2011-6-26    张楠
	 */
	public static String getGUID()
	{
		 UUID uuid=UUID.randomUUID();
		 return uuid.toString();
	}
}
