package com.diarioas.guialigas.dao.reader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageDAO {

	private static ImageDAO sInstance = null;
	private Context mContext = null;
	private int maskResource = -1;

	public static ImageDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new ImageDAO();
			sInstance.mContext = ctx;
		}
		return sInstance;
	}

	@SuppressLint("NewApi")
	private Point getDisplaySize(final Display display) {
		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

	private Point getPointScreen() {
		Display display = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = getDisplaySize(display);
		return size;
	}

	public void loadImageNoResize(String url, ImageView imageView) {
		Picasso.with(mContext).load(url).into(imageView);
	}

	public void loadRegularImage(String url, ImageView imageView) {
		loadRegularImage(url, imageView, -1, -1);
	}

	public void loadHeaderSectionImage(String url, ImageView imageView) {
		Picasso.with(mContext).load(url).into(imageView);
	}

	public void loadRegularImage(String url, ImageView imageView,
			int resourceLoading, int mask) {
		loadRegularImage(url, imageView, resourceLoading, mask, false);
	}

	public void loadRegularImage(String url, ImageView imageView,
			int resourceLoading, int mask, boolean needToReload) {
		Point size = getPointScreen();

		int height = (int) (size.y * 0.25);
		int width = (int) (size.x * 0.83);

		if (resourceLoading != -1) {
			if (mask != -1) {
				this.maskResource = mask;

				if (needToReload) {
					Picasso.with(mContext).load(url)
							.memoryPolicy(MemoryPolicy.NO_STORE)
							.networkPolicy(NetworkPolicy.NO_STORE)
							.placeholder(resourceLoading).resize(width, height)
							.transform(new MaskImageTransformation())
							.centerInside().into(imageView);
				} else {
					Picasso.with(mContext).load(url)
							.placeholder(resourceLoading).resize(width, height)
							.transform(new MaskImageTransformation())
							.centerInside().into(imageView);
				}
			} else {
				if (needToReload) {
					Picasso.with(mContext).load(url)
							.placeholder(resourceLoading)
							.memoryPolicy(MemoryPolicy.NO_STORE)
							.networkPolicy(NetworkPolicy.NO_STORE)
							.resize(width, height).centerInside()
							.into(imageView);
				} else {
					Picasso.with(mContext).load(url)
							.placeholder(resourceLoading).resize(width, height)
							.centerInside().into(imageView);
				}
			}

		} else {
			if (mask != -1) {
				this.maskResource = mask;

				if (needToReload) {
					Picasso.with(mContext).load(url).resize(width, height)
							.memoryPolicy(MemoryPolicy.NO_STORE)
							.networkPolicy(NetworkPolicy.NO_STORE)
							.transform(new MaskImageTransformation())
							.centerInside().into(imageView);
				} else {
					Picasso.with(mContext).load(url).resize(width, height)
							.transform(new MaskImageTransformation())
							.centerInside().into(imageView);
				}

			} else {
				if (needToReload) {
					Picasso.with(mContext).load(url).resize(width, height)
							.memoryPolicy(MemoryPolicy.NO_STORE)
							.networkPolicy(NetworkPolicy.NO_STORE)
							.centerInside().into(imageView);
				} else {

					Picasso.with(mContext).load(url).resize(width, height)
							.centerInside().into(imageView);
				}
			}
		}
	}

	public void loadPhotoImage(String url, ImageView imageView) {
		Point size = getPointScreen();

		int height = (int) (size.y * 0.2);
		int width = (int) (size.x * 0.5);

		Picasso.with(mContext).load(url).resize(width, height).centerInside()
				.into(imageView);
	}

	public void loadFullScreenImage(String url, ImageView imageView) {
		Point size = getPointScreen();

		int height = (int) (size.y * 0.8);
		int width = (int) (size.x);

		Picasso.with(mContext).load(url)
				.transform(new BitmapTransform(width, height))
				.skipMemoryCache().resize(width, height).centerInside()
				.into(imageView);
	}

	public void loadTwoPartScreenImage(String url, ImageView imageView) {
		Point size = getPointScreen();

		int height = (int) (size.y * 0.4);
		int width = (int) (size.x);

		Picasso.with(mContext).load(url).resize(width, height).centerInside()
				.into(imageView);
	}

	public void loadThreePartScreenImage(String url, ImageView imageView) {
		Point size = getPointScreen();

		int height = (int) (size.y / 4);
		int width = (int) (size.x);

		Picasso.with(mContext).load(url).resize(width, height).centerInside()
				.into(imageView);
	}

	public void loadFourthPartScreenImage(String url, ImageView imageView) {
		Point size = getPointScreen();

		int height = (int) (size.y / 5);
		int width = (int) (size.x);

		Picasso.with(mContext).load(url).resize(width, height).centerInside()
				.into(imageView);
	}

	public void loadPlayerTeamDetailImage(String url, int resourceLoading,
			ImageView imageView, int mask) {

		int height = mContext.getResources().getDimensionPixelSize(
				R.dimen.image_player_height_small);
		int width = mContext.getResources().getDimensionPixelSize(
				R.dimen.image_player_height_small);

		this.maskResource = mask;
		Picasso.with(mContext).load(url).skipMemoryCache()
				.placeholder(resourceLoading)
				.transform(new MaskImageTransformation()).resize(width, height)
				.centerInside().into(imageView);
	}

	/********************** Image Transformation ***********************/

	public class MaskImageTransformation implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {

			if (maskResource != -1) {
				Bitmap mask = BitmapFactory.decodeResource(
						mContext.getResources(), maskResource);
				Bitmap originalBitmapScaled = Bitmap.createScaledBitmap(source,
						mask.getWidth(), mask.getHeight(), false);
				Bitmap bitmap = Bitmap.createBitmap(mask.getWidth(),
						mask.getHeight(), Config.ARGB_8888);
				Canvas mCanvas = new Canvas(bitmap);
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
				mCanvas.drawBitmap(originalBitmapScaled, 0, 0, null);
				mCanvas.drawBitmap(mask, 0, 0, paint);
				paint.setXfermode(null);

				source.recycle();

				return bitmap;
			} else {
				return null;
			}
		}

		@Override
		public String key() {
			return "mask()";
		}
	}

	public class BitmapTransform implements Transformation {

		int maxWidth;
		int maxHeight;

		public BitmapTransform(int maxWidth, int maxHeight) {
			this.maxWidth = maxWidth;
			this.maxHeight = maxHeight;
		}

		@Override
		public Bitmap transform(Bitmap source) {
			int targetWidth, targetHeight;
			double aspectRatio;

			if (source.getWidth() > source.getHeight()) {
				targetWidth = maxWidth;
				aspectRatio = (double) source.getHeight()
						/ (double) source.getWidth();
				targetHeight = (int) (targetWidth * aspectRatio);
			} else {
				targetHeight = maxHeight;
				aspectRatio = (double) source.getWidth()
						/ (double) source.getHeight();
				targetWidth = (int) (targetHeight * aspectRatio);
			}

			Bitmap result = Bitmap.createScaledBitmap(source, targetWidth,
					targetHeight, false);
			if (result != source) {
				source.recycle();
			}
			return result;
		}

		@Override
		public String key() {
			return maxWidth + "x" + maxHeight;
		}

	};

}
