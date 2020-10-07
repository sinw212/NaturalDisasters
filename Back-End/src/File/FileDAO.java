package File;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import DBConnect.DBConnector;

public class FileDAO {
//  DB 연결변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 변수
	private String sql;
	private PreparedStatement pstmt;
	
	//  SQL 질의 결과 저장 변수
	private ResultSet rs;
	
	
	public FileDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	public int upload(int BoardID, String fileName, String fileRealName) {    //  파일 업로드
		sql = "insert into BoardFile(BoardID, FileName, FileRealName) value (?, ?, ?)";
		conn = dbConnector.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, BoardID);
			pstmt.setString(2, fileName);
			pstmt.setString(3, fileRealName);
			return pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("FileDAO upload SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("FiletDAO upload close SQLException error");
			}
		}
		
		return -1;    //  DB 오류
	}
	
	public ArrayList<String> getFile(int BoardID) {    //  파일 불러오기
		sql = "select FileRealName from BoardFile where BoardID = ? and Available = true";
		conn = dbConnector.getConnection();
		ArrayList<String> file = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, BoardID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				file.add(rs.getString(1));
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("FileDAO getFile SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("FiletDAO upload close SQLException error");
			}
		}
		
		return file;
	}
	
	public int delete(int BoardID, String FileName) {    //  파일 삭제
		sql = "update BoardFile set Available = false where BoardID = ? and FileName = ?";
		conn = dbConnector.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, BoardID);
			pstmt.setString(2, FileName);
			return pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("FileDAO delete SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("FiletDAO delete close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
}
