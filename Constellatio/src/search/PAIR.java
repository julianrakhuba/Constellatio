package search;

public class PAIR {
	private String fnc;
	private String val;
	
	public PAIR(String fnc, String val) {
		this.fnc = fnc;
		this.val = val;
	}
	

	public String getFnc() {
		return fnc;
	}

	public String getVal() {
		if(fnc.equals("LIKE")) return "%" + val + "%";
		else if(fnc.equals("IS NULL")) return null;
		else if(fnc.equals("IS NOT NULL")) return null;
		else return  val ;
	}
}
