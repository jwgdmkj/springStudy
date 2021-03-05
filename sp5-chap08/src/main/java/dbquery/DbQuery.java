package dbquery;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DbQuery {
	private DataSource dataSource;
	
	public DbQuery(DataSource dataSource) {
		this.dataSource=dataSource;
	}
	
	public int count() {
		Connection conn = null;
		try {
			conn= dataSource.getConnection(); //풀에서 구함
			//괄호 내: 실행후, final통해 close되어야하는 객체(file, stream)등이 자동으로 닫히게해줌
			//executequery: 수행결과, resultset형 객체 반환. 
			try(Statement stmt= conn.createStatement();
					ResultSet rs= stmt.executeQuery("select count(*) from MEMBER")) {
				rs.next();
				return rs.getInt(1);
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if(conn != null) 
				try {
					conn.close(); //풀에 반환
				} catch(SQLException e) {
					
				}
		}
	}
}
