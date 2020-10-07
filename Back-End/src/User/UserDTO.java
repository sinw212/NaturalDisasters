package User;

public class UserDTO {
	String id;
	String password;
	String name;
	String phone;
	String authority;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public UserDTO() {
		
	}
	public UserDTO(String id, String password, String name, String phone, String authority) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.authority = authority;
	}
}
