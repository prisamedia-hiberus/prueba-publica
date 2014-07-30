package com.diarioas.guialigas.utils.imageutils.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageLoader {

	// MemoryCache memoryCache=new MemoryCache();
	MemoryCache memoryCache = MemoryCache.getInstance();

	FileCache fileCache;
	private final Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Context mContext;

	public ImageLoader(Context context) {
		this.mContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int stub_id = -1;

	public void DisplayImage(String url, ImageView imageView, int auxResource) {
		stub_id = auxResource;

		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	public void DisplayImageWithMask(String url, ImageView imageView,
			int auxResource, int maskResource) {
		stub_id = auxResource;

		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			// imageView.setImageBitmap(bitmap);
			maskImage(imageView, bitmap, maskResource, auxResource);
		} else {
			// queuePhoto(url, imageView);
			queuePhotoWithMask(url, imageView, maskResource);
			imageView.setImageResource(stub_id);
		}
	}

	private void maskImage(ImageView imageView, Bitmap originalBitmap,
			int markResource, int background) {
		Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(),
				markResource);
		Bitmap originalBitmapScaled = Bitmap.createScaledBitmap(originalBitmap,
				mask.getWidth(), mask.getHeight(), false);
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
				Config.ARGB_8888);
		Canvas mCanvas = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mCanvas.drawBitmap(originalBitmapScaled, 0, 0, null);
		mCanvas.drawBitmap(mask, 0, 0, paint);
		paint.setXfermode(null);
		imageView.setImageBitmap(result);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		if (result == null) {
			imageView.setBackgroundResource(background);
		}
	}

	public void DisplayImageNotCache(String url, ImageView imageView,
			int auxResource) {
		stub_id = auxResource;

		imageViews.put(imageView, url);

		queuePhoto(url, imageView);
		imageView.setImageResource(stub_id);
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhotoWithMask(String url, ImageView imageView,
			int maskResource) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, true, maskResource);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			// final int REQUIRED_SIZE=70;
			// int width_tmp=o.outWidth, height_tmp=o.outHeight;
			int scale = 1;
			/*
			 * while(true){ if(width_tmp/2<REQUIRED_SIZE ||
			 * height_tmp/2<REQUIRED_SIZE) break; width_tmp/=2; height_tmp/=2;
			 * scale*=2; }
			 */

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int maskResource;
		public boolean needMask;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
			needMask = false;
		}

		public PhotoToLoad(String u, ImageView i, boolean mask, int maskResource) {
			url = u;
			imageView = i;
			needMask = mask;
			this.maskResource = maskResource;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				if (photoToLoad.needMask) {
					maskImage(photoToLoad.imageView, bitmap,
							photoToLoad.maskResource, stub_id);
				} else {
					photoToLoad.imageView.setImageBitmap(bitmap);
				}
			} else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}