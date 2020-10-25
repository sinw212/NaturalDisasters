package Shelter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DBConnect.DBConnector;

public class Earthquake {
	//  DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public Earthquake() {
		dbConnector = DBConnector.getInstance();
	}
	
	private String httpConnection(String targetUrl) {
	    URL url = null;
	    HttpURLConnection conn = null;
	    String jsonData = "";
	    BufferedReader br = null;
	    StringBuffer sb = null;
	    String returnText = "";
	 
	    try {
	        url = new URL(targetUrl);
	 
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.connect();
	 
	        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        sb = new StringBuffer();
	 
	        while ((jsonData = br.readLine()) != null) {
	            sb.append(jsonData);
	        }
	 
	        returnText = sb.toString();
	 
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (br != null) br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	    return returnText;
	}
	
	private int insertData(String arcd, String name, String addr, double ycord, double xcord) {
		String sql = "insert into ES values(?, ?, ?, ?, ?)";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, arcd);
			pstmt.setString(2, name);
			pstmt.setString(3, addr);
			pstmt.setDouble(4, ycord);
			pstmt.setDouble(5, xcord);
			return pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("Earthquake write SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("Earthquake write close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int shelterData() {
		for(int i = 1; i <= 5; i ++) {
			String result = httpConnection("http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList?ServiceKey=ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D&numOfRows=1000&type=json&flag=Y&pageNo=" + i);
			
			JSONParser parser = new JSONParser();
			JSONObject obj = null;
			JSONArray arr = null;
			try {
				obj = (JSONObject)parser.parse(result);
				arr = (JSONArray)obj.get("EarthquakeIndoors");
				obj = (JSONObject)parser.parse(arr.get(1).toString());
				arr = (JSONArray)obj.get("row");
				for(int j = 0; j < arr.size(); j++) {
					obj = (JSONObject)parser.parse(arr.get(j).toString());
					if(1 != insertData(obj.get("arcd").toString(), obj.get("vt_acmdfclty_nm").toString(), obj.get("dtl_adres").toString(), Double.parseDouble(obj.get("ycord").toString()), Double.parseDouble(obj.get("xcord").toString()))) {
						return -1;
					}
				}
			} catch (ParseException e) {
				System.err.println("Earthquake shelterData ParseExcetion error");
				return -1;
			}
		}
		return 1;
	}
	
	public String getData(String arcd) {
		String sql = "select Name, Addr, ycord, xcord from ES where Arcd = ?";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		ArrayList<JSONObject> shelterArray = new ArrayList<JSONObject>();    //  JSON데이터들을 담을 List
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, arcd);    //  지역코드
			rs = pstmt.executeQuery();
			while(rs.next()) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
				hashMap.put("name", rs.getString(1));    //  대피소명
				hashMap.put("addr", rs.getString(2));    //  대피소 주소
				hashMap.put("ycord", rs.getDouble(3));    //  위도
				hashMap.put("xcord", rs.getDouble(4));    //  경도
				JSONObject shelterObject = new JSONObject(hashMap);    //  JSON데이터로 만들기
				shelterArray.add(shelterObject);
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("Earthquake write SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();};
			} catch(SQLException e) {
				System.err.println("Earthquake write close SQLException error");
			}
		}
		
		return shelterArray.toString();
	}
}
