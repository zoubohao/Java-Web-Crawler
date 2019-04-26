package Demo;

import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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
		CloseableHttpClient client = HttpClients.createDefault();getClass();
		postOb.setHeader("Accept", "*/*");
		postOb.setHeader("Cookie", cookieParam);
		postOb.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
		postOb.setHeader("Host","www.semanticscholar.org");
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
		postOb.setEntity(entity);
		try {
			System.out.println("Start getting entity.");
			CloseableHttpResponse response = client.execute(postOb);
			result = EntityUtils.toString(response.getEntity());
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
		String testResult = post.doPostWithQuery("SPaM: a combined self-paced reading and masked-priming paradigm.",
				"s2Hist=2019-04-26T00%3A00%3A00.000Z%7C1; tid=rBIABlzCqN9GHQAJBGszAg==; _ga=GA1.2.938069798.1556261094; _gid=GA1.2.1874107658.1556261094; _hp2_props.2424575119=%7B%22feature%3Alibrary_tags%22%3Atrue%2C%22feature%3Apdp_entity_relations%22%3Afalse%2C%22experiment%3Aautocomplete%22%3A%22fresh%22%2C%22is_exposed%3Anull_hypothesis%22%3Atrue%2C%22feature%3Apdp_citation_intents%22%3Afalse%2C%22Is%2090-day%20Returning%20(Non-BoD)%22%3Afalse%2C%22experiment%3Aauthor_claim_flow%22%3A%22control%22%2C%22experiment%3Apdp_citations_card_histogram%22%3A%22control%22%2C%22feature%3Acopyright_banner%22%3Atrue%2C%22experiment%3Aserp_alert%22%3A%22%22%2C%22is_exposed%3Ahomepage_featured_content%22%3Atrue%2C%22is_exposed%3Aserp_filter_state%22%3Atrue%2C%22feature%3Aauthor_claim%22%3Afalse%2C%22is_exposed%3Apdp_figure_delayed_load%22%3Afalse%2C%22experiment%3Atest_experiment%22%3A%22control%22%2C%22feature%3Aalternate_sources%22%3Atrue%2C%22feature%3Apreview_box_entity_stats%22%3Afalse%2C%22feature%3Apdp_user_feedback%22%3Atrue%2C%22feature%3Apdp_paper_faqs_cs_only%22%3Atrue%2C%22feature%3Aauthor_influence_graph%22%3Atrue%2C%22Is%2014-day%20Returning%20(Non-BoD)%22%3Afalse%2C%22Is%201-day%20Returning%20(Non-BoD)%22%3Atrue%2C%22is_exposed%3Aauthor_claim_flow%22%3Afalse%2C%22experiment%3Anull_hypothesis%22%3A%22%22%2C%22Is%2028-day%20Returning%20(Non-BoD)%22%3Afalse%2C%22feature%3Apdp_paper_faqs%22%3Atrue%2C%22is_exposed%3Aautocomplete%22%3Atrue%2C%22is_exposed%3Apdp_citations_card_histogram%22%3Afalse%2C%22feature%3Alayover_logger%22%3Atrue%2C%22feature%3Apdp_paper_faqs_numerical_only%22%3Atrue%2C%22is_exposed%3Atest_experiment%22%3Afalse%2C%22is_exposed%3Aserp_alert%22%3Atrue%2C%22experiment%3Ahomepage_featured_content%22%3A%22%22%2C%22Is%207-day%20Returning%20(Non-BoD)%22%3Afalse%2C%22experiment%3Aserp_filter_state%22%3A%22%22%2C%22feature%3Aemergency_banner%22%3Afalse%2C%22experiment%3Apdp_figure_delayed_load%22%3A%22%22%2C%22feature%3Asimilar_papers%22%3Atrue%2C%22feature%3Aauthor_claim_link_to_invision%22%3Afalse%2C%22Is%20Signed%20In%22%3Afalse%7D; _hp2_id.2424575119=%7B%22userId%22%3A%225540590725634938%22%2C%22pageviewId%22%3A%220000470327816437%22%2C%22sessionId%22%3A%228994073767181606%22%2C%22identity%22%3Anull%2C%22trackerVersion%22%3A%224.0%22%7D; s2_copyright_dismissed=1; sid=cc2cab54-8244-4235-8826-a71a393b1395; _gat=1; _hp2_ses_props.2424575119=%7B%22ts%22%3A1556266670672%2C%22d%22%3A%22www.semanticscholar.org%22%2C%22h%22%3A%22%2F%22%7D" 
				);
		System.out.println(testResult);
	}
}
