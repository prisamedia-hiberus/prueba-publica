package com.diarioas.guialigas.dao.reader;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.imageutils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guialigas.utils.imageutils.bitmapfun.ImageFetcher;
import com.diarioas.guialigas.utils.imageutils.imageloader.ImageLoader;

public class ImageDAO {

	private static ImageDAO sInstance = null;
	private Context mContext = null;
	private ImageFetcher mImageFetcher = null;
	private ImageFetcher mImageFetcherOne = null;
	private ImageFetcher mImageFetcherHalf = null;
	private ImageFetcher mImageFetcherThird = null;
	private ImageFetcher mImageFetcherFourth = null;
	private ImageFetcher mImageFetcherHeader = null;
	// private ImageFetcher mImageFetcherSplash = null;
	// private ImageFetcher mImageFetcherActionBar = null;
	private ImageFetcher mImageFetcherPlayer;
	private ImageFetcher mImageFetcherSmallPlayer;

	private ImageFetcher mImageFetcherGallery;
	private ImageFetcher mImageFetcherNews;
	private ImageFetcher mImageFetcherStadiums;
	private ImageFetcher mImageFetcherStadiumsDetail;
	private ImageLoader imgL;

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
		// sInstance.mImageFetcherSplash = null;
		// sInstance.mImageFetcherActionBar = null;

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
		// configurSplashImageFetcher();
		// configurActionBarImageFetcher();

