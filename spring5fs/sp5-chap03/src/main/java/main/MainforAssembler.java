/*
 * package main; import java.io.BufferedReader; import java.io.IOException;
 * import java.io.InputStreamReader;
 * 
 * import Assembler.assembler; import spring.ChangePasswordService; import
 * spring.DuplicateMemberException; import spring.MemberNotFoundException;
 * import spring.RegisterRequest; import spring.WrongIdPasswrodException; import
 * spring.MemberRegisterService;
 * 
 * public class MainforAssembler { public static void main(String[] args) throws
 * IOException { BufferedReader reader = new BufferedReader (new
 * InputStreamReader(System.in)); while(true) {
 * System.out.println("명령어를 입력하십시오"); String command=reader.readLine();
 * if(command.equalsIgnoreCase("exit")) { System.out.println("종료합니다"); break; }
 * if(command.startsWith("new ")) { processNewCommand(command.split(" "));
 * continue; } else if(command.startsWith("change ")) {
 * processChangeCommand(command.split(" ")); continue; } printHelp(); } }
 * 
 * //assembler 객체 생성후, 의존 주입. 즉, assembler객체 생성시점에서 모든 사용할 객체가 생성됨 private
 * static assembler Assembler = new assembler(); private static void
 * processNewCommand(String[] arg) { if(arg.length != 5) { printHelp(); return;
 * }
 * 
 * //assembler객체 사용. MemberRegisterService regsrv= Assembler.getMemberreg();
 * RegisterRequest req = new RegisterRequest(); req.setEmail(arg[1]);
 * req.setName(arg[2]); req.setPw(arg[3]); req.setcomfirmpw(arg[4]);
 * 
 * if(!req.isPasswrodEqualtoComfirmpassword()) {
 * System.out.println("암호와 확인이 불일치합니다\n"); return; } try { regsrv.regist(req);
 * System.out.println("등록했습니다\n"); } catch (DuplicateMemberException e) {
 * System.out.println("이미 존재하는 이메일입니다\n"); } }
 * 
 * private static void processChangeCommand(String[] arg) { if(arg.length !=4) {
 * printHelp(); return; } ChangePasswordService changePwd =
 * Assembler.getChangePwdsrv();
 * 
 * try { changePwd.changePassword(arg[1], arg[2],arg[3]);
 * System.out.println("암호 변경 성공\n"); }catch(MemberNotFoundException e) {
 * System.out.println("존재하지 않는 이메일입니다\n"); }catch(WrongIdPasswrodException e) {
 * System.out.println("이메일과 암호가 일치하지 않습니다\n"); } }
 * 
 * private static void printHelp() { System.out.println();
 * System.out.println("잘못된 명령입니다\n"); } }
 */