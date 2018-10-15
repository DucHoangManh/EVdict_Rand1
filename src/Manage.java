package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Manage {
	private Connection connectToSqlite() {
		/* 
		 * File dict.sql được lấy qua nguồn http://www.lingoes.net
		 * Em sử dụng các câu lệnh tìm kiếm, thêm, sửa, xóa qua các câu lệnh truy vấn SQL
		 * 
		 */
		String url = "jdbc:sqlite:dict.sqlite"; // Đường dẫn kết nối tới file dữ liệu
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	// Hàm loại bỏ dấu VD: có dấu => co dau
	public static String removeAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("");
	}

	// Hàm trả về nghĩa của từ
	public String explain(String word) {
		String sql = "SELECT detail FROM tbl_edict WHERE word='" + word + "' "; // Câu lệnh truy vấn dữ liệu sql
		String result = "";
		try (Connection conn = this.connectToSqlite();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				/* 
				 * Sử dụng hàm replace() với chức năng sửa và xóa các phần không sử dụng đến 
				 * trong kết quả trả về
				 */
				result = rs.getString("detail"); // Lấy ra định nghĩa từ trong cột detail của dict.sqlite
				result = result.replace("<br />", "\n");
				result = result.replace("<C><F><I><N><Q>", ""); // Loại bỏ các thẻ không dùng đến với command
				result = result.replace("</Q></N></I></F></C>", "");
				result = result.replace("*", "\n*");
				result = result.replace("=", "\tVD: ");
				result = result.replace("-", "   -");
				result = result.replace("!", "\t!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		result = removeAccent(result);
		result = result.replace("Đ", "D");
		result = result.replace("đ", "d");
		if (result.equals(""))
			result = "Tu '" + word + "' khong co trong tu dien";
		return result;
	}

	// Thêm vào database từ word và thông tin của từ info
	public void insertWord(String word, String info) {
		Statement stm = null;
		try {
			Connection conn = this.connectToSqlite();
			stm = conn.createStatement();
			String sql = "INSERT INTO tbl_edict ('word', 'detail') VALUES ('" + word + "', '" + info + "')";
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Xóa từ word khỏi database
	public void deleteWord(String word) {
		Statement stm = null;
		try {
			Connection conn = this.connectToSqlite();
			stm = conn.createStatement();
			String sql = "DELETE FROM tbl_edict WHERE word ='" + word + "' ";
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Sửa từ word với nội dung mới info trong database
	public void editWord(String word, String info) {
		Statement stm = null;
		try {
			Connection conn = this.connectToSqlite();
			stm = conn.createStatement();
			String sql = "UPDATE tbl_edict SET detail = '" + info + "' WHERE word = '" + word + "'";
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Tìm các từ bắt đầu với word
	public void findWords(String startsWith) {
		Statement stm = null;
		try {
			Connection conn = this.connectToSqlite();
			stm = conn.createStatement();
			String sql = "SELECT word FROM tbl_edict WHERE word LIKE '" + startsWith + "%' ";
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("word"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
