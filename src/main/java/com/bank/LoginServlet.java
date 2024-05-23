package com.bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con;

    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "root");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Oracle JDBC Driver not found. Include it in your library path.", e);
        } catch (SQLException e) {
            throw new ServletException("Unable to connect to the database. Check your connection URL, username, and password.", e);
        }
    }


	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw;
		try {
	        pw = response.getWriter();
	        PreparedStatement pst = con.prepareStatement("select * from admin_user where uname = ? and  pass = ?");

	        String userid = request.getParameter("userId");
	        pst.setString(1, userid);
	        
	        String pass = request.getParameter("password");
	        pst.setString(2, pass);
	        
	        ResultSet rs = pst.executeQuery();

	        // Check if any row is returned by the query
	        if(rs.next()) {
	            // If a row is returned, user exists, forward to dashboard
	          response.sendRedirect("Dashboard/home.html");
	            
	        } else {
	            // If no row is returned, user doesn't exist, set error message attribute
	            pw = response.getWriter();
	            pw.println("<font color=yellow> Invalid Username/Password </font>");
	            // Forward to the login page
	            RequestDispatcher rd = request.getRequestDispatcher("/login.html");
	            rd.include(request, response);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}




	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}
