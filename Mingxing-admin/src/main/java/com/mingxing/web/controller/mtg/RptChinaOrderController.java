package com.mingxing.web.controller.mtg;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.core.page.TableDataInfo;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.common.utils.poi.ExcelUtil;
import com.mingxing.mtg.domain.RptChinaOrder;
import com.mingxing.mtg.service.IRptChinaOrderService;

@RestController
@RequestMapping("/mtg/report")
public class RptChinaOrderController extends BaseController {

    @Autowired
    private IRptChinaOrderService chinaOrderService;

    @PreAuthorize("@ss.hasPermi('mtg:report:list')")
    @GetMapping("/chinaOrder")
    public TableDataInfo list(@RequestParam String yMFrom, @RequestParam String yMTo,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        int total = chinaOrderService.selectChinaOrderCount(yMFrom, yMTo);
        List<RptChinaOrder> list = chinaOrderService.selectChinaOrderPaginated(yMFrom, yMTo, pageNum, pageSize);
        return new TableDataInfo(list, total);
    }

    @PreAuthorize("@ss.hasPermi('mtg:report:list')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam String yMFrom, @RequestParam String yMTo) {
        List<Map<String, Object>> list = chinaOrderService.selectChinaOrderSummary(yMFrom, yMTo);
        return success(list);
    }

    @Log(title = "中國營業接單數", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('mtg:report:export')")
    @PostMapping("/chinaOrder/export")
    public void export(HttpServletResponse response, @RequestParam String yMFrom, @RequestParam String yMTo) {
        List<RptChinaOrder> list = chinaOrderService.selectChinaOrderList(yMFrom, yMTo);
        ExcelUtil<RptChinaOrder> util = new ExcelUtil<>(RptChinaOrder.class);
        util.exportExcel(response, list, "中國營業接單數");
    }
}
