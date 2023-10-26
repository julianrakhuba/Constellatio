/*******************************************************************************
 * /*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *  *******************************************************************************/
 *******************************************************************************/
package connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import application.Constellatio;
import clients.Meta;
import clients.MySqlMeta;
import generic.BaseConnection;
import login.Login;

public class MysqlConn extends BaseConnection {
	private Meta meta;

	public MysqlConn(Login lgin, Constellatio napp) {
		super (lgin, napp);
	}
	
	public void connectToDB() {		
		
		try {
			con = DriverManager.getConnection(login.getUrl(), login.getUsername(), login.getPassword());
			Statement stmt  = con.createStatement();
	        login.getStatements().forEach(st ->{ try { stmt.execute(st); } catch (SQLException e) { e.printStackTrace(); }});
	        stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 1045) {
				System.out.println("WRONG DB LOGIN • " + e.getErrorCode() + " " +  e.getLocalizedMessage() + "    con:  " + con);
			}
		}
	}

	public String end() {
		return ";";
	}

	public Meta getClientMetaData() {
		if(meta == null) meta = new MySqlMeta(getJDBC());
		return  meta;
	}
}
