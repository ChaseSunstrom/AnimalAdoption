package com.animaladoption.controller;

import com.animaladoption.model.Animal;
import com.animaladoption.model.SearchCriteria;
import com.animaladoption.service.AnimalService;
import com.animaladoption.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for browsing and searching animals.
 */
@WebServlet(name = "AnimalBrowseServlet", urlPatterns = {"/animals"})
public class AnimalBrowseServlet extends HttpServlet {
    private AnimalService animalService;
    private static final int PAGE_SIZE = 12;

    @Override
    public void init() throws ServletException {
        animalService = new AnimalService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Build search criteria from request parameters
            SearchCriteria criteria = buildSearchCriteria(request);

            // Get page number
            int page = ValidationUtil.parseInt(request.getParameter("page"), 1);
            if (page < 1) page = 1;

            // Search animals
            List<Animal> animals = animalService.searchAnimals(criteria, page, PAGE_SIZE);
            int totalPages = animalService.getTotalPages(criteria, PAGE_SIZE);
            int totalCount = animalService.getSearchResultCount(criteria);

            // Set attributes for JSP
            request.setAttribute("animals", animals);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("criteria", criteria);

            // Forward to browse page
            request.getRequestDispatcher("/WEB-INF/views/animals/browse.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Build search criteria from request parameters.
     */
    private SearchCriteria buildSearchCriteria(HttpServletRequest request) {
        SearchCriteria criteria = new SearchCriteria();

        String species = ValidationUtil.sanitize(request.getParameter("species"));
        String breed = ValidationUtil.sanitize(request.getParameter("breed"));
        String size = ValidationUtil.sanitize(request.getParameter("size"));
        String color = ValidationUtil.sanitize(request.getParameter("color"));
        String gender = ValidationUtil.sanitize(request.getParameter("gender"));
        String city = ValidationUtil.sanitize(request.getParameter("city"));
        String state = ValidationUtil.sanitize(request.getParameter("state"));

        if (!ValidationUtil.isEmpty(species)) {
            criteria.setSpecies(species);
        }
        if (!ValidationUtil.isEmpty(breed)) {
            criteria.setBreed(breed);
        }
        if (!ValidationUtil.isEmpty(size)) {
            criteria.setSize(size);
        }
        if (!ValidationUtil.isEmpty(color)) {
            criteria.setColor(color);
        }
        if (!ValidationUtil.isEmpty(gender)) {
            criteria.setGender(gender);
        }
        if (!ValidationUtil.isEmpty(city)) {
            criteria.setCity(city);
        }
        if (!ValidationUtil.isEmpty(state)) {
            criteria.setState(state);
        }

        // Boolean filters
        if ("true".equals(request.getParameter("goodWithKids"))) {
            criteria.setGoodWithKids(true);
        }
        if ("true".equals(request.getParameter("goodWithDogs"))) {
            criteria.setGoodWithDogs(true);
        }
        if ("true".equals(request.getParameter("goodWithCats"))) {
            criteria.setGoodWithCats(true);
        }

        // Energy levels (can be multiple)
        String[] energyLevels = request.getParameterValues("energyLevel");
        if (energyLevels != null && energyLevels.length > 0) {
            List<String> levels = new ArrayList<>();
            for (String level : energyLevels) {
                if (!ValidationUtil.isEmpty(level)) {
                    levels.add(ValidationUtil.sanitize(level));
                }
            }
            if (!levels.isEmpty()) {
                criteria.setEnergyLevels(levels);
            }
        }

        // Age range
        int minAge = ValidationUtil.parseInt(request.getParameter("minAge"), 0);
        int maxAge = ValidationUtil.parseInt(request.getParameter("maxAge"), 0);

        if (minAge > 0) {
            criteria.setMinAgeYears(minAge);
        }
        if (maxAge > 0) {
            criteria.setMaxAgeYears(maxAge);
        }

        return criteria;
    }
}
