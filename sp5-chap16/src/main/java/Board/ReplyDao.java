package Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ReplyDao {
private JdbcTemplate jdbcTemplate;
	
	//SQL실행결과로 구한 ResultSet에서 한 행의 데이터를 읽어와 자바객체로 변환하는 매퍼
	private RowMapper<Reply> repRowMapper=
			new RowMapper<Reply>() {
		@Override
		public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
			Reply reply= new Reply(rs.getLong("BOARD_ID"), 
					rs.getString("WRITER"), rs.getString("REPLY_CONTENT"),	
					rs.getTimestamp("REGDATE").toLocalDateTime());

	//		reply.setBoard_id(rs.getLong("BOARD_ID"));
	//		reply.setWriter(rs.getString("WRITER"));
			reply.setParent_id(rs.getLong("PARENT_ID"));
			reply.setReply_id(rs.getLong("REPLY_ID"));
			reply.setDepth(rs.getLong("DEPTH"));

			return reply;
		}
};
	
	/*
	 * 드라이버 로드, 커넥션 생성 & DB 연결, SQL 실행, DB 연결 해제 부분은 매번 같은 동작을 반복한다
	 * JDBC Template을 이용해서 이러한 작업들을 간단하게 처리 할 수 있다
	 * 
	 * jdbtTemplate 객체를 위에서 생성했고, 이에 bean으로 등록한 DataSource를 주입.
	 * @Autowired에 의해 DataSource타입에 해당하는 bean을 찾아 ㅈㅜ입.
	 */
	public ReplyDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from REPLY", Integer.class);
		return count;
	}
	
	public List<Reply> selectByBoardId(Long boardNum) {
		List<Reply> results = jdbcTemplate.query(
				"select * from REPLY where BOARD_ID = ?",
				repRowMapper, boardNum);

		return results.isEmpty() ? null : results;
	}
	
	public Reply selectById(Long replyId) {
		List<Reply> results= jdbcTemplate.query(
				"select * from REPLY where REPLY_ID = ?",
				repRowMapper, replyId);
		
		return results.isEmpty()? null : results.get(0);
	}
	
	public boolean updateComment(Reply updatedReply, String comment_content) {
		jdbcTemplate.update(
				"UPDATE REPLY SET REPLY_CONTENT= ? WHERE REPLY_ID = ?", 
				comment_content, updatedReply.getReply_id());
//		System.out.print(comment_content);
		return true;
	}
	
	public boolean deleteComment(long reply_id) {
		jdbcTemplate.update(
				"DELETE FROM REPLY WHERE REPLY_ID=?", reply_id);
		
		return true;
	}
	
	public void regReply(Reply reply) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
//		System.out.println("q");
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
						"insert into REPLY (BOARD_ID, PARENT_ID, DEPTH, REPLY_CONTENT,"
						+ " WRITER, "
						+ "REGDATE) "
				+ "values (?, ?, ?, ?, ?, ?)",
				new String[] { "REPLY_ID" });

				// 인덱스 파라미터 값 설정
				pstmt.setLong(1, reply.getBoard_id());
				pstmt.setLong(2,  reply.getParent_id());
				pstmt.setLong(3, reply.getDepth());
				pstmt.setString(4, reply.getReply_content());
				pstmt.setString(5, reply.getWriter());
				pstmt.setTimestamp(6,
						Timestamp.valueOf(reply.getRegisterDateTime()));

				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		reply.setReply_id(keyValue.longValue());
	}
}
