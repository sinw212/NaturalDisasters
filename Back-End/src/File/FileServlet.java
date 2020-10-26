package File;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Board.BoardDAO;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/Write")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileDAO fileDAO = new FileDAO();
	private int MAX_SIZE = 1024 * 1024 * 100;    //  업로드 파일 최대 사이즈
	
	private boolean fileCheck(String fileName) {    //  파일 업로드 화이트리스트
		if(imageFileCheck(fileName) && !fileName.endsWith(".zip") && !fileName.endsWith(".hwp") &&
    			!fileName.endsWith(".pdf") && !fileName.endsWith(".txt") && !fileName.endsWith(".docx") &&
    			!fileName.endsWith(".xml") && !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls") &&
    			!fileName.endsWith(".pptx") && !fileName.endsWith(".ppt") && !fileName.endsWith(".show")) {
			return true;
		}
		return false;
	}
	
	private boolean imageFileCheck(String fileName) {    //  이미지 파일 업로드 화이트 리스트
		if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png") &&
    			!fileName.endsWith(".bmp") && !fileName.endsWith(".rle") &&
    			!fileName.endsWith(".dib") && !fileName.endsWith(".gif") &&
    			!fileName.endsWith(".tiff") && !fileName.endsWith(".raw")) {
			return true;
    	}
		return false;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		PrintWriter script = response.getWriter();
		String directory = request.getServletContext().getRealPath("") + "/upload";    //  파일 저장 경로
        File attachesDir = new File(directory);
        
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setRepository(attachesDir);
        fileItemFactory.setSizeThreshold(MAX_SIZE);
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
        StringBuilder sb = new StringBuilder("");
		try {
            List<FileItem> items = fileUpload.parseRequest(request);    //  form에서 넘어오는 item
            ArrayList<String> fileNames = new ArrayList<String>();    //  파일명
            ArrayList<String> fileSysNames = new ArrayList<String>();    //  시스템에 저장될 실제 파일명
            for (FileItem item : items) {
                if (item.isFormField()) {    //  파일이 아닌 input
                	sb.append(item.getString("utf-8"));
                    sb.append("-");
                } else {    //  파일 input
                	String overlap = UUID.randomUUID().toString();
                	if (item.getSize() > 0) {
                		String separator = File.separator;
                		int index =  item.getName().lastIndexOf(separator);
                		String fileName = item.getName().substring(index  + 1);
                		//  화이트리스트에 등록된 확장자가 아닐 경우 업로드하지 않음
                		if(fileCheck(fileName)) {    //  화이트리스트 체크
                			continue;
                		}
                		String fileSysName = overlap + "_" + fileName;    //  시스템에 저장될 파일명 중복방지
                		File uploadFile = new File(directory + separator + fileSysName);    //  파일 업로드
                		item.write(uploadFile);
                		fileNames.add(fileName);    //  파일명
                		fileSysNames.add(fileSysName);    //  시스템에 저장될 실제 파일명
                	}
                }
            }
            //  게시글 제목, 내용, 작성자 구분
            String[] value = sb.toString().substring(0, sb.toString().lastIndexOf("-")).split("-");

            BoardDAO boardDAO = new BoardDAO();
            int result = boardDAO.write(value[0], value[2], value[1]);    //  DB에 작성
            if(result == 1) {
                for(int i = 0; i < fileNames.size(); i++) {
                	fileDAO.upload(boardDAO.getNext() - 1, fileNames.get(i), fileSysNames.get(i));
                }
            }

            script.println("<script>");
        	script.println("location.href = '../PPND_Server/Board.jsp'");
    		script.println("</script>");
    		script.close();
    		return;
            
		} catch (FileUploadException e) {
			System.err.println("FileServlet FileUploadexception error");
			script.println("<script>");
			script.println("alert('fileupload error')");
			script.println("</script>");
    		script.close();
		} catch (Exception e) {
			System.err.println("FileServlet Exception error write");
			script.println("<script>");
			script.println("alert('file error')");
			script.println("</script>");
    		script.close();
		}
    }

}
