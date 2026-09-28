package com.mingxing.car.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mingxing.car.domain.CarExpense;
import com.mingxing.car.mapper.CarExpenseMapper;
import com.mingxing.car.service.ICarExpenseService;

@Service
public class CarExpenseServiceImpl implements ICarExpenseService {

    @Autowired
    private CarExpenseMapper carExpenseMapper;

    @Override
    public List<CarExpense> selectCarExpenseList(CarExpense carExpense) {
        return carExpenseMapper.selectCarExpenseList(carExpense);
    }

    @Override
    public CarExpense selectCarExpenseById(Long expenseId) {
        return carExpenseMapper.selectCarExpenseById(expenseId);
    }

    @Override
    public int insertCarExpense(CarExpense carExpense) {
        return carExpenseMapper.insertCarExpense(carExpense);
    }

    @Override
    public int updateCarExpense(CarExpense carExpense) {
        return carExpenseMapper.updateCarExpense(carExpense);
    }

    @Override
    public int deleteCarExpenseById(Long expenseId) {
        return carExpenseMapper.deleteCarExpenseById(expenseId);
    }

    @Override
    public int deleteCarExpenseByIds(Long[] expenseIds) {
        return carExpenseMapper.deleteCarExpenseByIds(expenseIds);
    }
}