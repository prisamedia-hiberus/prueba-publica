package com.diarioas.guiamundial.activities.news.fragment;

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
import com.diarioas.guiamundial.activities.news.NewsDetailActivity;
import com.diarioas.guiamundial.dao.model.news.NewsItem;
import com.diarioas.guiamundial.dao.reader.ImageDAO;
import com.diarioas.guiamundial.dao.reader.RemoteDataDAO;
import com.diarioas.guiamundial.dao.reader.RemoteNewsDAO;
import com.diarioas.guiamundial.dao.reader.RemoteNewsDAO.RemoteNewsDAOListener;
import com.diarioas.guiamundial.dao.reader.StatisticsDAO;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines.NativeAds;
import com.diarioas.guiamundial.utils.Defines.Omniture;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;

public class NewsSectionFragment extends FlipSectionFragment implements
		RemoteNewsDAOListener, ViewFlipListener {

	private NewsAdapter newsAdapter;

	// private RelativeLayout playerSpinner;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_news_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		RemoteNewsDAO.getInstance(mContext).removeListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (array != null) {
			array.clear();
			array = null;
		}
		newsAdapter = null;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void buildView() {

		RemoteNewsDAO.getInstance(mContext).addListener(this);
		RemoteNewsDAO.getInstance(mContext).getNewsInfo(section.getUrl());

		int currentCompetitionId = Integer.valueOf(competitionId);
		if (RemoteNewsDAO.getInstance(mContext).isNewsLoaded(
				currentCompetitionId)) {
			this.array = RemoteNewsDAO.getInstance(mContext).getNewsPreloaded(
					currentCompetitionId);
			loadData(true);
			((HomeActivity) getActivity()).stopAnimation();
		}
	}

	@Override
	protected void configureView() {
		super.configureView();
		array = getNews();
		configureListView();
		callToOmniture();
	}

	private void configureListView() {

		newsAdapter = new NewsAdapter();

		flipView = (FlipViewController) generalView.findViewById(R.id.flipView);
		flipView.setAdapter(newsAdapter);
		flipView.setOnViewFlipListener(this);

	}

	private ArrayList<NewsItem> getNews() {
		int competitionId = RemoteDataDAO.getInstance(mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		if (RemoteNewsDAO.getInstance(mContext).isNewsLoaded(competitionId)) {
			return RemoteNewsDAO.getInstance(mContext).getNewsPreloaded(
					competitionId);
		} else {
			return new ArrayList<NewsItem>();
		}
	}

	@Override
	protected void stopPlayerAnimation() {
		stopPlayerAnimation(RemoteNewsDAO.getInstance(mContext).getNewsDate());
	}

	@Override
	protected void updateData() {
		newsAdapter.notifyChange();
	}

	@Override
	protected void reloadData() {
		RemoteNewsDAO.getInstance(mContext).addListener(this);
		RemoteNewsDAO.getInstance(mContext).getNewsInfo(section.getUrl());
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext)
				.sendStatisticsState(
						getActivity().getApplication(),
						Omniture.SECTION_NEWS,
						null,
						null,
						null,
						Omniture.TYPE_PORTADA,
						Omniture.DETAILPAGE_PORTADA + " "
								+ Omniture.SECTION_NEWS, null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_NEWS + "/" + NativeAds.AD_PORTADA);
	}

	/***************************************************************************/
	/** RemotePalmaresDAOListener methods **/
	/***************************************************************************/

	@Override
	public void onSuccessRemoteconfig(ArrayList<NewsItem> newsArray) {
		Log.d("NEWS", "Updated Rss...");
		RemoteNewsDAO.getInstance(mContext).removeListener(this);
		stopPlayerAnimation();
		this.array = newsArray;
		((HomeActivity) getActivity()).stopAnimation();
		stopPlayerAnimation();
		loadData(false);

	}

	@Override
	public void onFailureRemoteconfig() {
		Log.d("NEWS", "Failed Updated Rss...");
		RemoteNewsDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_error_download),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						array = getNews();
						if (array != null && array.size() > 0) {
							loadData(false);
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
		Log.d("NEWS", "Failed Updated Rss...");
		RemoteNewsDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(
				getActivity(),
				getResources().getString(
						R.string.section_not_conection_notupdated),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						array = getNews();
						if (array != null && array.size() > 0) {
							loadData(false);
						} else {
							errorContainer.setVisibility(View.VISIBLE);
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
	class NewsAdapter extends BaseAdapter {

		private static final int TYPE_ONE_ITEM = 0;
		private static final int TYPE_TWO_ITEM = TYPE_ONE_ITEM + 1;
		private static final int TYPE_THREE_ITEM = TYPE_TWO_ITEM + 1;
		private static final int TYPE_FOUR_ITEM = TYPE_THREE_ITEM + 1;
		private static final int TYPE_HEADER_ITEM = TYPE_FOUR_ITEM + 1;
		private static final int TYPE_TOTAL = TYPE_HEADER_ITEM + 1;
		private int numItems;
		private final Bitmap placeholderBitmap;

		public NewsAdapter() {
			placeholderBitmap = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.galeria_imagenrecurso);
		}

		public void notifyChange() {
			reset();
			notifyDataSetChanged();
		}

		private void reset() {
			calculateTotal();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return numItems;

		}

		private void calculateTotal() {
			int accum = 0;
			numItems = 0;
			for (int i = 0; accum < array.size(); i++) {
				if (i % TYPE_TOTAL == TYPE_HEADER_ITEM)
					accum += i % TYPE_TOTAL;
				else
					accum += i % TYPE_TOTAL + 1;
				numItems++;
			}

		}

		@Override
		public int getItemViewType(int position) {
			return position % TYPE_TOTAL;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return TYPE_TOTAL;
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

			ViewHolder holder = null;

			int type = getItemViewType(position);
			int pos = calculatePosition(position, type);
			// if (convertView == null) {

			switch (type) {
			case TYPE_ONE_ITEM:
				convertView = inflater.inflate(
						R.layout.item_list_news_one_item, null);
				holder = getHolderOneType(convertView);
				break;
			case TYPE_TWO_ITEM:
				if (array.size() > (pos + 1)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_two_item, null);
					holder = getHolderTwoType(convertView);
				} else {
					convertView = inflater.inflate(
							R.layout.item_list_news_one_item, null);
					holder = getHolderOneType(convertView);
				}
				break;
			case TYPE_THREE_ITEM:
				if (array.size() > (pos + 2)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_three_item, null);
					holder = getHolderThreeType(convertView);
				} else if (array.size() > (pos + 1)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_two_item, null);
					holder = getHolderTwoType(convertView);
				} else {
					convertView = inflater.inflate(
							R.layout.item_list_news_one_item, null);
					holder = getHolderOneType(convertView);
				}

				break;
			case TYPE_FOUR_ITEM:
				if (array.size() > (pos + 3)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_four_item, null);
					holder = getHolderFourType(convertView);
				} else if (array.size() > (pos + 2)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_three_item, null);
					holder = getHolderThreeType(convertView);
				} else if (array.size() > (pos + 1)) {
					convertView = inflater.inflate(
							R.layout.item_list_news_two_item, null);
					holder = getHolderTwoType(convertView);
				} else {
					convertView = inflater.inflate(
							R.layout.item_list_news_one_item, null);
					holder = getHolderOneType(convertView);
				}

				break;
			case TYPE_HEADER_ITEM:
				convertView = inflater.inflate(
						R.layout.item_list_news_header_item, null);
				holder = getHolderHeaderType(convertView, pos);
				break;
			default:
				break;
			}

			convertView.setTag(holder);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }

			switch (type) {
			case TYPE_ONE_ITEM:
				setItemOneType(pos, holder);
				break;
			case TYPE_TWO_ITEM:
				if (array.size() > (pos + 1)) {
					setItemTwoType(pos, holder);
				} else {
					setItemOneType(pos, holder);
				}
				break;
			case TYPE_THREE_ITEM:
				if (array.size() > (pos + 2)) {
					setItemThreeType(pos, holder);
				} else if (array.size() > (pos + 1)) {
					setItemTwoType(pos, holder);
				} else {
					setItemOneType(pos, holder);
				}
				break;
			case TYPE_FOUR_ITEM:
				if (array.size() > (pos + 3)) {
					setItemFourType(pos, holder);
				} else if (array.size() > (pos + 2)) {
					setItemThreeType(pos, holder);
				} else if (array.size() > (pos + 1)) {
					setItemTwoType(pos, holder);
				} else {
					setItemOneType(pos, holder);
				}
				break;
			case TYPE_HEADER_ITEM:
				setItemHeaderType(pos, holder);
				break;
			default:
				break;
			}
			if (pos == 0) {
				firstView = convertView;
			}

			return convertView;
		}

		private int calculatePosition(int position, int type) {
			int accum = 0;
			for (int i = 0; i <= position; i++) {
				if (i > 0 && i % TYPE_TOTAL == 0) {
					accum += TYPE_TOTAL - 1;
				} else {
					accum += i % TYPE_TOTAL;
				}

			}

			// Log.d("CONTADOR", "Position: " + position +
			// "calculatePosition: "+ " Position: " + accum);

			return accum;
		}

		private ViewHolder getHolderOneType(View convertView) {
			ViewHolder holder = new ViewHolder();
			holder.content1 = (RelativeLayout) convertView
					.findViewById(R.id.content1);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);
			return holder;
		}

		private ViewHolder getHolderTwoType(View convertView) {

			ViewHolder holder = new ViewHolder();
			holder.content1 = (RelativeLayout) convertView
					.findViewById(R.id.content1);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content2 = (RelativeLayout) convertView
					.findViewById(R.id.content2);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);
			return holder;
		}

		private ViewHolder getHolderThreeType(View convertView) {
			ViewHolder holder = new ViewHolder();
			holder.content1 = (RelativeLayout) convertView
					.findViewById(R.id.content1);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content2 = (RelativeLayout) convertView
					.findViewById(R.id.content2);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content3 = (RelativeLayout) convertView
					.findViewById(R.id.content3);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);
			return holder;
		}

		private ViewHolder getHolderFourType(View convertView) {

			ViewHolder holder = new ViewHolder();
			holder.content1 = (RelativeLayout) convertView
					.findViewById(R.id.content1);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content2 = (RelativeLayout) convertView
					.findViewById(R.id.content2);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content3 = (RelativeLayout) convertView
					.findViewById(R.id.content3);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content4 = (RelativeLayout) convertView
					.findViewById(R.id.content4);
			FontUtils.setCustomfont(mContext,
					holder.content4.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content4.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);
			return holder;
		}

		private ViewHolder getHolderHeaderType(View convertView, int position) {
			int offset = 4;
			if (array.size() <= (position + 3)) {
				offset = 3;
			}
			int height = (((HomeActivity) getActivity()).getHeight() / offset)
					- 2 * DimenUtils.getRegularPixelFromDp(mContext, 4);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					height, height);
			params.addRule(RelativeLayout.CENTER_VERTICAL);

			ViewHolder holder = new ViewHolder();
			holder.content1 = (RelativeLayout) convertView
					.findViewById(R.id.content1);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content1.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content2 = (RelativeLayout) convertView
					.findViewById(R.id.content2);

			holder.content2.findViewById(R.id.imageContent).setLayoutParams(
					params);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content2.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content3 = (RelativeLayout) convertView
					.findViewById(R.id.content3);
			holder.content3.findViewById(R.id.imageContent).setLayoutParams(
					params);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content3.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);

			holder.content4 = (RelativeLayout) convertView
					.findViewById(R.id.content4);
			holder.content4.findViewById(R.id.imageContent).setLayoutParams(
					params);
			FontUtils.setCustomfont(mContext,
					holder.content4.findViewById(R.id.title),
					FontTypes.HELVETICANEUEBOLD);
			FontUtils.setCustomfont(mContext,
					holder.content4.findViewById(R.id.preTitle),
					FontTypes.HELVETICANEUEBOLD);
			return holder;

		}

		private void setItemOneType(int position, final ViewHolder holder) {
			setItem(holder.content1, (NewsItem) array.get(position),
					getUrl((NewsItem) array.get(position), TYPE_ONE_ITEM),
					ImageDAO.getInstance(mContext).getOneImageFetcher(),
					position, 0);
		}

		private void setItemTwoType(int position, final ViewHolder holder) {

			setItem(holder.content1, (NewsItem) array.get(position),
					getUrl((NewsItem) array.get(position), TYPE_TWO_ITEM),
					ImageDAO.getInstance(mContext).getHalfImageFetcher(),
					position, 0);

			if (array.size() > (position + 1)) {
				setItem(holder.content2,
						(NewsItem) array.get(position + 1),
						getUrl((NewsItem) array.get(position + 1),
								TYPE_TWO_ITEM), ImageDAO.getInstance(mContext)
								.getHalfImageFetcher(), position, 1);
			} else {
				holder.content2.setVisibility(View.GONE);
			}

		}

		private void setItemThreeType(int position, final ViewHolder holder) {

			setItem(holder.content1, (NewsItem) array.get(position),
					getUrl((NewsItem) array.get(position), TYPE_THREE_ITEM),
					ImageDAO.getInstance(mContext).getThirdImageFetcher(),
					position, 0);

			if (array.size() > (position + 1)) {
				setItem(holder.content2,
						(NewsItem) array.get(position + 1),
						getUrl((NewsItem) array.get(position + 1),
								TYPE_THREE_ITEM), ImageDAO
								.getInstance(mContext).getThirdImageFetcher(),
						position, 1);
				if (array.size() > (position + 2)) {
					setItem(holder.content3,
							(NewsItem) array.get(position + 2),
							getUrl((NewsItem) array.get(position + 2),
									TYPE_THREE_ITEM),
							ImageDAO.getInstance(mContext)
									.getThirdImageFetcher(), position, 2);
				} else {
					holder.content3.setVisibility(View.GONE);
				}
			} else {
				holder.content2.setVisibility(View.GONE);
				holder.content3.setVisibility(View.GONE);
			}

		}

		private void setItemFourType(int position, ViewHolder holder) {
			setItem(holder.content1, (NewsItem) array.get(position),
					getUrl((NewsItem) array.get(position), TYPE_FOUR_ITEM),
					ImageDAO.getInstance(mContext).getFourthImageFetcher(),
					position, 0);

			if (array.size() > (position + 1)) {
				setItem(holder.content2,
						(NewsItem) array.get(position + 1),
						getUrl((NewsItem) array.get(position + 1),
								TYPE_FOUR_ITEM), ImageDAO.getInstance(mContext)
								.getFourthImageFetcher(), position, 1);
				if (array.size() > (position + 2)) {
					setItem(holder.content3,
							(NewsItem) array.get(position + 2),
							getUrl((NewsItem) array.get(position + 2),
									TYPE_FOUR_ITEM),
							ImageDAO.getInstance(mContext)
									.getFourthImageFetcher(), position, 2);
					if (array.size() > (position + 3)) {
						setItem(holder.content4,
								(NewsItem) array.get(position + 3),
								getUrl((NewsItem) array.get(position + 3),
										TYPE_FOUR_ITEM),
								ImageDAO.getInstance(mContext)
										.getFourthImageFetcher(), position, 3);
					} else {
						holder.content4.setVisibility(View.GONE);
					}
				} else {
					holder.content3.setVisibility(View.GONE);
					holder.content4.setVisibility(View.GONE);
				}
			} else {
				holder.content2.setVisibility(View.GONE);
				holder.content3.setVisibility(View.GONE);
				holder.content4.setVisibility(View.GONE);
			}

		}

		private void setItemHeaderType(int position, ViewHolder holder) {
			setItem(holder.content1, (NewsItem) array.get(position),
					getUrl((NewsItem) array.get(position), TYPE_HEADER_ITEM),
					ImageDAO.getInstance(mContext).getFourthImageFetcher(),
					position, 0);

			boolean pos2 = array.size() > (position + 1);
			boolean pos3 = array.size() > (position + 2);
			boolean pos4 = array.size() > (position + 3);

			if (pos2) {
				String smallUrl;
				if (pos3)
					smallUrl = getSmallUrl((NewsItem) array.get(position + 1));
				else
					smallUrl = getUrl((NewsItem) array.get(position + 1),
							TYPE_HEADER_ITEM);

				setItem(holder.content2, (NewsItem) array.get(position + 1),
						smallUrl, ImageDAO.getInstance(mContext)
								.getHeaderImageFetcher(), position, 1);
				if (pos3) {
					smallUrl = getSmallUrl((NewsItem) array.get(position + 2));
					setItem(holder.content3,
							(NewsItem) array.get(position + 2), smallUrl,
							ImageDAO.getInstance(mContext)
									.getHeaderImageFetcher(), position, 2);
					if (pos4) {
						setItem(holder.content4,
								(NewsItem) array.get(position + 3),
								getSmallUrl((NewsItem) array.get(position + 3)),
								ImageDAO.getInstance(mContext)
										.getHeaderImageFetcher(), position, 3);
					} else {
						holder.content4.setVisibility(View.GONE);
					}
				} else {
					holder.content3.setVisibility(View.GONE);
					holder.content4.setVisibility(View.GONE);
				}
			} else {
				holder.content2.setVisibility(View.GONE);
				holder.content3.setVisibility(View.GONE);
				holder.content4.setVisibility(View.GONE);
			}

		}

		private void setItem(View content, NewsItem item, String urlPhoto,
				ImageFetcher imageFetcher, int position, int offset) {

			if (urlPhoto != null && urlPhoto.length() > 0) {

				ImageView imageView = (ImageView) content
						.findViewById(R.id.image);
				loadImage(urlPhoto, imageFetcher, imageView);

				if (item.getVideo() != null && item.getVideo().getUrl() != null) {
					// content.setTag(item.getVideo().getUrl());
					content.findViewById(R.id.iconVideo).setVisibility(
							View.VISIBLE);
					// } else {
					// content.setTag(item.getUrlDetail());
				}
			}

			content.setTag(position + offset);
			goToDetail(content);

			((TextView) content.findViewById(R.id.title)).setText(item
					.getTitle());
			((TextView) content.findViewById(R.id.preTitle)).setText(item
					.getPreTitle());
		}

		private void loadImage(String urlPhoto, ImageFetcher imageFetcher,
				ImageView imageView
		// , int position
		) {

			imageFetcher.loadImage(urlPhoto, imageView);

			// boolean needReload = true;
			// AsyncImageTask previousTask = AsyncDrawable.getTask(imageView);
			// if (previousTask != null) {
			// if (previousTask.getImageIndex() == position
			// && previousTask.getImageUrl().equals(urlPhoto))
			// // check if the convertView happens to be previously used
			// {
			// needReload = false;
			// } else {
			// previousTask.cancel(true);
			// }
			// }
			//
			// if (needReload && Reachability.isOnline(mContext)) {
			// AsyncImageTask task = new AsyncImageTask(imageView, flipView,
			// position, urlPhoto);
			// imageView.setImageDrawable(new AsyncDrawable(mContext
			// .getResources(), placeholderBitmap, task));
			//
			// task.execute();
			// }
		}

		private String getUrl(NewsItem item, int type) {
			String urlPhoto = null;

			if ((type == TYPE_ONE_ITEM || type == TYPE_TWO_ITEM)
					&& item.getPhotoBig() != null) {
				urlPhoto = item.getPhotoBig().getUrl();
			} else if (item.getPhotoNormal() != null) {
				urlPhoto = item.getPhotoNormal().getUrl();
			} else if (item.getVideo() != null) {
				if (item.getVideo().getPhoto() != null) {
					urlPhoto = item.getVideo().getPhoto().getUrl();
				}
			}
			return urlPhoto;
		}

		private String getSmallUrl(NewsItem item) {
			String urlPhoto = null;

			// } else if (item.getPhotoThumbnail() != null) {
			// urlPhoto = item.getPhotoThumbnail().getUrl();
			if (item.getPhotoNormal() != null) {
				urlPhoto = item.getPhotoNormal().getUrl();
			} else if (item.getVideo() != null) {
				if (item.getVideo().getPhoto() != null) {
					urlPhoto = item.getVideo().getPhoto().getUrl();
				}
			} else if (item.getPhotoBig() != null) {
				urlPhoto = item.getPhotoBig().getUrl();
			}
			return urlPhoto;
		}

		private void goToDetail(View image) {
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(mContext,
							NewsDetailActivity.class);
					intent.putExtra("position", (Integer) v.getTag());
					getActivity().startActivityForResult(intent,
							ReturnRequestCodes.PUBLI_BACK);
					getActivity().overridePendingTransition(
							R.anim.grow_from_middle, R.anim.shrink_to_middle);
				}
			});

		}
	}

	static class ViewHolder {

		public RelativeLayout content1;
		public RelativeLayout content2;
		public RelativeLayout content3;
		public RelativeLayout content4;

	}

}
