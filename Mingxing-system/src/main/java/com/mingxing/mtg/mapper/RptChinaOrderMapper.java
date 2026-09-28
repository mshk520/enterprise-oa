package com.mingxing.mtg.mapper;

import java.util.List;
import java.util.Map;
import com.mingxing.mtg.domain.RptChinaOrder;
import org.apache.ibatis.annotations.Param;

public interface RptChinaOrderMapper {
    List<RptChinaOrder> selectChinaOrderList(@Param("yMFrom") String yMFrom, @Param("yMTo") String yMTo);

    int selectChinaOrderCount(@Param("yMFrom") String yMFrom, @Param("yMTo") String yMTo);

    List<RptChinaOrder> selectChinaOrderPaginated(@Param("yMFrom") String yMFrom, @Param("yMTo") String yMTo, @Param("offset") int offset, @Param("pageSize") int pageSize);

    List<Map<String, Object>> selectChinaOrderSummary(@Param("yMFrom") String yMFrom, @Param("yMTo") String yMTo);
}
