package ncu.software.controller.user;

import lombok.extern.slf4j.Slf4j;
import ncu.software.constant.StatusConstant;
import ncu.software.dto.MilkPageQueryDTO;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.MilkService;
import ncu.software.vo.MilkDetailVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//为bean取别名
@RestController("userMilkController")
@RequestMapping("/user/milk")
@Slf4j
public class MilkController {
    @Autowired
    private MilkService milkService;

    /**
     * 分页查询牛奶列表
     * @param milkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult>page(MilkPageQueryDTO milkPageQueryDTO){
        //用户只能查到已启用的数据
        milkPageQueryDTO.setStatus(StatusConstant.ENABLE);
        PageResult pageResult=milkService.userPageQuery(milkPageQueryDTO);
        return Result.success(pageResult);
    }
@GetMapping("/info")
    public Result<MilkDetailVO>getMilkInfo(Long id){
        return Result.success(milkService.getMilk(id));
}



}
