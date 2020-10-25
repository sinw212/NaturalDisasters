package Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import DBConnect.DBConnector;

public class BoardDAO {
	//  DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public BoardDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	private String getDate() {    //  게시글 등록 서버 시간 획득
		String sql = "select now()";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO getDate SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getDate close SQLException error");
			}
		}
		return "";    //  DB 오류
	}

	public int getNext() {    //  게시판 저장시 게시글 번호 획득
		String sql = "select ID from Board order by ID desc";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;    //  DB에 저장되어 있는 게시글번호 + 1
			}
			return 1;    //  첫번째 게시글일 경우
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO getNext SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getNext close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int write(String title, String writer, String content) {    //  게시글 등록
		String sql = "insert into Board(ID, Title, Writer, Date, ReWriter, ReDate, Content) values(?, ?, ?, ?, ?, ?, ?)";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, title);
			pstmt.setString(3, writer);
			pstmt.setString(4, getDate());
			pstmt.setString(5, writer);
			pstmt.setString(6, getDate());
			pstmt.setString(7, content);
			return pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO write SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO write close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int update(int id, String title, String writer, String content) {    //  게시글 수정
		String sql = "update Board set Title=?, ReWriter=?, ReDate=?, Content=? where ID=?";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, writer);
			pstmt.setString(3, getDate());
			pstmt.setString(4, content);
			pstmt.setInt(5, id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO update SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch (SQLException e) {
				System.err.println("BoardDAO update close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int delete(int id) {    //  게시글 삭제
		String sql = "select ID from Board where Available=False";
		String filesql = "update BoardFile set Available = False where BoardID=?";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		PreparedStatement filepstmt = null;
		try {
			filepstmt = conn.prepareStatement(filesql);
			filepstmt.setInt(1, id);
			filepstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			PreparedStatement pstmt2 = null;
			if(rs.next()) {    //  삭제된 파일이 있을 경우
				int delID = rs.getInt(1) - 1;
				sql = "update Board set ID=?, Available=False where and ID=?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, delID);
				pstmt2.setInt(2, id);
				return pstmt2.executeUpdate();    //  삭제 성공시
			} else {    //  삭제된 파일이 없을 경우
				sql = "update Board set ID=-1, Available=False where ID=?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, id);
				return pstmt2.executeUpdate();    //  삭제 성공시
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO delete SQLException error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("BoardDAO delete close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public ArrayList<BoardDTO> getList(int pageNumber) {    //  게시글 리스트 획득
		String sql = "select bi.ID, bi.Title, ui.Name, bi.Date, u.Name, bi.ReDate, bi.Content "
				+ "from Board bi join User ui on ui.ID = bi.Writer "
				+ "join User u on u.ID = bi.ReWriter where bi.ID < ? and bi.Available = true "
				+ "order by ID desc limit ?";
		conn = dbConnector.getConnection();
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			pstmt.setInt(2, 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {    //  게시글 리스트 추가
				BoardDTO postDTO = new BoardDTO(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				list.add(postDTO);
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO getList SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getList close SQLException error");
			}
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {    // 게시글 리스트 다음 페이지 확인
		String sql = "select * from Board where ID < ? and Available = True";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(2, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {    //  다음 페이지가 있을 경우
				return true;
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO nextPage SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO nextPage close SQLException error");
			}
		}
		return false;    //  다음 페이지가 없을 경우, DB 오류
	}

	
	private String setNewLine(String content) {    //  view에서 줄바꿈, space 적용을 위한 함수
		return content.replaceAll("\r\n", "<br>").replaceAll(" ", "&nbsp;");
	}
	
	public BoardDTO getPost(int ID) {    //  게시글 확인
		String sql = "select bi.ID, bi.Title, ui.Name, bi.Date, u.Name, bi.ReDate, bi.Content "
				+ "from Board bi join User ui on ui.id = bi.Writer join User u on u.id = bi.ReWriter "
				+ "where bi.ID = ?";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ID);
			rs = pstmt.executeQuery();
			if(rs.next()) {    //  게시글 정보 반환
				return new BoardDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), setNewLine(rs.getString(7)));
			}
		} catch (SQLException e) {    //  에외처리, 대응부재 제거
			System.err.println("BoardDAO getPost SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getPost close SQLException error");
			}
		}
		return null;    //  DB 오류
	}
	
	public boolean checkWriter(int BoardID, String userID) {    //  게시글 작성자 확인
		String sql = "select Writer from Board where ID = ?";
		String writer = "";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, BoardID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				writer = rs.getString(1);
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO checkWriter SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO checkWriter close SQLException error");
			}
		}
		if(writer.equals(userID)) {    //  작성자일 경우
			return true;
		} else {    //  작성자와 다를 경우
			return false;
		}
	}
	
	private ArrayList<String> getImgName(int id) {    //  게시글 이미지사진 파일명 획득
		ArrayList<String> list = new ArrayList<String>();
		
		String sql = "select FileRealName from BoardFile where BoardID = ? and Available = 1";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO getImgName SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getImgName close SQLException error");
			}
		}
		return list;
	}
	
	public String getBoardData() {    //  모든 게시글 데이터 획득
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();    //  게시글 데이터를 저장할 ArrayList
		
		String sql = "select bi.ID, bi.Title, ui.Name, bi.Date, u.Name, bi.ReDate, bi.Content "
				+ "from Board bi join User ui on ui.id = bi.Writer join User u on u.id = bi.ReWriter "
				+ "order by Date desc";
		conn = dbConnector.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO boardDTO = new BoardDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				list.add(boardDTO);
			}
		} catch (SQLException e) {    //  예외처리, 대응부재 제거
			System.err.println("BoardDAO getBoardData SQLExceptoin error");
		} finally {    //  자원 해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("BoardDAO getBoardData close SQLException error");
			}
		}
		
		ArrayList<JSONObject> boardArray = new ArrayList<JSONObject>();    //  JSON데이터들을 담을 List
		for(int i = 0; i < list.size(); i++) {
			ArrayList<String> strList = getImgName(list.get(i).getId());
			HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
			hashMap.put("id", list.get(i).getId());    //  게시글 아이디
			hashMap.put("title", list.get(i).getTitle());    //  게시글 제목
			hashMap.put("writer", list.get(i).getWriter());    //  게시글 작성자
			hashMap.put("date", list.get(i).getDate());    //  게시글 작성시간
			hashMap.put("rewriter", list.get(i).getReWriter());    //  게시글 수정자
			hashMap.put("redate", list.get(i).getReDate());    //  게시글 수정 날짜
			hashMap.put("content", list.get(i).getContent());    //  게시글 내용
			hashMap.put("image", strList);
			JSONObject childObject = new JSONObject(hashMap);    //  JSON데이터로 만들기
			boardArray.add(childObject);    //  JSON List에 추가
		}
		
		return boardArray.toString();
	}
}