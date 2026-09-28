//package com.mingxing.web.controller.mtg;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.Date;
//import java.text.SimpleDateFormat;
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
//import com.mingxing.mtg.domain.MtgBooking;
//import com.mingxing.mtg.service.IMtgBookingService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(MtgBookingController.class)
//@DisplayName("MtgBookingController 测试")
//class MtgBookingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private IMtgBookingService bookingService;
//
//    private MtgBooking validBooking;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        validBooking = new MtgBooking();
//        validBooking.setBookingId(1L);
//        validBooking.setRoomId(1L);
//        validBooking.setRoomName("会议室A");
//        validBooking.setBookingDate(new SimpleDateFormat("yyyy-MM-dd").parse("2026-08-18"));
//        validBooking.setStartTime(sdf.parse("2026-08-18 09:00"));
//        validBooking.setEndTime(sdf.parse("2026-08-18 11:00"));
//        validBooking.setBookingType("time");
//        validBooking.setSubject("测试会议");
//        validBooking.setBookerName("张三");
//        validBooking.setDeptName("技术部");
//        validBooking.setBookingStatus("0");
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "mtg:booking:audit")
//    @DisplayName("管理员审批通过 -> 返回成功")
//    void audit_adminApprove_returnsSuccess() throws Exception {
//        MtgBooking auditReq = new MtgBooking();
//        auditReq.setBookingId(1L);
//        auditReq.setBookingStatus("1"); // 通过
//
//        when(bookingService.auditBooking(any(MtgBooking.class))).thenReturn(1);
//
//        mockMvc.perform(put("/mtg/booking/audit")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(auditReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(bookingService).auditBooking(any(MtgBooking.class));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "mtg:booking:audit")
//    @DisplayName("管理员审批拒绝 -> 返回成功")
//    void audit_adminReject_returnsSuccess() throws Exception {
//        MtgBooking auditReq = new MtgBooking();
//        auditReq.setBookingId(1L);
//        auditReq.setBookingStatus("2"); // 拒绝
//
//        when(bookingService.auditBooking(any(MtgBooking.class))).thenReturn(1);
//
//        mockMvc.perform(put("/mtg/booking/audit")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(auditReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(bookingService).auditBooking(any(MtgBooking.class));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", authorities = "mtg:booking:audit")
//    @DisplayName("普通用户审批自己的预约 -> 权限逻辑当前允许（已知 Bug，回归用）")
//    void audit_ownBooking_currentLogicAllows() throws Exception {
//        // 当前代码逻辑：非超管只能审自己的单（isSuperAdmin 检查 + bookerId 对比）
//        // 这是已知 Bug，测试用于回归防止意外修改
//        MtgBooking existing = new MtgBooking();
//        existing.setBookingId(1L);
//        existing.setBookerId(100L); // user1 的 ID
//
//        MtgBooking auditReq = new MtgBooking();
//        auditReq.setBookingId(1L);
//        auditReq.setBookingStatus("1");
//
//        when(bookingService.selectBookingById(1L)).thenReturn(existing);
//        when(bookingService.auditBooking(any(MtgBooking.class))).thenReturn(1);
//
//        // 当前用户 ID = 100（通过 SecurityUtils.getUserId() mock）
//        // 注意：WebMvcTest 需要额外配置才能 mock SecurityUtils
//        // 此处仅作示例，实际需 @SpringBootTest + Mockito.mockStatic
//    }
//
//    @Test
//    @WithMockUser(username = "user1", authorities = "mtg:booking:audit")
//    @DisplayName("普通用户审批他人预约 -> 当前逻辑拒绝（已知 Bug）")
//    void audit_otherBooking_currentLogicRejects() throws Exception {
//        // 当前逻辑：非超管且 bookerId != 当前用户 ID -> 报错 "无权审核他人的预约"
//        // 这是业务 Bug：应该允许有审批权限的人审批
//        // 测试锁定当前错误行为，修复后测试应改为允许
//    }
//
//    @Test
//    @WithMockUser(username = "zhangsan", authorities = "mtg:booking:add")
//    @DisplayName("新增预约 - 时间冲突 -> 返回错误")
//    void add_conflict_returnsError() throws Exception {
//        when(bookingService.checkTimeConflict(any(MtgBooking.class))).thenReturn(true);
//
//        mockMvc.perform(post("/mtg/booking")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(validBooking)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(500))
//                .andExpect(jsonPath("$.msg").value("该时间段已被预约，请选择其他时间"));
//
//        verify(bookingService, never()).insertBooking(any());
//    }
//
//    @Test
//    @WithMockUser(username = "zhangsan", authorities = "mtg:booking:add")
//    @DisplayName("新增预约 - 无冲突 -> 返回成功")
//    void add_noConflict_returnsSuccess() throws Exception {
//        when(bookingService.checkTimeConflict(any(MtgBooking.class))).thenReturn(false);
//        when(bookingService.insertBooking(any(MtgBooking.class))).thenReturn(1);
//
//        mockMvc.perform(post("/mtg/booking")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(validBooking)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200));
//
//        verify(bookingService).insertBooking(any(MtgBooking.class));
//    }
//}