package com.example.pc_.wangyi.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DouBanContent;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.model.ZhiHuContent;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.utils.NetConnectUtil;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by pc- on 2017/5/17.
 */
public class ZhiHuContentActivity extends BaseActivity implements View.OnClickListener {



    public DouBanContent douBanContent1;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Cursor cursor;
    public ZhiHuContent zhiHuNewsCommom;
    public Animation animation;
    public FetchManager fetchManager2;


    public String guoqiaoString;
    public String modelType;
    public int id;
    public int type;
    public boolean hasAnim = false;
    public String titleContent;
    public String imageUrl;
    public String firstIndex;
    public String lastIndex1;
    public boolean isFirst = true;
    public String doubanContentStr;
    public String shareTitle;



    @BindView(R.id.zhihu_content_more)
    ImageView zhihuContentMore;
    @BindView(R.id.zhihu_webview)
    WebView zhihuWebview;
    @BindView(R.id.baocun_text)
    TextView baocunText;
    @BindView(R.id.link_text)
    TextView linkText;
    @BindView(R.id.zhihu_content_linear)
    RelativeLayout zhihuContentLinear;


    public boolean isSuccess=false;

    @Override
    public int getLayout() {
        return R.layout.zhihu_content;
    }


    @Override
    public void initView() {


        fetchManager2=FetchManager.newInstance(this);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("Id");
        modelType = bundle.getString("ModelType");
        titleContent = bundle.getString("Title");
        imageUrl = bundle.getString("ImageUrl");
        dataBaseHelper = DataBaseHelper.newInstance(this);
        WebSettings settings = zhihuWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        zhihuWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public void initData() {
        switch (modelType) {
            case "GuoQiao":
                if (NetConnectUtil.isNetworkConnected(this)) {
                    compositeSubscription.add(fetchManager2.fetchContent(id, "GuoQiao"));
                    fetchManager2.setiFetchInstanceCompleteListener(new FetchManager.IFetchInstanceCompleteListener() {
                        @Override
                        public void loadSuccess(Object object) {
                            convertGuokrContent((String) object);
                            zhihuWebview.loadDataWithBaseURL("x-data://base", guoqiaoString, "text/html", "utf-8", null);
                            isSuccess=true;
                        }
                    });
                }
                break;
            case "ZhiHu":
                if (NetConnectUtil.isNetworkConnected(this)) {
                    compositeSubscription.add(fetchManager2.fetchContent(id, "ZhiHu"));
                    fetchManager2.setiFetchInstanceCompleteListener(new FetchManager.IFetchInstanceCompleteListener() {
                        @Override
                        public void loadSuccess(Object object) {
                            ZhiHuContent zhiHuContent = (ZhiHuContent) object;
                            zhiHuNewsCommom = zhiHuContent;
                            if (zhiHuContent.getBody() == null) {
                                zhihuWebview.loadUrl(zhiHuContent.getShare_url());
                                isSuccess=true;
                            } else {
                                zhihuWebview.loadDataWithBaseURL("x-data://base", convertZhihuContent(zhiHuContent.getBody()), "text/html", "utf-8", null);
                                sqLiteDatabase = dataBaseHelper.getWritableDatabase();
                                cursor = sqLiteDatabase.query("ArtContent", null, null, null, null, null, null);
                                isSuccess=true;
                                if (cursor.getCount() == 0 || cursor == null) {
                                    //如果没有保存过该数据
                                    saveHistoryData(zhiHuContent.getBody());
                                } else if (cursor.getCount() > 0) {
                                   //不保存同名，只保存15个数据
                                    boolean isSave=true;
                                    int allCount=cursor.getCount();
                                    while (cursor.moveToNext()&&isSave){
                                        String titleContent2=cursor.getString(cursor.getColumnIndex("title"));
                                        //如果标签一样，不保存
                                        if(titleContent2.equals(titleContent)){
                                            isSave=false;
                                        }
                                    }
                                    //cursor = sqLiteDatabase.query("ArtContent", null, null, null, null, null, null);
                                    if(isSave){
                                        saveHistoryData(zhiHuContent.getBody());
//                                        if(cursor.getCount()==15){
//                                            int i=0;
//                                            String lastIndex=null;
//                                            while (cursor.moveToNext()&&i<15){
//                                                i++;
//                                                lastIndex=cursor.getString(cursor.getColumnIndex("indexCount"));
//                                            }
//                                            cursor = sqLiteDatabase.query("ArtContent", null, null, null, null, null, null);
//                                            boolean isFirst=true;
//                                            while (cursor.moveToNext()&&isFirst){
//                                                String indexCount=cursor.getString(cursor.getColumnIndex("indexCount"));
//                                                sqLiteDatabase.delete("ArtContent","indexCount=?",new String[]{indexCount});
//                                                isFirst=false;
//                                            }
//                                            int lastIndexInt=Integer.valueOf(lastIndex)+1;
//                                            saveHistoryData(lastIndexInt,zhiHuContent.getBody());
//
//                                        }else  if(cursor.getCount()<15){
//                                            int j=0;
//                                            int allCount=cursor.getCount();
//                                            String lastIndex2=null;
//                                            while (cursor.moveToNext()&&j<allCount){
//                                                j++;
//                                                lastIndex2=cursor.getString(cursor.getColumnIndex("indexCount"));
//                                            }
//                                            int lastIndexInt=Integer.valueOf(lastIndex2)+1;
//                                             saveHistoryData(lastIndexInt,zhiHuContent.getBody());
//
//                                        }
                               }


                                }
                                cursor.close();
                            }
                        }

                    });
                }
                break;
            case "DouBan":
                if (NetConnectUtil.isNetworkConnected(this)) {
                    compositeSubscription.add(fetchManager2.fetchContent(id, "DouBan"));
                    fetchManager2.setiFetchInstanceCompleteListener(new FetchManager.IFetchInstanceCompleteListener() {
                        @Override
                        public void loadSuccess(Object object) {
                            douBanContent1 = (DouBanContent) object;
                            doubanContentStr = convertDoubanContent();
                            zhihuWebview.loadDataWithBaseURL("x-data://base", doubanContentStr, "text/html", "utf-8", null);
                            isSuccess=true;

                        }
                    });

                }
                break;
        }


        zhihuWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    public void  saveHistoryData(String bodyHtml){
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("textId", id);
        contentValues2.put("type", "知乎");
        contentValues2.put("title", titleContent);
        contentValues2.put("imageurl", imageUrl);
        contentValues2.put("html", bodyHtml);
        sqLiteDatabase.insert("ArtContent", null, contentValues2);

    }
    public void saveDownData(){
        ContentValues contentValuesDown = new ContentValues();
        contentValuesDown.put("typeDown", "知乎");
        contentValuesDown.put("textDownId", id);
        contentValuesDown.put("titleDown", titleContent);
        contentValuesDown.put("imageurlDown", imageUrl);
        contentValuesDown.put("htmlDown", zhiHuNewsCommom.getBody());
        sqLiteDatabase.insert("ArtDownContent", null, contentValuesDown);

    }

    @Override
    public void initEvent() {
        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                shareTitle = "" + titleContent + " ";

                switch (modelType) {
                    case "ZhiHu":
                        shareTitle += zhiHuNewsCommom.getShare_url();
                        break;
                    case "GuoQiao":
                        String GUOKR_SHARE_LINK = "http://jingxuan.guokr.com/pick/";
                        shareTitle += GUOKR_SHARE_LINK + id;
                        break;
                    case "DouBan":
                        shareTitle += douBanContent1.getShort_url();
                }
                Animation downText = AnimationUtils.loadAnimation(ZhiHuContentActivity.this, R.anim.more_anim);
                linkText.startAnimation(downText);
                String shareExtrs = "分享自 HaoYun";
                String shareTo = "分享至";
                shareTitle = shareTitle + "\t\t\t" + shareExtrs;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareTitle);
                startActivity(Intent.createChooser(shareIntent, shareTo));
            }
        });

        baocunText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (modelType.equals("ZhiHu")) {
                    Cursor downCursor = sqLiteDatabase.query("artDownContent", null, null, null, null, null, null);
                    if (downCursor.getCount() == 0) {
                        if (imageUrl == null) {
                            imageUrl = "NoUrl";
                        }
                        saveDownData();

                    } else {

                            boolean hasContent = false;
                            while (downCursor.moveToNext()) {
                                if (downCursor.getString(downCursor.getColumnIndex("titleDown")).equals(titleContent)) {
                                    hasContent = true;
                                    Toast.makeText(ZhiHuContentActivity.this, "该内容已经存在", Toast.LENGTH_SHORT).show();
                                }

                            }
                            if (!hasContent) {
                                if (imageUrl == null) {
                                    imageUrl = "NoUrl";
                                }
                                saveDownData();
                            }
                            downCursor.close();
                            Animation downText = AnimationUtils.loadAnimation(ZhiHuContentActivity.this, R.anim.more_anim);
                            baocunText.startAnimation(downText);

                        }

                }
                else{
                    Toast.makeText(ZhiHuContentActivity.this,"只提供知乎的内容下载",Toast.LENGTH_SHORT).show();
                }


            }
        });

        zhihuContentMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAnim) {
                    Animation animation1 = AnimationUtils.loadAnimation(ZhiHuContentActivity.this, R.anim.close_down_anim);
                    zhihuContentLinear.setVisibility(View.GONE);
                    zhihuContentLinear.startAnimation(animation1);
                    hasAnim = false;
                } else {
                    animation = AnimationUtils.loadAnimation(ZhiHuContentActivity.this, R.anim.zhihu_down);
                    zhihuContentLinear.setVisibility(View.VISIBLE);
                    zhihuContentLinear.startAnimation(animation);
                    hasAnim = true;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           if(isSuccess){
               this.finish();
           }else {
               Toast.makeText(this,"数据加载中",Toast.LENGTH_SHORT).show();
           }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetchManager2.setiFetchInstanceCompleteListener(null);
    }

    private String convertZhihuContent(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(preResult)
                .append("</body></html>").toString();
    }

    private void convertGuokrContent(String content) {
        // 简单粗暴的去掉下载的div部分
        this.guoqiaoString = content.replace("<div class=\"down\" id=\"down-footer\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"\" class=\"app-down\" id=\"app-down-footer\">下载</a>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"down-pc\" id=\"down-pc\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"http://www.guokr.com/mobile/\" class=\"app-down\">下载</a>\n" +
                "    </div>", "");

        // 替换css文件为本地文件
        guoqiaoString = guoqiaoString.replace("<link rel=\"stylesheet\" href=\"http://static.guokr.com/apps/handpick/styles/d48b771f.article.css\" />",
                "<link rel=\"stylesheet\" href=\"file:///android_asset/guokr.article.css\" />");

        // 替换js文件为本地文件
        guoqiaoString = guoqiaoString.replace("<script src=\"http://static.guokr.com/apps/handpick/scripts/9c661fc7.base.js\"></script>",
                "<script src=\"file:///android_asset/guokr.base.js\"></script>");

    }

    private String convertDoubanContent() {


        if (douBanContent1.getContent() == null) {
            return null;
        }

        String css;
        if ((this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_dark.css\" type=\"text/css\">";
        } else {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";
        }
        String content = douBanContent1.getContent();
        ArrayList<DouBanNews.posts.thumbs> imageList = douBanContent1.getPhotos();
        for (int i = 0; i < imageList.size(); i++) {
            String old = "<img id=\"" + imageList.get(i).getTag_name() + "\" />";
            String newStr = "<img id=\"" + imageList.get(i).getTag_name() + "\" "
                    + "src=\"" + imageList.get(i).getMedium().getUrl() + "\"/>";
            content = content.replace(old, newStr);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>\n<meta charset=\"utf-8\" />\n");
        builder.append(css);
        builder.append("\n</head>\n<body>\n");
        builder.append("<div class=\"container bs-docs-container\">\n");
        builder.append("<div class=\"post-container\">\n");
        builder.append(content);
        builder.append("</div>\n</div>\n</body>\n</html>");

        return builder.toString();
    }


}
