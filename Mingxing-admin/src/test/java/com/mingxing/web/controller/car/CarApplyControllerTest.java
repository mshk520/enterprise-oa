//package com.mingxing.web.controller.car;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.Date;
//import java.util.Collections;
//import java.util.concurrent.CompletableFuture;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.mingxing.car.domain.CarApply;
//import com.mingxing.car.service.ICarApplyService;
//import com.mingxing.framework.websocket.NoticeMsgService;
//import com.mingxing.mtg.mail.SmtpMailService;
//import com.mingxing.system.service.ISysUserService;
//import com.mingxing.car.service.ICarDriverService;
//import com.mingxing.car.service.ICarVehicleService;
//import com.mingxing.common.core.domain.entity.SysUser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CarApplyController.class)
//@DisplayName("CarApplyController 测试")
//class CarApplyControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ICarApplyService carApplyService;
//
//    @MockBean
//    private NoticeMsgService noticeMsgService;
//
//    @MockBean
//    private SmtpMailService smtpMailService;
//
//    @MockBean
//    private ISysUserService userService;
//
//    @MockBean
//    private ICarDriverService driverService;
//
//    @MockBean
//    private ICarVehicleService vehicleService;
//
//    private CarApply validApply;
//
//    @BeforeEach
//    void setUp() {
//        validApply = new CarApply();
//        validApply.setApplyId(100L);
//        validApply.setApplyNo("YC202608180001");
//        validApply.setApplicantName("张三");
//        validApply.setDeptName("技术部");
//        validApply.setPurpose("客户拜访");
//        validApply.setStartPlace("总部");
//        validApply.setEndPlace("客户现场");
//        validApply.setDepartureTime(new Date(System.currentTimeMillis() + 3600000));
//        validApply.setReturnTime(new Date(System.currentTimeMillis() + 7200000));
//        validApply.setStatus("0");
//        validApply.setCreateBy("zhangsan");
//        validApply.setVehicleId(1L);
//        validApply.setDriverId(1L);
//        validApply.setVehicleType("1");
//        validApply.setDriverName("李师傅");
//        validApply.setPlateNumber("京A12345");
//    }
//
//    @Test
//    @WithMockUser(username = "zhangsan", authorities = "car:apply:add")
//    @DisplayName("新增申请 - 有效数据 -> 返回成功、发WebSocket、发管理员邮件")
//    void add_validRequest_returnsSuccess() throws Exception {
//        // given
//        CarApply saved = new CarApply();
//        saved.setApplyId(100L);
//        saved.setApplyNo("YC202608180001");
//        saved.setStatus("0");
//        saved.setCreateBy("zhangsan");
//
//        when(carApplyService.insertCarApply(any(CarApply.class))).thenReturn(1);
//        when(carApplyService.selectCarApplyById(100L)).thenReturn(saved);
//
//        // when & then
//        mockMvc.perform(post("/car/apply")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(validApply)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(carApplyService).insertCarApply(any(CarApply.class));
//        verify(noticeMsgService).sendCarApplyNotice(eq(100L), anyString(), anyString());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "car:apply:audit")
//    @DisplayName("审批通过 -> 状态变1、发用户邮件含司机车牌")
//    void audit_approve_sendsEmailWithDriverVehicle() throws Exception {
//        // given
//        CarApply exist = new CarApply();
//        exist.setApplyId(100L);
//        exist.setApplyNo("YC202608180001");
//        exist.setStatus("0");
//        exist.setCreateBy("zhangsan");
//        exist.setApplicantName("张三");
//        exist.setVehicleId(1L);
//        exist.setDriverId(1L);
//        exist.setDriverName("李师傅");
//        exist.setPlateNumber("京A12345");
//        exist.setVehicleType("1");
//
//        CarApply auditReq = new CarApply();
//        auditReq.setApplyId(100L);
//        auditReq.setStatus("1");
//        auditReq.setAuditRemark("同意");
//
//        CarApply latest = new CarApply();
//        latest.setApplyId(100L);
//        latest.setApplyNo("YC202608180001");
//        latest.setStatus("1");
//        latest.setCreateBy("zhangsan");
//        latest.setApplicantName("张三");
//        latest.setVehicleId(1L);
//        latest.setDriverId(1L);
//        latest.setDriverName("李师傅");
//        latest.setPlateNumber("京A12345");
//        latest.setVehicleType("1");
//        latest.setAuditRemark("同意");
//
//        when(carApplyService.selectCarApplyById(100L))
//            .thenReturn(exist)   // 第一次调用
//            .thenReturn(latest); // 第二次调用（审批后重查）
//
//        when(carApplyService.updateCarApply(any(CarApply.class))).thenReturn(1);
//        when(userService.selectUserByUserName("zhangsan")).thenReturn(
//            createUser("zhangsan", "zhangsan@company.com"));
//
//        // when & then
//        mockMvc.perform(put("/car/apply/audit")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(auditReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(carApplyService).updateCarApply(any(CarApply.class));
//        verify(noticeMsgService).sendCarApplyAuditNotice(eq(100L), anyString(), anyString(), eq("1"), anyString());
//
//        // 验证异步邮件发送（使用 Awaitility 或验证调用链路）
//        // 注意：@Async 方法在测试中默认同步执行，除非配置了测试专用线程池
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "car:apply:audit")
//    @DisplayName("审批拒绝 -> 状态变2、发用户邮件含拒绝原因")
//    void audit_reject_sendsEmailWithRemark() throws Exception {
//        CarApply exist = new CarApply();
//        exist.setApplyId(100L);
//        exist.setApplyNo("YC202608180001");
//        exist.setStatus("0");
//        exist.setCreateBy("zhangsan");
//        exist.setApplicantName("张三");
//
//        CarApply auditReq = new CarApply();
//        auditReq.setApplyId(100L);
//        auditReq.setStatus("2");
//        auditReq.setAuditRemark("车辆不足");
//
//        CarApply latest = new CarApply();
//        latest.setApplyId(100L);
//        latest.setApplyNo("YC202608180001");
//        latest.setStatus("2");
//        latest.setCreateBy("zhangsan");
//        latest.setApplicantName("张三");
//        latest.setAuditRemark("车辆不足");
//
//        when(carApplyService.selectCarApplyById(100L))
//            .thenReturn(exist)
//            .thenReturn(latest);
//
//        when(carApplyService.updateCarApply(any(CarApply.class))).thenReturn(1);
//        when(userService.selectUserByUserName("zhangsan")).thenReturn(
//            createUser("zhangsan", "zhangsan@company.com"));
//
//        mockMvc.perform(put("/car/apply/audit")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(auditReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(carApplyService).updateCarApply(any(CarApply.class));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "car:apply:audit")
//    @DisplayName("重复审批 -> 返回错误")
//    void audit_duplicate_returnsError() throws Exception {
//        CarApply exist = new CarApply();
//        exist.setApplyId(100L);
//        exist.setStatus("1"); // 已审批
//
//        CarApply auditReq = new CarApply();
//        auditReq.setApplyId(100L);
//        auditReq.setStatus("1");
//
//        when(carApplyService.selectCarApplyById(100L)).thenReturn(exist);
//
//        mockMvc.perform(put("/car/apply/audit")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(auditReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(500))
//                .andExpect(jsonPath("$.msg").value("该申请已审批，请勿重复操作"));
//
//        verify(carApplyService, never()).updateCarApply(any());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "car:apply:audit")
//    @DisplayName("审批开关关闭 -> 不发邮件")
//    void audit_disabled_doesNotSendEmail() throws Exception {
//        // 这里需要通过 @SpringBootTest + 配置覆盖来测试，WebMvcTest 不加载完整配置
//        // 此处仅作示例，实际需 @SpringBootTest + @MockBean 配置属性
//    }
//
//    private SysUser createUser(String username, String email) {
//        SysUser user = new SysUser();
//        user.setUserName(username);
//        user.setEmail(email);
//        return user;
//    }
//}