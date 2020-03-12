import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.javafx.collections.MappingChange.Map;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/*
--------------+-------------+------+-----+---------+-------+
| Field        | Type        | Null | Key | Default | Extra |
+--------------+-------------+------+-----+---------+-------+
| builder      | varchar(20) | NO   |     | NULL    |       |
| model        | varchar(20) | NO   |     | NULL    |       |
| type         | varchar(20) | NO   |     | NULL    |       |
| numStrings   | varchar(20) | NO   |     | NULL    |       |
| topWood      | varchar(20) | NO   |     | NULL    |       |
| backWood     | varchar(20) | NO   |     | NULL    |       |
| price        | double      | YES  |     | NULL    |       |
| serialnumber | varchar(20) | YES  |     | NULL    |       |
+--------------+-------------+------+-----+---------+-------+
*/
public class DB {
	Guitar guitar;

	// DB 연결시 필요한 변수
	Connection conn = null;
	PreparedStatement state = null;

	public DB() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://localhost/rick_store?serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "125710");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		}
	}

//	public void Open() {
//		try {
//
//			Class.forName("com.mysql.cj.jdbc.Driver");
//
//			String url = "jdbc:mysql://localhost/rick_store?serverTimezone=UTC";
//			conn = DriverManager.getConnection(url, "root", "125710");
//
//		} catch (ClassNotFoundException e) {
//			System.out.println("드라이버 로딩 실패");
//		} catch (SQLException e) {
//			System.out.println("에러: " + e);
//		}
//	}

	public void Insert(Guitar guitar) {
		String sql = "INSERT INTO rick_store VALUES(?,?,?,?,?,?,?,?)";
		try {
			state = conn.prepareStatement(sql);
			/* guitar value insert */
			state.setDouble(7, guitar.getPrice());
			state.setString(8, guitar.getSerialNumber());

			/* guitarSpec value insert */
			state.setString(1, guitar.spec.getBuilder().toString());
			state.setString(2, guitar.spec.getModel().toString());
			state.setString(3, guitar.spec.getType().toString());
			state.setInt(4, guitar.spec.getNumStrings());
			state.setString(5, guitar.spec.getTopWood().toString());
			state.setString(6, guitar.spec.getBackWood().toString());
			state.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public LinkedList Search(GuitarSpec spec) {
		LinkedList list = new LinkedList();
		String sql = "SELECT * FROM rick_store WHERE " + "builder IN ('" + spec.getBuilder() + "')" + " AND "
				+ "model IN ('" + spec.getModel() + "')" + " AND " + "type IN ('" + spec.getType() + "')" + " AND "
				+ "numStrings IN ('" + spec.getNumStrings() + "')" + " AND " + "backWood IN ('" + spec.getBackWood()
				+ "')" + " AND " + "topWood IN ('" + spec.getTopWood() + "')";
//		System.out.println(sql);
		try {
			state = conn.prepareStatement(sql);

			ResultSet result = state.executeQuery(sql);
			
			if (result.next()) {
				do {
					String str = "  We have a " + result.getString(1) + " " + result.getString(2) + " "
							+ result.getString(3) + " guitar:\n     " + result.getString(6) + " back and sides,\n     "
							+ result.getString(5) + " top.\n  You can have it for only $" + result.getString(7)
							+ "!\n  ----";
					list.add(str);
//					result.getString(1);// builder
//					result.getString(2);// model
//					result.getString(3);// type
//					result.getInt(4);// numString
//					result.getString(5);// topWood
//					result.getString(6);// BackWood
//					result.getDouble(7);// price
//					result.getString(8);// serialNumber
				}while (result.next());
			}else {
				return null;
			}


		} catch (NullPointerException e) {


		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(list);
		return list;
	}

	public void Close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 타입변환
	public Wood toWood(String str) {
		switch (str) {
		case "Indian Rosewood":
			return Wood.INDIAN_ROSEWOOD;
		case "Brazilian Rosewood":
			return Wood.BRAZILIAN_ROSEWOOD;
		case "Mahogany":
			return Wood.MAHOGANY;
		case "Maple":
			return Wood.MAPLE;
		case "Cocobolo":
			return Wood.COCOBOLO;
		case "Cedar":
			return Wood.CEDAR;
		case "Adirondack":
			return Wood.ADIRONDACK;
		case "Alder":
			return Wood.ALDER;
		case "Sitka":
			return Wood.SITKA;
		default:
			return null;

		}
	}

	public GType toType(String str) {
		switch (str) {
		case "acoustic":
			return GType.ACOUSTIC;
		case "electric":
			return GType.ELECTRIC;

		default:
			return null;

		}
	}

	public Builder toBuilder(String str) {
		switch (str) {
		case "Fender":
			return Builder.FENDER;
		case "Martin":
			return Builder.MARTIN;
		case "Gibson":
			return Builder.GIBSON;
		case "Collings":
			return Builder.COLLINGS;
		case "Olson":
			return Builder.OLSON;
		case "Ryan":
			return Builder.RYAN;
		case "PRS":
			return Builder.PRS;
		default:
			return null;
		}
	}

}
