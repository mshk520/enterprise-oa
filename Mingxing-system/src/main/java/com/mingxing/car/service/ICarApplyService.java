package com.mingxing.car.service;

import java.util.List;
import com.mingxing.car.domain.CarApply;

public interface ICarApplyService {

    public List<CarApply> selectCarApplyList(CarApply carApply);

    public CarApply selectCarApplyById(Long applyId);

    public int insertCarApply(CarApply carApply);

    public int updateCarApply(CarApply carApply);

    public int deleteCarApplyById(Long applyId);

    public int deleteCarApplyByIds(Long[] applyIds);

    public int updateApplyHidden(Long applyId, Long hiddenBy);
}