package ncu.software.service;

import ncu.software.dto.CategoryDTO;
import ncu.software.dto.CategoryPageQueryDTO;
import ncu.software.entity.Category;
import ncu.software.result.PageResult;

import java.util.ArrayList;

public interface CategoryService {
    /**
     * 添加分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);

    /**
     * 根据分类类型查询
     * @param type
     * @return
     */
    ArrayList<Category> queryByType(Integer type);
}
