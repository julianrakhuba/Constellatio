package logic;

import java.util.ArrayList;

import application.JoinLine;
import elements.NText;
import generic.BaseConnection;
import generic.DLayer;
import generic.LAY;
import pivot.FieldVersion;
import status.JoinType;

public class SQL {
	
	public SQL() {
//		System.out.println("new SQL()");
	}

	private ArrayList<NText> texts = new ArrayList<NText>();	
	
	public SQL append(String txt) {
		this.addNText(new NText(txt, null));
		return this;
	}
	
	public SQL SELECT () {
		this.append(" SELECT ");
		return this;
	}
	
	public SQL WHERE() {
		this.append(" WHERE ");
		return this;
	}

	public SQL AND() {
		this.append(" AND ");
		return this;
	}
	
	public SQL OR() {
		this.append(" OR ");
		return this;
	}
	
	public SQL ON() {
		this.append(" ON ");
		return this;
	}
	
	public SQL ORDERBY() {
		this.append(" ORDER BY ");
		return this;
	}
	
	public SQL open() {
		this.append("(");
		return this;
	}

	public SQL close() {
		this.append(")");
		return this;
	}

	public SQL WITHROLLUP() {
		this.append(" WITH ROLLUP ");
		return this;
	}
	
	public SQL DISTINCT(String string) {
		this.append(" DISTINCT " + string);	
		return this;
	}
	
	public SQL GROUPBY() {
		this.append(" GROUP BY ");
		return this;
	}
	
	public SQL FROM(LAY lay) {
		this.addNText(new NText(" FROM " + lay.nnode.getFullNameWithOptionalQuotes() + " " + lay.getAliase(), lay));	
		return this;
	}
	
	public SQL JOIN(JoinLine jLine, LAY lay) {
		String string;
		JoinType jt = jLine.getJoinType();
		if(lay instanceof DLayer) {
			string = "(" + ((DLayer)lay).getParentLay().getSQL() + ") " + lay.getAliase();
		}else {
			string = lay.nnode.getFullNameWithOptionalQuotes() + " " + lay.getAliase();
		}
		
		if(jt == JoinType.JOIN) this.addNText(new NText(" JOIN " + string, lay));	
		else if(jt == JoinType.LEFT)  this.addNText(new NText(" LEFT JOIN " + string, lay));	
		else if(jt == JoinType.RIGHT)  this.addNText(new NText(" RIGHT JOIN " + string, lay));	
		return this;
	}
	
	public SQL SUBQRY(DLayer dlay) {
		this.addNText(new NText(" FROM (" + dlay.getParentLay().getSQL().toString() + ") " + dlay.getAliase() + " ", dlay));	
		return this;
	}
	
	public SQL line() {
		this.append(System.getProperty("line.separator"));
		return this;
	}
	
	public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
		texts.forEach(e -> stringBuilder.append(e.getString()));		
		return stringBuilder.toString();		
	}
	
	public void addNText(NText ntext) {
		texts.add(ntext);
	}

	public void addAllNTexts(ArrayList<NText> textSql) {
		textSql.forEach(ntext ->{
			texts.add(ntext);
		});		
	}

	public SQL endStatement(BaseConnection db) {
		this.append(db.end());
		return this;		
	}
	
	public SQL VERSION_AS(FieldVersion ver) {
		this.addNText(new NText(ver.getFunction_Column() + " AS " + ver.getAliase(), ver));	
		return this;
	}

	public SQL FIELD_AS(Field fld) {
		this.addNText(new NText(fld.getFunction_Column() + " AS " + fld.getAliase(), fld));
		return this;
	}
	
	public SQL FIELD(Field fld) {
		this.addNText(new NText(fld.getFunction_Column(), fld));
		return this;
	}

	public ArrayList<NText> getTexts() {
		return texts;
	}	
}

