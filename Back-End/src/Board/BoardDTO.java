package Board;

public class BoardDTO {
	int id;
	String title;
	String writer;
	String date;
	String reWriter;
	String reDate;
	String content;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReWriter() {
		return reWriter;
	}
	public void setReWriter(String reWriter) {
		this.reWriter = reWriter;
	}
	public String getReDate() {
		return reDate;
	}
	public void setReDate(String reDate) {
		this.reDate = reDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public BoardDTO(int id, String title, String writer, String date, String reWriter, String reDate, String content) {
		super();
		this.id = id;
		this.title = title;
		this.writer = writer;
		this.date = date;
		this.reWriter = reWriter;
		this.reDate = reDate;
		this.content = content;
	}
}
