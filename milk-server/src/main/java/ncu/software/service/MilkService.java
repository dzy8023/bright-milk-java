package ncu.software.service;

import ncu.software.dto.MilkPageQueryDTO;
import ncu.software.dto.MilkDTO;
import ncu.software.entity.Pack;
import ncu.software.result.PageResult;
import ncu.software.vo.MilkDetailVO;
import ncu.software.vo.MilkDisDetailVO;
import ncu.software.vo.MilkSaleDataVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MilkService {
    /**
     * 添加牛奶
     *
     * @param milkDTO
     */
    void addMilk(MilkDTO milkDTO);

    /**
     * 修改分类
     *
     * @param milkDTO
     */
    void update(MilkDTO milkDTO);

    /**
     * 分页查询
     *
     * @param milkPageQueryDTO
     * @return
     */
    PageResult pageQuery(MilkPageQueryDTO milkPageQueryDTO);

    /**
     * 启用禁用分类
     *
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    /**
     * 删除分类
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 根据id获取牛奶详细信息
     *
     * @param id
     */

    MilkDetailVO getMilk(Long id);

    /**
     * 获取包装信息
     *
     * @return
     */
    ArrayList<Pack> listPack();

    /**
     * 进货
     *
     * @param id
     * @param amount
     */
    void addAmount(Long id, Integer amount);

    /**
     * 用户
     * @param milkPageQueryDTO
     * @return
     */
    PageResult userPageQuery(MilkPageQueryDTO milkPageQueryDTO);

    MilkDisDetailVO getMilkDisDetail(Long id);

    List<HashMap<String, Integer>> getMilkSaleData(LocalDate begin, LocalDate end, Long id);

    List<MilkSaleDataVO> getMilksSaleData(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    List<String> getAllMilkName();
}
