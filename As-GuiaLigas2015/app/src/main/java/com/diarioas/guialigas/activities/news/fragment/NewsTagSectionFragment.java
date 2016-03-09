package com.diarioas.guialigas.activities.news.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.news.NewsTagDetailActivity;
import com.diarioas.guialigas.dao.model.news.NewsItemTag;
import com.diarioas.guialigas.dao.model.news.SectionNewsTagSections;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO.RemoteNewsTagDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class NewsTagSectionFragment extends SectionFragment implements
		RemoteNewsTagDAOListener {

	private ArrayList<NewsItemTag> news = null;
	private NewsAdapter newsAdapter = null;
	private ListView tagList = null;
	private View errorContainer = null;

	private SwipeRefreshLayout swipeRefreshLayout = null;

	/********** Fragment lifecycle methods ***********************************/
	/***********************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_NEWS_TAG + "/" + NativeAds.AD_PORTADA;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		generalView = inflater.inflate(R.layout.fragment_news_section,
				container, false);
		return generalView;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		RemoteNewsDAO.getInstance(mContext).removeTagListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.newsAdapter = null;
	}

	/****************** Configuration methods *********************************/
	/***************************************************************************/

	@Override
	protected void configureView() {

		configureErrorView();
		configureListView();
		callToOmniture();
		configureSwipeLoader();
	}

	private void configureErrorView() {
		errorContainer = getErrorContainer();
		errorContainer.setVisibility(View.INVISIBLE);
		((RelativeLayout) generalView).addView(errorContainer);
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

	@Override
	protected void loadInformation() {
		RemoteNewsDAO.getInstance(mContext).addTagListener(this);
		RemoteNewsDAO.getInstance(mContext).loadTagData(section.getUrl());
	}

	private void configureListView() {
		newsAdapter = new NewsAdapter();

		tagList = (ListView) generalView.findViewById(R.id.tagList);
		this.tagList.setDivider(null);
		this.tagList.setDividerHeight(0);
		this.tagList.setCacheColorHint(0);

		this.tagList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NewsItemTag currentItem = (NewsItemTag) news.get(position);

//				if ((currentItem.getJumpToWeb() == Defines.JUMP_TO_WEB_OK)
//						|| (currentItem.getLink() != null && RemoteNewsDAO
//								.getInstance(mContext).isURLNeedOut(
//										currentItem.getLink()))) {
//
//					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
//							.parse(currentItem.getLink()));
//					getActivity().startActivity(browserIntent);
//				} else {

					Intent intent = new Intent(mContext,
							NewsTagDetailActivity.class);
					intent.putExtra("news", news);
					intent.putExtra("position", position);
					getActivity().startActivityForResult(intent,
							ReturnRequestCodes.PUBLI_BACK);
					getActivity().overridePendingTransition(
							R.anim.grow_from_middle, R.anim.shrink_to_middle);
//				}
			}
		});

		tagList.setAdapter(newsAdapter);
	}

	protected void updateData() {
		if (newsAdapter != null) {
			newsAdapter.notifyDataSetChanged();
		}
	}

	/********************* Libraries methods *********************************/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext)
				.sendStatisticsState(
						getActivity().getApplication(),
                        FileUtils.readOmnitureProperties(mContext, "SECTION_NEWS_TAG"),
						null,
						null,
						null,
                        FileUtils.readOmnitureProperties(mContext, "TYPE_PORTADA"),
                        FileUtils.readOmnitureProperties(mContext, "DETAILPAGE_PORTADA") + " "
								+ FileUtils.readOmnitureProperties(mContext, "SECTION_NEWS_TAG"), null);
	}

	/************** RemotePalmaresDAOListener methods ***********************/
	/***************************************************************************/

	@Override
	public void onSuccessTagRemoteconfig(SectionNewsTagSections results) {
		RemoteNewsDAO.getInstance(mContext).removeTagListener(this);
		fillInformationAction(results);
	}

	private void fillInformationAction(SectionNewsTagSections results) {
		tagList.setVisibility(View.VISIBLE);
		errorContainer.setVisibility(View.GONE);
		this.news = results.getItems();
		closePullToRefresh();
		updateData();
		((HomeActivity) getActivity()).stopAnimation();
	}

	private void closePullToRefresh() {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	public void onFailureTagRemoteconfig() {
		errorRemoteNewsAction();
	}

	@Override
	public void onFailureTagNotConnectionRemoteconfig() {
		errorRemoteNewsAction();
	}

	private void errorRemoteNewsAction() {
		RemoteNewsDAO.getInstance(mContext).removeTagListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_error_download),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (news != null && news.size() > 0) {
							updateData();
						} else {
							errorContainer.setVisibility(View.VISIBLE);
						}
					}

				});

		closePullToRefresh();
		((HomeActivity) getActivity()).stopAnimation();
	}

	/************** Adapter methods ************************************************/
	/*****************************************************************************/

	public static final int NUM_TAGS_CELLS = 7;

	class NewsAdapter extends BaseAdapter {
		private static final int ITEM_NORMAL = 0;
		private static final int ITEM_FOOTER = ITEM_NORMAL + 1;
		private static final int TOTAL_ITEM = ITEM_FOOTER + 1;

		private SimpleDateFormat formatterPull;

		private int heightCell;

		public NewsAdapter() {

			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int height = size.y / NUM_TAGS_CELLS;
			this.formatterPull = new SimpleDateFormat(
					DateFormat.ALL_DAY_FORMAT, Locale.getDefault());
			int dim = (int) getResources().getDimension(
					R.dimen.flip_size_padding);
			this.heightCell = height - dim;
		}

		@Override
		public int getCount() {
			if (news != null && news.size() > 0) {
				return news.size() + 1;
			} else {
				return 0;
			}
		}

		@Override
		public int getItemViewType(int position) {
			if (position > 0 && position == news.size()) {
				return ITEM_FOOTER;
			} else {
				return ITEM_NORMAL;
			}
		}

		@Override
		public int getViewTypeCount() {
			return TOTAL_ITEM;
		}

		@Override
		public Object getItem(int arg0) {

			if (news != null && news.size() > arg0) {
				return news.get(arg0);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == ITEM_FOOTER) {
				View view = new View(mContext);
				view.setMinimumHeight((int) getResources().getDimension(
						R.dimen.footer_height));
				return view;
			} else {
				ImageView currentImage = null;
				TextView body = null;
				TextView title = null;
				ImageView button_play = null;

				if (convertView == null) {

					convertView = inflater
							.inflate(R.layout.tag_item_list, null);
					convertView.findViewById(R.id.teamContent)
							.setMinimumHeight(heightCell);

					currentImage = (ImageView) convertView
							.findViewById(R.id.imageTag);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							heightCell, heightCell);
					currentImage.setLayoutParams(params);

					ImageView overImageTag = (ImageView) convertView
							.findViewById(R.id.overImageTag);
					RelativeLayout.LayoutParams paramsOver = new RelativeLayout.LayoutParams(
							heightCell, heightCell);
					overImageTag.setLayoutParams(paramsOver);

					FontUtils.setCustomfont(mContext,
							(TextView) convertView.findViewById(R.id.title),
							FontTypes.ROBOTO_BOLD);

					FontUtils.setCustomfont(mContext,
							(TextView) convertView.findViewById(R.id.body),
							FontTypes.ROBOTO_REGULAR);
				}

				currentImage = (ImageView) convertView
						.findViewById(R.id.imageTag);
				body = (TextView) convertView.findViewById(R.id.body);
				title = (TextView) convertView.findViewById(R.id.title);
				button_play = (ImageView) convertView
						.findViewById(R.id.button_play);

				NewsItemTag item = (NewsItemTag) getItem(position);
				if (item != null) {

					if (item.getTimeStamp() > 0) {
						title.setText(formatterPull.format(new Date((long) item
								.getTimeStamp() * 1000))
								+ " | "
								+ item.getAuthor());
					} else {
						title.setText(item.getAuthor());
					}
					body.setText(item.getTitle());

					ImageView separator_news = (ImageView) convertView
							.findViewById(R.id.separator_news);
					if (position >= news.size() - 1) {
						separator_news.setVisibility(View.GONE);
					} else {
						separator_news.setVisibility(View.VISIBLE);
					}

					currentImage.setImageDrawable(null);
					if (currentImage != null) {
						if (item.getThumbnailPhoto() != null
								&& item.getThumbnailPhoto().getUrl() != null) {
							convertView.findViewById(R.id.imageTagContent)
									.setVisibility(View.VISIBLE);
							ImageDAO.getInstance(getActivity())
									.loadRegularImage(
											item.getThumbnailPhoto().getUrl(),
											currentImage,
											-1, -1);
//							R.drawable.icn_loading, -1);
						} else if (item.getNewsPhoto() != null
								&& item.getNewsPhoto().getUrl() != null) {
							convertView.findViewById(R.id.imageTagContent)
									.setVisibility(View.VISIBLE);
							ImageDAO.getInstance(getActivity())
									.loadRegularImage(
											item.getNewsPhoto().getUrl(),
											currentImage,
											-1, -1);
//							R.drawable.icn_loading, -1);
						} else {
							convertView.findViewById(R.id.imageTagContent)
									.setVisibility(View.GONE);
						}
					}

					if (item.getVideos() != null && item.getVideos().size() > 0) {
						button_play.setVisibility(View.VISIBLE);
					} else {
						button_play.setVisibility(View.GONE);
					}
				}

				return convertView;
			}
		}

	}
}
