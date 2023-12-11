import entities.Expense;
import entities.ExpenseCategory;
import exceptions.InvalidExpenseException;
import interfaces.ExpenseAmountValidator;
import interfaces.ExpenseAmountValidatorImpl;
import interfaces.ExpenseCalculator;
import interfaces.ExpenseCalculatorImpl;
import utilities.Utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.*;

public class Main {

    public static int counter = 1;

    public static void main(String[] args) throws InvalidExpenseException {

        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/gastos", "mbarca", "123456");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        Scanner scanner = new Scanner(System.in);
        double amount;
        boolean isWrongType = false;

        ExpenseAmountValidator expenseAmountValidator = new ExpenseAmountValidatorImpl();

//        do {
//            System.out.print("Ingrese la cantidad de gastos a registrar: ");
//            if (scanner.hasNextInt()){
//                cantGastosAIngresar = scanner.nextInt();
//            } else {
//                System.out.println("Dato erroneo");
//            }
//        }while (isWrongType);

        List<Expense> expenses = new ArrayList<>();
        ExpenseCalculator expenseCalculator = new ExpenseCalculatorImpl();

        Map<String, Integer> countCategoryMap = new HashMap<>();

        boolean done;
        System.out.println("Desea ingresar un gasto? S/N");
        done = Objects.equals(scanner.nextLine().toLowerCase(), "s");

        while (done){
            Expense expense = new Expense();
            ExpenseCategory category = new ExpenseCategory();
            System.out.print("Ingrese el monto del gasto:");
            amount = scanner.nextDouble();

            if(expenseAmountValidator.validateAmount(amount)){
                System.out.println("El monto no es valido");
            }

            scanner.nextLine();
            System.out.print("Ingrese la categoria del gasto: ");
            String name = scanner.nextLine().toLowerCase().trim();
            category.setName(name);
            System.out.print("Ingrese la fecha del gasto: ");
            String date = scanner.nextLine();

            countCategoryMap.put(name, countCategoryMap.getOrDefault(name, 0) +1);

            expense.setId(counter);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDate(date);

            expenses.add(expense);

            counter++;

            System.out.println("Desea ingresar otro gasto? S/N");
            done = Objects.equals(scanner.nextLine().toLowerCase(), "s");
        };
        System.out.println("Total de gastos ingresados: " + expenseCalculator.calculateTotalExpense(expenses));
        countCategoryMap.forEach((category, count) -> System.out.println(category + " :" + count));

        System.out.println("Detalle de gastos ingresados:");
        Utilities.printElements(expenses);
    }
}