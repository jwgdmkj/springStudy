package spring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.sql.PreparedStatement;

import java.util.Collection;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/*
 * 스프링 사용시, DataSource나 Connection, Statement, ResultSet을 직접사용하지 않고, JdbcTemplate를
 * 이용해 편리하게 쿼리를 실행할 수 있다.
 */

public class MemberDao {
	
	//jdbcTemplate객체 생성
	private JdbcTemplate jdbcTemplate;
	
	//JdbcTemplate을 생성하는 코드를 MemberDao클래스에 추가했으니, 스프링 설정에서도 MemberDao빈설정을 추가
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate= new JdbcTemplate(dataSource);
	}
	
	public Member selectByEmail(String email) {
		//query()메서드는 sql파라미터로 전달받은 쿼리를 실행하고, RowMapper을 이용해 ResultSet의 결과를 자바객체로 변환
		List<Member> results = jdbcTemplate.query(
				"select*from MEMBER where EMAIL=?", 
				//?=인덱스파라미터. 여기에 들어갈 값은 return member 아래의 email이 됨.
				
				//Member형을 저장하는 RowMapper가 ResultSet에서 데이터를 읽어와 Member객체로 변환. 
				//sql파라미터가 인덱스기반 파라미터를 가진 쿼리라면, args파라미터를 이용해 각 인덱스 파라미터의 값을 지정.
				new RowMapper<Member>() {
					//파라미터로 전달받은 ResultSet에서 데이터를 읽어와 Member객체를 생성해 리턴.
					@Override
					public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
						Member member = new Member(rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								rs.getTimestamp("REGDATE").toLocalDateTime());
						member.setId(rs.getLong("ID"));
						return member;
					}
				},
				email);
		
		/* 람다를 이용해, 임의클래스를 사용하지 않고
		 * List<Member> results= jdbcTemplate.query(
		 * "select*from MEMBER where EMAIL = "?",
		 * (ResultSet rs, int rowNum) -> {
		 * Member member = new Member(
		 * rs.getString("EMAIL"),
		 * rs.getString("PASSWORD"),
		 * rs.getString("NAME"),
		 * rs.getTimestamp("REGDATE").toLocalDateTime());
		 * member.setId(rs.getLong("ID"));
		 * return member;
		 * 라 하거나,
		 * 
		 * RowMapper 인터페이스를 구현한 클래스를 만들어서 코드 중복을 막을 수 있다
		 * public class MemberRowMapper implemets RowMapper<Member> {
		 * @Override
		 * public MemberRow(ResultSet rs, int rowNum) hrows SQLExecption {
		 * Member member= new Member(
		 * rs.getString("EMAIL"),
		 * rs.getString("PASSWORD"),
		 * rs.getString("NAME"),
		 * rs.getTimestamp("REGDATE").toLocalDateTime());
		 * member.setId(rs.getLong("ID"));
		 * return member;
		 * }
		 * }
		 * 
		 *List <Member> results = jdbcTemplate.query(
		 *"select*from Member where EMAIL=? and NAME=?", new MemberRowMapper(), email, name);
		 */
		return results.isEmpty() ? null : results.get(0);
	}
	//selectByEmail은 지정한 이메일에 해당하는 MEMBER데이터가 존재시 해당 Member객체를 리턴, 없으면 null을 리턴.
	
	
	//keyHolder을 이용, 자동생성 키값 구하기(by ID int auto_increment primary key)
	//해당 칼럼값은 자동생성이므로, update등에선 ID칼럼 값을 지정하지 않음. 이 자동생성키값을 구하는 법은 KeyHolder
	public void insert(final Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		//PreparedStatementCreator 임의클래스를 이용해 PreparedStatement객체를 임의생성
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				//두 번째 배열은 string배열 "ID", 자동생성되는 키칼럼목록 지정에 사용
				//MEMBER테이블은 ID칼럼이 자동증가키칼럼이기에, 두번째 파라미터값으로 "ID"를 줌
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) " +
						"values (?, ?, ?, ?)",
						new String[] { "ID" });
				// 인덱스 파라미터 값 설정
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4,
						Timestamp.valueOf(member.getRegisterDateTime()));
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
			//JdbcTemplate.update()의 두번째인자는 위에서 생성한 KeyHolder객체.
		}, keyHolder);
		//PreparedStatement를 실행후, 자동생성된 키값을 KeyHolder에 보관. 이는 getKey()통해 구하기 가능.
		
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	}
	/* 또다른 방식은 람다를 이용한
	 * jdbcTemplate.update((Connection con) -> {
	 * 	PreparedStatement pstmt = con.prepareStatement(
	 * 		"insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE)" +
	 * 		"values(?,?,?,?)", new String[]{"ID"});
	 * 	
	 * pstmt.setString(1, member.getEmail());
	 * pstmt.setString(2, member.getPassword());
	 * pstmt.setString(3, member.getName());
	 * pstmt.setTimestamp(4,
	 * Timestamp.valueOf(member.getRegisterDateTime()));
	
	 * return pstmt;
	 *	}
	 * }, keyHolder);
	 */
	
	//쿼리 실행 결과로 변경된 행의 개수 리턴
	public void update(Member member) {

		//member.getName()등을 인자로, 쿼리의 인덱스 파라미터 값을 전달
		jdbcTemplate.update(
				"update MEMBER set NAME=?, PASSWORD=? where EMAIL=?",
				member.getName(), member.getPassword(), member.getEmail());
	}
	
	//이 역시, 위처럼 람다를 이용하거나 따로 인터페이스 구현식으로 변경가능
	public List<Member> selectAll() {
		List<Member> results= jdbcTemplate.query("select*from MEMBER", 
				new RowMapper<Member>() {
			@Override
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = new Member(
						rs.getString("EMAIL"),
						rs.getString("PASSWORD"),
						rs.getString("NAME"),
						rs.getTimestamp("REGDATE").toLocalDateTime());
				member.setId(rs.getLong("ID"));
				return member;
			}
		});
		
		return results;
	}
	
	/*
	 * 본래 MEMBER테이블의 전체 행 개수를 구하는 코드는
	 * List<Integer> results= jdbcTemplae.query(
	 * "select count(*)from MEMBER", 
	 * new RowMapper<Integer>() {
	 * @Override
	 * public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	 * return rs.getInt(1):
	 * }
	 * });
	 * return results.get(0);
	 * 
	 * 그러나 count(*)쿼리는 결과가 한 행이므로, 쿼리결과는 list보다는 integer로 받는게 낫다.
	 * 따라서 queryForObject()를 이용한다. 여기서 두번째 파라미터는, 칼럼을 읽어올 때 쓸 타입
	 * double이면 Double.class가 될것
	 * double avg= queryForObject("select avg(height) from FURNITURE where TYPE=? 
	 * and STATUS=?", Double.class, 100, "S");
	 */
	public int count() {
		Integer count= jdbcTemplate.queryForObject(
				"select count(*) from MEMBER", Integer.class);
		return count;
	}
}