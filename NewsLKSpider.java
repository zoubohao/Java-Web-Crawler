package Demo;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

public class NewsLKSpider {
	
	
	String newsURL = "https://news.lk/component/search/?searchword=hambantota%20port&searchphrase=all&start=";
	String searchCookie = "003dd3de8a26d61e2113a85e2ef96f53=3c646ea899f4c23320d8c7242810a0ec; "
			+ "_ga=GA1.2.295306136.1564725237; "
			+ "_gid=GA1.2.1559612636.1564725237; "
			+ "_gat_gtag_UA_20292891_6=1";
	String newsReferer = "https://news.lk/";
	Map<String,String> aElemenets = new HashMap<>();
			
	
	public String getContent(String url,String cookieParam,String referer) {
		String content = ""; 
		HttpGet getMethod = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		getMethod.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		getMethod.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		getMethod.setHeader("Cookie", cookieParam);
		getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		getMethod.setHeader("Host","news.lk");
		getMethod.setHeader("Referer",referer);
		getMethod.setHeader("Connection", "keep-alive");
		getMethod.setHeader("Upgrade-Insecure-Requests","1");
		try {
			System.out.println("Start getting entity.");
			CloseableHttpResponse response = client.execute(getMethod);
			content = EntityUtils.toString(response.getEntity());
			response.close();
			getMethod.clear();
			client.close();
			System.out.println("Completed.");
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("An Error has taken place .");
			e.printStackTrace();
		}
		return content;
	}
	
	public void getAElement(String content) {
		Document doc = Jsoup.parse(content);
		Elements searchEles = doc.getElementsByClass("search-results");
		for (Element ele : searchEles) {
			Elements aEles = ele.getElementsByTag("a");
			for (Element a : aEles) {
				String title = a.text();
				String thisTitleURL = "https://news.lk" + a.attr("href");
				System.out.println(title + " : " + thisTitleURL);
				this.aElemenets.put(title, thisTitleURL);
			}
		}
	}
	
	public void getNewsContent(String url,String title) {
		String content = ""; 
		HttpGet getMethod = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10000, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10000, TimeUnit.SECONDS).build();
		getMethod.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		getMethod.setHeader("Accept", "*/*");
		getMethod.setHeader("Accept-Encoding","gzip, deflate, br");
		getMethod.setHeader("Cookie", "NID=188=TXH1gPAj9Cki9KPbw4ndshaKLnTF1J9yUlLvgGcljpDrY17XPH9j_HtR_Z3V1vtguYfE2SSFGYXMW"
				+ "pf3loKbwwC-2Wx-IHHEMRHriNJZszUgQCXgitqym_3ZkvKdDWWetj2T3DKR9W2_onXP50AfaTXZBkq0bKosjmJzsegojyrlE_HbtrV727Qnv1l04"
				+ "uahAW5_gxYsXJ4Nv0EZ5yceFxZrI3D4jPieF4v6Zfse0bVLLEMpN_BvWVsdFEw3; "
				+ "CONSENT=WP.27701a; SID=mwclEHTuIkNINjkEYHNdtFeXP3hEUwUPa0wd706KIcWU958_3AERzE79xf6sxTYfKHa69w.; "
				+ "HSID=Ask040uN-0wJcyd2h; SSID=A17W4K03UDdrANhHj; "
				+ "APISID=u2QIbvK_PateZTvj/AmhQ2sougAm2z35MQ; "
				+ "SAPISID=2sMwScduimQYLqcN/AbNuPvM-mDlKEELFU; "
				+ "SIDCC=AN0-TYtXLBoVOaqnnRT90HdwcoG2Re8gtRtO8M0dUk5En4xO87t_TZHwBeuhlYYqKbeUf-Og9w; "
				+ "ANID=AHWqTUljKo6kaUtERamkHPMj3t8DTLRw5PYGFibOWJyxfni180n1feB4QqZ3Wo82; "
				+ "1P_JAR=2019-08-02-02");
		getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		getMethod.setHeader("Host","news.lk");
		getMethod.setHeader("Referer",this.newsReferer);
		getMethod.setHeader("Connection", "keep-alive");
		try {
			CloseableHttpResponse response = client.execute(getMethod);
			content = EntityUtils.toString(response.getEntity());
			response.close();
			getMethod.clear();
			client.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("An Error has taken place .");
			System.out.println(title + " : " + url);
		}
		String newsText = "";
		Document doc = Jsoup.parse(content);
		Elements fullText = doc.getElementsByClass("itemFullText");
		for (Element ele : fullText) {
			Elements pEles = ele.getElementsByTag("p");
			for (Element p : pEles) {
				newsText = newsText + p.text();
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("d:\\NEWSLK.txt"),true));
			writer.write(title + "\t" + newsText + "\n");
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(title + " : " + url);
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NewsLKSpider spider = new NewsLKSpider();
		int i = 0;
		while (i <= 280) {
			System.out.println(i);
			String thisUrl = spider.newsURL + String.valueOf(i);
			String referer = "";
			if (i == 0) {
				referer = spider.newsReferer;
			}
			else {
				referer = spider.newsURL + String.valueOf(i - 20);
			}
			String searchContent = spider.getContent(thisUrl, spider.searchCookie, referer);
			spider.getAElement(searchContent);
			int times = (int) (Math.random() * 10000 + 10000.);
			System.out.println(times);
			try {
				Thread.sleep(times);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i = i + 20;
		}
		System.out.println("There are " + spider.aElemenets.size() + " news in list.");
		i = 0;
		Set<Entry<String,String>> keyValue= spider.aElemenets.entrySet();
		for (Entry<String,String> onePaire : keyValue) {
			System.out.println(i + " : " + onePaire.getValue());
			spider.getNewsContent(onePaire.getValue(), onePaire.getKey());
			i += 1;
			int times = (int) (Math.random() * 10000 + 5000.);
			System.out.println("Sleep times :"+times);
			try {
				Thread.sleep(times);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
