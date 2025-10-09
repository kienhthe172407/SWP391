package servlet;

import com.google.gson.Gson;
import dao.TrainingCourseDAO;
import model.TrainingCourse;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/training-courses/*")
public class TrainingCourseApiServlet extends HttpServlet {
    private TrainingCourseDAO dao;
    private Gson gson;

    @Override
    public void init() {
        dao = new TrainingCourseDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<TrainingCourse> list = dao.getAllCourses();
            res.getWriter().print(gson.toJson(list));
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            TrainingCourse c = dao.getCourseById(id);
            res.getWriter().print(gson.toJson(c));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader = req.getReader();
        TrainingCourse course = gson.fromJson(reader, TrainingCourse.class);
        dao.insertCourse(course);
        res.setContentType("application/json");
        res.getWriter().print("{\"message\": \"Created\"}");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BufferedReader reader = req.getReader();
        TrainingCourse course = gson.fromJson(reader, TrainingCourse.class);
        dao.updateCourse(course);
        res.setContentType("application/json");
        res.getWriter().print("{\"message\": \"Updated\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            dao.deleteCourse(id);
        }
        res.setContentType("application/json");
        res.getWriter().print("{\"message\": \"Deleted\"}");
    }
}
