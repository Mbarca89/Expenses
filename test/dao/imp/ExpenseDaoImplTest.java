package dao.imp;

import dao.ExpenseDao;
import dao.dto.ExpenseDto;
import entities.Expense;
import entities.ExpenseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseDaoImplTest {

    private final Connection connectionMock = mock(Connection.class);
    private PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    private ExpenseDao expenseDao;
    private final ExpenseCategory category = new ExpenseCategory();

    @BeforeEach
    void setUp() throws SQLException {
        category.setName("test");
        expenseDao = new ExpenseDaoImpl(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    }

    @Test
    void inter_ShouldInsertExpense_WhenValidExpense() throws SQLException {
        //GIVEN
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(1000.00);
        expenseDto.setCategory(category);

        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        //WHEN
        expenseDao.createExpense(expenseDto);

        //THEN

        verify(preparedStatementMock).setDouble(1, expenseDto.getAmount());
    }

}