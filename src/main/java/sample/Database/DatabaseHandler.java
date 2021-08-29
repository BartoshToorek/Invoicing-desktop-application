package sample.Database;


import sample.AlertBox;
import sample.model.*;


import java.sql.*;

import static sample.controller.LoginController.userColumn;


public class DatabaseHandler extends Configs {
    Connection dbConnection;
    private Object Exception;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
                +"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public boolean DUPLICATE = false;
    public String generatedColumns[] = { "ID" };

    //REJESTRACJA
    public void signUpUser(Account account){
        String insert = "INSERT INTO " + Const.ACCOUNT_TABLE + "(" + Const.ACCOUNT_LOGIN + "," + Const.ACCOUNT_PASSWORD
                + "," + Const.ACCOUNT_FIRSTNAME + "," + Const.ACCOUNT_LASTNAME + "," + Const.ACCOUNT_BANKNUMBER + ")"
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, account.getLogin());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getAccount_firstname());
            preparedStatement.setString(4, account.getAccount_lastname());
            preparedStatement.setString(5, account.getAccount_banknumber());

            preparedStatement.executeUpdate();
            AlertBox.display("Udana rejestracja", "Teraz możesz sie zalogować");
        } catch(SQLIntegrityConstraintViolationException e){
            //e.printStackTrace();
            AlertBox.display("Takie konto juz istnieje", "Login, hasło oraz numer konta bankowego muszą być unikalne");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    //ACCOUNT METHODS /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ResultSet getAccount(Account account){
        ResultSet resultSet = null;

        String search = "SELECT * FROM "+Const.ACCOUNT_TABLE+" WHERE "+Const.ACCOUNT_LOGIN+"=?"+" AND "
                +Const.ACCOUNT_PASSWORD+"=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, account.getLogin());
            preparedStatement.setString(2, account.getPassword());
            resultSet =  preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet currentUser(Account account){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.ACCOUNT_ID+" FROM "+Const.ACCOUNT_TABLE+" WHERE "+Const.ACCOUNT_LOGIN+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, account.getLogin());
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    //PRODUCT METHODS /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void AddProduct(Product product) {
        String insert = "INSERT INTO " + Const.PRODUCT_TABLE + "(" + Const.PRODUCT_NAME + "," + Const.PRODUCT_NETTO + ","
                + Const.PRODUCT_VAT + ")" + "VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setFloat(2, product.getProduct_netto());
            preparedStatement.setFloat(3, product.getProduct_vat());

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getProduct(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.PRODUCT_TABLE+"."+Const.PRODUCT_NAME+","+Const.PRODUCT_TABLE+"."+Const.PRODUCT_NETTO+","
                +Const.PRODUCT_TABLE+"."+Const.PRODUCT_VAT+","+Const.PRODUCT_TABLE+"."+Const.PRODUCT_ID+
                " FROM "+ Const.PRODUCT_TABLE+
                " INNER JOIN "+ Const.ACCOUNT_HAS_PRODUCT_TABLE+
                " ON "+ Const.PRODUCT_TABLE+"."+Const.PRODUCT_ID+" = "+Const.ACCOUNT_HAS_PRODUCT_TABLE+"."+Const.FK_PRODUCT_ID +
                " WHERE "+Const.ACCOUNT_HAS_PRODUCT_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public void deleteProduct(Integer productID){
        String delete = "DELETE FROM "+Const.PRODUCT_TABLE+" WHERE "+Const.PRODUCT_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product, Integer productID){
        String update = "UPDATE "+Const.PRODUCT_TABLE+
                " SET "+Const.PRODUCT_NAME+"=?"+","+Const.PRODUCT_NETTO+"=?"+","+Const.PRODUCT_VAT+"=?"+
                " WHERE "+Const.PRODUCT_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setFloat(2, product.getProduct_netto());
            preparedStatement.setFloat(3, product.getProduct_vat());
            preparedStatement.setInt(4, productID);
            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void addAccountHasProduct(Product product){
        String insert = "INSERT INTO "+ Const.ACCOUNT_HAS_PRODUCT_TABLE+"("+Const.FK_PRODUCT_ID+","+Const.FK_ACCOUNT_ID+")"+
                " SELECT "+Const.PRODUCT_ID+","+Const.ACCOUNT_ID+
                " FROM "+Const.PRODUCT_TABLE+","+Const.ACCOUNT_TABLE+
                " WHERE "+Const.PRODUCT_NAME+"=?"+" AND "+Const.ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    //FIRMDATA METHODS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addFirmData(Firmdata firmdata){
        String insert = "INSERT INTO " + Const.FIRMDATA_TABLE + "("+Const.FIRMDATA_NIP+","+Const.FIRMDATA_NAME+","
                +Const.FIRMDATA_PHONE_NUMBER+","+Const.FIRMDATA_EMAIL+")"+"VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, firmdata.getFirmdata_nip());
            preparedStatement.setString(2, firmdata.getFirmdata_name());
            preparedStatement.setString(3, firmdata.getFirmdata_phonenr());
            preparedStatement.setString(4, firmdata.getFirmdata_email());

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch(SQLIntegrityConstraintViolationException e) {
            AlertBox.display("Takie dane juz istnieja", "NIP, Nazwa firmy, numer telefonu i email musza byc unikalne");
            DUPLICATE = true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getFirmData(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_NIP+","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_NAME+","
                +Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_PHONE_NUMBER+","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_EMAIL+
                " FROM "+ Const.FIRMDATA_TABLE+
                " INNER JOIN "+ Const.ACCOUNT_HAS_FIRMDATA_TABLE+
                " ON "+ Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID +
                " WHERE "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet =  preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public void addAccountHasFirmdata(Firmdata firmdata){
        String insert = "INSERT INTO "+ Const.ACCOUNT_HAS_FIRMDATA_TABLE+"("+Const.FK_FIRMDATA_ID+","+Const.FK_ACCOUNT_ID+")"+
                " SELECT "+Const.FIRMDATA_ID+","+Const.ACCOUNT_ID+
                " FROM "+Const.FIRMDATA_TABLE+","+Const.ACCOUNT_TABLE+
                " WHERE "+Const.FIRMDATA_NIP+"=?"+" AND "+Const.ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, firmdata.getFirmdata_nip());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void deleteAccountHasFirmdata(){
        String delete = "DELETE "+Const.FIRMDATA_TABLE+ " FROM "+Const.FIRMDATA_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+
                " ON "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID+
                " WHERE "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void deleteContractorHasFirmData(Integer firmdataID){
        String delete = "DELETE FROM "+Const.FIRMDATA_TABLE+" WHERE "+Const.FIRMDATA_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.setInt(1, firmdataID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateAccountHasFirmdata(Firmdata firmdata){
        String update = "UPDATE "+Const.FIRMDATA_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+
                " ON "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID+
                " SET "+Const.FIRMDATA_NIP+"=?"+","+Const.FIRMDATA_NAME+"=?"+","+Const.FIRMDATA_PHONE_NUMBER+"=?"+","+Const.FIRMDATA_EMAIL+"=?"+
                " WHERE "+Const.ACCOUNT_HAS_FIRMDATA_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, firmdata.getFirmdata_nip());
            preparedStatement.setString(2, firmdata.getFirmdata_name());
            preparedStatement.setString(3, firmdata.getFirmdata_phonenr());
            preparedStatement.setString(4, firmdata.getFirmdata_email());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateContractorHasFirmdata(Firmdata firmdata){
        String update = "UPDATE "+Const.FIRMDATA_TABLE+
                " SET "+Const.FIRMDATA_NIP+"=?"+","+Const.FIRMDATA_NAME+"=?"+","+Const.FIRMDATA_PHONE_NUMBER+"=?"+","+Const.FIRMDATA_EMAIL+"=?"+
                " WHERE "+Const.FIRMDATA_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, firmdata.getFirmdata_nip());
            preparedStatement.setString(2, firmdata.getFirmdata_name());
            preparedStatement.setString(3, firmdata.getFirmdata_phonenr());
            preparedStatement.setString(4, firmdata.getFirmdata_email());
            preparedStatement.setInt(5, firmdata.getFirmdata_ID());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            AlertBox.display("Takie dane juz istnieja", "NIP, Nazwa firmy, numer telefonu i email musza byc unikalne");
            DUPLICATE = true;
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void addContractorHasFirmdata(Firmdata firmdata, Contractor contractor){
        String insert = "INSERT INTO "+ Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"("+Const.FK_CONTRACTOR_ID+","+Const.FK_FIRMDATA_ID+")"+
                " SELECT "+Const.CONTRACTOR_ID+","+Const.FIRMDATA_ID+
                " FROM "+Const.CONTRACTOR_TABLE+","+Const.FIRMDATA_TABLE+
                " WHERE "+Const.CONTRACTOR_FIRSTNAME+"=?"+" AND "+Const.CONTRACTOR_LASTNAME+"=?"+" AND "+Const.FIRMDATA_NIP+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, contractor.getContractor_firstname());
            preparedStatement.setString(2, contractor.getContractor_lastname());
            preparedStatement.setString(3, firmdata.getFirmdata_nip());
            preparedStatement.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ResultSet getContractorHasFirmdata(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_NIP+","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_NAME+
                ","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_PHONE_NUMBER+ ","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_EMAIL+
                ","+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+
                " FROM "+Const.FIRMDATA_TABLE+
                " INNER JOIN "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+
                " ON "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID+
                " INNER JOIN "+Const.CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " INNER JOIN "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " WHERE "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        return resultSet;
    }

    //LOCATION METHODS ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addLocation(Location location){
        String insert = "INSERT INTO "+Const.LOCATION_TABLE+"("+Const.LOCATION_CITY+","+Const.LOCATION_STREET+","+
                Const.LOCATION_POSTALCODE+")"+"VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, location.getLocation_city());
            preparedStatement.setString(2, location.getLocation_street());
            preparedStatement.setString(3, location.getLocation_postalcode());

            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            AlertBox.display("Taki adres zamieszkania juz istnieje","Ulica wraz z numerem domu/mieszkania musi byc unikalna");
            DUPLICATE = true;
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ResultSet getLocation(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.LOCATION_TABLE+"."+Const.LOCATION_CITY+","+Const.LOCATION_TABLE+"."+Const.LOCATION_STREET+","
                +Const.LOCATION_TABLE+"."+Const.LOCATION_POSTALCODE+
                " FROM "+ Const.LOCATION_TABLE+
                " INNER JOIN "+ Const.ACCOUNT_HAS_LOCATION_TABLE+
                " ON "+ Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID +
                " WHERE "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet =  preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getContractorHasLocation(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.LOCATION_TABLE+"."+Const.LOCATION_CITY+","+Const.LOCATION_TABLE+"."+Const.LOCATION_STREET
                +","+Const.LOCATION_TABLE+"."+Const.LOCATION_POSTALCODE+","+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+
                " FROM "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.CONTRACTOR_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.CONTRACTOR_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " INNER JOIN "+Const.CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.CONTRACTOR_HAS_LOCATION_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " INNER JOIN "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " WHERE "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addAccountHasLocation(Location location){
        String insert = "INSERT INTO "+ Const.ACCOUNT_HAS_LOCATION_TABLE+"("+Const.FK_ACCOUNT_ID+","+Const.FK_LOCATION_ID+")"+
                " SELECT "+Const.ACCOUNT_ID+","+Const.LOCATION_ID+
                " FROM "+Const.ACCOUNT_TABLE+","+Const.LOCATION_TABLE+
                " WHERE "+Const.ACCOUNT_ID+"="+userColumn+" AND "+Const.LOCATION_STREET+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, location.getLocation_street());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void deleteAccountHasLocation(){
        String delete = "DELETE "+Const.LOCATION_TABLE+" FROM "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " WHERE "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void deleteContractorHasLocation(Integer locationID){
        String delete ="DELETE FROM "+Const.LOCATION_TABLE+" WHERE "+Const.LOCATION_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.setInt(1, locationID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateContractorHasLocation(Location location){
        String update ="UPDATE "+Const.LOCATION_TABLE+
                " SET "+Const.LOCATION_CITY+"=?"+","+Const.LOCATION_STREET+"=?"+","+Const.LOCATION_POSTALCODE+"=?"+
                " WHERE "+Const.LOCATION_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, location.getLocation_city());
            preparedStatement.setString(2, location.getLocation_street());
            preparedStatement.setString(3, location.getLocation_postalcode());
            preparedStatement.setInt(4, location.getLocation_ID());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            AlertBox.display("Taki adres zamieszkania juz istnieje","Ulica wraz z numerem domu/mieszkania musi byc unikalna");
            DUPLICATE = true;
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateAccountHasLocation(Location location){
        String update = "UPDATE "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " SET "+Const.LOCATION_CITY+"=?"+","+Const.LOCATION_STREET+"=?"+","+Const.LOCATION_POSTALCODE+"=?"+
                " WHERE "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, location.getLocation_city());
            preparedStatement.setString(2, location.getLocation_street());
            preparedStatement.setString(3, location.getLocation_postalcode());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void addContractorHasLocation(Location location, Contractor contractor){
        String insert = "INSERT INTO "+Const.CONTRACTOR_HAS_LOCATION_TABLE+"("+Const.FK_CONTRACTOR_ID+","+Const.FK_LOCATION_ID+")"+
                " SELECT "+Const.CONTRACTOR_ID+","+Const.LOCATION_ID+
                " FROM "+Const.CONTRACTOR_TABLE+","+Const.LOCATION_TABLE+
                " WHERE "+Const.CONTRACTOR_FIRSTNAME+"=?"+" AND "+Const.CONTRACTOR_LASTNAME+"=?"+" AND "+Const.LOCATION_STREET+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, contractor.getContractor_firstname());
            preparedStatement.setString(2, contractor.getContractor_lastname());
            preparedStatement.setString(3, location.getLocation_street());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    //CONTRACTOR METHODS /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addContractor(Contractor contractor){
        String insert = "INSERT INTO "+ Const.CONTRACTOR_TABLE+"("+Const.CONTRACTOR_FIRSTNAME+","+Const.CONTRACTOR_LASTNAME+")"+
                "VALUES(?,?)";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, contractor.getContractor_firstname());
            preparedStatement.setString(2, contractor.getContractor_lastname());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateContractor(Contractor contractor){
        String update ="UPDATE "+Const.CONTRACTOR_TABLE+
                " SET "+Const.CONTRACTOR_FIRSTNAME+"=?"+","+Const.CONTRACTOR_LASTNAME+"=?"+
                " WHERE "+Const.CONTRACTOR_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, contractor.getContractor_firstname());
            preparedStatement.setString(2, contractor.getContractor_lastname());
            preparedStatement.setInt(3,contractor.getContractor_ID());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
        } catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ResultSet getContractor(){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_FIRSTNAME+","+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_LASTNAME+","+
                Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+
                " FROM "+Const.CONTRACTOR_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " WHERE "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addAccountHasContractor(Contractor contractor){
        String insert = "INSERT INTO "+Const.ACCOUNT_HAS_CONTRACTOR_TABLE+"("+Const.FK_CONTRACTOR_ID+","+Const.FK_ACCOUNT_ID+")"+
                " SELECT "+Const.CONTRACTOR_ID+","+Const.ACCOUNT_ID+
                " FROM "+Const.CONTRACTOR_TABLE+","+Const.ACCOUNT_TABLE+
                " WHERE "+Const.CONTRACTOR_FIRSTNAME+"=?"+" AND "+Const.CONTRACTOR_LASTNAME+"=?"+" AND "+Const.ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, contractor.getContractor_firstname());
            preparedStatement.setString(2, contractor.getContractor_lastname());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void deleteContractor(Integer contractorID){
        String delete = "DELETE FROM "+Const.CONTRACTOR_TABLE+" WHERE "+Const.CONTRACTOR_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.setInt(1, contractorID);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // INVOICE CREATION METHODES///////////////////////////////////////////////////////////////////////////////////////////////

    public ResultSet addTransaction(Transaction transaction){
        ResultSet resultSet = null;
        String insert = "INSERT INTO "+ Const.TRANSACTION_TABLE+"("+Const.TRANSACTION_QUANTITY+","+Const.TRANSACTION_UNIT+")"+
                "VALUES(?,?)";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, transaction.getTransaction_quantity());
            preparedStatement.setString(2, transaction.getTransaction_unit());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addTransactionHasProduct(Integer transactionID, Integer productID){
        String insert = "INSERT INTO "+Const.TRANSACTION_HAS_PRODUCT_TABLE+"("+Const.FK_TRANSACTION_ID+","+Const.FK_PRODUCT_ID+")"+
                " SELECT "+Const.TRANSACTION_ID+","+Const.PRODUCT_ID+
                " FROM "+Const.TRANSACTION_TABLE+","+Const.PRODUCT_TABLE+
                " WHERE "+Const.TRANSACTION_ID+"=?"+" AND "+Const.PRODUCT_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setInt(1, transactionID);
            preparedStatement.setInt(2, productID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ResultSet addInvoice(Invoice invoice){
        ResultSet resultSet = null;
        String insert = "INSERT INTO "+Const.INVOICE_TABLE+"("+Const.INVOICE_SERIAL+","+Const.INVOICE_TYPE+","+Const.INVOICE_DATE+
                ","+Const.INVOICE_TRANSACTION_DATE+","+Const.INVOICE_PAYMENT_DATE+","+Const.INVOICE_PAYMENT_TYPE+")"+
                "VALUES(?,?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, invoice.getInvoiceSerial());
            preparedStatement.setString(2, invoice.getInvoiceType());
            preparedStatement.setString(3, invoice.getInvoiceDate());
            preparedStatement.setString(4, invoice.getInvoiceTransactionDate());
            preparedStatement.setString(5, invoice.getInvoicePaymentDate());
            preparedStatement.setString(6, invoice.getInvoicePaymentType());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addAccountHasInvoice(Integer invoiceID){
        String insert = "INSERT INTO "+Const.ACCOUNT_HAS_INVOICE_TABLE+"("+Const.FK_ACCOUNT_ID+","+Const.FK_INVOICE_ID+")"+
                " SELECT "+Const.ACCOUNT_ID+","+Const.INVOICE_ID+
                " FROM "+Const.ACCOUNT_TABLE+","+Const.INVOICE_TABLE+
                " WHERE "+Const.INVOICE_ID+"=?"+" AND "+Const.ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setInt(1, invoiceID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void addInvoiceHasLocation(Integer invoiceID,Integer locationID){
        String insert = "INSERT INTO "+Const.INVOICE_HAS_LOCATION_TABLE+"("+Const.FK_INVOICE_ID+","+Const.FK_LOCATION_ID+")"+
                " SELECT "+Const.INVOICE_ID+","+Const.LOCATION_ID+
                " FROM "+Const.INVOICE_TABLE+","+Const.LOCATION_TABLE+
                " WHERE "+Const.INVOICE_ID+"=?"+" AND "+Const.LOCATION_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setInt(1, invoiceID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    public void addInvoiceHasTransaction(Invoice invoice, Integer transactionID){
        String insert = "INSERT INTO "+Const.INVOICE_HAS_TRANSACTION_TABLE+"("+Const.FK_TRANSACTION_ID+","+Const.FK_INVOICE_ID+")"+
                " SELECT "+Const.TRANSACTION_ID+","+Const.INVOICE_ID+
                " FROM "+Const.TRANSACTION_TABLE+","+Const.INVOICE_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_INVOICE_TABLE+
                " ON "+Const.INVOICE_TABLE+"."+Const.INVOICE_ID+" = "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_INVOICE_ID+
                " WHERE "+Const.INVOICE_SERIAL+"=?"+" AND "+Const.TRANSACTION_ID+"=?"+" AND "+
                Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, invoice.getInvoiceSerial());
            preparedStatement.setInt(2, transactionID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void addContractorHasTransaction(Integer contractorID, Integer transactionID){
        String insert = "INSERT INTO "+Const.CONTRACTOR_HAS_TRANSACTION_TABLE+"("+Const.FK_CONTRACTOR_ID+","+Const.FK_TRANSACTION_ID+")"+
                " SELECT "+Const.CONTRACTOR_ID+","+Const.TRANSACTION_ID+
                " FROM "+Const.CONTRACTOR_TABLE+","+Const.TRANSACTION_TABLE+
                " WHERE "+Const.CONTRACTOR_ID+"=?"+" AND "+Const.TRANSACTION_ID+"=?";
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setInt(1, contractorID);
            preparedStatement.setInt(2,transactionID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ResultSet getContractorID(String frimdataNip){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.CONTRACTOR_ID+
                " FROM "+Const.CONTRACTOR_TABLE+
                " INNER JOIN "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " INNER JOIN "+Const.FIRMDATA_TABLE+
                " ON "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID+
                " WHERE "+Const.FIRMDATA_NIP+"=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, frimdataNip);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getAccountLocationID() {
        ResultSet resultSet = null;

        String search = "SELECT "+Const.LOCATION_ID+
                " FROM "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " WHERE "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getInvoiceID(Invoice invoice){
        ResultSet resultSet = null;

        String search = "SELECT "+Const.INVOICE_ID+
                " FROM "+Const.INVOICE_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_INVOICE_TABLE+
                " ON "+Const.INVOICE_TABLE+"."+Const.INVOICE_ID+" = "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_INVOICE_ID+
                " WHERE "+Const.INVOICE_SERIAL+"=?"+" AND "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, invoice.getInvoiceSerial());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getInvoiceDatesForSerial(){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.INVOICE_DATE+
                " FROM "+Const.INVOICE_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_INVOICE_TABLE+
                " ON "+Const.INVOICE_TABLE+"."+Const.INVOICE_ID+" = "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_INVOICE_ID+
                " WHERE "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //// METHODS TO POPULATE THE INVOICE //////////////////////////////////////////////////////////////////////////////////////

    public ResultSet getAccountBankNumber(){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.ACCOUNT_BANKNUMBER+
                " FROM "+Const.ACCOUNT_TABLE+
                " WHERE "+Const.ACCOUNT_ID+"="+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    ////getFirmdata() - zwraca zestaw danych fimry uzytkownika do faktury - NIP, Nazwa firmy, numer tel, email
    ////getLocation() = zwraca zestaw danych lokacji dla sprzedawcy w fakturze

    public ResultSet getAccountCity(){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.LOCATION_CITY+
                " FROM "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " WHERE "+Const.ACCOUNT_HAS_LOCATION_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getInvoiceData(String invoiceSerial){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.INVOICE_TYPE+","+Const.INVOICE_SERIAL+","+Const.INVOICE_DATE+","+Const.INVOICE_TRANSACTION_DATE+","+
                Const.INVOICE_PAYMENT_DATE+","+Const.INVOICE_PAYMENT_TYPE+
                " FROM "+Const.INVOICE_TABLE+
                " INNER JOIN "+Const.ACCOUNT_HAS_INVOICE_TABLE+
                " ON "+Const.INVOICE_TABLE+"."+Const.INVOICE_ID+" = "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_INVOICE_ID+
                " WHERE "+Const.INVOICE_SERIAL+"=?"+" AND "+Const.ACCOUNT_HAS_INVOICE_TABLE+"."+Const.FK_ACCOUNT_ID+" = "+userColumn;
        System.out.println(search);
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, invoiceSerial);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getContractorFirmData(String contractorNip){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.FIRMDATA_NIP+","+Const.FIRMDATA_NAME+","+Const.FIRMDATA_PHONE_NUMBER+","+Const.FIRMDATA_EMAIL+
                " FROM "+Const.FIRMDATA_TABLE+
                " WHERE "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_NIP+"=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, contractorNip);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getContractorLocation(String contractorNip){
        ResultSet resultSet = null;
        String search = "SELECT "+Const.LOCATION_CITY+","+Const.LOCATION_STREET+","+Const.LOCATION_POSTALCODE+
                " FROM "+Const.LOCATION_TABLE+
                " INNER JOIN "+Const.CONTRACTOR_HAS_LOCATION_TABLE+
                " ON "+Const.LOCATION_TABLE+"."+Const.LOCATION_ID+" = "+Const.CONTRACTOR_HAS_LOCATION_TABLE+"."+Const.FK_LOCATION_ID+
                " INNER JOIN "+Const.CONTRACTOR_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.CONTRACTOR_HAS_LOCATION_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " INNER JOIN "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+
                " ON "+Const.CONTRACTOR_TABLE+"."+Const.CONTRACTOR_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_CONTRACTOR_ID+
                " INNER JOIN "+Const.FIRMDATA_TABLE+
                " ON "+Const.FIRMDATA_TABLE+"."+Const.FIRMDATA_ID+" = "+Const.CONTRACTOR_HAS_FIRMDATA_TABLE+"."+Const.FK_FIRMDATA_ID+
                " WHERE "+Const.FIRMDATA_NIP+"=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(search);
            preparedStatement.setString(1, contractorNip);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }













}



