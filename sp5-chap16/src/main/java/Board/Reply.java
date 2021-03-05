package Board;

import java.time.LocalDateTime;

public class Reply {
	private Long reply_id;
    private Long board_id;
    private Long parent_id;
    private Long depth;
    private String reply_content;
    private String writer;
    private LocalDateTime registerDateTime;
     
    public Reply(Long board_id, String writer,
    		String reply_content, LocalDateTime regTime) {
		this.board_id=board_id;
		this.writer=writer;
    	this.reply_content=reply_content;
		this.registerDateTime=regTime;
	}
    
    public Long getReply_id() {
        return reply_id;
    }
    public void setReply_id(Long reply_id) {
        this.reply_id = reply_id;
    }
    public Long getBoard_id() {
        return board_id;
    }
    public void setBoard_id(Long board_id) {
        this.board_id = board_id;
    }
    public Long getParent_id() {
        return parent_id;
    }
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }
    public Long getDepth() {
        return depth;
    }
    public void setDepth(Long depth) {
        this.depth = depth;
    }
    public String getReply_content() {
        return reply_content;
    }
    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }
    public String getWriter() {
        return writer;
    }
    public void setWriter(String reply_writer) {
        this.writer = reply_writer;
    }

    public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}
    
    public void setRegisterDateTime(LocalDateTime register_datetime) {
        this.registerDateTime = register_datetime;
    }
}
