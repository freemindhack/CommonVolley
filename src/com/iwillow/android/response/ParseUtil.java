package com.iwillow.android.response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import com.iwillow.android.helper.BitmapCache;
import com.iwillow.android.http.FormImage;
import com.iwillow.android.http.FormParams;
import com.iwillow.android.http.VolleyUtil;
import com.iwillow.android.request.FromObjectRequest;
import com.iwillow.android.request.GetObjectRequest;
import com.iwillow.android.request.PostObjectRequest;
import com.iwillow.android.request.PostUploadRequest;
import com.iwillow.android.request.ResponseListener;
import com.iwillow.android.request.XmlRequest;
import com.iwillow.android.request.FromObjectRequest.DataResponseListener;

/**
 * @author Eric Gao
 * 
 * @description 通用请求解析类
 * 
 * @date 2015-03-28
 *
 * 
 */
public class ParseUtil {
	/**
	 * @param url
	 * @param listener
	 */
	public static <T> void doGet(String url, ResponseListener<T> listener) {

		Request<T> request = new GetObjectRequest<T>(url, new TypeToken<T>() {
		}.getType(), listener);

		VolleyUtil.getRequestQueue().add(request);

	}

	/**
	 * @param url
	 * @param param
	 * @param listener
	 */
	public static <T> void doPost(String url, HashMap<String, String> param,
			ResponseListener<T> listener) {

		Request<T> request = new PostObjectRequest<T>(url, param,
				new TypeToken<T>() {
				}.getType(), listener);

		VolleyUtil.getRequestQueue().add(request);

	}

	public static <T> void doGetWithCache(String url,
			ResponseListener<T> listener) {

		Request<T> request = new GetObjectRequest<T>(url, new TypeToken<T>() {
		}.getType(), listener);
		request.setShouldCache(true);
		// request.setCacheTime(10 * 60);
		VolleyUtil.getRequestQueue().add(request);

	}

	public static <T> void doFormPost(String url, List<FormParams> listItem,
			Type type, DataResponseListener<T> listener) {
		Request<T> request = new FromObjectRequest<T>(url, listItem, type,
				listener);
		VolleyUtil.getRequestQueue().add(request);

	}

	public static void getXml(String url,
			ResponseListener<XmlPullParser> listener) {
		Request<XmlPullParser> request = new XmlRequest(url, listener);
		VolleyUtil.getRequestQueue().add(request);
	}

	public static void upLoadImage(String url, List<FormImage> listItem,
			ResponseListener<String> listener) {
		PostUploadRequest request = new PostUploadRequest(url, listItem,
				listener);
		VolleyUtil.getRequestQueue().add(request);
	}

	public static ImageLoader getImageLoader() {

		return new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());

	}
}
