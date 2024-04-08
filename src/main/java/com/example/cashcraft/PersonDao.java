package com.example.cashcraft;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class PersonDao {

    public static void addCategory(PersonClasses.Category category) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into category values(null, ?, ?)");
            preparedStatement.setString(1, category.name);
            preparedStatement.setString(2, category.desc);
            preparedStatement.executeUpdate();
            //connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static  boolean isCategoryExist(String name) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category where category.category_name = ?");
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addTransaction(PersonClasses.Expense transaction) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into expense values(?,?,?,?,?,?)");

            // Convert date to SQL date
            java.sql.Date sqlDate = new java.sql.Date(transaction.date.getTime());
            preparedStatement.setDate(1, sqlDate);

            // Insert category UUID
            preparedStatement.setString(2, transaction.category.uuid);

            preparedStatement.setString(3, transaction.desc);
            preparedStatement.setDouble(4, transaction.amount);

            // Insert people UUID
            preparedStatement.setString(5, transaction.people.uuid);

            // Insert wallet UUID
            preparedStatement.setString(6, transaction.wallet.uuid);

            preparedStatement.executeUpdate();
            connection.commit(); // Commit changes
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPeople(PersonClasses.People people) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into people values(null,?,?)");
            preparedStatement.setString(1, people.name);
            preparedStatement.setString(2, people.desc);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPeopleExist(String name) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from people where people.people_name = ?");
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public  static void addPlace(PersonClasses.Place place) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into place values(null,?,?)");
            preparedStatement.setString(1, place.name);
            preparedStatement.setString(2, place.desc);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPlaceExist(String name) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from place where place.place_name = ?");
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addWallet(PersonClasses.Wallet wallet) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into wallet values(null,?,?)");
            preparedStatement.setString(1, wallet.name);
            preparedStatement.setString(2, wallet.desc);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isWalletExist(String name) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from wallet where wallet.wallet_name = ?");
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editTransaction(PersonClasses.Income_and_expense_String transaction, Connection connection, String id, String type) {
        try {
            if (type.equals("Income")) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Income SET amount=?, people=?, place=?, category=?, notes=?, desc=?, date=?, wallet=? WHERE income_id=?");

                preparedStatement.setFloat(1, Float.parseFloat(transaction.amount));

                if (transaction.people_id != null) {
                    preparedStatement.setObject(2, UUID.fromString(transaction.people_id));
                } else {
                    preparedStatement.setObject(2, null); // Set the column to NULL
                }

                if (transaction.place_id != null) {
                    preparedStatement.setObject(3, UUID.fromString(transaction.place_id));
                } else {
                    preparedStatement.setObject(3, null); // Set the column to NULL
                }

                if (transaction.category_id != null) {
                    preparedStatement.setObject(4, UUID.fromString(transaction.category_id));
                } else {
                    preparedStatement.setObject(4, null); // Set the column to NULL
                }

                preparedStatement.setString(5, transaction.note);
                preparedStatement.setString(6, transaction.desc);
                preparedStatement.setDate(7, Date.valueOf(transaction.date));

                if (transaction.main_wallet_id != null) {
                    preparedStatement.setObject(8, UUID.fromString(transaction.main_wallet_id));
                } else {
                    preparedStatement.setObject(8, null); // Set the column to NULL
                }

                preparedStatement.setObject(9, UUID.fromString(id));

                preparedStatement.executeUpdate();
                System.out.println("Success income update!");
                //System.out.println(Date.valueOf(transaction.date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }
    }
}
