package chap07;

public class ExeTimeCalculator implements Calculator{
	private Calculator delegate; // calculator객체를 전달받아 delegate필드에 할당
	
	public ExeTimeCalculator(Calculator delegate) {
		this.delegate= delegate;
	}
	
	@Override
	public long factorial(long num) {
		long start = System.nanoTime();
		long result= delegate.factorial(num);
		long end = System.nanoTime();
		
		System.out.printf("%s.factorial(%d) 실행시간 = %d\n", delegate.getClass().getSimpleName(),
				num, (end-start));
		
		return result;
	}
}

/* 이 떄 측정방법은
 * ImpeCaculator impecal = new ImpeCalculator();
 * ExeTimeCalculator calculator = new ExeTimeCalculator(impeCal);
 * long result = calculator.factorial(4);
 */
