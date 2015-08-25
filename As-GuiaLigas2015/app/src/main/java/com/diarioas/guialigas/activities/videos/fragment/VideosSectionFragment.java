package com.diarioas.guialigas.activities.videos.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.videos.VideoActivity;
import com.diarioas.guialigas.dao.model.video.VideoItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteVideosDAO;
import com.diarioas.guialigas.dao.reader.RemoteVideosDAO.RemoteVideosDAOListener;
import com.diarioas.guialigas.dao.reader.RemoteVideosDAO.RemoteVideosDAOVideoGeoBlock;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.Reachability;

import java.util.ArrayList;

public class VideosSectionFragment extends SectionFragment implements
		RemoteVideosDAOListener, RemoteVideosDAOVideoGeoBlock {

	private SwipeRefreshLayout swipeRefreshLayout = null;
	private VideoAdapter videoAdapter = null;
	private ArrayList<VideoItem> currentArrayVideoItems = new ArrayList<VideoItem>();
	private View errorContainer = null;
	private ListView videoList = null;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_VIDEOS + "/" + NativeAds.AD_PORTADA;
	}

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
		super.onStop();
		RemoteVideosDAO.getInstance(mContext).cancelCall();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RemoteVideosDAO.getInstance(mContext).cancelCall();

		if (this.currentArrayVideoItems != null) {
			this.currentArrayVideoItems.clear();
			this.currentArrayVideoItems = null;
		}

		if (this.videoAdapter != null) {
			this.videoAdapter = null;
		}
	}

	/******************* Configuration methods *********************************/
	/***************************************************************************/

	@Override
	protected void configureView() {
		configureErrorView();
		configureListView();
		callToOmniture();
		configureSwipeLoader();
	}

	private void configureErrorView() {
		this.errorContainer = getErrorContainer();
		if (this.errorContainer != null) {
			errorContainer.setVisibility(View.INVISIBLE);
			((RelativeLayout) generalView).addView(errorContainer);
		}
	}

	private void configureSwipeLoader() {
		this.swipeRefreshLayout = (SwipeRefreshLayout) generalView
				.findViewById(R.id.swipe_container);
		this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadInformation();
			}
		});
		this.swipeRefreshLayout.setColorSchemeResources(R.color.black,
				R.color.red, R.color.white);
	}

	private void closePullToRefresh() {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}
	}

	private void configureListView() {

		this.videoAdapter = new VideoAdapter();
		this.videoList = (ListView) generalView.findViewById(R.id.videoList);
		this.videoList.setDivider(null);
		this.videoList.setDividerHeight(0);
		this.videoList.setCacheColorHint(0);

		this.videoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goToVideo(position);
			}
		});

		this.videoList.setAdapter(this.videoAdapter);
	}

	/******************* Load Information methods *********************************/
	/***************************************************************************/

	@Override
	protected void loadInformation() {
		RemoteVideosDAO.getInstance(mContext).addVideosListener(this);
		RemoteVideosDAO.getInstance(mContext).getVideosInfo(section.getUrl());
	}

	private ArrayList<VideoItem> getVideoNews() {
		if (RemoteVideosDAO.getInstance(mContext).isVideosLoaded()) {
			return RemoteVideosDAO.getInstance(mContext).getVideosPreloaded();
		} else {
			return new ArrayList<VideoItem>();
		}
	}

	private static String GEOBLOCK_BU = "as";
	public int currentVideoPosition = 0;

	private void goToVideo(Integer pos) {

		if (Reachability.isOnline(mContext)) {

			Intent intent = new Intent(mContext, VideoActivity.class);
			VideoItem videoItem = (VideoItem) currentArrayVideoItems.get(pos);

			if (videoItem != null && videoItem.getUrlEnclosure() != null
					&& videoItem.getUrlEnclosure().length() > 0) {
				intent.putExtra("url", videoItem.getUrlEnclosure());
				intent.putExtra("section", videoItem.getTitle());
				intent.putExtra("subsection", videoItem.getTitle());

				getActivity().startActivityForResult(intent,
						ReturnRequestCodes.PUBLI_BACK);
				getActivity().overridePendingTransition(
						R.anim.grow_from_middle, R.anim.shrink_to_middle);
			} else if (videoItem != null && videoItem.getIdBC() != null
					&& videoItem.getIdBC().length() > 0) {

				this.currentVideoPosition = pos;
				RemoteVideosDAO.getInstance(mContext).addVideoBlockListener(
						this);
				RemoteVideosDAO.getInstance(mContext).requestVideoBlock(
						videoItem.getIdBC() + GEOBLOCK_BU);
			} else {
				errVideoRegion(getResources().getString(
						R.string.video_error_general));
			}
		} else {
			errVideoRegion(getResources().getString(
					R.string.video_error_message_no_internet));
		}
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				Omniture.SECTION_VIDEOS,
				null,
				null,
				null,
				Omniture.TYPE_ARTICLE,
				Omniture.DETAILPAGE_PORTADA + " " + Omniture.SECTION_VIDEOS,
				null);
	}

	/************** RemoteVideosDAOListener methods *******************************/
	/***************************************************************************/

	@Override
	public void onSuccessRemoteVideos(ArrayList<VideoItem> videos) {

		RemoteVideosDAO.getInstance(mContext).removeVideosListener(this);
		fillInformationAction(videos);
	}

	private void fillInformationAction(ArrayList<VideoItem> videos) {
		this.currentArrayVideoItems = videos;
		this.videoList.setVisibility(View.VISIBLE);
		closePullToRefresh();
		this.videoAdapter.notifyDataSetChanged();
		((HomeActivity) getActivity()).stopAnimation();
	}

	@Override
	public void onFailureRemoteVideos() {
		RemoteVideosDAO.getInstance(mContext).removeVideosListener(this);
		errorInformationAction();
	}

	private void errorInformationAction() {
		closePullToRefresh();
		videoList.setVisibility(View.INVISIBLE);
		errorContainer.setVisibility(View.VISIBLE);
		((HomeActivity) getActivity()).stopAnimation();
	}

	/***************************************************************************/
	/** VideoAdapter **/
	/***************************************************************************/

	class VideoAdapter extends BaseAdapter {
		private static final int ITEM_NORMAL = 0;
		private static final int TOTAL_ITEM = ITEM_NORMAL + 1;

		public VideoAdapter() {
		}

		@Override
		public int getCount() {
			if (currentArrayVideoItems != null) {
				return currentArrayVideoItems.size();
			} else {
				return 0;
			}
		}

		@Override
		public int getItemViewType(int position) {
			return ITEM_NORMAL;
		}

		@Override
		public int getViewTypeCount() {
			return TOTAL_ITEM;
		}

		@Override
		public Object getItem(int position) {
			return currentArrayVideoItems.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.frontpage_video_item,
						null);
				FontUtils.setCustomfont(mContext,
						(TextView) convertView.findViewById(R.id.title_video),
						FontTypes.ROBOTO_MEDIUM);
			}

			ImageView currentImage = (ImageView) convertView
					.findViewById(R.id.background_video_image);
			TextView title = (TextView) convertView
					.findViewById(R.id.title_video);

			VideoItem videoItem = (VideoItem) getItem(position);
			title.setText(videoItem.getTitle());

			currentImage.setImageDrawable(null);
			if (currentImage != null) {

				if (videoItem.getUrlThumbnail() != null) {
					currentImage.setVisibility(View.VISIBLE);
					ImageDAO.getInstance(getActivity()).loadImageNoResize(
							videoItem.getUrlThumbnail(), currentImage);
				} else {
					currentImage.setVisibility(View.GONE);
				}
			}

			FrameLayout space_separator = (FrameLayout) convertView
					.findViewById(R.id.space_separator);
			if (position + 1 >= currentArrayVideoItems.size()) {
				space_separator.setVisibility(View.GONE);
			} else {
				space_separator.setVisibility(View.VISIBLE);
			}

			return convertView;
		}
	}

	/********************** NewsDAOVideoGeoBlock methods ***********************/

	@Override
	public void onSuccessVideoGeoBlock(String urlVideo) {
		RemoteVideosDAO.getInstance(mContext).removeVideoBlockListener(this);

		if (this.currentVideoPosition != -1
				&& currentArrayVideoItems != null
				&& currentArrayVideoItems.size() > this.currentVideoPosition
				&& currentArrayVideoItems.get(this.currentVideoPosition) != null) {

			Intent intent = new Intent(mContext, VideoActivity.class);
			VideoItem videoItem = (VideoItem) currentArrayVideoItems.get(this.currentVideoPosition);

			intent.putExtra("url", urlVideo);
			intent.putExtra("section", videoItem.getTitle());
			intent.putExtra("subsection", videoItem.getTitle());

			getActivity().startActivityForResult(intent,
					ReturnRequestCodes.PUBLI_BACK);
			getActivity().overridePendingTransition(R.anim.grow_from_middle,
					R.anim.shrink_to_middle);

		} else {
			errVideoRegion(getResources().getString(
					R.string.video_error_message_region));
		}
	}

	@Override
	public void onFailureVideoGeoBlock() {
		RemoteVideosDAO.getInstance(mContext).removeVideoBlockListener(this);

		errVideoRegion(getResources().getString(
				R.string.video_error_message_region));
	}

	private void errVideoRegion(String message) {
		AlertManager.showAlertOkDialog(getActivity(), message, getResources()
				.getString(R.string.video_error),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
	}
}
