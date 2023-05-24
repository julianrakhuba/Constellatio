package application;

import java.util.HashMap;

import javafx.scene.input.KeyCode;

public class KeyboardKeys {
	private HashMap<KeyCode,String> codes = new HashMap<KeyCode,String>();
	private HashMap<KeyCode,String> shiftCodes = new HashMap<KeyCode,String>();
	
	public KeyboardKeys() {

		codes.put(KeyCode.MINUS,"-");// -
		codes.put(KeyCode.SLASH,"/");// /
		codes.put(KeyCode.EQUALS," = ");// /
		codes.put(KeyCode.QUOTE ,"\'");//single quote
		
		codes.put(KeyCode.DIGIT0,"0");// 0
		codes.put(KeyCode.DIGIT1,"1");// /	
		codes.put(KeyCode.DIGIT2,"2");// /	
		codes.put(KeyCode.DIGIT3,"3");// /	
		codes.put(KeyCode.DIGIT4,"4");// /	
		codes.put(KeyCode.DIGIT5,"5");// /	
		codes.put(KeyCode.DIGIT6,"6");// /	
		codes.put(KeyCode.DIGIT7,"7");// /	
		codes.put(KeyCode.DIGIT8,"8");// /	
		codes.put(KeyCode.DIGIT9,"9");// /	
		codes.put(KeyCode.PERIOD ,".");// /	
		
		codes.put(KeyCode.COMMA ,",");// /	
//		codes.put(KeyCode.SPACE ," ");// /	

		
		//
		codes.put(KeyCode.A ,"a");
		codes.put(KeyCode.B ,"b");// /	
		codes.put(KeyCode.C ,"c");// /	
		codes.put(KeyCode.D ,"d");// /	
		codes.put(KeyCode.E ,"e");// /	
		codes.put(KeyCode.F ,"f");// /	
		codes.put(KeyCode.G ,"g");// /	
		codes.put(KeyCode.H ,"h");// /	
		codes.put(KeyCode.I ,"i");// /	
		codes.put(KeyCode.J ,"j");// /	
		codes.put(KeyCode.K ,"k");// /	
		codes.put(KeyCode.L ,"l");// /	
		codes.put(KeyCode.M ,"m");// /	
		codes.put(KeyCode.N ,"n");// /	
		codes.put(KeyCode.O ,"o");// /	
		codes.put(KeyCode.P ,"p");// /	
		codes.put(KeyCode.Q ,"q");// /	
		codes.put(KeyCode.R ,"r");// /	
		codes.put(KeyCode.S ,"s");// /	
		codes.put(KeyCode.T ,"t");// /	
		codes.put(KeyCode.U ,"u");// /	
		codes.put(KeyCode.V ,"v");// /	
		codes.put(KeyCode.W ,"w");// /	
		codes.put(KeyCode.X ,"x");// /	
		codes.put(KeyCode.Y ,"y");// /	
		codes.put(KeyCode.Z ,"z");// /	
		
		shiftCodes.put(KeyCode.EQUALS,"+");// +
		shiftCodes.put(KeyCode.DIGIT8,"*");// *
		shiftCodes.put(KeyCode.DIGIT9,"(");// (
		shiftCodes.put(KeyCode.DIGIT0,")");// )
		shiftCodes.put(KeyCode.DIGIT5,"%");// /	
		
		shiftCodes.put(KeyCode.UNDERSCORE,"_");// -

		
		shiftCodes.put(KeyCode.A ,"A");
		shiftCodes.put(KeyCode.B ,"B");// /	
		shiftCodes.put(KeyCode.C ,"C");// /	
		shiftCodes.put(KeyCode.D ,"D");// /	
		shiftCodes.put(KeyCode.E ,"E");// /	
		shiftCodes.put(KeyCode.F ,"F");// /	
		shiftCodes.put(KeyCode.G ,"G");// /	
		shiftCodes.put(KeyCode.H ,"H");// /	
		shiftCodes.put(KeyCode.I ,"I");// /	
		shiftCodes.put(KeyCode.J ,"J");// /	
		shiftCodes.put(KeyCode.K ,"K");// /	
		shiftCodes.put(KeyCode.L ,"L");// /	
		shiftCodes.put(KeyCode.M ,"M");// /	
		shiftCodes.put(KeyCode.N ,"N");// /	
		shiftCodes.put(KeyCode.O ,"O");// /	
		shiftCodes.put(KeyCode.P ,"P");// /	
		shiftCodes.put(KeyCode.Q ,"Q");// /	
		shiftCodes.put(KeyCode.R ,"R");// /	
		shiftCodes.put(KeyCode.S ,"S");// /	
		shiftCodes.put(KeyCode.T ,"T");// /	
		shiftCodes.put(KeyCode.U ,"U");// /	
		shiftCodes.put(KeyCode.V ,"V");// /	
		shiftCodes.put(KeyCode.W ,"W");// /	
		shiftCodes.put(KeyCode.X ,"X");// /	
		shiftCodes.put(KeyCode.Y ,"Y");// /	
		shiftCodes.put(KeyCode.Z ,"Z");// /	
		
		shiftCodes.put(KeyCode.QUOTE ,"\"");// double quote
		
	}

	public HashMap<KeyCode, String> getCodes() {
		return codes;
	}

	public HashMap<KeyCode, String> getShiftCodes() {
		return shiftCodes;
	}
	
	
}
