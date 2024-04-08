package com.example.cashcraft;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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
    public static void editCategory(PersonClasses.Category oldcategory,PersonClasses.Category newcategory) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update category set category_name = ?, category_desc = ? where category_name = ?");
            preparedStatement.setString(1, newcategory.name);
            preparedStatement.setString(2, newcategory.desc);
            preparedStatement.setString(3, oldcategory.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonClasses.Category> getCategories() {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category");
            return PersonClasses.Category.fromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteCategory(PersonClasses.Category selectedCategory) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from category where category_name = ?");
            preparedStatement.setString(1, selectedCategory.name);
            preparedStatement.executeUpdate();
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

    public static List<PersonClasses.People> getPeople() {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from people");
            return PersonClasses.People.fromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editPerson(PersonClasses.People selectedPerson, PersonClasses.People newPerson) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update people set people_name = ?, people_desc = ? where people_name = ?");
            preparedStatement.setString(1, newPerson.name);
            preparedStatement.setString(2, newPerson.desc);
            preparedStatement.setString(3, selectedPerson.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePerson(PersonClasses.People selectedPerson) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from people where people_name = ?");
            preparedStatement.setString(1, selectedPerson.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonClasses.Place> getPlaces() {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from place");
            return PersonClasses.Place.fromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editPlace(PersonClasses.Place selectedPlace, PersonClasses.Place newPlace) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update place set place_name = ?, place_desc = ? where place_name = ?");
            preparedStatement.setString(1, newPlace.name);
            preparedStatement.setString(2, newPlace.desc);
            preparedStatement.setString(3, selectedPlace.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePlace(PersonClasses.Place selectedPlace) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from place where place_name = ?");
            preparedStatement.setString(1, selectedPlace.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonClasses.Wallet> getWallets() {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from wallet");
            return PersonClasses.Wallet.fromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editWallet(PersonClasses.Wallet selectedWallet, PersonClasses.Wallet newWallet) {
        try (Connection connection = Makeconnection.makeconnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update wallet set wallet_name = ?, wallet_desc = ? where wallet_name = ?");
            preparedStatement.setString(1, newWallet.name);
            preparedStatement.setString(2, newWallet.desc);
            preparedStatement.setString(3, selectedWallet.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWallet(PersonClasses.Wallet selectedWallet) {
        try (Connection connection = Makeconnection.makeconnection()) {
            connection.setAutoCommit(false);


            String updateIncomeQuery = "UPDATE Income SET wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = 'Common wallet') WHERE wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = ?)";
            String updateExpenseQuery = "UPDATE expense SET wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = 'Common wallet') WHERE wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = ?)";
            String updateTransferQuery = "UPDATE transfer SET from_wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = 'Common wallet') WHERE from_wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = ?)";
            String updateTransferQuery2 = "UPDATE transfer SET to_wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = 'Common wallet') WHERE to_wallet = (SELECT wallet_id FROM wallet WHERE wallet_name = ?)";

            try (PreparedStatement updateIncomeStatement = connection.prepareStatement(updateIncomeQuery);
                 PreparedStatement updateExpenseStatement = connection.prepareStatement(updateExpenseQuery);
                 PreparedStatement updateTransferStatement = connection.prepareStatement(updateTransferQuery);
                 PreparedStatement updateTransferStatement2 = connection.prepareStatement(updateTransferQuery2)) {

                updateIncomeStatement.setString(1, selectedWallet.name);
                updateIncomeStatement.executeUpdate();

                updateExpenseStatement.setString(1, selectedWallet.name);
                updateExpenseStatement.executeUpdate();

                updateTransferStatement.setString(1, selectedWallet.name);
                updateTransferStatement.executeUpdate();

                updateTransferStatement2.setString(1, selectedWallet.name);
                updateTransferStatement2.executeUpdate();


                String deleteWalletQuery = "DELETE FROM wallet WHERE wallet_name = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteWalletQuery)) {
                    deleteStatement.setString(1, selectedWallet.name);
                    deleteStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error deleting wallet", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }




}
