package dao;

import dao.dto.ExpenseDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExpenseDao {

    void createExpense(ExpenseDto expenseDto);
    void getExpenses();
    ResultSet getExpenseById(int id);
    void updateExpense(int id, String newCategory, Double newAmount);
}
