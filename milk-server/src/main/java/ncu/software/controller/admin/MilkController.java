package ncu.software.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import ncu.software.dto.MilkDTO;
import ncu.software.dto.MilkPageQueryDTO;
import ncu.software.entity.Pack;
import ncu.software.result.PageResult;
import ncu.software.result.Result;
import ncu.software.service.MilkService;
import ncu.software.vo.MilkDetailVO;
import ncu.software.vo.MilkDisDetailVO;
import ncu.software.vo.MilkSaleDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/milk")
@Slf4j
public class MilkController {
    @Autowired
    private MilkService milkService;

    /**
     * 添加分类
     *
     * @param milkDTO
     * @return
     */
    @PostMapping
    public Result<String> addMilk(@RequestBody MilkDTO milkDTO) {
        milkService.addMilk(milkDTO);
        return Result.success();
    }

    /**
     * 修改牛奶
     *
     * @param milkDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody MilkDTO milkDTO) {
        milkService.update(milkDTO);
        return Result.success();
    }

    /**
     * 牛奶分页查询
     *
     * @param milkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(MilkPageQueryDTO milkPageQueryDTO) {
        log.info("milkPageQueryDTO:{}", milkPageQueryDTO);
        PageResult pageResult = milkService.pageQuery(milkPageQueryDTO);
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
        log.info("id:{},status:{}", id, status);
        milkService.startOrStop(id, status);
        return Result.success();
    }

    /**
     * 进货
     *
     * @param id
     * @param amount
     * @return
     */
    @PostMapping("/addAmount/{amount}")
    public Result<String> addAmount(Long id, @PathVariable Integer amount) {
        log.info("id:{},status:{}", id, amount);
        milkService.addAmount(id, amount);
        return Result.success();
    }

    /**
     * 删除牛奶
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        milkService.delete(id);
        return Result.success();
    }


    @GetMapping
    public Result<MilkDetailVO> getMilk(Long id) {
        MilkDetailVO milkDetailVO = milkService.getMilk(id);
        return Result.success(milkDetailVO);
    }

    @GetMapping("/pack")
    public Result<ArrayList<Pack>> listPack() {
        return Result.success(milkService.listPack());
    }

    @GetMapping("/milkDisDetail/{id}")
    public Result<MilkDisDetailVO> getMilkDisDetail(@PathVariable Long id) {
        MilkDisDetailVO milkDisDetailVO = milkService.getMilkDisDetail(id);
        return Result.success(milkDisDetailVO);
    }
    @GetMapping("/saleData/{id}")
    public Result<List<HashMap<String, Integer>>> getMilkSaleData(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                                  @PathVariable Long id) {
        List<HashMap<String, Integer>> data = milkService.getMilkSaleData(begin, end, id);
        return Result.success(data);
    }
    @GetMapping("/saleDataOfDay/{date}")
public Result<List<MilkSaleDataVO>> getMilkSaleDataOfDay(@PathVariable String  date) {
        LocalDate day = LocalDate.parse(date);
        LocalDateTime beginDateTime = LocalDateTime.of(day, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(day, LocalTime.MAX);
        return Result.success(milkService.getMilksSaleData(beginDateTime, endDateTime));
    }
    @GetMapping("/allMilkName")
    public Result<List<String>> getAllMilkName() {
        List<String> milkNames = milkService.getAllMilkName();
        return Result.success(milkNames);
    }
}
