public class AnnotationWithinWarningSuppression {

	@SuppressWarnings("all") // all annotations within shouldn't be included in the IAC's output
	public static void main() {
 		int x;
 		/* some methods here...*/
 		ArrayList<@NonNull CalendarComponent> clist = calendar.getComponents();

 		String z;
	}
}

