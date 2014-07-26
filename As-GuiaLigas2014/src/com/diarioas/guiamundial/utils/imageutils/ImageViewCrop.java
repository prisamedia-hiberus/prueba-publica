package com.diarioas.guiamundial.utils.imageutils;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author doriancussen
 */
public abstract class ImageViewCrop extends ImageView {

	protected boolean isTop = true;

	public ImageViewCrop(Context context) {
		super(context);
		setScaleType(ScaleType.MATRIX);
	}

	public ImageViewCrop(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScaleType(ScaleType.MATRIX);
	}

	public ImageViewCrop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ScaleType.MATRIX);
	}

	@Override
	protected boolean setFrame(int frameLeft, int frameTop, int frameRight,
			int frameBottom) {
		try {

			float frameWidth = frameRight - frameLeft;
			float frameHeight = frameBottom - frameTop;

			float originalImageWidth = getDrawable().getIntrinsicWidth();
			float originalImageHeight = getDrawable().getIntrinsicHeight();

			float usedScaleFactor = 1;

			if ((frameWidth > originalImageWidth)
					|| (frameHeight > originalImageHeight)) {
				// If frame is bigger than image
				// => Crop it, keep aspect ratio and position it at the bottom
				// and
				// center horizontally

				float fitHorizontallyScaleFactor = frameWidth
						/ originalImageWidth;
				float fitVerticallyScaleFactor = frameHeight
						/ originalImageHeight;

				usedScaleFactor = Math.max(fitHorizontallyScaleFactor,
						fitVerticallyScaleFactor);
			}

			float newImageWidth = originalImageWidth * usedScaleFactor;
			float newImageHeight = originalImageHeight * usedScaleFactor;

			Matrix matrix = getImageMatrix();
			// Scale the Image
			matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0);

			if (isTop) {
				// Move the image to center Top
				matrix.postTranslate((frameWidth - newImageWidth) / 2, 0);
			} else {
				// Move the image to center Bottom
				matrix.postTranslate((frameWidth - newImageWidth) / 2,
						frameHeight - newImageHeight);
			}
			setImageMatrix(matrix);
		} catch (Exception e) {
			Log.e("IMAGEVIEWCROP", "Error in ImageViewCrop: " + e.getMessage());
		}
		return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
	}

}
