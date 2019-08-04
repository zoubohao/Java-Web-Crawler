package Demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SundayObserver {
	
	
	String searchURL = "http://www.sundayobserver.lk/search/node/";
	Map<String,String> title2Link = new HashMap<>();
	
	
	public String getSearchContent(String url , String keyWord , String page) {
		String content = "";
		@SuppressWarnings("deprecation")
		HttpGet get = new HttpGet(url + URLEncoder.encode(keyWord) + "?page=" + page);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		get.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		get.setHeader("Connection","keep-alive");;
		get.setHeader("Cookie", "__cfduid=d13f76025f373ebf7f7834e84821ade091564800386; has_js=1; "
				+ "_ga=GA1.2.881028285.1564800440; "
				+ "_gid=GA1.2.1036763018.1564800440; session_depth=www.sundayobserver.lk%3D1%7C484643417%3D1%7C770884544%3D1%7C782586334%3D1; "
				+ "hbcm_sd=1%7C1564800439997; __gads=ID=44fdba5dcb21dbd2:T=1564800444:S=ALNI_MZDb2EVT7nvaZ_3YS5ECA4rO03KhQ; "
				+ "_gat=1; _fbp=fb.1.1564800705692.509965346");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		get.setHeader("Referer","http://www.sundayobserver.lk/");
		get.setHeader("TE","Trailers");
		get.setHeader("Upgrade-Insecure-Requests","1");
		try {
			CloseableHttpResponse response = client.execute(get);
			content = EntityUtils.toString(response.getEntity());
			response.close();
			get.clear();
			client.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("An error has taken place in the processing.");
		}
		return content ;
	}
	
	public void parseSearchContent(String html) {
		Document doc = Jsoup.parse(html);
		Elements searchResults = doc.getElementsByClass("search-result");
		for (Element oneResult : searchResults) {
			Elements aEles = oneResult.getElementsByTag("a");
			for (Element a : aEles) {
				String title = a.text();
				String link = a.attr("href");
				System.out.println(title + " : " + link);
				this.title2Link.put(title, link);
			}
		}
	}
	
	public void getNewsContent(String title , String url ) {
		String content = "";
		HttpGet get = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		get.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		get.setHeader("Connection","keep-alive");
		get.setHeader("Cookie", "__cfduid=d13f76025f373ebf7f7834e84821ade091564800386;"
				+ " has_js=1; _ga=GA1.2.881028285.1564800440; "
				+ "_gid=GA1.2.1036763018.1564800440; "
				+ "session_depth=www.sundayobserver.lk%3D2%7C484643417%3D2%7C770884544%3D2%7C782586334%3D2; "
				+ "hbcm_sd=2%7C1564800439997; __gads=ID=44fdba5dcb21dbd2:T=1564800444:S=ALNI_MZDb2EVT7nvaZ_3YS5ECA4rO03KhQ; "
				+ "_fbp=fb.1.1564800705692.509965346");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		get.setHeader("Host","www.sundayobserver.lk");
		get.setHeader("Upgrade-Insecure-Requests","1");
		try {
			CloseableHttpResponse response = client.execute(get);
			content = EntityUtils.toString(response.getEntity());
			response.close();
			get.clear();
			client.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("An error has taken place in the processing.");
		}
		String newsContent = "";
		Document doc = Jsoup.parse(content);
		Elements newsEles = doc.getElementsByClass("content");
		for (Element ele : newsEles) {
			Elements pTags = ele.getElementsByTag("p");
			for (Element p : pTags) {
				newsContent = newsContent + p.text() + " ";
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("d:\\SundayObserver.txt"),true));
			writer.write(title  + "\t" + newsContent + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SundayObserver spider = new SundayObserver();
		int pages = 21;
		int i = 0;
		while(i <= pages) {
			System.out.println("It is at page " + i);
			String searchHtml = spider.getSearchContent(spider.searchURL, "hambantota port", String.valueOf(i));
			spider.parseSearchContent(searchHtml);
			for(Entry<String,String> onePair : spider.title2Link.entrySet()) {
				System.out.println(onePair.getValue());
				spider.getNewsContent(onePair.getKey(), onePair.getValue());
				int times = (int) (Math.random() * 5000 + 5000.);
				System.out.println("Sleep times " + times);
				try {
					Thread.sleep(times);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			spider.title2Link.clear();
			i++;
		}
	}

}
