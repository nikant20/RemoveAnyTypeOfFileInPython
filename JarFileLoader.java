package tech.nikant.jarloader.abc;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class JarFileLoader implements EnvironmentAware{
	
	private static String jarPath1;
	private static String jarPath2;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.jarPath1 = environment.getProperty("jar.location");
		this.jarPath2 = environment.getProperty("licensed.jar.location");
		
	}
	
    public static void loadJar(String driverName) throws Exception { 
    	URL u1 = new URL("jar:file:"+jarPath1+"!/");
    	URL u2 = new URL("jar:file:"+jarPath2+"!/");
    	String classname = driverName;
		URLClassLoader ucl = new URLClassLoader(new URL[] { u1,u2 });
		for (URL url: ucl.getURLs()) {
			System.out.println(url.getFile());
		}
		Driver d = (Driver)Class.forName(classname, true, ucl).newInstance();
		DriverManager.registerDriver(new DriverShim(d));
		
		
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
		
		try {

            connection = DriverManager.getConnection(
                      "jdbc:db2://23.229.8.213:446/LOCRGNA",
                    "mateba",
                    "mas526");

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM EMPLOYEE");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while(resultSet.next()) {
            	for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }finally {
            try {
                if(null != connection) {
                    resultSet.close();
                    statement.close();
                    connection.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
		
		
    }

	
	
}

class DriverShim implements Driver {
	private Driver driver;
	DriverShim(Driver d) {
		this.driver = d;
	}
	public boolean acceptsURL(String u) throws SQLException {
		return this.driver.acceptsURL(u);
	}
	@Override
	public Connection connect(String u, Properties p) throws SQLException {
		return this.driver.connect(u, p);
	}
	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}
	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
		return this.driver.getPropertyInfo(u, p);
	}
	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}


