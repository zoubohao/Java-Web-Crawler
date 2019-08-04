package Demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DailyMirror {
	
	String dailyMirrorUrl = "http://www.dailymirror.lk/home/search";
	String cookie = "__utma=105203478.2032646332.1564734393.1564734393.1564734393.1; "
			+ "__utmb=105203478; "
			+ "__utmc=105203478; "
			+ "__utmz=105203478.1564734399.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); "
			+ "nsd=1; mobile=0; "
			+ "__atuvc=1%7C31; __atuvs=5d43f44a2c646875000";
	String newsUrl = "http://www.dailymirror.lk/";
	Map<String,String> urlsOfOneSearch = new HashMap<>();
	
	public String getJsonContent(String url , String cookie, int start) {
		String content = "";
		HttpPost post = new HttpPost(url);
		RequestConfig config =  RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		post.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		// Set request header
		post.setHeader("Host","www.dailymirror.lk");
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		post.setHeader("Accept","application/json, text/plain, */*");
		post.setHeader("Accept-Encoding","gzip, deflate");
		post.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		post.setHeader("Connection","keep-alive");
//		post.setHeader("Content-Length","76");
		post.setHeader("Content-Type","application/x-www-form-urlencoded");
		post.setHeader("Cookie",this.cookie);
		post.setHeader("Referer","http://www.dailymirror.lk/search");
		List<BasicNameValuePair> paraList = new ArrayList<>();
		paraList.add(new BasicNameValuePair("category", "0"));
		paraList.add(new BasicNameValuePair("order", "DESC"));
		paraList.add(new BasicNameValuePair("datefrom", "0"));
		paraList.add(new BasicNameValuePair("dateto", "0"));
		paraList.add(new BasicNameValuePair("start", String.valueOf(start)));
		paraList.add(new BasicNameValuePair("keywords", "hambantota port"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paraList);
		post.setEntity(entity);
		try {
			CloseableHttpResponse response = client.execute(post);
			content = EntityUtils.toString(response.getEntity());
			response.close();
			post.clear();
			client.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR!!!! Can not get json file.  Try again !!!!");
			this.getJsonContent(url,cookie, start);
		}
		return content;
	}
	
	@SuppressWarnings("deprecation")
	public void parseJson(String jsonText) {
		JSONObject jsonObj = JSON.parseObject(jsonText);
		JSONArray jsonAtrical =  jsonObj.getJSONArray("articles");
		for (Object thisObj : jsonAtrical) {
			JSONObject thisObjT = (JSONObject) thisObj;
			String articalID = thisObjT.getString("ARTICLE_ID");
			String encoderARID = URLEncoder.encode(articalID);
			String title = thisObjT.getString("TITLE");
			char [] titleChars = title.toCharArray();
			int i = 0;
			for (char one : titleChars) {
				if (String.valueOf(one).equals(" ")) {
					titleChars[i] = '-';
				}
				i++;
			}
 			String newTitle = new String(titleChars);
			String encoderTitle = URLEncoder.encode(newTitle);
			JSONArray category = thisObjT.getJSONArray("URL_CATEGORY");
			for (Object cateObj : category) {
				JSONObject cateObjT = (JSONObject) cateObj;
				String categoryID = cateObjT.getString("CATEGORY_ID");
				String encoderCateID = URLEncoder.encode(categoryID);
				String alias = cateObjT.getString("ALIAS");
				String encoderAlias = URLEncoder.encode(alias);
				String wholeURL = this.newsUrl + encoderAlias + "/" + encoderTitle + "/" + encoderCateID + "-" + encoderARID;
				System.out.println(title + " : " + wholeURL);
				this.urlsOfOneSearch.put(title , wholeURL);
			}
		}
	}
	
	
	public void getNewsContent(String url , String title) {
		HttpGet get = new HttpGet(url);
		RequestConfig config =  RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		get.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Host","www.dailymirror.lk");
		get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		get.setHeader("Accept-Encoding","gzip, deflate");
		get.setHeader("Connection","keep-alive");
		get.setHeader("Cookie","__utma=105203478.2032646332.1564734393.1564736883.1564745606.3; "
				+ "__utmc=105203478; "
				+ "__utmz=105203478.1564734399.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); "
				+ "__atuvc=9%7C31; nsdf=1; "
				+ "__gads=ID=e04b14f80dda3baf:T=1564736886:S=ALNI_MaxE0EjjCfqFmv0oijUskDhMeYseg; "
				+ "__utmb=105203478; __atuvs=5d441f8682a424d2002; mobile=0");
		get.setHeader("Upgrade-Insecure-Requests","1");
		try {
			CloseableHttpResponse response = client.execute(get);
			String htmlContent = EntityUtils.toString(response.getEntity());
			Document doc = Jsoup.parse(htmlContent);
			Elements innerContent = doc.getElementsByClass("inner-content");
			String newsContent = "";
			for (Element inner : innerContent) {
				Elements pTags = inner.getElementsByTag("p");
				for (Element p : pTags) {
					newsContent = newsContent + p.text();
				}
				if (newsContent.equals("")) {
					newsContent = newsContent + inner.text();
				}
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("d:\\DailyMirror.txt"),true));
			writer.write(title + "\t" + newsContent + "\n");
			writer.close();
			response.close();
			get.clear();
			client.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR!!!! Can not get news content. Try again !!!!");
			this.getNewsContent(url, title);
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int totalNumber = 11571;
		int pages = (int)totalNumber / 30;
		DailyMirror spider = new DailyMirror();
		int i = 165;
		int start = i * 30;
		while (i <= pages) {
			System.out.println("It is at the " + i + " page.");
			String jsonText = spider.getJsonContent(spider.dailyMirrorUrl, spider.cookie, start);
			spider.parseJson(jsonText);
			for (Entry<String, String> entry : spider.urlsOfOneSearch.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
				spider.getNewsContent(entry.getValue(), entry.getKey());
				int times = (int) (Math.random() * 2000 + 1000.);
				System.out.println("Sleep times " + times);
				try {
					Thread.sleep(times);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			spider.urlsOfOneSearch.clear();
			start = start + 30;
			i++;
		}
	}

}
