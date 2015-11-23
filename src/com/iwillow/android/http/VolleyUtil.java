package com.iwillow.android.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author gyzhong
 * 
 * @date 2015-03-28
 * 
 * @description this class is used for share the same RequestQueue in a project
 * 
 */
public class VolleyUtil {
	private static RequestQueue mRequestQueue;

	public static synchronized void initialize(Context context) {
		if (mRequestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (mRequestQueue == null) {
					mRequestQueue = Volley.newRequestQueue(context);
				}
			}
		}
		mRequestQueue.start();
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue == null)
			throw new RuntimeException("please initialize a RequestQueue first");
		return mRequestQueue;
	}
	
   public static void removeQuest(String tag){
	   mRequestQueue.cancelAll(tag);
   }
   
}
