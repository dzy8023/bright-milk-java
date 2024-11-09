package ncu.software.service.impl;

import ncu.software.constant.AmountConstant;
import ncu.software.constant.MessageConstant;
import ncu.software.constant.StatusConstant;
import ncu.software.context.BaseContext;
import ncu.software.dto.ShoppingCartDTO;
import ncu.software.entity.Milk;
import ncu.software.entity.MilkDetail;
import ncu.software.entity.ShoppingCart;
import ncu.software.exception.AmountNotEnoughException;
import ncu.software.exception.MilkNoFoundException;
import ncu.software.mapper.MilkDetailMapper;
import ncu.software.mapper.MilkMapper;
import ncu.software.mapper.ShoppingCartMapper;
import ncu.software.service.ShoppingCartService;
import ncu.software.vo.MilkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private MilkMapper milkMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO
     */
    @Transactional
    @Override
    public BigDecimal addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Milk milk = new Milk();
        milk.setId(shoppingCartDTO.getMilkId());
        milk.setStatus(StatusConstant.ENABLE);
        if(shoppingCartDTO.getNumber()==null||shoppingCartDTO.getNumber() < AmountConstant.DEFAULT_Min){
            throw new RuntimeException("非法输入");
        }
        //先查milk是否还有库存
        MilkVO milkVO = milkMapper.getMVOByMID(milk);
        if (milkVO == null) {
            throw new MilkNoFoundException(MessageConstant.MILK_NOT_EXIST);
        }
        if (milkVO.getAmount() < shoppingCartDTO.getNumber()) {
            throw new AmountNotEnoughException(MessageConstant.AMOUNT_NOT_ENOUGH);
        }
        BigDecimal result;
        //根据userId和milkId查找购物车表是否有数据，如果有就更新数量和金额，否则就插入一条数据
        ShoppingCart shoppingCart = shoppingCartMapper.getByUIDMID(BaseContext.getCurrentId(), milkVO.getId());
        if (shoppingCart == null) {
            result=milkVO.getPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getNumber()));
            shoppingCart = ShoppingCart.builder()
                    .userId(BaseContext.getCurrentId())
                    .milkId(milkVO.getId())
                    .name(milkVO.getName())
                    .image(milkVO.getImage())
                    .number(shoppingCartDTO.getNumber())
                    .amount(result)
                    .createTime(LocalDateTime.now())
                    .build();
            shoppingCartMapper.insert(shoppingCart);
        } else {
            result=shoppingCart.getAmount().add(milkVO.getPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getNumber())));
            shoppingCart.setNumber(shoppingCart.getNumber() + shoppingCartDTO.getNumber());
            shoppingCart.setAmount(result);
            shoppingCartMapper.updateNA(shoppingCart);
        }
       return result;
    }

    /**
     * 获取购物车商品列表
     *
     * @return
     */
    @Override
    public List<ShoppingCart> getShoppingCart() {
        Long id = BaseContext.getCurrentId();
        return shoppingCartMapper.list(id);
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        Long id = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUID(id);
    }

    /**
     * 从购物车移除一个商品
     *
     * @param shoppingCartDTO
     */
    @Transactional
    @Override
    public BigDecimal subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Milk milk = new Milk();
        milk.setId(shoppingCartDTO.getMilkId());
        milk.setStatus(StatusConstant.ENABLE);
        //先查milk是否还有库存
        MilkVO milkVO = milkMapper.getMVOByMID(milk);
        if (milkVO == null) {
            throw new MilkNoFoundException(MessageConstant.MILK_NOT_EXIST);
        }
        //先判断购物车是否有该商品
        ShoppingCart shoppingCart = shoppingCartMapper.getByUIDMID(BaseContext.getCurrentId(), shoppingCartDTO.getMilkId());
        if (shoppingCart == null) {
            throw new MilkNoFoundException(MessageConstant.MILK_NOT_EXIST);
        }
        BigDecimal result;
        if (shoppingCart.getNumber() <= AmountConstant.DEFAULT_Min) {
            result=BigDecimal.ZERO;
            shoppingCartMapper.deleteByID(shoppingCart.getId());
        } else {
            result=shoppingCart.getAmount().subtract(milkVO.getPrice());
            shoppingCart.setNumber(shoppingCart.getNumber() - AmountConstant.DEFAULT_Min);
            shoppingCart.setAmount(result);
            shoppingCartMapper.updateNA(shoppingCart);
        }
        return result;
    }

    @Override
    public void delShoppingCart(Long id) {
        shoppingCartMapper.deleteByID(id);
    }

}
