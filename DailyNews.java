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


public class DailyNews {
	
	String searchURL = "http://www.dailynews.lk/search/node/";
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
		get.setHeader("Alt-Used","www.dailynews.lk:443");
		get.setHeader("Cookie", "__cfduid=dec15a5e25f803f305f58706b019c20491564796086; "
				+ "has_js=1; "
				+ "session_depth=www.dailynews.lk%3D5%7C772828533%3D1%7C782167861%3D4%7C117542121%3D4; "
				+ "hbcm_sd=5%7C1564796089572; _ga=GA1.2.1346601825.1564796090; "
				+ "_gid=GA1.2.1820720722.1564796091; _fbp=fb.1.1564796106955.573865372; "
				+ "_gat_gtag_UA_2630033_1=1; _gat=1");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		get.setHeader("Host","www.dailynews.lk");
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
	
	public void parseSearchContent (String content ) {
		Document doc = Jsoup.parse(content);
		Elements searchElements = doc.getElementsByClass("search-result");
		for (Element oneResult : searchElements) {
			Elements aLinks = oneResult.getElementsByTag("a");
			for (Element a : aLinks) {
				String title = a.text();
				String link = a.attr("href");
				this.title2Link.put(title,link);
				System.out.println(title + " : " + link);
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
		get.setHeader("Alt-Used","www.dailynews.lk:443");
		get.setHeader("Connection","keep-alive");
		get.setHeader("Cookie", "__cfduid=dec15a5e25f803f305f58706b019c20491564796086;"
				+ " has_js=1; session_depth=www.dailynews.lk%3D8%7C772828533%3D1%7C782167861%3D7%7C117542121%3D7;"
				+ " hbcm_sd=2%7C1564797090586; "
				+ "_ga=GA1.2.1346601825.1564796090; "
				+ "_gid=GA1.2.1820720722.1564796091; _fbp=fb.1.1564796106955.573865372; "
				+ "__gads=ID=8afd69ee86d52e2c:T=1564796419:S=ALNI_MZYHSOrgRtp0DRqoxbZYSDnKIrUuw");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		get.setHeader("Host","www.dailynews.lk");
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
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("d:\\DailyNews.txt"),true));
			writer.write(title  + "\t" + newsContent + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyNews spider = new DailyNews();
		int pages = 96;
		int i = 0;
		while(i <= pages) {
			System.out.println("It is at page " + i);
			String searchHtml = spider.getSearchContent(spider.searchURL,"hambantota port",String.valueOf(i));
			spider.parseSearchContent(searchHtml);
			for (Entry<String,String> onePair : spider.title2Link.entrySet()) {
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
