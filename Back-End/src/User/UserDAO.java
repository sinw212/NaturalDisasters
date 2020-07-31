package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBConnector;

public class UserDAO {
	// DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public UserDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	private boolean checkID(String id) {    //  아이디 중복확인
		String sql = "select ID from USER_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;	//  아이디 중복
			} else {
				return true;    //  아이디 사용가능
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("UserDAO checkID SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO checkID close SQLException error");
			}
		}
		return false;    //  DB 오류
	}

	public String join(String id, String name, String phone, String password) {    //  회원가입
		int result = 0;
		if(checkID(id)) {
			String sql = "insert into USER_INFO values(?, ?, ?, ?)";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);    //  아이디
				pstmt.setString(2, name);    //  비밀번호
				pstmt.setString(3, phone);    //  이름
				pstmt.setString(4, password);    //  핸드폰번호
				result = pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("UserDAO join SQLExceptoin error");
				result =  -1;    //  DB 오류
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("UserDAO join close SQLException error");
				}
			}
		}
		if(result == 1) {
			return "JoinSuccess";
		} else if(result == 0) {
			return "AlreadyID";
		} else {
			return "DBError";
		}
	}
	
	public String login(String id, String password) {    //  로그인
		String sql = "select PASSWORD from USER_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(password)) {
					return "LoginSuccess";    //  로그인 성공
				} else {
					return "LoginFail";    //  비밀번호 오류
				}
			}
			return "NoID";    //  아이디 없음
		} catch (SQLException e) {    //  예외처리
			System.err.println("UserDAO login SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO login close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public String findPW(String id, String name, String phone) {    //  패스워드 찾기
		String sql = "select ID from USER_INFO where ID = ? and NAME = ? and PHONE = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  아이디
			pstmt.setString(2, name);    //  이름
			pstmt.setString(3, phone);    //  핸드폰번호
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "FindPWSuccess";    //  해당아이디 확인 성공, 비밀번호 변경 가능
			}
			return "FindPWFail";    //  해당아이디 확인 실패, 비밀번호 변경 불가
		} catch (SQLException e) {    //  예외처리
			System.err.println("UserDAO findPW SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO findPW close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public int newPW(String id, String newPassword, String phone) {    //  새로운 비밀번호 변경(비밀번호 잃어버렸을 때)
		String sql = "update USER_INFO set PASSWORD = ? where ID = ? and PHONE = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPassword);    //  새로운 비밀번호
			pstmt.setString(2, id);    //  아이디
			pstmt.setString(3, phone);    //  핸드폰번호
			return pstmt.executeUpdate();    //  비밀번호 변경 완료
		} catch (SQLException e) {    //  예외처리
			System.err.println("UserDAO newPW SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("UserDAO newPW close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public String changePW(String id, String password, String newPassword) {    //  패스워드 바꾸기(유저가 비밀번호 변경할 때)
		int result = 0;
		if(!checkID(id)) {
			String sql = "update USER_INFO set PASSWORD = ? where ID = ? and PASSWORD = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newPassword);    //  바꿀 비밀번호
				pstmt.setString(2, id);    //  아이디
				pstmt.setString(3, password);    //  이전 비밀번호
				result = pstmt.executeUpdate();    //  비밀번호 변경 완료
			} catch (SQLException e) {    //  예외처리
				System.err.println("UserDAO changePW SQLExceptoin error");
				result = -1;    //  DB 오류
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("UserDAO changePW close SQLException error");
				}
			}
			if(result != 1) {
				result = -2;    //  기존 비밀번호가 정확하지 않음
			}
		}
		
		if(result == 1) {
			return "ChangeSuccess";
		} else if(result == 0) {
			return "NoID";
		} else if(result == -2) {
			return "NotPW";
		} else {
			return "DBError";
		}
		
	}
}