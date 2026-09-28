package com.mingxing.car.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mingxing.car.domain.CarApply;

public interface CarApplyMapper {

    public List<CarApply> selectCarApplyList(CarApply carApply);

    public CarApply selectCarApplyById(Long applyId);

    public int insertCarApply(CarApply carApply);

    public int updateCarApply(CarApply carApply);

    public int deleteCarApplyById(Long applyId);

    public int deleteCarApplyByIds(Long[] applyIds);

    public int updateApplyHidden(@Param("applyId") Long applyId, @Param("hiddenBy") Long hiddenBy);
}