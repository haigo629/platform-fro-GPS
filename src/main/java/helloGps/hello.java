package helloGps;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.json.JSONObject;

import com.gps.test.Redis.redis;
import com.gps.test.TCP.tcpServer;
import com.gps.test.model.device;
import com.gps.test.util.util;

public class hello extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private redis redis=new redis();
	private tcpServer tcp = null;
	public hello() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		resp.setContentType("text/html;charset=utf-8");
		
		String action = req.getParameter("action");
         if("queryJSP".equals(action)) { 
        	 req.setAttribute("lat", "0");
        	 req.setAttribute("lng", "0");
        	 req.setAttribute("lat1", "0");
           	 req.setAttribute("lng1", "0");
     		 req.setAttribute("Keys", null);
             req.getRequestDispatcher("query.jsp").forward(req , resp);  
         } else if("tcpStart".equals(action)) {
 			if(tcp==null) {
 				tcp = new tcpServer();
 				tcp.initServer(32769);
 			}
 			
 		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		resp.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
		resp.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
		
		String action = req.getParameter("action");
		
		switch (action) {
			case "queryJSP":
				queryJSP(req,resp);
				break;
			case "queryAll":
				queryAll(resp);
				break;
			case "queryOne":
				queryOne(req,resp);
				break;
			case "queryAllKey":
				queryAllKey(req,resp);
				break;
			case "queryDevByKey":
				queryDevByKey(req,resp);
				break;
			default:break;
		}
	}
	/**
	 * 查询所有设备的key
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void queryAllKey(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		//获取所有设备位置
		HashMap<String, device> map = redis.getAll();
		if(map.size()>0) {
			List<String> keyLists = new ArrayList<String>();
			for (String key : map.keySet()) {
				keyLists.add(key);
			}
			req.setAttribute("keys", keyLists);
		}
		req.setAttribute("lat", "0");
      	req.setAttribute("lng", "0");
      	req.setAttribute("lat1", "0");
      	req.setAttribute("lng1", "0");
      	req.setAttribute("time", new Date().toString());
      	req.setAttribute("time1", new Date()+":1");
		req.getRequestDispatcher("query.jsp").forward(req , resp);
	}
	/**
	 * 根据设备的key查询其位置
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void queryDevByKey(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		//查询的key
		String key = req.getParameter("key");
		device dev = (device)util.unserizlize(redis.getFromRedis(key));
		if(dev!=null) {
	       	 req.setAttribute("lat1", dev.getLat());
	       	 req.setAttribute("lng1", dev.getLng());
	       	req.setAttribute("time1", dev.getNowTime());
	   	 }else {
	   		 req.setAttribute("lat1", "0");
	       	 req.setAttribute("lng1", "0");
	   	 }
		req.setAttribute("lat", "0");
      	req.setAttribute("lng", "0");
		req.setAttribute("Keys", null);
		req.setAttribute("time", new Date().toString());
	    req.getRequestDispatcher("query.jsp").forward(req , resp); 
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	protected void queryOne(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		//查询的key
		String key = req.getParameter("key");
		device dev = (device)util.unserizlize(redis.getFromRedis(key));
		
		JSONObject[] array=new JSONObject[1];
		JSONObject data=new JSONObject();
		JSONObject devD=new JSONObject();
			
		JSONObject location=new JSONObject();
		location.put("lat", dev.getLat());
		location.put("lng", dev.getLng());
		location.put("time", dev.getNowTime());
		devD.put(key, location);
		
		array[0] = devD;
		data.put("data", array);
		
		
		PrintWriter out =null ;
		out =resp.getWriter() ;
		out.write(data.toString());
		out.close();
	}
	/**
	 * 查询目前设备  测试
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void queryJSP(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		device dev = (device)util.unserizlize(redis.getFromRedis("014100001304"));
	   	 if(dev!=null) {
	       	 req.setAttribute("lat", dev.getLat());
	       	 req.setAttribute("lng", dev.getLng());
	       	 req.setAttribute("time", dev.getNowTime());
	   	 }else {
	   		 req.setAttribute("lat", "0");
	       	 req.setAttribute("lng", "0");
	   	 }    
	   	req.setAttribute("lat1", "0");
      	req.setAttribute("lng1", "0");
      	req.setAttribute("time1", new Date()+":1");
		req.setAttribute("Keys", null);
	    req.getRequestDispatcher("query.jsp").forward(req , resp);  
	}
	/**
	 * 获取所有设备位置
	 * @param resp
	 * @throws IOException
	 */
	protected void queryAll(HttpServletResponse resp) throws IOException {
		//获取所有设备位置
		HashMap<String, device> map = redis.getAll();
		if(map.size()>0) {
			int i =0 ;
			JSONObject[] array=new JSONObject[map.size()];
	        JSONObject data=new JSONObject();
			for (String key : map.keySet()) {
				
				device dev = map.get(key);
				JSONObject devD=new JSONObject();
				JSONObject location=new JSONObject();
				location.put("lat", dev.getLat());
				location.put("lng", dev.getLng());
				location.put("time", dev.getNowTime());
				devD.put(key, location);
				array[i] = devD;
				i++;
			}
			data.put("data", array);
			PrintWriter out =null ;
			out =resp.getWriter() ;
			out.write(data.toString());
			out.close();
		}
	}

}
