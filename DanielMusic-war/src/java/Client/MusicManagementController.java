package Client;

import EntityManager.ReturnHelper;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class MusicManagementController extends HttpServlet {

    @EJB
    private MusicManagementBeanLocal musicManagementBean;

    String nextPage = "", goodMsg = "", errMsg = "";
    HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("Welcome to client account managment controller");
        String target = request.getParameter("target");

        session = request.getSession();
        session.removeAttribute("message");
        ReturnHelper returnHelper;
        try {
            switch (target) {
                case "UploadMusic":
                    Part part = request.getPart("javafile");
                    if (part != null) {
                        String fileName = part.getSubmittedFileName();
                        String imageURL = request.getServletContext().getRealPath("/images/") + "\\" + fileName;
                        System.out.println("file name is " + fileName);
                        String s = part.getHeader("content-disposition");
                        InputStream fileInputStream = part.getInputStream();
                        OutputStream fileOutputStream = new FileOutputStream(imageURL);
                        System.out.println("writing to... " + imageURL);
                        int nextByte;
                        while ((nextByte = fileInputStream.read()) != -1) {
                            fileOutputStream.write(nextByte);
                        }
                        fileOutputStream.close();
                        fileInputStream.close();

                        //AmazonS3FileTransfer transfer = new AmazonS3FileTransfer();
                        returnHelper = musicManagementBean.uploadMusic(null);
                        //boolean result = createMusic(SKU, name, description, awsImagePath, Double.parseDouble(price), Long.parseLong(categoryId));
                        if (returnHelper.getResult()) {

                        } else {

                        }
                        File f = new File(imageURL);
                        System.out.println("deleting file... " + f.delete());
                    }
            }

        } catch (Exception ex) {
            response.sendRedirect("error500.html");
            ex.printStackTrace();
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
