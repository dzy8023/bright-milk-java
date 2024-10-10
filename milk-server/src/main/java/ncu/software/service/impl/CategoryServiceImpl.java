package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ncu.software.constant.StatusConstant;
import ncu.software.dto.CategoryDTO;
import ncu.software.dto.CategoryPageQueryDTO;
import ncu.software.entity.Category;
import ncu.software.exception.BaseException;
import ncu.software.mapper.CategoryMapper;
import ncu.software.mapper.MilkMapper;
import ncu.software.result.PageResult;
import ncu.software.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private MilkMapper milkMapper;

    /**
     * 添加分类
     *
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insert(category);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category>page=categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 启用禁用分类
     *
     * @param id
     * @param status
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.update(category);
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        //查看该分类下是否有牛奶
        Integer count = milkMapper.countByMilkId(id);
        if(count>0){
            throw new BaseException("该分类下有牛奶，无法删除");
        }
        categoryMapper.delete(id);
    }

    /**
     * 根据分类类型查询
     *
     * @param type
     * @return
     */
    @Override
    public ArrayList<Category> queryByType(Integer type) {
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setType(type);
        return categoryMapper.pageQuery(categoryPageQueryDTO);
    }
}
