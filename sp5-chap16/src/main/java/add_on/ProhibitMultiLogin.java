/*
 * package add_on;
 * 
 * import java.util.Map; import java.util.concurrent.ConcurrentHashMap;
 * 
 * import javax.servlet.annotation.WebListener; import
 * javax.servlet.http.HttpSession; import javax.servlet.http.HttpSessionEvent;
 * 
 * import org.springframework.context.annotation.Configuration;
 * 
 * import spring.AuthInfo;
 * 
 * @Configuration
 * 
 * @WebListener //중복 로그인 방지 기능 //sessions라는 Map을 만들어, (고유생성아이디값, 값)으로
 * HttpSession을 갖도록 함(키값 - 세션의 고유생성아이디값) //getSessionIDCheck는, 세션의 값과 비교할 로그인
 * 아이디 값을 받음 public class ProhibitMultiLogin { private static final Map<String,
 * HttpSession> sessions = new ConcurrentHashMap<>();
 * 
 * //중복로그인 지우기 public synchronized static String getSessionidCheck(String type,
 * String compareId){ String result = ""; for( String key : sessions.keySet() ){
 * HttpSession hs = sessions.get(key); AuthInfo auth = new AuthInfo(); if(hs !=
 * null) { auth = (AuthInfo) hs.getAttribute(type); if(auth != null &&
 * auth.getUserId().toString().equals(compareId)) { result = key.toString(); } }
 * } removeSessionForDoubleLogin(result); return result; }
 * 
 * //세션에 저장된 id값이, 키가 user_id이면서 값이 admin일 경우, (2)처럼 호출하여 세션에서 중복된 아이디 검사 후 제거
 * private static void removeSessionForDoubleLogin(String userId){
 * System.out.println("remove userId : " + userId); if(userId != null &&
 * userId.length() > 0){ sessions.get(userId).invalidate();
 * sessions.remove(userId); } }
 * 
 * //로그인 성공 후 세션이 만들어지면, 에노테이션 WebListener에 의해 세션값이 session에 저장됨 //@Override
 * public void sessionCreated(HttpSessionEvent hse) { System.out.println(hse);
 * sessions.put(hse.getSession().getId(), hse.getSession()); }
 * 
 * //로그아웃, 브라우저 종료 통해 세션이 사라지면, Destroyed에 의해 제거됨 //@Override public void
 * sessionDestroyed(HttpSessionEvent hse) {
 * if(sessions.get(hse.getSession().getId()) != null){
 * sessions.get(hse.getSession().getId()).invalidate();
 * sessions.remove(hse.getSession().getId()); } } }
 */