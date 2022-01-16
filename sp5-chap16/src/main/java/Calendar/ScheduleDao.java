package Calendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ScheduleDao {
private JdbcTemplate jdbcTemplate;
	
	private RowMapper<Schedule> scheduleRowMapper=
			new RowMapper<Schedule>() {
		@Override
		public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
			Schedule schedule= new Schedule(rs.getInt("MID"),
					rs.getString("FID"), rs.getInt("MEAL"),
					rs.getInt("YEAR"), rs.getInt("MONTH"), rs.getInt("DATE"),
					rs.getString("MealName"), rs.getLong("Kcal"));
			return schedule;
		}
	};
	
	public ScheduleDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//@SuppressWarnings("null")
	public List<Schedule> findByMid(long userId, int mealtime) {
		
		List<Schedule> results;
		//아침 점심 저녁 간식 야식 순으로 results에 넣기
		switch(mealtime) {
		case 0:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ?", scheduleRowMapper, 
					userId);
			return results;
		case 1:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 1", scheduleRowMapper, 
					userId);
			return results;
		case 2:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 2", scheduleRowMapper, 
					userId);
			return results;
		case 3:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 3", scheduleRowMapper, 
					userId);
			return results;
		case 4:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 4", scheduleRowMapper, 
					userId);
			return results;
		case 5:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 5", scheduleRowMapper, 
					userId);
			return results;
		}
		return null;
	}
	
	public List<Schedule>findByMidDate(long userId, long year, long month, long date) {
		List<Schedule> results = jdbcTemplate.query(
				"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
				scheduleRowMapper, userId, year, month, date);
		return results;
	};
	public List<Schedule> findByFidDate(long fid, long year, long month, long date) {
		List<Schedule> results = jdbcTemplate.query(
				"select * from schedule where FID = ? and YEAR = ? and MONTH = ? and DATE = ?",
				scheduleRowMapper, fid, year, month, date);
		return results;
	}
	
	//7일짜리 음식 가져오기
	public List<Schedule> findWeekSchedule(long userId, long year, long month, long date) {
		//년도 넘어가는 경우, 가령 1월 3일이라면 1월2, 1, 12월31, 30, 29, 28, 27
		int [][] datearr = new int[7][3];
		if(month == 1 && date < 7) {
			int idx=0;
			for(idx= 0; idx <= 7-(int)date; idx++) {
				datearr[idx][0] = (int)year-1; datearr[idx][1] = 12; datearr[idx][2] = 31-idx;
			}
			int monthdate = 1;
			for(; idx<7; idx++) {
				datearr[idx][0] = (int)year; datearr[idx][1] = 1; datearr[idx][2] = monthdate++;
			}
		}
		//달 넘어가는 경우, 이전달이 30일까지인지, 31일까지인지 체크, 윤년 역시 체크
		else if(date < 7) {
			//이전달이 2월인 경우
			if(month == 3) {
				int idx=0;
				for(idx= 0; idx <= 7-(int)date; idx++) {
					datearr[idx][0] = (int)year; datearr[idx][1] = 2; 
					if(year%4 == 0) //윤년인 경우랑 
						datearr[idx][2] = 29-idx;
					else //윤년 아닌 경우
						datearr[idx][2] = 28-idx;
				}
				int monthdate = 1;
				for(; idx<7; idx++) {
					datearr[idx][0] = (int)year; datearr[idx][1] = 2; 
					datearr[idx][2] = monthdate++;
				}
			}
			//나머지 달에 대해
			else{
				int idx=0;
				for(idx= 0; idx <= 7-(int)date; idx++) {
					datearr[idx][0] = (int)year; datearr[idx][1] = (int) (month-1); 
					if(month == 4 || month == 6 || month == 8 || month == 10 || month == 12) 
					//전달이 30일까지라면
						datearr[idx][2] = 30-idx;
					//이전달이 31일인 경우
					else if(month == 2 || month == 5 || month == 7 || month == 9 || month == 11) {
						datearr[idx][2] = 31-idx;
					}
				}
				int monthdate = 1;
				for(; idx<7; idx++) {
					datearr[idx][0] = (int)year; datearr[idx][1] = (int)month; 
					datearr[idx][2] = monthdate++;
				}
			}
		}
		//아니라면, 그냥 대입
		else {
			int idx=1;
			for(idx= 1; idx <= 7; idx++) {
				datearr[idx-1][0] = (int)year; datearr[idx-1][1] = (int)month; 
				datearr[idx-1][2] = (int) (date-idx);
			}
		}

		List<Schedule> results1 = null; List<Schedule> results5 = null; 
		List<Schedule> results2 = null; List<Schedule> results6 = null;
		List<Schedule> results3 = null; List<Schedule> results7 = null;
		List<Schedule> results4 = null;
		for(int i=0; i<7; i++) {
			if(i==0)
				results1 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==1) 
				results2 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==2) 
				results3 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==3) 
				results4 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==4) 
				results5 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==5) 
				results6 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
			else if(i==6) 
				results7 = jdbcTemplate.query(
						"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
						scheduleRowMapper, userId, datearr[i][0], datearr[i][1], datearr[i][2]);
		}
		results1.addAll(results2); results1.addAll(results3); results1.addAll(results4);
		results1.addAll(results5); results1.addAll(results6); results1.addAll(results7);

		return results1;
	}
}
