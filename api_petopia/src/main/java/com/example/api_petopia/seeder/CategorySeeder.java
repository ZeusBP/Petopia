package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySeeder {

    @Autowired
    CategoryRepository categoryRepository;

    public static List<Category> categoryList= new ArrayList<>();

    public void generate(){
        Category category1 = new Category(1,"Food pet", "https://i0.wp.com/post.healthline.com/wp-content/uploads/2020/06/dog-food-1296x728-header.jpg?w=1155&h=1528");
        Category category2 = new Category(2,"Toys for pets", "https://cdn.thewirecutter.com/wp-content/media/2021/12/dog-toys-2048px-0004.jpg");
        Category category3 = new Category(3,"Pet tools", "https://i.ebayimg.com/images/g/~OAAAOSw0HthQqi1/s-l400.jpg");
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);
        categoryRepository.saveAll(categoryList);
    }
}
