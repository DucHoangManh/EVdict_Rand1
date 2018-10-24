package sample;

import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Manage {
	private Connection connectToSqlite() {
		// Kết nối tới file sqlite
		String url = "jdbc:sqlite:dict.db"; // Đường dẫn kết nối tới file dữ liệu
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	// Hàm trả về nghĩa của từ
	public String explain(String word) {
		String sql = "SELECT html FROM av WHERE word='" + word + "' "; // Câu lệnh truy vấn dữ liệu sql
		String result = "";
		try (Connection conn = this.connectToSqlite();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				result = rs.getString("html"); // Lấy ra định nghĩa từ trong cột detail của dict.sqlite
//				result = result.replace("<br />", "\n");
//				result = result.replace("<C><F><I><N><Q>", ""); // Loại bỏ các thẻ không dùng đến với command
//				result = result.replace("</Q></N></I></F></C>", "");
				result = result.replace("*", "\n*");
                //result = result.replace("list-style-type:circle", "list-style-type:square");
                result = result.replace("'", "`");
				result = "<style>\th1{color: #ff6b6b;} ul>li>ul>li{color: #e84118;}  h2{color:#34495e;}</style>" + result;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
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
			String sql = "INSERT INTO av ('word', 'html') VALUES ('" + word + "', '" + info + "')";
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
			String sql = "DELETE FROM av WHERE word ='" + word + "' ";
			stm.executeUpdate(sql);
			System.out.println(sql);
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
			String sql = "UPDATE av SET html = '" + info + "' WHERE word = '" + word + "'";
			System.out.println(sql);
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Tìm các từ bắt đầu với word
	public void findWords(String startsWith, ArrayList<String> resultSet) {
		Statement stm = null;
		try {
			Connection conn = this.connectToSqlite();
			stm = conn.createStatement();
			String sql = "SELECT word FROM av WHERE word LIKE '" + startsWith + "%' ";
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				resultSet.add(rs.getString("word"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Dịch đoạn văn
	public String translator(String text) throws IOException {
		String langFrom = "en";
		String langTo = "vi";
		String urlStr = "https://script.google.com/macros/s/AKfycbzyx5OhvtgGBXat2CeMHf6yE5Wv1FCSBFROtk5Rc44XLopmoaBH/exec"
				+ "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + langTo + "&source=" + langFrom;
		URL url = new URL(urlStr);
		StringBuilder response = new StringBuilder();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String result = response.toString();
		result = result.replace("&quot;", "'");
		return result;
	}
}
