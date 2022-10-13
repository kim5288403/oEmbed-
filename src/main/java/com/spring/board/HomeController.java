package com.spring.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static JSONParser jsonParser = null;
	private static List<String> lst = null;
	private static JSONArray jsonArray = null;
	private URL url;

	public static void providerData() throws IOException {
	    lst = new ArrayList<String>();
	    jsonParser = new JSONParser();

	    ClassPathResource classPathResource = new ClassPathResource("/providers.json");
	    BufferedReader rd = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
	    try {
	        Object obj = jsonParser.parse(rd);

	        JSONArray jsonArr = (JSONArray) obj;

	        for (int i = 0; i < jsonArr.size(); i++) {
	            JSONObject provider_url = (JSONObject) jsonArr.get(i);
	            String url = (String) provider_url.get("endpoints").toString();

	            Object obj2 = jsonParser.parse(url);
	            jsonArray = new JSONArray();
	            jsonArray = (JSONArray) obj2;
	            JSONObject urlData = (JSONObject) jsonArray.get(0);

	            String value = (String) urlData.get("url");
	            lst.add(value);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public String hostCheck(String str) {
	    String result = "";
	    try {
	        url = new URL(str);

	        String[] split = url.getHost().split("\\.");

	        if (split.length == 2) {
	            result = split[0];
	        } else if (split.length == 3) {
	            result = split[1];
	        }

	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	public String createAddr(String host, String encode) {
	    String oembedUrl = "";

	    for (String str : lst) {
	        if (str.contains(host)) {

	            if (str.contains("oembed.")) {
	                if (str.contains("{format}")) {
	                    str = str.replace("{format}", "json");
	                }

	                oembedUrl = str + "?url=" + encode;

	            } else {

	                oembedUrl = str + "?format=json&url=" + encode;

	            }

	            break;
	        }
	    }

	    return oembedUrl;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws ClientProtocolException, IOException , URISyntaxException{
		providerData();

		return "home";
	}
	
	
	@GetMapping("/oembedResponse")
	@ResponseBody
	public String oembedResponse(@RequestParam("userUrlData") String userUrlData)
	        throws ClientProtocolException, IOException {
	    String result = "";
	    try {
	        String host = hostCheck(userUrlData);
	        String encode = URLEncoder.encode(userUrlData, StandardCharsets.UTF_8);
	        String oembedUrl = createAddr(host, encode);

	        CloseableHttpClient hc = HttpClients.createDefault();
	        HttpGet httpGet = new HttpGet(oembedUrl);
	        httpGet.addHeader("Content-Type", "application/json");

	        CloseableHttpResponse httpResponse = hc.execute(httpGet);

	        result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

	    } catch (Exception e) {
	        e.printStackTrace();
	        result = "";
	    }

	    return result;
	}
	
}
