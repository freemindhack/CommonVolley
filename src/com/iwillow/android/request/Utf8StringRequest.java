package com.iwillow.android.request;

import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;

public class Utf8StringRequest extends Request<String> {
	protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";

	/**
	 * 正确处理数据回调时使用
	 */

	private final ResponseListener<String> mListener;

	public Utf8StringRequest(String url,
			ResponseListener<String> listener) {
		super(Method.GET, url, listener);
		this.mListener = listener;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String parsed;
		try {
			String type = response.headers.get(HTTP.CONTENT_TYPE);
			if (type == null) {
				type = TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			} else if (!type.contains("UTF-8")) {
				type += ";" + TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			}
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

}
