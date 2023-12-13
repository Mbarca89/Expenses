package dao.imp;

import config.JdbcConfig;
import dao.ExpenseDao;
import dao.dto.ExpenseDto;
import entities.Expense;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseDaoImpl implements ExpenseDao {

    public ExpenseDaoImpl() {
        this.connection = JdbcConfig.getDBConnection();
    }

    private final Connection connection;
    @Override
    public void createExpense(ExpenseDto expenseDto) {
        try {

            Expense newExpense = new Expense();
            newExpense.setAmount(expenseDto.getAmount());
            newExpense.setCategory(expenseDto.getCategory());

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO expense (amount, category) VALUES (?, ?)");
            preparedStatement.setDouble(1, expenseDto.getAmount());
            preparedStatement.setString(2, expenseDto.getCategory().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getExpenses(){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM expense");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                System.out.println("Gasto Nº " + rs.getInt("id"));
                System.out.println(("Monto: " + rs.getDouble("amount")));
                System.out.println("Categoria: " + rs.getString("category"));
                System.out.println("Fecha: " + rs.getString("date_added"));
                System.out.println("-----------------------------------------------------");
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}