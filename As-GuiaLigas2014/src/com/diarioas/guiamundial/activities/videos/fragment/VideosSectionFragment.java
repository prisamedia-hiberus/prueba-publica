package com.diarioas.guiamundial.activities.videos.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.general.fragment.FlipSectionFragment;
import com.diarioas.guiamundial.activities.home.HomeActivity;
import com.diarioas.guiamundial.activities.team.TeamVideoActivity;
import com.diarioas.guiamundial.dao.model.video.VideoItem;
import com.diarioas.guiamundial.dao.reader.ImageDAO;
import com.diarioas.guiamundial.dao.reader.RemoteVideosDAO;
import com.diarioas.guiamundial.dao.reader.RemoteVideosDAO.RemoteVideosDAOListener;
import com.diarioas.guiamundial.dao.reader.StatisticsDAO;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines.NativeAds;
import com.diarioas.guiamundial.utils.Defines.Omniture;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;

public class VideosSectionFragment extends FlipSectionFragment implements
		RemoteVideosDAOListener, ViewFlipListener {

	private VideoAdapter videoAdapter;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;

		generalView = inflater.inflate(R.layout.fragment_video_section,
				container, false);

		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		RemoteVideosDAO.getInstance(mContext).removeListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (array != null) {
			array.clear();
			array = null;
		}
		videoAdapter = null;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void buildView() {
		reloadData();

	}

	@Override
	protected void configureView() {
		super.configureView();

		array = getVideoNews();

		configureListView();
		callToOmniture();
	}

	private ArrayList<VideoItem> getVideoNews() {
		if (RemoteVideosDAO.getInstance(mContext).isVideosLoaded()) {
			return RemoteVideosDAO.getInstance(mContext).getVideosPreloaded();
		} else {
			return new ArrayList<VideoItem>();
		}
	}

	private void configureListView() {

		videoAdapter = new VideoAdapter();
		flipView = (FlipViewController) generalView.findViewById(R.id.flipView);

		flipView.setAdapter(videoAdapter);
		flipView.setOnViewFlipListener(this);

	}

	@Override
	protected void stopPlayerAnimation() {
		stopPlayerAnimation(RemoteVideosDAO.getInstance(mContext)
				.getDateVideoUpdated());
	}

	@Override
	protected void updateData() {
		videoAdapter.notifyChange();
	}

	@Override
	protected void reloadData() {
		RemoteVideosDAO.getInstance(mContext).addListener(this);
		RemoteVideosDAO.getInstance(mContext).getVideosInfo(section.getUrl());
	}

	private void goToVideo(Integer pos) {
		Intent intent = new Intent(mContext, TeamVideoActivity.class);
		VideoItem videoItem = (VideoItem) array.get(pos);
		intent.putExtra("url", videoItem.getUrlEnclosure());
		intent.putExtra("section", videoItem.getTitle());
		intent.putExtra("subsection", videoItem.getTitle());

		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(), Omniture.SECTION_VIDEOS, null,
				null, null, Omniture.TYPE_PORTADA,
				Omniture.DETAILPAGE_PORTADA + " " + Omniture.SECTION_VIDEOS,
				null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_VIDEOS + "/" + NativeAds.AD_PORTADA);
	}

	/***************************************************************************/
	/** RemotePalmaresDAOListener methods **/
	/***************************************************************************/

	@Override
	public void onSuccessRemoteconfig(ArrayList<VideoItem> videosNewsArray) {
		Log.d("VIDEOS", "Updated Rss - onSuccessRemoteconfig");
		RemoteVideosDAO.getInstance(mContext).removeListener(this);
		this.array = videosNewsArray;
		((HomeActivity) getActivity()).stopAnimation();
		stopPlayerAnimation();
		loadData(true);
	}

	@Override
	public void onFailureRemoteconfig() {
		Log.d("VIDEOS", "Updated Rss - onFailureRemoteconfig");
		RemoteVideosDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						array = getVideoNews();

						if (array != null && array.size() > 0) {
							loadData(true);
						} else {
							errorContainer.setVisibility(View.VISIBLE);
							// loadData(true);
						}
					}

				});

		((HomeActivity) getActivity()).stopAnimation();
		stopPlayerAnimation();
	}

	@Override
	public void onFailureNotConnection() {
		Log.d("VIDEOS", "Updated Rss - onFailureNotConnection");
		RemoteVideosDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						array = getVideoNews();
						if (array != null && array.size() > 0) {
							loadData(true);
						} else {
							errorContainer.setVisibility(View.VISIBLE);
							// videoAdapter.addHeader(getErrorContainer());
							// loadData(true);
						}
					}

				});

		((HomeActivity) getActivity()).stopAnimation();
		stopPlayerAnimation();

	}

	/***************************************************************************/
	/** VideoAdapter **/
	/***************************************************************************/
	class VideoAdapter extends BaseAdapter {

		private final Bitmap placeholderBitmap;

		public VideoAdapter() {
			placeholderBitmap = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.galeria_imagenrecurso);
		}

		public void notifyChange() {
			notifyDataSetChanged();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			int i = array.size() / 2;

			if (array.size() % 2 != 0)
				i++;

			return i;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_list_video, null);
				holder.image1 = (ImageView) convertView
						.findViewById(R.id.image1);
				holder.titleLabel1 = (TextView) convertView
						.findViewById(R.id.titleLabel1);
				FontUtils.setCustomfont(mContext, holder.titleLabel1,
						FontTypes.HELVETICANEUEBOLD);
				holder.content1 = (RelativeLayout) convertView
						.findViewById(R.id.content1);

				holder.image2 = (ImageView) convertView
						.findViewById(R.id.image2);
				holder.titleLabel2 = (TextView) convertView
						.findViewById(R.id.titleLabel2);
				FontUtils.setCustomfont(mContext, holder.titleLabel2,
						FontTypes.HELVETICANEUEBOLD);
				holder.content2 = (RelativeLayout) convertView
						.findViewById(R.id.content2);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			VideoItem videoItemFirst = (VideoItem) array.get(2 * position);
			holder.titleLabel1.setText(videoItemFirst.getTitle());
			loadImage(videoItemFirst.getUrlThumbnail(), holder.image1);

			holder.content1.setTag(2 * position);
			holder.content1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToVideo((Integer) v.getTag());
				}
			});

			if (array.size() > ((2 * position) + 1)) {
				VideoItem videoItemSecond = (VideoItem) array
						.get((2 * position) + 1);
				holder.titleLabel2.setText(videoItemSecond.getTitle());
				loadImage(videoItemSecond.getUrlThumbnail(), holder.image2);

				holder.content2.setVisibility(View.VISIBLE);
				holder.content2.setTag((2 * position) + 1);
				holder.content2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						goToVideo((Integer) v.getTag());
					}

				});
			} else {
				holder.content2.setVisibility(View.INVISIBLE);
			}

			if (position == 0) {
				firstView = convertView;
			}
			return convertView;
		}

		private void loadImage(String urlPhoto, ImageView imageView) {
			ImageDAO.getInstance(mContext).loadHalfImage(urlPhoto, imageView);
		}
	}

	static class ViewHolder {

		public RelativeLayout content1;
		public RelativeLayout content2;
		public TextView titleLabel2;
		public ImageView image2;
		public TextView titleLabel1;
		public ImageView image1;

	}

}