		configureImageLoader();
	}

	private void configureImageLoader() {
		imgL = new ImageLoader(mContext);
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

	// private void configurSplashImageFetcher() {
	// ImageCacheParams cacheParams = new ImageCacheParams(mContext,
	// Defines.NAME_CACHE_THUMBS + "splash");
	// cacheParams.setMemCacheSizePercent(0.25f);
	//
	// mImageFetcherSplash = new ImageFetcher(mContext, mContext
	// .getResources().getDimensionPixelSize(
	// R.dimen.image_thumb_height));
	// // mImageFetcherSplash.setLoadingImage(R.drawable.galeria_imagenrecurso);
	// mImageFetcherSplash.addImageCache(
	// ((FragmentActivity) this.mContext).getSupportFragmentManager(),
	// cacheParams);
	// mImageFetcherSplash.setImageFadeIn(true);
	// }
	//
	// private void configurActionBarImageFetcher() {
	// ImageCacheParams cacheParams = new ImageCacheParams(mContext,
	// Defines.NAME_CACHE_THUMBS + "actionbar");
	// cacheParams.setMemCacheSizePercent(0.10f);
	//
	// mImageFetcherActionBar = new ImageFetcher(mContext, mContext
	// .getResources().getDimensionPixelSize(
	// R.dimen.image_actionbar_height));
	// mImageFetcherActionBar.addImageCache(
	// ((FragmentActivity) this.mContext).getSupportFragmentManager(),
	// cacheParams);
	// mImageFetcherActionBar.setImageFadeIn(true);
	// }

	// private void configureImageFetcherPlantilla() {
	// ImageCacheParams cacheParams = new ImageCacheParams(mContext,
	// Defines.NAME_CACHE_THUMBS);
	// cacheParams.setMemCacheSizePercent(0.25f);
	//
	// mImageFetcher = new ImageFetcher(mContext, mContext.getResources()
	// .getDimensionPixelSize(R.dimen.image_thumb_height));
	// mImageFetcher.setLoadingImage(R.drawable.galeria_imagenrecurso);
	// mImageFetcher.addImageCache(((FragmentActivity)
	// this.mContext).getSupportFragmentManager(),
	// cacheParams);
	// }
	private void configureImageFetcherPlayer() {
		ImageCacheParams cacheParams2 = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "2");
		cacheParams2.setMemCacheSizePercent(0.25f);

		mImageFetcherPlayer = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_player_height));
		mImageFetcherPlayer.setLoadingImage(R.drawable.foto_generica);
		mImageFetcherPlayer.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams2);
	}

	private void configureImageFetcherSmallPlayer() {
		ImageCacheParams cacheParams3 = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "3");
		cacheParams3.setMemCacheSizePercent(0.25f);
		mImageFetcherSmallPlayer = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_player_height_small));
		mImageFetcherSmallPlayer
				.setLoadingImage(R.drawable.foto_plantilla_generica);
		mImageFetcherSmallPlayer.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams3);
	}

	private void configureImageFetcherGallery() {
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		Point size = DimenUtils.getSize(windowManager);

		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "gallery");
		cacheParams.setMemCacheSizePercent(0.20f);

		mImageFetcherGallery = new ImageFetcher(mContext, size.y);

		mImageFetcherGallery.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);
	}

	private void configureImageFetcherNews() {

		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "news");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcherNews = new ImageFetcher(mContext, (int) mContext
				.getResources().getDimension(R.dimen.image_news_detail_height));
		mImageFetcherNews.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);

	}

	private void configureImageFetcherStadium() {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "stadiums");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcherStadiums = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_thumb_height));
		mImageFetcherStadiums.setLoadingImage(R.drawable.galeria_imagenrecurso);
		mImageFetcherStadiums.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);

	}

	private void configureImageFetcherStadiumDetail() {
		ImageCacheParams cacheParams = new ImageCacheParams(mContext,
				Defines.NAME_CACHE_THUMBS + "stadiumDetail");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcherStadiumsDetail = new ImageFetcher(mContext, mContext
				.getResources().getDimensionPixelSize(
						R.dimen.image_image_height));
		// mImageFetcherStadiumsDetail.setLoadingImage(R.drawable.galeria_imagenrecurso);
		mImageFetcherStadiumsDetail.addImageCache(
				((FragmentActivity) this.mContext).getSupportFragmentManager(),
				cacheParams);

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
		// this.mImageFetcherSplash.setExitTasksEarly(false);
		// this.mImageFetcherActionBar.setExitTasksEarly(false);
	}

	public void exitPlayerTaskEarly() {
		if (this.mImageFetcherPlayer != null)
			this.mImageFetcherPlayer.setExitTasksEarly(false);
		if (this.mImageFetcherSmallPlayer != null)
			this.mImageFetcherSmallPlayer.setExitTasksEarly(false);
	}

	public void exitGalleryTaskEarly() {
		if (this.mImageFetcherGallery != null)
			this.mImageFetcherGallery.setExitTasksEarly(false);
	}

	public void exitNewsTaskEarly() {
		if (this.mImageFetcherNews != null)
			this.mImageFetcherNews.setExitTasksEarly(false);
	}

	public void exitStadiumTaskEarly() {
		if (this.mImageFetcherStadiums != null)
			this.mImageFetcherStadiums.setExitTasksEarly(false);
		if (this.mImageFetcherStadiumsDetail != null)
			this.mImageFetcherStadiumsDetail.setExitTasksEarly(false);
	}

	public void clearCache() {
		if (this.mImageFetcher != null)
			this.mImageFetcher.clearCache();
		if (this.mImageFetcherOne != null)
			this.mImageFetcherOne.clearCache();
		if (this.mImageFetcherHalf != null)
			this.mImageFetcherHalf.clearCache();
		if (this.mImageFetcherThird != null)
			this.mImageFetcherThird.clearCache();
		if (this.mImageFetcherFourth != null)
			this.mImageFetcherFourth.clearCache();
		if (this.mImageFetcherHeader != null)
			this.mImageFetcherHeader.clearCache();
	}

	public void clearPlayerCache() {
		if (this.mImageFetcherPlayer != null)
			this.mImageFetcherPlayer.clearCache();
		if (this.mImageFetcherSmallPlayer != null)
			this.mImageFetcherSmallPlayer.clearCache();
	}

	public void clearGalleryCache() {
		if (this.mImageFetcherGallery != null)
			this.mImageFetcherGallery.clearCache();
	}

	public void clearNewsCache() {
		if (this.mImageFetcherNews != null)
			this.mImageFetcherNews.clearCache();
	}

	public void clearStadiumsCache() {
		if (this.mImageFetcherStadiums != null)
			this.mImageFetcherStadiums.clearCache();
		if (this.mImageFetcherStadiumsDetail != null)
			this.mImageFetcherStadiumsDetail.clearCache();
	}

	public void clearImageLoaderCache() {
		if (this.imgL != null)
			this.imgL.clearCache();
		imgL.clearCache();
	}

	public void flushCache() {
		this.mImageFetcher.flushCache();
		this.mImageFetcherOne.flushCache();
		this.mImageFetcherHalf.flushCache();
		this.mImageFetcherThird.flushCache();
		this.mImageFetcherFourth.flushCache();
		this.mImageFetcherHeader.flushCache();
		// this.mImageFetcherSplash.flushCache();
		// this.mImageFetcherActionBar.flushCache();
	}

	public void flushPlayerCache() {
		if (this.mImageFetcherPlayer != null)
			this.mImageFetcherPlayer.flushCache();
		if (this.mImageFetcherSmallPlayer != null)
			this.mImageFetcherSmallPlayer.flushCache();
	}

	public void flushGalleryCache() {
		if (this.mImageFetcherGallery != null)
			this.mImageFetcherGallery.flushCache();
	}

	public void flushNewsCache() {
		if (this.mImageFetcherNews != null)
			this.mImageFetcherNews.flushCache();
	}

	public void flushStadiumsCache() {
		if (this.mImageFetcherStadiums != null)
			this.mImageFetcherStadiums.flushCache();
		if (this.mImageFetcherStadiumsDetail != null)
			this.mImageFetcherStadiumsDetail.flushCache();
	}

	public void closeCache() {
		this.mImageFetcher.closeCache();
		this.mImageFetcherOne.closeCache();
		this.mImageFetcherHalf.closeCache();
		this.mImageFetcherThird.closeCache();
		this.mImageFetcherFourth.closeCache();
		this.mImageFetcherHeader.closeCache();
		// this.mImageFetcherSplash.closeCache();
		// this.mImageFetcherActionBar.closeCache();

	}

	public void closePlayerCache() {
		if (this.mImageFetcherPlayer != null)
			this.mImageFetcherPlayer.closeCache();
		if (this.mImageFetcherSmallPlayer != null)
			this.mImageFetcherSmallPlayer.closeCache();
	}

	public void closeGalleryCache() {
		if (this.mImageFetcherGallery != null)
			this.mImageFetcherGallery.closeCache();
	}

	public void closeNewsCache() {
		if (this.mImageFetcherNews != null)
			this.mImageFetcherNews.closeCache();
	}

	public void closeStadiumsCache() {
		if (this.mImageFetcherStadiums != null)
			this.mImageFetcherStadiums.closeCache();
		if (this.mImageFetcherStadiumsDetail != null)
			this.mImageFetcherStadiumsDetail.closeCache();
	}

	public void pauseWork(boolean pause) {
		this.mImageFetcher.setPauseWork(pause);
		this.mImageFetcherOne.setPauseWork(pause);
		this.mImageFetcherHalf.setPauseWork(pause);
		this.mImageFetcherThird.setPauseWork(pause);
		this.mImageFetcherFourth.setPauseWork(pause);
		this.mImageFetcherHeader.setPauseWork(pause);
		// this.mImageFetcherSplash.setPauseWork(pause);
		// this.mImageFetcherActionBar.setPauseWork(pause);
	}

	public void pausePlayerWork(boolean pause) {
		if (this.mImageFetcherPlayer != null)
			this.mImageFetcherPlayer.setPauseWork(pause);
		if (this.mImageFetcherSmallPlayer != null)
			this.mImageFetcherSmallPlayer.setPauseWork(pause);
	}

	public void pauseStadiumsWork(boolean pause) {
		if (this.mImageFetcherStadiums != null)
			this.mImageFetcherStadiums.setPauseWork(pause);
		if (this.mImageFetcherStadiumsDetail != null)
			this.mImageFetcherStadiumsDetail.setPauseWork(pause);
	}

	public void erasePlayerCache() {
		sInstance.mImageFetcherPlayer = null;
		sInstance.mImageFetcherSmallPlayer = null;
	}

	public void eraseGalleryCache() {
		sInstance.mImageFetcherGallery = null;
	}

	public void eraseNewsCache() {
		sInstance.mImageFetcherNews = null;
	}

	public void eraseStadiumsCache() {
		sInstance.mImageFetcherStadiums = null;
	}

	public void eraseImageLoader() {
		sInstance.imgL = null;
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

	// public ImageFetcher getSplashImageFetcher() {
	// // TODO Auto-generated method stub
	// return mImageFetcherSplash;
	// }
	//
	// public void loadSplashImage(String imageUrl, ImageView imageView) {
	// this.mImageFetcherSplash.loadImage(imageUrl, imageView);
	// }
	//
	// public void loadSplashImage(String imageUrl, ImageView imageView,
	// int loadingResId) {
	// this.mImageFetcherSplash.loadImage(imageUrl, imageView, loadingResId);
	// }
	//
	// public ImageFetcher getActionBarImageFetcher() {
	// // TODO Auto-generated method stub
	// return mImageFetcherActionBar;
	// }
	//
	// public void loadActionBarImage(String imageUrl, ImageView imageView) {
	// this.mImageFetcherActionBar.loadImage(imageUrl, imageView);
	// }
	//
	// public void loadActionBarImage(String imageUrl, ImageView imageView,
	// int loadingResId) {
	// this.mImageFetcherActionBar
	// .loadImage(imageUrl, imageView, loadingResId);
	// }

	public ImageFetcher getPlayerImageFetcher() {
		if (mImageFetcherPlayer == null) {
			configureImageFetcherPlayer();
		}
		return mImageFetcherPlayer;
	}

	public void loadPlayerImage(String imageUrl, ImageView imageView) {
		this.getPlayerImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadPlayerImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getPlayerImageFetcher().loadImage(imageUrl, imageView,
				loadingResId);
	}

	public ImageFetcher getSmallPlayerImageFetcher() {
		if (mImageFetcherSmallPlayer == null) {
			configureImageFetcherSmallPlayer();
		}
		return mImageFetcherSmallPlayer;
	}

	public void loadSmallPlayerImage(String imageUrl, ImageView imageView) {
		this.getSmallPlayerImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadSmallPlayerImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getSmallPlayerImageFetcher().loadImage(imageUrl, imageView,
				loadingResId);
	}

	/*****************************************************************************/
	public ImageFetcher getGalleryImageFetcher() {
		if (mImageFetcherGallery == null) {
			configureImageFetcherGallery();
		}
		return mImageFetcherGallery;
	}

	public void loadGalleryImage(String imageUrl, ImageView imageView) {
		this.getGalleryImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadGalleryImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getGalleryImageFetcher().loadImage(imageUrl, imageView,
				loadingResId);
	}

	/*****************************************************************************/
	public ImageFetcher getNewsImageFetcher() {
		if (mImageFetcherNews == null) {
			configureImageFetcherNews();
		}
		return mImageFetcherNews;
	}

	public void loadNewsImage(String imageUrl, ImageView imageView) {
		this.getNewsImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadNewsImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getNewsImageFetcher().loadImage(imageUrl, imageView, loadingResId);
	}

	/*****************************************************************************/
	public ImageFetcher getStadiumImageFetcher() {
		if (mImageFetcherStadiums == null) {
			configureImageFetcherStadium();
		}
		return mImageFetcherStadiums;
	}

	public void loadStadiumImage(String imageUrl, ImageView imageView) {
		this.getStadiumImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadStadiumImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getStadiumImageFetcher().loadImage(imageUrl, imageView,
				loadingResId);
	}

	public ImageFetcher getStadiumDetailImageFetcher() {
		if (mImageFetcherStadiums == null) {
			configureImageFetcherStadiumDetail();
		}
		return mImageFetcherStadiumsDetail;
	}

	public void loadStadiumDetailImage(String imageUrl, ImageView imageView) {
		this.getStadiumDetailImageFetcher().loadImage(imageUrl, imageView);
	}

	public void loadStadiumDetailImage(String imageUrl, ImageView imageView,
			int loadingResId) {
		this.getStadiumDetailImageFetcher().loadImage(imageUrl, imageView,
				loadingResId);
	}

	/*****************************************************************************/

	public void displayImage(String url, ImageView imageView, int auxResource) {
		imgL.DisplayImage(url, imageView, auxResource);
	}

	public void displayImageNotCache(String url, ImageView imageView,
			int auxResource) {
		imgL.DisplayImageNotCache(url, imageView, auxResource);
	}

	public void displayImage(String url, ImageView imageView, int auxResource,
			int maskResource) {
		imgL.DisplayImageWithMask(url, imageView, auxResource, maskResource);
	}
}
