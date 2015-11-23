package com.iwillow.android.request;

import com.android.volley.Response;

/**
 * @author gyzhong
 * 
 * @date 2015-03-01
 * 
 * @description 简化回调接口,这里把错误回调Response.ErrorListener
 *              和正确的数据回调Response.Listener合并成一个ResponseListener
 *
 * @param <T> 实体类型
 */
public interface ResponseListener<T> extends Response.ErrorListener,
		Response.Listener<T> {

}
