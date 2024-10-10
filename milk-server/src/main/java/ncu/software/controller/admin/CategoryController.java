package ncu.software.controller.admin;

import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.CategoryDTO;
import ncu.software.dto.CategoryPageQueryDTO;
import ncu.software.entity.Category;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("categoryPageQueryDTO:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或禁用分类
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(Long id, @PathVariable Integer status) {
        categoryService.startOrStop(id, status);
        return Result.success();
    }


    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 根据分类类型查询
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<ArrayList<Category>> queryByType(Integer type){
        return Result.success(categoryService.queryByType(type));
    }


}
