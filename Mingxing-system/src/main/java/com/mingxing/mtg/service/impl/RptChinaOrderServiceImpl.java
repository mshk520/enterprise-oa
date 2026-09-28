package com.mingxing.mtg.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.common.annotation.DataSource;
import com.mingxing.common.enums.DataSourceType;
import com.mingxing.mtg.domain.RptChinaOrder;
import com.mingxing.mtg.mapper.RptChinaOrderMapper;
import com.mingxing.mtg.service.IRptChinaOrderService;

@Service
public class RptChinaOrderServiceImpl implements IRptChinaOrderService {

    @Autowired
    private RptChinaOrderMapper chinaOrderMapper;

    @Override
    @DataSource(DataSourceType.SQLSERVER)
    public List<RptChinaOrder> selectChinaOrderList(String yMFrom, String yMTo) {
        return chinaOrderMapper.selectChinaOrderList(yMFrom, yMTo);
    }

    @Override
    @DataSource(DataSourceType.SQLSERVER)
    public int selectChinaOrderCount(String yMFrom, String yMTo) {
        return chinaOrderMapper.selectChinaOrderCount(yMFrom, yMTo);
    }

    @Override
    @DataSource(DataSourceType.SQLSERVER)
    public List<RptChinaOrder> selectChinaOrderPaginated(String yMFrom, String yMTo, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return chinaOrderMapper.selectChinaOrderPaginated(yMFrom, yMTo, offset, pageSize);
    }

    @Override
    @DataSource(DataSourceType.SQLSERVER)
    public List<Map<String, Object>> selectChinaOrderSummary(String yMFrom, String yMTo) {
        return chinaOrderMapper.selectChinaOrderSummary(yMFrom, yMTo);
    }
}
