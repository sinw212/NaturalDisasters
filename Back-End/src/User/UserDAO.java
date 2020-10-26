package User;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import DBConnect.DBConnector;

public class UserDAO {
	//  DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 변수
	private String sql = "";
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//  관리자 확인 변수
	private Properties authority;
	private FileInputStream fis_authority;
	
	public UserDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	private String getDate() {    //  가입 시간
		sql = "select now()";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);    //  DB 시간 반환
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("UserDAO getDate SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO getDate close SQLException error");
			}
		}
		return "";    //  DB 오류
	}
	
	private String expireDate() {    //  아이디 만료 날짜
		String date = getDate();
		int year = Integer.parseInt(date.split("-")[0]);
		year++;
		return year + date.substring(4);
	}
	
	public int add(UserDTO user) {    //  새로운 아이디 추가
		sql = "insert into User (ID, Password, Name, PhoneNumber, Authority, Date, EndDate) values(?,?,?,?,?,?,?)";
		conn = dbConnector.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPhone());
			pstmt.setString(5, user.getAuthority());
			pstmt.setString(6, getDate());
			pstmt.setString(7, expireDate());
			return pstmt.executeUpdate();    //  아이디 추가 성공
		} catch(SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("UserDAO join SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO join close SQLException error");
			}
		}
		return 0;    //  아이디추가 실패(아이디 중복, DB 오류)
	}
	
	public String login(String userID, String userPassword) {    //  로그인
		sql = "select Password, Name, Authority from User where ID = ?";
		conn = dbConnector.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, (userID));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(userPassword.equals(rs.getString(1))) {
					return "success," + rs.getString(2) + "," + rs.getString(3);    //  로그인 성공
				} else {
					return "error,password";    //  비밀번호 오류
				}
			}
			return "error,ID";    // 아이디 오류
		} catch(SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("UserDAO login SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO login close SQLException error");
			}
		}
		return "error,DB";    //  DB 오류
	}
	
	public boolean checkAuthority(String userAuthority) {    // 권한 확인
		try {
			authority = new Properties();
			fis_authority = new FileInputStream("/volume1/Security/LabWebSite/authority.properties");
			authority.load(new BufferedInputStream(fis_authority));
			
			if(userAuthority.equals(authority.getProperty("admin"))) {
				return true;    //  관리자일 경우
			} else {
				return false;    //  관리자가 아닐 경우
			}
		} catch (FileNotFoundException e) {    //  예외처리, 대응부재 제거
			System.err.println("UserDAO checkAuthority FileNotFoundException error");
		} catch (IOException e) {
			System.err.println("UserDAO checkAuthority IOException error");
		} finally {    //  자원 해제
			try {
				if(fis_authority != null) {fis_authority.close();}
			} catch (IOException e) {
				System.err.println("UserDAO checkAuthority close IOException error");
			}
		}
		return false;    //  관리자가 아닐 경우
	}
}