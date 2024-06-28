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
@RequestMapping("/categories")
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
    public List<Category> getAll() {
        // find and return all categories

            return categoryDao.getAllCategories();
        }



    @GetMapping("/{id}")
    public Category getById(@PathVariable int id) {

            Category category = categoryDao.getById(id);


            if (category == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
            }

            return category;
        }





        @PostMapping()
        @ResponseStatus(value = HttpStatus.CREATED)
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
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public void updateCategory(@PathVariable int id, @RequestBody Category category){
                categoryDao.update(id, category);



        }


        // add annotation to call this method for a DELETE action - the url path must include the categoryId
        // add annotation to ensure that only an ADMIN can call this function
        @DeleteMapping("{id}")
        @ResponseStatus(value = HttpStatus.NO_CONTENT)
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

