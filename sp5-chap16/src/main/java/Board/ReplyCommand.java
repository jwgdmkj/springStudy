package Board;

public class ReplyCommand {
	private Long board_id;
    private Long parent_id;	//부모 id(대댓글용.)
    private Long depth;
    private String reply_content;
    private String writer;
    
	public String getReply_content() { return reply_content; }
	public void setReply_content(String reply_content) 
	{	this.reply_content = reply_content; }
	
	public void setWriter(String writer) { this.writer= writer; }
	public String getWriter() { return writer; }

	public Long getBoard_id() { return board_id; }
	public void setBoard_id(Long board_id) { this.board_id = board_id; }
	  
	public Long getParent_id() { return parent_id; }
	public void setParent_id(Long parent_id) { this.parent_id = parent_id; }
	  
	public Long getDepth() { return depth; } 
	public void setDepth(Long depth) { this.depth = depth; } 
}
