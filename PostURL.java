package Demo;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;


public class PostURL {
	
	private String url = "https://www.semanticscholar.org/api/1/search";
	
	public String doPostWithQuery(String queryString,String cookieParam) {
		String result = null;
		HttpPost postOb = new HttpPost(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10, TimeUnit.SECONDS)
				.setCircularRedirectsAllowed(true).setConnectTimeout(10, TimeUnit.SECONDS).setRedirectsEnabled(true)
				.setResponseTimeout(10, TimeUnit.SECONDS).build();
		postOb.setConfig(config);
		CloseableHttpClient client = HttpClients.createDefault();
		postOb.setHeader("Accept", "*/*");
		postOb.setHeader("Cookie", cookieParam);
		postOb.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
		postOb.setHeader("Host","www.semanticscholar.org");
		postOb.setHeader("Content-Type", "application/json");
		@SuppressWarnings("deprecation")
		String encoder = URLEncoder.encode(queryString);
		postOb.setHeader("Referer", "https://www.semanticscholar.org/search?"
				+ "q=" + encoder + "&sort=relevance");
		JSONObject jsonOb = new JSONObject();
		jsonOb.put("authors",new ArrayList<String>());
		jsonOb.put("queryString", queryString);
		jsonOb.put("coAuthors", new ArrayList<String>());
		jsonOb.put("page",1);
		jsonOb.put("pageSize",20);
		jsonOb.put("publicationTypes",new ArrayList<String>());
		jsonOb.put("requireViewablePdf",false);
		jsonOb.put("sort","relevance");
		jsonOb.put("venues",new ArrayList<String>());
		jsonOb.put("yearFilter",null);
		ContentType type = ContentType.APPLICATION_JSON;
		StringEntity entity = new StringEntity(jsonOb.toString(),type);
//		List<BasicNameValuePair> paraList = new ArrayList<>();
//		paraList.add(new BasicNameValuePair("", ""));
//		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paraList);
		postOb.setEntity(entity);
		try {
			System.out.println("Start getting entity.");
			CloseableHttpResponse response = client.execute(postOb);
			result = EntityUtils.toString(response.getEntity());
			response.close();
			postOb.clear();
			client.close();
			System.out.println("Completed.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("An Error has taken place .");
			e.printStackTrace();
			System.exit(1);
		}
		return result;
	}
	
	public static void main(String args []) {
		PostURL post = new PostURL();
		String testResult = post.doPostWithQuery("thesis","");
		System.out.println(testResult);
	}
}
