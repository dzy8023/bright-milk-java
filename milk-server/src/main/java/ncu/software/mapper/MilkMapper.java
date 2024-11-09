package ncu.software.mapper;

import com.github.pagehelper.Page;
import ncu.software.annotation.AutoFill;
import ncu.software.dto.MilkPageQueryDTO;
import ncu.software.entity.Milk;
import ncu.software.entity.Pack;
import ncu.software.enumeration.OperationType;
import ncu.software.vo.MilkDetailVO;
import ncu.software.vo.MilkDisDetailVO;
import ncu.software.vo.MilkVO;
import ncu.software.vo.UserMilkVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MilkMapper {
    /**
     * 添加牛奶
     *
     * @param milk
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Milk milk);

    /**
     * 修改牛奶
     *
     * @param milk
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Milk milk);

    /**
     * 分页查询
     *
     * @param milkPageQueryDTO
     * @return
     */
    Page<MilkVO> pageQuery(MilkPageQueryDTO milkPageQueryDTO);

    /**
     * 根据packId获取包装
     *
     * @param packId
     * @return
     */
    Pack getPackByPId(Integer packId);


    /**
     * 根据id删除牛奶
     *
     * @param id
     */
    @Delete("delete from milk where id = #{id}")
    void delete(Long id);

    /**
     * 获取牛奶详细信息
     *
     * @param milk
     * @return
     */

    MilkVO getMVOByMID(Milk milk);

    /**
     * 根据id获取牛奶详细信息
     *
     * @param id
     * @return
     */
    @Select("select m.id,m.name,m.category_id,c.type,m.status,md.price,md.image,md.standard,md.pack_id,md.description,md.amount " +
            "from category as c,milk as m left join milk_detail as md on m.id=md.milk_id " +
            "where m.id = #{id} and c.id = m.category_id")
    MilkDetailVO getById(Long id);

    /**
     * 获取包装列表
     * @return
     */
    @Select("select * from pack")
    ArrayList<Pack> listPack();
    Page<UserMilkVO> userPageQuery(MilkPageQueryDTO milkPageQueryDTO);

    /**
     * 根据分类id统计牛奶数量
     * @param id
     * @return
     */
    @Select("select count(*) from milk where category_id = #{id}")
    Integer countByMilkId(Long id);

    MilkDisDetailVO getMilkDisDetail(Long id);
    @Select("select distinct name as value from milk")
    List<String> getAllMilkName();
}
