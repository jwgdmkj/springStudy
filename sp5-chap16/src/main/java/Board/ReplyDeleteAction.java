package Board;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReplyDeleteAction {
	private ReplyDao replyDao;
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao= replyDao;
	}
	
	@PostMapping("/bulletin/ReplyDeleteAction")
	public String execute(HttpServletRequest request,
	           HttpServletResponse response) throws Exception {

	    long comment_num = Integer.parseInt(request.getParameter("comment_num"));
	    boolean result = replyDao.deleteComment(comment_num);
	        
	    response.setContentType("text/html;charset=euc-kr");
	    PrintWriter out = response.getWriter();

	    // 정상적으로 댓글을 삭제했을경우 1을 전달한다.
	    if(result) out.println("1");
	        
	    out.close();
	    return null;
	}
}
