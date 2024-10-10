package ncu.software.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ncu.software.constant.AmountConstant;
import ncu.software.constant.MessageConstant;
import ncu.software.constant.StatusConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.MilkDTO;
import ncu.software.dto.MilkPageQueryDTO;
import ncu.software.entity.Milk;
import ncu.software.entity.MilkDetail;
import ncu.software.entity.Pack;
import ncu.software.exception.PackNoFundException;
import ncu.software.mapper.MilkDetailMapper;
import ncu.software.mapper.MilkMapper;
import ncu.software.mapper.OrderDetailMapper;
import ncu.software.mapper.OrderMapper;
import ncu.software.result.PageResult;
import ncu.software.service.MilkService;
import ncu.software.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class MilkServiceImpl implements MilkService {
    @Autowired
    private MilkMapper milkMapper;
    @Autowired
    private MilkDetailMapper milkDetailMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 添加牛奶
     *
     * @param milkDTO
     */
    @Override
    @Transactional
    public void addMilk(MilkDTO milkDTO) {
        Milk milk = new Milk();
        BeanUtils.copyProperties(milkDTO, milk);
        milk.setStatus(StatusConstant.DISABLE);
        //首先向milk插入一条数据
        milkMapper.insert(milk);
        //向milkDetail插入一条数据
        Pack pack=milkMapper.getPackByPId(milkDTO.getPackId());
        if(pack==null){
            throw new PackNoFundException(MessageConstant.PACK_NOT_EXIST);
        }
        MilkDetail milkDetail=new MilkDetail();
        BeanUtils.copyProperties(milkDTO, milkDetail);
        milkDetail.setMilkId(milk.getId());
        milkDetail.setAmount(AmountConstant.DEFAULT_INT);
        milkDetailMapper.insert(milkDetail);
    }

    /**
     * 修改牛奶
     *
     * @param milkDTO
     */
    @Transactional
    @Override
    public void update(MilkDTO milkDTO) {
        Milk milk = new Milk();
        BeanUtils.copyProperties(milkDTO, milk);
        milkMapper.update(milk);
        //向milkDetail插入一条数据
        Pack pack=milkMapper.getPackByPId(milkDTO.getPackId());
        if(pack==null){
            throw new PackNoFundException(MessageConstant.PACK_NOT_EXIST);
        }
        MilkDetail milkDetail=new MilkDetail();
        BeanUtils.copyProperties(milkDTO, milkDetail);
        milkDetail.setMilkId(milk.getId());
        milkDetailMapper.updateByMID(milkDetail);
    }

    /**
     * 分页查询
     *
     * @param milkPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(MilkPageQueryDTO milkPageQueryDTO) {
        PageHelper.startPage(milkPageQueryDTO.getPage(), milkPageQueryDTO.getPageSize());
        Page<MilkVO>page=milkMapper.pageQuery(milkPageQueryDTO);
        log.info("page:{}",page.getResult());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 启用禁用牛奶
     *
     * @param id
     * @param status
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        Milk milk = new Milk();
        milk.setId(id);
        milk.setStatus(status);
        milkMapper.update(milk);
    }

    /**
     * 删除牛奶
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        //删除牛奶
        milkMapper.delete(id);
        //删除牛奶详细信息
        milkDetailMapper.deleteByMId(id);
    }

    /**
     * 根据id获取牛奶详细信息
     * @param id
     */
    @Override
    public MilkDetailVO getMilk(Long id) {
      return  milkMapper.getById(id);
    }

    @Override
    public ArrayList<Pack> listPack() {
        return milkMapper.listPack();
    }

    /**
     * 添加牛奶数量
     * @param id
     * @param amount
     */
    @Override
    public void addAmount(Long id, Integer amount) {
        milkDetailMapper.addAmountByMID(id,amount,LocalDateTime.now(),BaseContext.getCurrentId());
    }

    @Override
    public PageResult userPageQuery(MilkPageQueryDTO milkPageQueryDTO) {
        PageHelper.startPage(milkPageQueryDTO.getPage(), milkPageQueryDTO.getPageSize());
        Page<UserMilkVO>page=milkMapper.userPageQuery(milkPageQueryDTO);
        log.info("page:{}",page.getResult());
        return new PageResult(page.getTotal(),page.getResult());
    }
    @Override
    public MilkDisDetailVO getMilkDisDetail(Long id) {
        return milkMapper.getMilkDisDetail(id);
    }

    @Override
    public List<HashMap<String, Integer>> getMilkSaleData(LocalDate begin, LocalDate end, Long id) {
        LocalDateTime beginDateTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);
        return  orderMapper.getMilkSaleData(beginDateTime, endDateTime, id);
    }

    @Override
    public List<MilkSaleDataVO> getMilksSaleData(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return orderMapper.getMilksSaleData(beginDateTime, endDateTime);
    }
}
