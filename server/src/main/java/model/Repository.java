package model;

import controller.commands.Add;
import entity.LabWorkDTO;
import controller.utilities.GetLoginId;
import controller.utilities.TableCreator;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Repository {
    public static String add(LabWorkDTO a, String login) {
        try {
            LabWorkDTO lab = a;
//            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            Connection c = TableCreator.getConnection();
            String CreateInSql = "INSERT INTO LabWorks(name, x, y, creation_date, minimalPoint, personalQualitiesMinimum, difficulty, author_name,\n" +
                    "                     birthday, weight, eye_color, login_id)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                    "        ?, ?);";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, lab.getName());
            stmt.setDouble(2, Double.parseDouble(String.valueOf(lab.getCoordinates().getX())));
            stmt.setLong(3, lab.getCoordinates().getY());
            stmt.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            stmt.setDouble(5, lab.getMinimalPoint());
            stmt.setInt(6, lab.getPersonalQualitiesMinimum());
            stmt.setString(7, lab.getDifficulty().toString());
            stmt.setString(8, lab.getAuthor().getName());
            try {
                stmt.setDate(9, Date.valueOf(lab.getAuthor().getBirthday()));
            } catch (NullPointerException e) {
                stmt.setDate(9, null);
            }
            stmt.setFloat(10, lab.getAuthor().getWeight());
            try {
                stmt.setString(11, lab.getAuthor().getEyeColor().toString());
            } catch (NullPointerException e) {
                stmt.setDate(11, null);
            }
            stmt.setInt(12, GetLoginId.getIdOfLogin(login));
            stmt.executeUpdate();
            return "??????????????????";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "??????-???? ?????????? ???? ??????";
    }

    public static String addIfMin(LabWorkDTO a, String login) {

        try {
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select name from labworks order by name limit 1";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                System.out.println(res.getString("name"));
                System.out.println(res.getString("name").compareTo(a.getName()));
                if (res.getString("name").compareTo(a.getName()) < 0)
                    return "?????? ?????????????? ???? ??????????????????????";
            }
            return new Add().execute(a, login);

        } catch (SQLException e) {
            e.printStackTrace();
            return "??????-???? ?????????? ???? ??????";
        }

    }

    public static String clearUsersLabworks(String login) {
        try {
            Connection c = TableCreator.getConnection();
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "delete from labworks\n" +
                    "where login_id = ?;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, GetLoginId.getIdOfLogin(login));
            stmt.executeUpdate();
            return "???? ???????? ?????????????? ?????? ???????? ????????????";
        } catch (SQLException e) {
            e.printStackTrace();
            return "??????-???? ?????????? ???? ??????";
        }
    }

    public static String CountLessThanMinimalPoint(int min) {
        try {
            Connection c = TableCreator.getConnection();
//            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "select minimalpoint from labworks " +
                    "where minimalpoint<?;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, min);
            ResultSet res = stmt.executeQuery();
            int counter = 0;
            while (res.next()) {
                counter += 1;
            }
            return ("?????????? " + counter);
        } catch (SQLException e) {
            return "??????-???? ?????????? ???? ??????";
        }
    }

    public static String getCollectionFromSql() throws SQLException {
        Connection ?? = TableCreator.getConnection();
        String CreateInSql = "select * from labworks order by name";
        PreparedStatement stmt = null;
        try {
            stmt = TableCreator.getConnection().prepareStatement(CreateInSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String anwser = "";
        boolean isEmpty = true;
        while (res.next()) {
            isEmpty = false;
            anwser += "\n========================" +
                    "\n???????????????????????? ????????????:" +
                    "\n   ????????????????: " + res.getString("name") +
                    "\n   id: " + res.getInt("id") +
                    "\n   ????????????????????: x = " + res.getFloat("x") + " y = " + res.getInt("y") +
                    "\n   ???????? ????????????????: " + res.getTimestamp("creation_date") +
                    "\n   minimalPoint: " + res.getFloat("minimalpoint") +
                    "\n   personalQualitiesMinimum: " + res.getInt("personalqualitiesminimum") +
                    "\n   ??????????????????: " + res.getString("difficulty") +
                    "\n??????????: " +
                    "\n   ??????: " + res.getString("author_name") +
                    "\n   ??????: " + res.getFloat("weight") +
                    "\n   ???????? ????????????????: ";
            try {
                anwser += res.getDate("author_birthday");
            } catch (SQLException e) {
                anwser += "???? ??????????????";
            } finally {
                anwser += "\n   ???????? ????????: ";
            }
            try {
                String eye_color = res.getString("eye_color");
                if (eye_color != null)
                    anwser += res.getString("eye_color");
                else
                    anwser += "???? ????????????";
            } catch (SQLException e) {
                anwser += "???? ????????????";
            } finally {
                anwser += "\n========================";
            }
        }
        if (!isEmpty)
            return anwser;
        else
            return "========================\n\n" + "?????????????????? ??????????\n\n" + "========================\n";
    }

    public static String getCollectionFromSqlDescending() throws SQLException {
        Connection ?? = TableCreator.getConnection();
        String CreateInSql = "select * from labworks order by name desc";
        PreparedStatement stmt = null;
        try {
            stmt = TableCreator.getConnection().prepareStatement(CreateInSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String anwser = "";
        boolean isEmpty = true;
        while (res.next()) {
            isEmpty = false;
            anwser += "\n========================" +
                    "\n???????????????????????? ????????????:" +
                    "\n   ????????????????: " + res.getString("name") +
                    "\n   id: " + res.getInt("id") +
                    "\n   ????????????????????: x = " + res.getFloat("x") + " y = " + res.getInt("y") +
                    "\n   ???????? ????????????????: " + res.getTimestamp("creation_date") +
                    "\n   minimalPoint: " + res.getFloat("minimalpoint") +
                    "\n   personalQualitiesMinimum: " + res.getInt("personalqualitiesminimum") +
                    "\n   ??????????????????: " + res.getString("difficulty") +
                    "\n??????????: " +
                    "\n   ??????: " + res.getString("author_name") +
                    "\n   ??????: " + res.getFloat("weight") +
                    "\n   ???????? ????????????????: ";
            try {
                anwser += res.getDate("author_birthday");
            } catch (SQLException e) {
                anwser += "???? ??????????????";
            } finally {
                anwser += "\n   ???????? ????????: ";
            }
            try {
                String eye_color = res.getString("eye_color");
                if (eye_color != null)
                    anwser += res.getString("eye_color");
                else
                    anwser += "???? ????????????";
            } catch (SQLException e) {
                anwser += "???? ????????????";
            } finally {
                anwser += "\n========================";
            }
        }
        if (!isEmpty)
            return anwser;
        else
            return "========================\n\n" + "?????????????????? ??????????\n\n" + "========================\n";
    }

    public static String getUniqueMinimalPoint(){
        try {
            Connection c = TableCreator.getConnection();
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "select minimalpoint from labworks"; //distinct
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            ResultSet res = stmt.executeQuery();
            ArrayList<Integer> a = new ArrayList();
            while(res.next()){
                int k = res.getInt(1);
                if (!a.contains(k))
                    a.add(k);
            }
            if (a.size() != 0)
                return a.toString().replace("[", "").replace("]", "");
            else
                return "?????????????????? ??????????";
        } catch (SQLException e) {
            return "?????????????????? ??????????";
        }
    }

    public static String removeById(int id, String login){
        try {
            Connection c = TableCreator.getConnection();
//            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "select id from labworks " +
                    "where id = ? and login_id = ?";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, id);
            stmt.setInt(2, GetLoginId.getIdOfLogin(login));
            ResultSet res = stmt.executeQuery();
            res.next();
            if (res.getInt("id") > 0) {
                CreateInSql = "delete from labworks\n" +
                        "where id = ?;";
                stmt = c.prepareStatement(CreateInSql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                return "?????????????? ????????????";

            } else
                return "?????? ???????????????? ?? = " + id;
        }catch (SQLException a){
            return"???????????????? ?? ?????????? id ?????? ?????? ???? ???????????? ???? ????????";
        }
    }

    public static String removeFirst(String login){
        try {
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select id from labworks  \n" +
                    "where login_id = ?";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, GetLoginId.getIdOfLogin(login));
            ResultSet res = stmt.executeQuery();
            ArrayList<Integer> a = new ArrayList();
            while (res.next()) {
                a.add(res.getInt(1));
            }
            a.sort(null);
            if (a.isEmpty())
                return "?????????????????? ?????????? ?????? ?? ?????? ?????? ?????????????????? ?????????????????? ?????? ????????????????";
            CreateInSql = "delete from labworks" +
                    " where id = " + a.get(0) + " ;";
            stmt = c.prepareStatement(CreateInSql);
            stmt.executeUpdate();
            return "?????????????? ????????????";
        } catch (SQLException e) {
            return "?? ?????????????????? ?????? ???? ???????????????? ??????????????????, ?????????????????? ????????";
        }
    }

    public static String removeLower(LabWorkDTO labWork, String login){
        try {
            Connection c = TableCreator.getConnection();
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "delete from labworks" +
                    " where login_id = ? and name < ?;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, GetLoginId.getIdOfLogin(login));
            stmt.setString(2, labWork.getName());
            stmt.executeUpdate();
            return "???????????????????? ???????????????? ??????????????";
        } catch (SQLException e) {
            return "?? ?????????????????? ?????? ???? ???????????????? ??????????????????, ?????????????????? ????????";
        }
    }

    public static String updateID(LabWorkDTO lab, String login){
        try {
            Connection c = TableCreator.getConnection();
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "UPDATE labworks SET name = ?, x= ?, y= ?, creation_date= ?, minimalPoint= ?, personalQualitiesMinimum= ?, difficulty= ?, author_name= ?,birthday= ?, weight= ?, eye_color= ? WHERE id = ? and login_id = ?;;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, lab.getName());
            stmt.setDouble(2, Double.parseDouble(String.valueOf(lab.getCoordinates().getX())));
            stmt.setLong(3, lab.getCoordinates().getY());
            stmt.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            stmt.setDouble(5, lab.getMinimalPoint());
            stmt.setInt(6, lab.getPersonalQualitiesMinimum());
            stmt.setString(7, lab.getDifficulty().toString());
            stmt.setString(8, lab.getAuthor().getName());
            try {
                stmt.setDate(9, Date.valueOf(lab.getAuthor().getBirthday()));
            } catch (NullPointerException e) {
                stmt.setDate(9, null);
            }
            stmt.setFloat(10, lab.getAuthor().getWeight());
            try {
                stmt.setString(11, lab.getAuthor().getEyeColor().toString());
            } catch (NullPointerException e) {
                stmt.setDate(11, null);
            }
            stmt.setInt(12, lab.getId());
            stmt.setInt(13, GetLoginId.getIdOfLogin(login));
            stmt.executeUpdate();
            return "???????????????????? ?????????????? ??????????????(???????? ?????????? ??????)";
        } catch (SQLException e) {
            return "?? ?????????????????? ?????? ???? ???????????????? ??????????????????, ?????????????????? ????????";
        }
    }
}


