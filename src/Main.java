import config.JdbcConfig;
import dao.ExpenseDao;
import dao.dto.ExpenseDto;
import dao.imp.ExpenseDaoImpl;
import entities.ExpenseCategory;
import exceptions.InvalidExpenseException;
import interfaces.ExpenseAmountValidator;
import interfaces.ExpenseAmountValidatorImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try(Connection connection = JdbcConfig.getDBConnection();) {

            ExpenseAmountValidator expenseAmountValidator = new ExpenseAmountValidatorImpl();
            ExpenseDao expenseDao = new ExpenseDaoImpl(connection);

            int option;
            double amount = 0;
            boolean done = false;
            boolean exit = false;
            boolean invalid;


            while(!exit){
                System.out.println("Bienvenido! ingrese una opción: ");
                System.out.println("1 - Ingresar gasto.");
                System.out.println("2 - Consultar gastos.");
                System.out.println("3 - Modificar gasto.");
                System.out.println("4 - Eliminar gasto.");
                System.out.println("5 - Salir.");

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

                            expense.setAmount(amount);
                            expense.setCategory(category);

                            expenseDao.createExpense(expense);

                            System.out.println("Desea ingresar otro gasto? S/N");
                            done = !Objects.equals(scanner.nextLine().toLowerCase(), "s");
                        }
                        done = false;
                        break;
                    case 2:
                        try{
                            expenseDao.getExpenses();
                        }catch (RuntimeException e){
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        String newCategory;
                        double newAmount;
                        System.out.println("Ingrese el ID del gasto a modificar: ");
                        try{
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            ResultSet rs = expenseDao.getExpenseById(id);
                            if (rs.next()) {
                                System.out.println("Ingrese la nueva categoria. (Valor alcutual: " + rs.getString("category") + ")");
                                newCategory = scanner.nextLine();
                                if (Objects.equals(newCategory, "")) newCategory = rs.getString("category");
                                System.out.println("Ingrese el nuevo monto. (Valor alcutual: " + rs.getString("amount") + ")");
                                String aux = scanner.nextLine();
                                if (aux.isEmpty()) newAmount = rs.getDouble("amount");
                                else {
                                    try {
                                        newAmount = Double.parseDouble(aux);
                                    }catch (NumberFormatException e){
                                        System.out.println("Número no válido. No se modificará el monto");
                                        newAmount = rs.getDouble("amount");
                                    }
                                }

                                expenseDao.updateExpense(id,newCategory,newAmount);
                                rs.close();
                            } else {
                                System.out.println("No se encontraron resultados para el ID: " + id);
                            }
                        }catch (SQLException e){
                            System.out.println("Error: " + e.getMessage());
                        }catch (java.util.InputMismatchException e) {
                            System.out.println("Formato no válido. Por favor, ingrese un número.");
                            scanner.nextLine();
                        }
                        break;
                    case 4:
                        System.out.println("Ingrese el ID del gasto a eliminar: ");
                        try{
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            expenseDao.deleteExpense(id);
                        }catch (RuntimeException e){
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


                System.out.println("Gracias, vuelva prontos!");
        }
    }