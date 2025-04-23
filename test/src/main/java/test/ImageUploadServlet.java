package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class ImageUploadServlet
 */
@WebServlet("/ImageUploadServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,   // 1 MB
        maxFileSize = 1024 * 1024 * 10,        // 10 MB
        maxRequestSize = 1024 * 1024 * 100     // 100 MB
)
public class ImageUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageUploadServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // 获取上传文件
            Part filePart = request.getPart("edit"); // 对应前端form.name配置的字段名
            String fileName = getFileName(filePart);

            // 生成唯一文件名
            String uniqueName = UUID.randomUUID().toString() + "_" + fileName;

            // 保存路径配置（示例路径）
            String uploadPath = getServletContext().getRealPath("/uploads");
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 保存文件
            Path filePath = uploadDir.resolve(uniqueName);
            filePart.write(filePath.toString());

            // 构造返回JSON
            JsonObject json = new JsonObject();
            json.addProperty("code", 0);
            json.addProperty("msg", "上传成功");
            json.addProperty("data", request.getContextPath() + "/uploads/" + uniqueName);

            out.print(json.toString());
            System.out.println("Image uploaded successfully: " + json.toString());
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("code", 1);
            error.addProperty("msg", "上传失败，请稍后重试");
            response.getWriter().print(error.toString());
            System.err.println("Image upload failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}