package com.iwillow.android.helper;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {
	private LruCache<String, Bitmap> mCache;
	
	public BitmapCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int maxSize = maxMemory / 8;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				Log.d("新闻",
						"图片宽度：" + bitmap.getWidth() + ";图片高度:"
								+ bitmap.getHeight());

				return bitmap.getRowBytes() * bitmap.getHeight();
				// return bitmap.getByteCount()/ 1024;//返回圖片數量
			}
		};
	}


	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		mCache.put(url, bitmap);
	}

}
