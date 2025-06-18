package HA.Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import HA.TestAutomation.Driver;
import HA.TestAutomation.HATF_properties;
import HA.TestAutomation.Component.Common;
import HA.data.TwoDimensionHashMap;

public class DB {

	public static HATF_properties _properties = new HATF_properties();
	static Connection conn = null;
	static Statement stmt = null;
	static ResultSet rs= null;
	static ResultSetMetaData rsmd = null;

	public static void conDB() throws ClassNotFoundException, SQLException{
		try{
			String db_connect_string=_properties.getProperty(HATF_properties.CONN_STR);
			Class.forName(_properties.getProperty(HATF_properties.SQLJAVADriver));
			conn = DriverManager.getConnection(db_connect_string);
			HA.TestAutomation.Driver.getLogger().info("DB Connection Success");
			stmt = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}

	public static void conDB(String datafile, String dataset) throws ClassNotFoundException, SQLException{
		try{
			String db_connect_string=Common.Getxml(datafile, dataset, "DBConStr");

			Class.forName(_properties.getProperty(HATF_properties.SQLJAVADriver));
			conn = DriverManager.getConnection(db_connect_string);
			HA.TestAutomation.Driver.getLogger().info("DB Connection Success");
			stmt = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}

	public static String QueryDB(String queryString) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			conDB();
			//queryString = "Select PARAM_VAL from HOST_BPM_CONSTANTS WHERE PARAM_KEY='Horizon_DataLoad'";
			rs = stmt.executeQuery(queryString);
			rsmd = rs.getMetaData();
			// int dbcols = rsmd.getColumnCount();


			while (rs.next()) {

				Driver.getLogger().info(rs.getString(1));    		

				Horizon = rs.getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
		return Horizon;

	}

	public static String getDBdata(String query) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			conDB();

			rs = stmt.executeQuery(query);
			rsmd = rs.getMetaData();
			// int dbcols = rsmd.getColumnCount();

			while (rs.next()) {	     	

				Horizon = rs.getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
		return Horizon;

	}

	public static String queryDBData(String query) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			rs = stmt.executeQuery(query);
			rsmd = rs.getMetaData();
			// int dbcols = rsmd.getColumnCount();

			while (rs.next()) {	     	

				Horizon = rs.getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
		return Horizon;

	}

	public static void CompareDBdata() throws Exception{

		FileOutputStream  file = null;
		try{

			conDB();
			//String queryString = "Select * from HOST_BPM_CONSTANTS WHERE PARAM_KEY='Horizon_DataLoad'";
			//String queryString = "Select * from HOST_BPM_CONSTANTS";
			String queryString = "Select * from S_APP_VERSION";
			rs = stmt.executeQuery(queryString);
			rsmd = rs.getMetaData();
			int dbcols = rsmd.getColumnCount();
			String strDate = HTML.GetDateTime();

			String dbFilename = "Execution_Report_" + strDate+ ".xlsx";
			//String dbFilename = "Execution_Report.xlsx";
			String dbtargetpath = System.getProperty("user.dir") + "\\test-output\\"+ dbFilename;
			//file = new FileOutputStream(new File(dbtargetpath));   
			XSSFWorkbook wb = new XSSFWorkbook();

			XSSFSheet sheet = wb.createSheet("DBQueryOutput");

			//XSSFSheet sheet = wb.createSheet(); 
			//wb.setSheetName(0, "DBQueryOutput");
			Row row = sheet.createRow(0);

			for(int i = 1;i<=dbcols;i++){
				Driver.getLogger().info(rsmd.getColumnName(i));

				Cell cell = row.createCell(i-1);
				cell.setCellValue(rsmd.getColumnName(i));
				Driver.getLogger().info(i);
			}

			int rowcount=1;
			while (rs.next()) {
				row =sheet.createRow(rowcount);
				for(int i = 1;i<=dbcols;i++){					 
					Cell cell = row.createCell(i-1);
					cell.setCellValue(rs.getString(i));					 
				}
				rowcount++;			           
			}
			file = new FileOutputStream(new File(dbtargetpath));

			wb.write(file);
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{

			file.close();
			rs.close();                       // Close the ResultSet                  
			stmt.close();
			// file.close();// Close the Statement
		}


	}

	public static void exeDB(String queryString) throws ClassNotFoundException, SQLException{
		try{
			conDB();			
			int rs  =stmt.executeUpdate(queryString);
			Driver.getLogger().info(rs);
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			//			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
	}

	public static void exeQuery(String queryString) throws ClassNotFoundException, SQLException{
		try{
			int rs  =stmt.executeUpdate(queryString);
			Driver.getLogger().info(rs);
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			//			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
	}

	public static void exeDBmultiline(String[] query) throws ClassNotFoundException, SQLException{
		try{
			for(String queryString: query)
			{
				int rs  =stmt.executeUpdate(queryString);
				Driver.getLogger().info(rs);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			//rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
	}


	public static TwoDimensionHashMap executeQuery(String queryString) throws SQLException{
		ResultSet rs  = null;
		System.out.println("");
		TwoDimensionHashMap twoDimension = new TwoDimensionHashMap();
		try{
			conDB();			
			rs  = stmt.executeQuery(queryString);
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();			
			if(rs == null){
				Verifications.fail("Query-> returned with null values ::  " +queryString);
			}
			ArrayList<String>  columnNames = new ArrayList<String>();
			for(int i=1;i<=columnCount;i++){
				columnNames.add(rsmd.getColumnName(i));
			}
			int coount = 0;
			while(rs.next()){				
				LinkedHashMap<String,String> eachRow = new LinkedHashMap<String,String> ();
				for(String label:columnNames){
					eachRow.put(label, rs.getString(label));
				}
				twoDimension.addRow(eachRow);
			}

			Driver.getLogger().info(rs);
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			//			rs.close();                       // Close the ResultSet                  
			stmt.close();   				   // Close the Statement
		}
		return twoDimension;
	}

	public static void closeAll()
	{
		try {
			//rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.getLogger().error(e);
		}

	}

	public static void runQuery(String datafile, String dataset,String[] query) throws ClassNotFoundException, SQLException
	{
		DB.conDB(datafile, dataset);
		DB.exeDBmultiline(query);
		Driver.getLogger().info("Query------------"+query);
		DB.closeAll();
	}

	public static String getQueriedData(String datafile, String dataset,String query) throws ClassNotFoundException, SQLException
	{
		DB.conDB(datafile, dataset);
		String row=DB.queryDBData(query);
		Driver.getLogger().info("Query------------"+query);
		DB.closeAll();
		return row;
	}

	public static void conDB(String connString) throws ClassNotFoundException, SQLException{
		try{
			Class.forName(_properties.getProperty(HATF_properties.SQLJAVADriver));
			conn = DriverManager.getConnection(connString);
			HA.TestAutomation.Driver.getLogger().info("DB Connection Success");
			stmt = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}



}
