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

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ExpenseAmountValidator expenseAmountValidator = new ExpenseAmountValidatorImpl();
        ExpenseDao expenseDao = new ExpenseDaoImpl();

        int option = 0;
        double amount = 0;
        List<ExpenseDto> expenses = new ArrayList<>();
        Map<String, Integer> countCategoryMap = new HashMap<>();
        boolean done = false;
        boolean exit = false;
        boolean invalid = true;


        while(!exit){
            System.out.println("Bienvenido! ingrese una opción: ");
            System.out.println("1 - Ingresar gasto.");
            System.out.println("2 - Consultar gastos.");
            System.out.println("3 - Salir.");

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Opción no válida. ingrese una opción: ");
                scanner.nextLine();
                option = scanner.nextInt();
            }

            switch (option) {
                case 1:
                   while (!done) {
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
                    break;
                case 2:
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. ingrese una opción: ");
                    try {
                        scanner.nextInt();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Opción no válida. ingrese una opción: ");
                        scanner.nextLine();
                        option = scanner.nextInt();
                    }
            }
        }

                System.out.println("Gracias, vuelva prontos!");
                Utilities.printElements(expenses);
        }
    }