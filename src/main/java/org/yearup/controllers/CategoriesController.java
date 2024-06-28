package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping
@CrossOrigin

public class CategoriesController {
    private final CategoryDao categoryDao;
    private final ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    // add the appropriate annotation for a get action
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {

        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        // find and return all categories
        try {
            return categoryDao.getAllCategories();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }

    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id) {
        try {
            Category category = categoryDao.getById(id);


            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return category; //get the category by id
        }
            catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        }




        @PostMapping()
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public Category addCategory(@RequestBody Category category) {

            try {
                return categoryDao.create(category);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        }


        // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
        // add annotation to ensure that only an ADMIN can call this function
        @PutMapping("{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public void updateCategory(@PathVariable int id, @RequestBody Category category){
            try {
                categoryDao.update(id, category);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

            }
        }


        // add annotation to call this method for a DELETE action - the url path must include the categoryId
        // add annotation to ensure that only an ADMIN can call this function
        @DeleteMapping("{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public void deleteCategory (@PathVariable int id) {
            try {
                Category category = categoryDao.getById(id);

                if (category == null)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);

                categoryDao.delete(id);

            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }

        }

    }

