import dao.ExpenseDao;
import dao.dto.ExpenseDto;
import dao.imp.ExpenseDaoImpl;
import entities.ExpenseCategory;
import exceptions.InvalidExpenseException;
import interfaces.ExpenseAmountValidator;
import interfaces.ExpenseAmountValidatorImpl;
import utilities.Utilities;

import java.util.*;

public class Main {

    public static int counter = 1;

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        ExpenseAmountValidator expenseAmountValidator = new ExpenseAmountValidatorImpl();
        ExpenseDao expenseDao = new ExpenseDaoImpl();

        double amount = 0;
        List<ExpenseDto> expenses = new ArrayList<>();
        Map<String, Integer> countCategoryMap = new HashMap<>();
        boolean done;
        boolean invalid = true;

        System.out.println("Desea ingresar un gasto? S/N");
        done = Objects.equals(scanner.nextLine().toLowerCase(), "s");

        while (done) {
            ExpenseDto expense = new ExpenseDto();
            ExpenseCategory category = new ExpenseCategory();
            invalid = true;

            while (invalid) {
                System.out.print("Ingrese el monto del gasto:");

                try {
                    amount = scanner.nextDouble();
                    if (!expenseAmountValidator.validateAmount(amount)) {
                        invalid = false;
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Formato no válido. Por favor, ingrese un número.");
                    scanner.nextLine();
                } catch (InvalidExpenseException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            }

            scanner.nextLine();
            System.out.print("Ingrese la categoria del gasto: ");
            String name = scanner.nextLine().toLowerCase().trim();
            category.setName(name);

            countCategoryMap.put(name, countCategoryMap.getOrDefault(name, 0) + 1);

            expense.setId(counter);
            expense.setAmount(amount);
            expense.setCategory(category);

            expenses.add(expense);

            expenseDao.createExpense(expense);

            counter++;

            System.out.println("Desea ingresar otro gasto? S/N");
            done = Objects.equals(scanner.nextLine().toLowerCase(), "s");
        }
//        System.out.println("Total de gastos ingresados: " + expenseCalculator.calculateTotalExpense(expenses));
//        countCategoryMap.forEach((category, count) -> System.out.println(category + " :" + count));

        System.out.println("Detalle de gastos ingresados:");
        Utilities.printElements(expenses);
    }
}