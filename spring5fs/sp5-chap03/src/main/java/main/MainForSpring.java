package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

//import config.AppCtx;
import config.AppConf1;
import config.AppConf2;

import spring.ChangePasswordService;
import spring.DuplicateMemberException;
import spring.MemberInfoPrinter;
import spring.MemberNotFoundException;
import spring.RegisterRequest;
import spring.WrongIdPasswrodException;
import spring.MemberRegisterService;

//
import spring.MemberListPrinter;
import spring.VersionPrinter;

public class MainForSpring {
	private static ApplicationContext ctx=null;
	
	public static void main(String[] args) throws IOException {
//		ctx= new AnnotationConfigApplicationContext(AppCtx.class);
		ctx = new AnnotationConfigApplicationContext(AppConf1.class, AppConf2.class);
		
		BufferedReader reader = new BufferedReader
				(new InputStreamReader(System.in));
		while(true) {
			System.out.println("명령어를 입력하십시오");
			String command=reader.readLine();
			if(command.equalsIgnoreCase("exit")) {
				System.out.println("종료합니다");
				break;
			}
			if(command.startsWith("new ")) {
				processNewCommand(command.split(" "));
				continue;
			}
			else if(command.startsWith("change ")) {
				processChangeCommand(command.split(" "));
				continue;
			}
			else if(command.startsWith("list ")) {
				processListCommand();
				continue;
			}
			else if(command.startsWith("Info ")) {
				processInfoCommand(command.split(" "));
				continue;
			}
			else if(command.startsWith("version")) {
				processVersionCommand();
				continue;
			}
			printHelp();
		}
	}
	
	//assembler 객체 생성후, 의존 주입. 즉, assembler객체 생성시점에서 모든 사용할 객체가 생성됨
//	private static assembler Assembler = new assembler();
	private static void processNewCommand(String[] arg) {
		if(arg.length != 5) {
			printHelp();
			return;
		}
		
		//assembler객체 사용.
		MemberRegisterService regSv= ctx.getBean("memberRegSvc",
				MemberRegisterService.class);
		RegisterRequest req = new RegisterRequest();
		req.setEmail(arg[1]);
		req.setName(arg[2]);
		req.setPw(arg[3]);
		req.setcomfirmpw(arg[4]);
		
		if(!req.isPasswrodEqualtoComfirmpassword()) {
			System.out.println("암호와 확인이 불일치합니다\n");
			return;
		}
		try {
			regSv.regist(req);
			System.out.println("등록했습니다\n");
		} catch (DuplicateMemberException e) {
			System.out.println("이미 존재하는 이메일입니다\n");
		}
	}
	
	private static void processChangeCommand(String[] arg) {
		if(arg.length !=4) {
			printHelp();
			return;
		}
		ChangePasswordService changePwd = 
				ctx.getBean("changePwdSvc", ChangePasswordService.class);
		
		try {
			changePwd.changePassword(arg[1], arg[2],arg[3]);
			System.out.println("암호 변경 성공\n");
		}catch(MemberNotFoundException e) {
			System.out.println("존재하지 않는 이메일입니다\n");
		}catch(WrongIdPasswrodException e) {
			System.out.println("이메일과 암호가 일치하지 않습니다\n");
		}
	}
	
	//스프링 컨텡너로부터 memberlistprinter클래슨 ㅐ의 listPrinter이란 이름의 빈 객체를 찾아내, printall을 실행 
	private static void processListCommand() {
		MemberListPrinter listPrinter =
				ctx.getBean("listPrinter", MemberListPrinter.class);
		listPrinter.printAll();
	}
	
	//memberinfoprinter클래스를 사용하는 코드
	private static void processInfoCommand(String[] args) {
		if(args.length !=2) {
			printHelp();
			return;
		}
		
		MemberInfoPrinter infoPrinter = ctx.getBean("infoPrinter", MemberInfoPrinter.class);
		infoPrinter.printMemberInfo(args[1]);
	}
	
	private static void processVersionCommand() {
		VersionPrinter versionPrinter=
				ctx.getBean("versionPrinter", VersionPrinter.class);
		versionPrinter.print();
	}
	
	private static void printHelp() {
		System.out.println();
		System.out.println("잘못된 명령입니다\n");
		}
}

//annotationconfigapplicationcontext를 사용해 스프링 컨테이너를 생성. assembler과 같이, 객체 생성후 의존객체 주입
//assembler은 객체를 직접 생성하나, anno~는 설정파일(appctx클래스)로부터 생성할 객체와 의존 주입 대상을 정한다

//53, 54는 스프링컨테이너로부터 이름이 memberregsvc인 빈 객체를 구하고, 78 79는 이름이 changepwdsvc인 걸 구함

/*
 * 스프링 컨테이너가 생성한 빈은 싱글톤 객체이다. 스프링 컨테이너는 @Bean이 붙은 메서드에 대해 단 하나의 객체만 생성한다.
 * 즉, 다른 메서드에서 memberDao()를 몇 번 호출하더라도 언제나 같은 객체를 리턴한다.
 * 따라서 memberRegSvc()메서드와 changePwdSvc()메서드에서 memberDao()를 각각 실행해도, 동일한 memberDao객체를 쓴다.
 */