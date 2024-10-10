package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.constant.StatusConstant;
import ncu.software.dto.CategoryPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        categoryPageQueryDTO.setStatus(StatusConstant.ENABLE);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

}
