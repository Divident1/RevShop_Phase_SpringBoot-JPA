package com.revshop.service;

import com.revshop.dao.CategoryDAO;
import com.revshop.model.Category;
import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
}
