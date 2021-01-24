package spring;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

public class MemberPrinter {
	private DateTimeFormatter dateTimeFormatter;
	
	public void print(Member member) {
		
		if(dateTimeFormatter == null) {
		System.out.printf("회원정보- 아이디 %d, 이메일 %s, 이름 %s, 등록일 %tF\n",
				member.getID(), member.getEM(), member.getName(),
				member.getDate());
		}
		else {
			System.out.printf("회원정보- 아이디 %d, 이메일 %s, 이름 %s, 등록일 %s\n",
					member.getID(), member.getEM(), member.getName(),
					dateTimeFormatter.format(member.getDate()));
		}
	}
	
	//dateTimeFormatter가 널이면 날싸형식은 %tF, 아니면 %s.
	//@Autowired는 이를 붙인 타입에 해당하는 빈이 존재하지 않으면 익셉션이 발생.
	//따라서, setDateFormatter()메서드에서 요구하는 DateTimeFormatter타입의 빈이 존재하지 않으면 익셉션
	//MemberPrinter은 setDateFormatter()메서드에 자동주입할 빈이 존재하지 않으면 익셉션이 발생하기 도다는
	//dateTimeFormatter가 null이면 된다.
	
	//자동주입할 대상이 필수가 아닌 경우엔, 오토와이어의 required속성을 false로 지정하면 된다.
	@Autowired
	//@Autowried(required=false)
	/*
	 * 이를통해 매칭되는 빈이 없어도 익셉션이 발생하지 않고, 자동주입을 수행하지 않는다. DateTimeFormatter타입의 빈이
	 * 존재하지 않을 시 익셉션은 발생하지 않고, setDateFormatter()를 실행하지 않음
	 */
	/*
	 * @Autowired
	 * public void setDateFormatter(Optional<DateTimeFormatter> formatterOpt) {
	 * 	if(formatterOpt.isPresent()) {
	 * 		this.dateTimeFormatter= formatterOpt.get();
	 * }
	 * 	else {
	 * 		this.dateTimeFormatter = null
	 * 	}
	 * }
	 * 자동주입대상이 optional인 경우, 일치하는 빈이 없으면 값이 없는 optinal을 인자로 전달
	 * 있으면 해당 빈을 값으로 갖는 optional을 인자로 전달(formatterOpt.get())
	 * 위의 예는, Optinal#isPresetn()가 true면 값이 존재하므로, 해당 값을 dateTimeFormatter필드에 할당
	 * 즉 DateTimeFormatter 타입 빈을 주입받아 dateTimeFormatter필드에 할당
	 * 값이 없으면 주입받은 빈 객체가 없으니, 필드에 널을 할당
	 */
	public void setDateFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter=dateTimeFormatter;
	}
}

/*
 * 또는 nullable도 가능
 * @Autowired
 * public void setDateFormatter(@Nullable DatTimeFormatter dateTimeFormatter) {
 * 	this.dateTimeFormatter = dateTimeFormatter;
 * 	}
 * }
 * 오토와이어를 붙인 세터메서드에서 @Nullable 애노테이션을 '의존 주입 대상 파라미터'에 붙이면, 스프링 컨테이너는 
 * 세터 메서드를 호추랗ㄹ 때 자동 주입할 빈이 존재하면 해당 빈을 인자로 전달, 존재 안하면 널을 전달
 * 즉, required=false와의 차이점은, 자동주입 빈이 존재하지 않아도 메서드가 호출됨
 */
