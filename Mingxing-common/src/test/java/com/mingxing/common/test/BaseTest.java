package com.mingxing.common.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 测试基类：统一测试配置
 * - 使用 test profile
 * - 关闭定时任务、异步线程池等副作用
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {
    // 子类继承即可获得统一配置
}