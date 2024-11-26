import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.IOException;
import java.util.List;

@MultipartConfig
public class FileUpload extends HttpServlet {
    private static final String[] ALLOWED_EXTENSIONS = {"txt", "jpg", "png"};

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            if (ServletFileUpload.isMultipartContent(request)) {
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                List<FileItem> formItems = upload.parseRequest(request);

                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                        if (!isAllowedExtension(fileExtension)) {
                            response.getWriter().println("Unsupported file type: " + fileExtension);
                            return;
                        }

                        String filePath = uploadPath + File.separator + fileName;
                        item.write(new File(filePath));

                        response.getWriter().println("File uploaded successfully. Download URL: /files/" + fileName);
                        return;
                    }
                }
            } else {
                response.getWriter().println("Invalid form submission.");
            }
        } catch (Exception ex) {
            response.getWriter().println("Error: " + ex.getMessage());
        }
    }

    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
