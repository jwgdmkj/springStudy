package Calendar;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.Month;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.AuthInfo;
import spring.MemberDao;

@Controller
public class FoodSearchController {
	
	private FoodDao foodDao; 
	public void setsearchfoodservice(FoodDao foodDao) {
		this.foodDao = foodDao; 
	}
	private MemberDao memberDao;
	public void setMemberdao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	private ScheduleDao scheduleDao;
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
	 
	
	@RequestMapping(value = "calendar/searchfood", method = RequestMethod.GET)
	public String searchfood(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			Model model) {

	  	return "calendar/searchfood"; 
	}
	
	@RequestMapping(value = "calendar/searchfood", method = RequestMethod.POST)
	public String searchfood(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			Model model, Errors errors) {
		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/searchfood";
		}
		
		List<Food> foodlist = foodDao.finder(searchfood);
		
	  	model.addAttribute("foodlist", foodlist);
	  	return "calendar/searchfood"; 
	}
	
	@PostMapping("/calendar/RegistFood")
	public String execute(HttpServletRequest request,
	           HttpServletResponse response) throws Exception {

	    //long food_num = Integer.parseInt(request.getParameter("food_num"));
	    String food_num = request.getParameter("food_num");
	    Food food = foodDao.findbyNum(food_num);
	    
	    response.setContentType("text/html;charset=euc-kr");
	    PrintWriter out = response.getWriter();
	    
	    boolean result = foodDao.registfoodIn(food);
	    if(result) out.println("1");

	    out.close();
	    return null;
	}
	
	///////////////////////////////////////////////
	//캘린더 칸 누르면 나오는 창, 처음
	@RequestMapping(value = "/calendar/calendar/"
			+ "{memId}/scheduleInsertForm", method = RequestMethod.GET)
	public String registerWindow(@PathVariable("memId") Long memId,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			HttpSession session, Model model) throws Exception {
		
		//첫 화면은 연월일 데이터 필요
		long yearNum = Integer.parseInt(request.getParameter("year"));
		long monthNum = Integer.parseInt(request.getParameter("month"));
		long dateNum = Integer.parseInt(request.getParameter("date"));
		
		model.addAttribute("year", yearNum);
		model.addAttribute("month", monthNum+1);
		model.addAttribute("date", dateNum);
		model.addAttribute("memid", memId);
		
		//유저 아이디 구하기 위해, 로그인 정보 확인
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> scheduleList_1 = scheduleDao.findByMid(memId, 1);
		//if(scheduleList_1.size() > 1)
		//	System.out.println(scheduleList_1.get(0).getMonth());
		List<Schedule> scheduleList_2 = scheduleDao.findByMid(memId, 2);
		List<Schedule> scheduleList_3 = scheduleDao.findByMid(memId, 3);
		List<Schedule> scheduleList_4 = scheduleDao.findByMid(memId, 4);
		List<Schedule> scheduleList_5 = scheduleDao.findByMid(memId, 5);
		//System.out.println("달 출력하기 "+scheduleList_1.get(0).getMonth());
		model.addAttribute("scheduleList_1",scheduleList_1);
		model.addAttribute("scheduleList_2",scheduleList_2);
		model.addAttribute("scheduleList_3",scheduleList_3);
		model.addAttribute("scheduleList_4",scheduleList_4);
		model.addAttribute("scheduleList_5",scheduleList_5);
		return "/calendar/foodRegistForm";
	}
	
	//오늘먹은것은 창
	@RequestMapping(value = "/calendar/calendar/{memId}/scheduleInsertForm", 
			method = RequestMethod.POST)
	public String registerWindowbyPost(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			@PathVariable("memId") Long memId, HttpSession session, Model model, Errors errors) throws Exception {
		
		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/foodRegistForm";
		}
		//System.out.println(searchfood.getDate());
		//System.out.println(searchfood.getMonth());
		//System.out.println(searchfood.getYear());
		
		List<Food> foodlist = foodDao.finder(searchfood);
		
	  	model.addAttribute("foodlist", foodlist);
	  	model.addAttribute("date", searchfood.getDate());
	  	model.addAttribute("month", searchfood.getMonth());
	  	model.addAttribute("year", searchfood.getYear());
	  	model.addAttribute("memid", memId);
	  	
	  	//로그인 정보 확인
	  	long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
	  	List<Schedule> scheduleList_1 = scheduleDao.findByMid(memId, 1);
	//	if(scheduleList_1.size() > 0)
		//	System.out.println("숫자 출력: "+scheduleList_1.get(0).getFid());
		//else
			//System.out.println("없어용");

		List<Schedule> scheduleList_2 = scheduleDao.findByMid(memId, 2);
	//	if(scheduleList_2.size() > 0)
		//	System.out.println("숫자 출력 "+scheduleList_2.get(0).getFid());
	//	else
		//	System.out.println("2도 없어용");
		
		List<Schedule> scheduleList_3 = scheduleDao.findByMid(memId, 3);
		List<Schedule> scheduleList_4 = scheduleDao.findByMid(memId, 4);
		List<Schedule> scheduleList_5 = scheduleDao.findByMid(memId, 5);
		
		model.addAttribute("scheduleList_1",scheduleList_1);
		model.addAttribute("scheduleList_2",scheduleList_2);
		model.addAttribute("scheduleList_3",scheduleList_3);
		model.addAttribute("scheduleList_4",scheduleList_4);
		model.addAttribute("scheduleList_5",scheduleList_5);
		
	  	return "calendar/foodRegistForm";
	}
	//캘린더 누르면 나오는 창, 음식을 검색했을 시
	////////////////////////////////////////////////////
	//음식을 검색하면 이를 등록하는 action
	@PostMapping("/calendar/calendar/{memId}/foodRegistAction") 
	@ResponseBody
    public Object foodRegistAction(@RequestParam Map<String, Object> paramMap
    		, HttpSession session) {
	//	System.out.println(paramMap.get("meal"));
        //리턴값
        Map<String, Object> retVal = new HashMap<String, Object>();
        AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
        String usermail = authInfo.getEmail();
        long userId = memberDao.selectByEmail(usermail).getId();

        long mealNum = 0;
		if(paramMap.get("meal").equals("breakfast")) {
			mealNum = 1;
		} else if(paramMap.get("meal").equals("lunch")) {
			mealNum = 2;
		} else if(paramMap.get("meal").equals("dinner")) {
			mealNum = 3;
		} else if(paramMap.get("meal").equals("snack")) {
			mealNum = 4;
		} else if(paramMap.get("meal").equals("midnight")) {
			mealNum = 5;
		}
		String foodNum = (String) paramMap.get("foodnum");
		String mealname = (String) paramMap.get("mealname");

		double kcal = Double.parseDouble((String) paramMap.get("kcal"));

		long year = Long.parseLong((String) paramMap.get("year"));
		long month = Long.parseLong((String) paramMap.get("month"));
		long date = Long.parseLong((String) paramMap.get("date"));

        //정보입력
        int result = foodDao.insertIntoMember(userId, mealNum, foodNum, year, month, date,
        		mealname, kcal);

        if(result>0){
            retVal.put("code", "OK");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "등록에 실패했습니다.");
        }
 
        return retVal;
    }
	
	//////////////////////////////////////////////////////////////////////
	//[나와 오늘 같은 음식 먹은 사람]을 위해, 일단 내가 오늘 먹은 음식 리스트가 뜨고,(GET)
	@RequestMapping(value = "/calendar/calendar/"
			+ "{memId}/findsamefooduser", method = RequestMethod.GET)
	public String findSamefooduser_get(@PathVariable("memId") Long memId,
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) throws Exception {

		//첫 화면은 연월일 데이터 필요
		long yearNum = Integer.parseInt(request.getParameter("year"));
		long monthNum = Integer.parseInt(request.getParameter("month"));
		long dateNum = Integer.parseInt(request.getParameter("date"));
		
		model.addAttribute("year", yearNum);
		model.addAttribute("month", monthNum+1);
		model.addAttribute("date", dateNum);
		model.addAttribute("memid", memId);
		
		//유저 아이디 구하기 위해, 로그인 정보 확인
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> samefoodList = scheduleDao.findByMidDate(userId, yearNum,
				monthNum+1, dateNum);
		model.addAttribute("samefoodList",samefoodList);
		
		
		return "/calendar/finduserForm";
	}
	//이 중 음식 하나를 누르면 그 음식을 먹은 사람들의 이름이 뜨게 됨(POST)
	@PostMapping("/calendar/calendar/{memId}/findsamefooduser")
	public String findSamefooduser_post(
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood, 
			@PathVariable("memId") Long memId,
			HttpSession session, Model model, Errors errors)
			throws Exception {

		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/foodRegistForm";
		}
		
		//유저 아이디 구하기 위해, 로그인 정보 확인
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
			String usermail = authInfo.getEmail();
			userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> samefoodList = scheduleDao.findByMidDate(userId, searchfood.getYear(),
				searchfood.getMonth()+1, searchfood.getDate());
		model.addAttribute("samefoodList",samefoodList);
		
		//음식 찾기
		model.addAttribute("memid", memId);
		String searchfood_name = "'" + searchfood.getFoodname() + "'";
		//정보입력
		Food findedfood = foodDao.finderSpecific(searchfood_name);
		
		if(findedfood != null) {
			//System.out.println("아이디 " + findedfood.getID());
			//System.out.println("연 " + searchfood.getYear());
			//System.out.println("월 " + searchfood.getMonth());
			//System.out.println("일 " + searchfood.getDate());
			List<Schedule> sameuserList = scheduleDao.findByFidDate(findedfood.getID(),
	        		searchfood.getYear(),searchfood.getMonth()+1, searchfood.getDate());

	        model.addAttribute("sameuserList",sameuserList);
	     //   System.out.println(searchfood_name);
	    //    System.out.println(searchfood_name);
	    //    System.out.println("세임유저 " + sameuserList.get(0).getMid());
		}

        return "/calendar/finduserForm";
	}
	
	///////////////////////////////////////////////////////////////////////
	//그날 먹은 음식 기준 7일 이전의 것들을 가져와
	@RequestMapping(value = "/calendar/calendar/"
			+ "{memId}/recommendFood", method = RequestMethod.GET)
	public String recFood_get(@ModelAttribute("searchfoodObject") searchfoodObject searchfood, 
			@PathVariable("memId") Long memId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model, Errors errors)
			throws Exception {

		//첫 화면은 연월일 데이터 필요
		long yearNum = Integer.parseInt(request.getParameter("year"));
		long monthNum = Integer.parseInt(request.getParameter("month"));
		long dateNum = Integer.parseInt(request.getParameter("date"));
		
		//유저 아이디 구하기 위해, 로그인 정보 확인
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
			String usermail = authInfo.getEmail();
			userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int dayOfMonth = now.getDayOfMonth();
		
		//기존 7일의 음식 가져오기
		List<Schedule> day7food = scheduleDao.findWeekSchedule(userId, 
				year, month, dayOfMonth);
		model.addAttribute("7dayfood", day7food);
		for(int i=0; i<day7food.size(); i++)
			System.out.println(day7food.get(i).getFid());
		
		double kcalint =0, protint=0, fatint=0, carbsint =0, sugarint=0, natint=0,
				choleint=0, saturint=0, transint=0;
		
		for(int i=0; i<day7food.size(); i++) {
			Food findedfood = foodDao.findbyNum(day7food.get(i).getFid());
			if(findedfood.getKcal().length() > 2)
				kcalint +=  Double.valueOf(findedfood.getKcal().substring(1, findedfood.getKcal().length()-1));
			if(findedfood.getFat().length() > 2)
				fatint +=  Double.valueOf(findedfood.getFat().substring(1, findedfood.getFat().length()-1));
			if(findedfood.getCarbs().length() > 2)
				carbsint +=  Double.valueOf(findedfood.getCarbs().substring(1, findedfood.getCarbs().length()-1));
			if(findedfood.getSugar().length() > 2)
				sugarint +=  Double.valueOf(findedfood.getSugar().substring(1, findedfood.getSugar().length()-1));
			if(findedfood.getNat().length() > 2)
				natint +=  Double.valueOf(findedfood.getNat().substring(1, findedfood.getNat().length()-1));
			if(findedfood.getChole().length() > 2)
				choleint +=  Double.valueOf(findedfood.getChole().substring(1, findedfood.getChole().length()-1));
			if(findedfood.getSatur().length() > 2)
				saturint +=  Double.valueOf(findedfood.getSatur().substring(1, findedfood.getSatur().length()-1));
			//transint +=  Double.valueOf(findedfood.getTrans().substring(1, findedfood.getTrans().length()-1));
			if(findedfood.getProt().length() > 2)
				protint +=  Double.valueOf(findedfood.getProt().substring(1, findedfood.getProt().length()-1));
		}
		String kcalstr = Double.toString(kcalint); 
		String protstr = Double.toString(protint); 
		String fatstr = Double.toString(fatint); 
		String carbsstr = Double.toString(carbsint);
		String sugarstr = Double.toString(sugarint); 
		String natstr = Double.toString(natint);
		String cholestr = Double.toString(choleint); 
		String saturstr = Double.toString(saturint); 
		//String transstr = Double.toString(transint);
		
		/*
				"{\"kcal\": "+ kcalstr + "," +
				"\"chole\": " + cholestr + "," + "\"prot\": " + protstr + "," + 
				"\"carbs\": " + carbsstr + "," + "\"sugar\": " + sugarstr + "," +
				"\"nat\": " + natstr + "," + "\satur\": " + saturstr + "," + 
				"\"fat\": " + fatstr + "}"
		 */
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> __request__ = new HttpEntity<>("{\"kcal\": " + kcalstr + "," +
				"\"chole\": " + cholestr + "," + "\"prot\": " + protstr + "," +
				"\"carbs\": " + carbsstr + "," + "\"sugar\": " + sugarstr + "," + 
				"\"nat\": " + natstr + "," + "\"satur\": " + saturstr + "," +
				"\"fat\": " + fatstr + "," + "\"userId\": " + userId + "}", headers);
		//ResponseEntity response = rt.exchange("http://localhost:8081/test", HttpMethod.GET, request, String.class);		
		String __response__ = rt.postForObject("http://127.0.0.1:5000/tospring", 
				__request__, String.class);		
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(__response__);
		
		String[] resparr = new String[7]; 
		String tmp = ""; int idx = 0;
		for(int i=0; i<__response__.length(); i++) {
			if(__response__.charAt(i) == ' ') {
				resparr[idx++] = tmp;
				tmp = "";
				if(idx == 6) {
					resparr[6] = __response__.substring(i + 1, __response__.length());
					break;
				}
			}
			else tmp += __response__.charAt(i);
		}
		model.addAttribute("resparr", resparr);
		
		for(int i=0; i<7; i++) {
			System.out.println(resparr[i]);
		}
		//JSONObject jObject = new JSONObject(__response__);
		/*
		 * try { JSONParser parser = new JSONParser(); JSONObject jsonobject =
		 * (JSONObject)parser.parse(__response__); System.out.println("qwer");
		 * System.out.println(jsonobject.get("kcal")); System.out.println("qwer");
		 * System.out.println(jsonobject.get("prot"));
		 * System.out.println(jsonobject.get("fat"));
		 * System.out.println(jsonobject.get("carbs"));
		 * System.out.println(jsonobject.get("sugar"));
		 * System.out.println(jsonobject.get("nat"));
		 * System.out.println(jsonobject.get("recfoodname")); } catch(ParseException e)
		 * { System.out.println(e); }
		 */
		
		return "/calendar/recommendfood";
	}
}
