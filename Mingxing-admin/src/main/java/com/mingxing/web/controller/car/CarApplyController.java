package com.mingxing.web.controller.car;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mingxing.common.annotation.Log;
import com.mingxing.common.annotation.RepeatSubmit;
import com.mingxing.common.core.controller.BaseController;
import com.mingxing.common.core.domain.AjaxResult;
import com.mingxing.common.core.page.TableDataInfo;
import com.mingxing.common.enums.BusinessType;
import com.mingxing.common.utils.poi.ExcelUtil;
import com.mingxing.common.utils.SecurityUtils;
import com.mingxing.car.domain.CarApply;
import com.mingxing.car.domain.CarVehicle;
import com.mingxing.car.domain.CarDriver;
import com.mingxing.car.service.ICarApplyService;
import com.mingxing.car.service.ICarVehicleService;
import com.mingxing.car.service.ICarDriverService;

@RestController
@RequestMapping("/car/apply")
public class CarApplyController extends BaseController {

    @Autowired
    private ICarApplyService carApplyService;

    @Autowired
    private ICarVehicleService carVehicleService;

    @Autowired
    private ICarDriverService carDriverService;

    @Autowired
    private com.mingxing.framework.websocket.NoticeMsgService noticeMsgService;

    @PreAuthorize("@ss.hasPermi('car:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarApply carApply) {
        // 非管理员只能查看本人申请
        if (!SecurityUtils.hasPermi("car:apply:audit")) {
            carApply.setCreateBy(getUsername());
        }
        carApply.setHiddenBy(SecurityUtils.getUserId());
        startPage();
        List<CarApply> list = carApplyService.selectCarApplyList(carApply);
        return getDataTable(list);
    }

    @Log(title = "用车申请", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('car:apply:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarApply carApply) {
        List<CarApply> list = carApplyService.selectCarApplyList(carApply);
        ExcelUtil<CarApply> util = new ExcelUtil<CarApply>(CarApply.class);
        util.exportExcel(response, list, "用车申请数据");
    }

    @PreAuthorize("@ss.hasPermi('car:apply:query')")
    @GetMapping(value = "/{applyId:\\d+}")
    public AjaxResult getInfo(@PathVariable Long applyId) {
        return success(carApplyService.selectCarApplyById(applyId));
    }

    @RepeatSubmit(interval = 5000)
    @PreAuthorize("@ss.hasPermi('car:apply:add')")
    @Log(title = "用车申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CarApply carApply) {
        carApply.setApplyNo(generateApplyNo());
        if (carApply.getStatus() == null) {
            carApply.setStatus("0");
        }
        carApply.setCreateBy(getUsername());
        int rows = carApplyService.insertCarApply(carApply);
        if (rows > 0) {
            try {
                String currentUser = getUsername();
                // 推送审批通知：传申请人真实 loginName 用于排除自己不弹窗，applicantName 作为展示姓名
                noticeMsgService.sendCarApplyNotice(carApply.getApplyId(),
                    currentUser,
                    carApply.getApplicantName() != null ? carApply.getApplicantName() : currentUser);
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(CarApplyController.class).warn("推送失败", e);
            }
        }
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('car:apply:edit')")
    @Log(title = "用车申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CarApply carApply) {
        if (!SecurityUtils.hasPermi("car:apply:audit")) {
            CarApply exist = carApplyService.selectCarApplyById(carApply.getApplyId());
            if (exist == null || !getUsername().equals(exist.getCreateBy())) {
                return error("只能修改自己的申请");
            }
        }
        carApply.setUpdateBy(getUsername());
        return toAjax(carApplyService.updateCarApply(carApply));
    }

    @PreAuthorize("@ss.hasPermi('car:apply:audit')")
    @Log(title = "用车申请", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody CarApply carApply) {
        CarApply exist = carApplyService.selectCarApplyById(carApply.getApplyId());
        if (exist == null) {
            return error("用车申请不存在");
        }
        if ("1".equals(exist.getStatus())) {
            return error("该申请已审批，请勿重复操作");
        }
        carApply.setAuditBy(getUsername());
        carApply.setAuditTime(new Date());
        int rows = carApplyService.updateCarApply(carApply);
        if (rows > 0) {
            try {
                noticeMsgService.sendCarApplyAuditNotice(carApply.getApplyId(),
                    exist.getApplicantName(),
                    exist.getCreateBy(),
                    carApply.getStatus(),
                    carApply.getAuditRemark());
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(CarApplyController.class).warn("审核结果推送失败", e);
            }
            // 审批通过 → 通知保安有新的可出厂车辆
            if ("1".equals(carApply.getStatus())) {
                try {
                    noticeMsgService.sendCarApplyApprovedNotice(
                        carApply.getApplyId(),
                        exist.getApplicantName(),
                        exist.getPlateNumber(),
                        exist.getDriverName());
                } catch (Exception e) {
                    org.slf4j.LoggerFactory.getLogger(CarApplyController.class).warn("保安通知推送失败", e);
                }
            }
        }
        return toAjax(rows);
    }

    /** 补签：完善紧急出厂申请信息并取消紧急标记 */
    @PreAuthorize("@ss.hasPermi('car:apply:supplement')")
    @Log(title = "用车申请-补签", businessType = BusinessType.UPDATE)
    @PutMapping("/supplement")
    public AjaxResult supplement(@RequestBody CarApply carApply) {
        CarApply exist = carApplyService.selectCarApplyById(carApply.getApplyId());
        if (exist == null) {
            return error("用车申请不存在");
        }
        if (!"1".equals(exist.getIsUrgent())) {
            return error("该申请不是紧急申请，无需补签");
        }
        carApply.setIsUrgent("0");
        carApply.setUpdateBy(getUsername());
        return toAjax(carApplyService.updateCarApply(carApply));
    }

    @PreAuthorize("@ss.hasPermi('car:apply:audit')")
    @GetMapping("/available-vehicles")
    public AjaxResult availableVehicles() {
        CarVehicle query = new CarVehicle();
        query.setStatus("0");
        return success(carVehicleService.selectVehicleList(query));
    }

    @PreAuthorize("@ss.hasPermi('car:apply:audit')")
    @GetMapping("/available-drivers")
    public AjaxResult availableDrivers() {
        CarDriver query = new CarDriver();
        query.setStatus("0");
        return success(carDriverService.selectCarDriverList(query));
    }

    @PreAuthorize("@ss.hasPermi('car:apply:list')")
    @PutMapping("/{applyId:\\d+}/hide")
    public AjaxResult hideApply(@PathVariable Long applyId) {
        return toAjax(carApplyService.updateApplyHidden(applyId, SecurityUtils.getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('car:apply:remove')")
    @Log(title = "用车申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds:[\\d,]+}")
    public AjaxResult remove(@PathVariable Long[] applyIds) {
        if (!SecurityUtils.hasPermi("car:apply:audit")) {
            for (Long applyId : applyIds) {
                CarApply exist = carApplyService.selectCarApplyById(applyId);
                if (exist == null || !getUsername().equals(exist.getCreateBy())) {
                    return error("只能删除自己的申请");
                }
            }
        }
        return toAjax(carApplyService.deleteCarApplyByIds(applyIds));
    }

    /**
     * 生成申请单号: YYMMDD + 4位随机数
     */
    private String generateApplyNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        return "YC" + date + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }
}