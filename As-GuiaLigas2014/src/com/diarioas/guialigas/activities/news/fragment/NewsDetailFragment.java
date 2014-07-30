package com.diarioas.guialigas.activities.news.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.news.NewsDetailActivity;
import com.diarioas.guialigas.activities.team.TeamVideoActivity;
import com.diarioas.guialigas.dao.model.news.NewsItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.Reachability;
import com.diarioas.guialigas.utils.scroll.ParallaxScrollView;

public class NewsDetailFragment extends Fragment

{

	private View generalView;
	private Context mContext;

	private NewsItem item;
	private View contentVideo;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity().getApplicationContext();

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_news_detail,
				container, false);

		return generalView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// if (savedInstanceState == null) {

		// if (item == null) {
		configureView();
		// this.firstExecution = false;
		// }

		// }
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
		generalView = null;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	private void configureView() {
		int competitionId = RemoteDataDAO.getInstance(mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		item = RemoteNewsDAO.getInstance(mContext).getNewsPreloaded(
				competitionId, getArguments().getInt("itemNumber"));
		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		int maxScroll = DimenUtils.getRegularPixelFromDp(mContext, size.y / 4);

		// int headerHeight = 300;
		int headerHeight = (int) getResources().getDimension(
				R.dimen.image_news_detail_height);

		View header = generalView.findViewById(R.id.header);
		header.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				headerHeight));

		contentVideo = generalView.findViewById(R.id.contentVideo);
		if (item.getVideo() != null && item.getVideo().getUrl() != null) {
			contentVideo.setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, headerHeight));

			header.setTag(getArguments().getInt("itemNumber"));
			header.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					goToVideo();
				}

				private void goToVideo() {

					Intent intent = new Intent(mContext,
							TeamVideoActivity.class);
					intent.putExtra("url", item.getVideo().getUrl());
					intent.putExtra("section", item.getTitle());
					intent.putExtra("subsection", item.getTitle());

					getActivity().startActivityForResult(intent,
							ReturnRequestCodes.PUBLI_BACK);
					getActivity().overridePendingTransition(
							R.anim.grow_from_middle, R.anim.shrink_to_middle);
				}
			});

		} else {
			contentVideo.setVisibility(View.GONE);
		}

		ImageView imageView = (ImageView) generalView.findViewById(R.id.image);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, headerHeight);
		imageView.setLayoutParams(imageParams);
		String urlphoto = getUrl();
		if (urlphoto != null && !urlphoto.equalsIgnoreCase("")) {
			ImageDAO.getInstance(getActivity()).loadNewsImage(urlphoto, imageView);
		} else {
			header.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
		}
		TextView preTitle = (TextView) generalView.findViewById(R.id.preTitle);
		FontUtils.setCustomfont(mContext, preTitle, FontTypes.HELVETICANEUE);
		preTitle.setText(item.getPreTitleSection());

		TextView title = (TextView) generalView.findViewById(R.id.title);
		FontUtils.setCustomfont(mContext, title, FontTypes.HELVETICANEUE);
		title.setText(item.getTitle());

		TextView abstractText = (TextView) generalView
				.findViewById(R.id.abstractText);
		FontUtils
				.setCustomfont(mContext, abstractText, FontTypes.HELVETICANEUE);
		abstractText.setText(item.getAbstract());

		configureWebView();

		ParallaxScrollView scroll = (ParallaxScrollView) generalView
				.findViewById(R.id.scrollNews);
		scroll.setMaxYOverscrollDistance(maxScroll);
		scroll.setImageInBackground(generalView.findViewById(R.id.contentImage));

	}

	@SuppressLint("NewApi")
	private void configureWebView() {
		String body = getBody();

		if (body != null && !body.equalsIgnoreCase("")) {

			final WebView webViewNewsDetail = (WebView) generalView
					.findViewById(R.id.webViewNewsDetail);
			webViewNewsDetail.setFocusable(false);
			webViewNewsDetail.getSettings().setJavaScriptEnabled(true);
			// webViewNewsDetail.getSettings().setFixedFontFamily(
			// FontTypes.HELVETICANEUE);
			webViewNewsDetail.getSettings().setDefaultFontSize(
					(int) getResources().getDimension(R.dimen.size_news_body));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				webViewNewsDetail.getSettings().setDisplayZoomControls(false);
			}
			webViewNewsDetail.loadDataWithBaseURL(item.getUrlDetail(), body,
					"text/html", "UTF-8", null);

		}
	}

	private String getBody() {
		if (item.getBody() == null)
			return null;

		String htmlFileString = FileUtils.readFileFromAssets(getActivity()
				.getAssets(), "web/salida_noticia_noimage_template.html");
		String cssFileString = FileUtils.readFileFromAssets(getActivity()
				.getAssets(), "web/salida_noticia.css");

		String htmlWebView = "";

		htmlWebView = htmlFileString.replace("cssAndroid", cssFileString)
				.replace("bodyAndroid", item.getBody());

		Log.d("WEBVIEW", "Body: " + htmlWebView);
		return htmlWebView;
	}

	private String getUrl() {
		if (Reachability.isOnline(mContext)) {
			if (item.getPhotoBig() != null
					&& item.getPhotoBig().getUrl() != null)
				return item.getPhotoBig().getUrl();
			else if (item.getPhotoNormal() != null
					&& item.getPhotoNormal().getUrl() != null)
				return item.getPhotoNormal().getUrl();
			else if (item.getVideo() != null
					&& item.getVideo().getPhoto() != null
					&& item.getVideo().getPhoto().getUrl() != null)
				return item.getVideo().getPhoto().getUrl();
			else if (item.getPhotoThumbnail() != null
					&& item.getPhotoThumbnail().getUrl() != null)
				return item.getPhotoThumbnail().getUrl();
			else
				return null;
		} else {
			return null;
		}
	}

}
