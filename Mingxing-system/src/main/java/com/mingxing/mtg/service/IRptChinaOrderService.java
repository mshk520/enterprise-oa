package com.mingxing.mtg.service;

import java.util.List;
import java.util.Map;
import com.mingxing.mtg.domain.RptChinaOrder;

public interface IRptChinaOrderService {
    List<RptChinaOrder> selectChinaOrderList(String yMFrom, String yMTo);

    int selectChinaOrderCount(String yMFrom, String yMTo);

    List<RptChinaOrder> selectChinaOrderPaginated(String yMFrom, String yMTo, int pageNum, int pageSize);

    List<Map<String, Object>> selectChinaOrderSummary(String yMFrom, String yMTo);
}
