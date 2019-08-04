package Demo;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class JsoupTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String html = "India Aviation Ministry says no proposal to buy controlling stake of Mattala airport" + "\t" 
		+"In response to a question in Lok Sabha today whether the Airport Authority of India (AAI) is considering purchasing a controlling stake in the Mattala airport, thereby gaining ownership of the airport, the Minister of State for Civil Aviation Jayant Sinha has said there is no such proposal under consideration at present. Sri Lanka's Civil Aviation Minister Nimal Siripala de Silva on July 19 told parliament that Airport Authority of India has been requested to submit its business plan for operating the loss-making Mattala Rajapaksa International Airport (MRIA) in Mattala.When asked whether the AAI would build a flying school and a maintenance, repair and overhaul unit at the Mattala airport";
		Document doc = Jsoup.parse(html);
		System.out.println(doc.text());
		Map<String,String>  a = new HashedMap<>();
		a.put("a", "b");
		a.clear();
		System.out.println(a.size());
		String [] splitList = html.split("\t");
		System.out.println(splitList[0]);
		System.out.println(splitList[1]);
	}

}
