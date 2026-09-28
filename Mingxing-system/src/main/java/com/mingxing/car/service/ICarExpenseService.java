package com.mingxing.car.service;

import java.util.List;
import com.mingxing.car.domain.CarExpense;

public interface ICarExpenseService {
    List<CarExpense> selectCarExpenseList(CarExpense carExpense);

    CarExpense selectCarExpenseById(Long expenseId);

    int insertCarExpense(CarExpense carExpense);

    int updateCarExpense(CarExpense carExpense);

    int deleteCarExpenseById(Long expenseId);

    int deleteCarExpenseByIds(Long[] expenseIds);
}