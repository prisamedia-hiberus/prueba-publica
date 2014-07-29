package com.diarioas.guiamundial.dao.reader;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;

public class ImageDAO {

	private static ImageDAO sInstance = null;
	private Context mContext = null;
	private ImageFetcher mImageFetcher = null;
	private ImageFetcher mImageFetcherOne = null;
	private ImageFetcher mImageFetcherHalf = null;
	private ImageFetcher mImageFetcherThird = null;
	private ImageFetcher mImageFetcherFourth = null;
	private ImageFetcher mImageFetcherHeader = null;
	private ImageFetcher mImageFetcherSplash = null;
	private ImageFetcher mImageFetcherActionBar = null;

	public static ImageDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new ImageDAO();
			sInstance.mContext = ctx;
			sInstance.init();
		}
		return sInstance;
	}

	public static void removeInstance() {
		sInstance.mImageFetcher = null;
		sInstance.mImageFetcherOne = null;
		sInstance.mImageFetcherHalf = null;
		sInstance.mImageFetcherThird = null;
		sInstance.mImageFetcherFourth = null;
		sInstance.mImageFetcherHeader = null;
		sInstance.mImageFetcherSplash = null;		
		sInstance.mImageFetcherActionBar = null;

		sInstance = null;

	}

	private void init() {

		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		Point size = DimenUtils.getSize(windowManager);

		configureImageFetcher(size.x / 2, size.y / 2);
		configureImageFetcherOne(size.x, size.y);
		configureImageFetcherTwo(size.x, size.y / 2);
		configureImageFetcherThree(size.x, size.y / 3);
		configureImageFetcherFourth(size.x, size.y / 4);
		configureImageFetcherHeader(size.x / 2, size.y / 4);
		configurSplashImageFetcher();
		configurActionBarImageFetcher();
	}

	/**
	 * Configure the image manager
	 */
	private void configureImageFetcher(int width, int height) {

		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS);
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(mContext, width, height);
		mImageFetcher.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		this.mImageFetcher.setImageFadeIn(true);

	}

	private void configureImageFetcherOne(int width, int height) {
		ImageCacheParams cacheParamsOne = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "1");
		cacheParamsOne.setMemCacheSizePercent(0.15f);

		mImageFetcherOne = new ImageFetcher(mContext, width, height);
		mImageFetcherOne.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParamsOne);
		this.mImageFetcherOne.setImageFadeIn(true);
	}

	private void configureImageFetcherTwo(int width, int height) {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "2");
		cacheParams.setMemCacheSizePercent(0.20f);

		mImageFetcherHalf = new ImageFetcher(mContext, width, height);
		mImageFetcherHalf.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		this.mImageFetcherHalf.setImageFadeIn(true);
	}

	private void configureImageFetcherThree(int width, int height) {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "3");
		cacheParams.setMemCacheSizePercent(0.20f);

		mImageFetcherThird = new ImageFetcher(mContext, width, height);
		mImageFetcherThird.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		this.mImageFetcherThird.setImageFadeIn(true);
	}

	private void configureImageFetcherFourth(int width, int height) {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "4");
		cacheParams.setMemCacheSizePercent(0.15f);

		mImageFetcherFourth = new ImageFetcher(mContext, width, height);
		mImageFetcherFourth.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		this.mImageFetcherFourth.setImageFadeIn(true);
	}

	private void configureImageFetcherHeader(int width, int height) {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "1");
		cacheParams.setMemCacheSizePercent(0.15f);

		mImageFetcherHeader = new ImageFetcher(mContext, width, height);
		mImageFetcherHeader.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		this.mImageFetcherHeader.setImageFadeIn(true);
	}

	private void configurSplashImageFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "splash");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcherSplash = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_thumb_height));
		// mImageFetcherSplash.setLoadingImage(R.drawable.galeria_imagenrecurso);
		mImageFetcherSplash.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		// mImageFetcherSplash.setImageFadeIn(true);
	}

	private void configurActionBarImageFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "actionbar");
		cacheParams.setMemCacheSizePercent(0.10f);

		mImageFetcherActionBar = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_actionbar_height));
		mImageFetcherActionBar.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
		// mImageFetcherActionBar.setImageFadeIn(true);
	}

	/***************************************************************************/
	/** Public methods **/
	/***************************************************************************/

	public void exitTaskEarly() {
		this.mImageFetcher.setExitTasksEarly(false);
		this.mImageFetcherOne.setExitTasksEarly(false);
		this.mImageFetcherHalf.setExitTasksEarly(false);
		this.mImageFetcherThird.setExitTasksEarly(false);
		this.mImageFetcherFourth.setExitTasksEarly(false);
		this.mImageFetcherHeader.setExitTasksEarly(false);
		this.mImageFetcherSplash.setExitTasksEarly(false);
		this.mImageFetcherActionBar.setExitTasksEarly(false);
	}

	public void clearCache() {
		this.mImageFetcher.clearCache();
		this.mImageFetcherOne.clearCache();
		this.mImageFetcherHalf.clearCache();
		this.mImageFetcherThird.clearCache();
		this.mImageFetcherFourth.clearCache();
		this.mImageFetcherHeader.clearCache();
		this.mImageFetcherSplash.clearCache();
		this.mImageFetcherActionBar.clearCache();
	}

	public void flushCache() {
		this.mImageFetcher.flushCache();
		this.mImageFetcherOne.flushCache();
		this.mImageFetcherHalf.flushCache();
		this.mImageFetcherThird.flushCache();
		this.mImageFetcherFourth.flushCache();
		this.mImageFetcherHeader.flushCache();
		this.mImageFetcherSplash.flushCache();
		this.mImageFetcherActionBar.flushCache();
	}

	public void closeCache() {
		this.mImageFetcher.closeCache();
		this.mImageFetcherOne.closeCache();
		this.mImageFetcherHalf.closeCache();
		this.mImageFetcherThird.closeCache();
		this.mImageFetcherFourth.closeCache();
		this.mImageFetcherHeader.closeCache();
		this.mImageFetcherSplash.closeCache();
		this.mImageFetcherActionBar.closeCache();
	}

	public void pauseWork(boolean pause) {
		this.mImageFetcher.setPauseWork(pause);
		this.mImageFetcherOne.setPauseWork(pause);
		this.mImageFetcherHalf.setPauseWork(pause);
		this.mImageFetcherThird.setPauseWork(pause);
		this.mImageFetcherFourth.setPauseWork(pause);
		this.mImageFetcherHeader.setPauseWork(pause);
		this.mImageFetcherSplash.setPauseWork(pause);
		this.mImageFetcherActionBar.setPauseWork(pause);
	}

	/***************************************************************************/
	/** Image methods **/
	/***************************************************************************/

	public ImageFetcher getImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcher;
	}

	public void loadImage(String imageUrl, ImageView imageView) {
		this.mImageFetcher.loadImage(imageUrl, imageView);
	}

	public void loadImage(String imageUrl, ImageView imageView, int loadingResId) {
		this.mImageFetcher.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getOneImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherOne;
	}

	public void loadOneImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherOne.loadImage(imageUrl, imageView);
	}

	public void loadOneImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherOne.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getHalfImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherHalf;
	}

	public void loadHalfImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherHalf.loadImage(imageUrl, imageView);
	}

	public void loadHalfImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherHalf.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getThirdImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherThird;
	}

	public void loadThirdImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherThird.loadImage(imageUrl, imageView);
	}

	public void loadThirdImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherThird.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getFourthImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherFourth;
	}

	public void loadFourthImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherFourth.loadImage(imageUrl, imageView);
	}

	public void loadFourthImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherFourth.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getHeaderImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherHeader;
	}

	public void loadHeaderImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherHeader.loadImage(imageUrl, imageView);
	}

	public void loadHeaderImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherHeader.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getSplashImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherSplash;
	}

	public void loadSplashImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherSplash.loadImage(imageUrl, imageView);
	}

	public void loadSplashImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherSplash.loadImage(imageUrl, imageView, loadingResId);
	}

	public ImageFetcher getActionBarImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcherActionBar;
	}

	public void loadActionBarImage(String imageUrl, ImageView imageView) {
		this.mImageFetcherActionBar.loadImage(imageUrl, imageView);
	}

	public void loadActionBarImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.mImageFetcherActionBar
				.loadImage(imageUrl, imageView, loadingResId);
	}

}
