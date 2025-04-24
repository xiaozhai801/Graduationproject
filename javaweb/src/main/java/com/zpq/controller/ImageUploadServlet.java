package com.zpq.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.google.gson.JsonObject;

import java.io.File;

/**
 * Servlet implementation class imgServlet
 */
@WebServlet("/ImageUploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class ImageUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ImageUploadServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "unknown";
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
	        Part filePart = request.getPart("edit");
	        String fileName = getFileName(filePart);
	        String uniqueName = UUID.randomUUID().toString() + "_" + fileName;

	        // 直接使用ServletContext的真实路径，不进行路径截断
	        String uploadPath = getServletContext().getRealPath("/img/Article");
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs(); // 创建目录，包括不存在的父目录
	        }

	        // 保存文件到部署目录
	        String filePath = uploadPath + File.separator + uniqueName;
	        filePart.write(filePath);

	        // 构造返回URL
	        String fileUrl = request.getContextPath() + "/img/Article/" + uniqueName;

	        JsonObject json = new JsonObject();
	        json.addProperty("code", 0);
	        json.addProperty("msg", "上传成功");
	        json.addProperty("data", fileUrl);
	        out.print(json.toString());
		} catch (Exception e) {
			JsonObject error = new JsonObject();
			error.addProperty("code", 1);
			error.addProperty("msg", "上传失败: " + e.getMessage());
			out.print(error.toString());
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}
