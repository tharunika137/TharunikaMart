package com.tharunika.tharunikamart.controller;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import java.io.*;import javax.sql.DataSource;
@WebServlet("/api/v1/health")
public class HealthServlet extends HttpServlet {
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException{
  s.setContentType("application/json"); boolean up=false; try{((DataSource)getServletContext().getAttribute("ds")).getConnection().close();up=true;}catch(Exception ignored){}
  s.setStatus(up?200:500);s.getWriter().print(up?"{\"status\":\"UP\",\"db\":\"UP\"}":"{\"status\":\"DOWN\",\"db\":\"DOWN\"}");
 }
}
