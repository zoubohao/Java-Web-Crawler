package Demo;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject jsonOb = new JSONObject();
		jsonOb.put("authors",new ArrayList<>());
		jsonOb.put("queryString", "asdf");
		System.out.println(jsonOb.toString());
		

	}

}
