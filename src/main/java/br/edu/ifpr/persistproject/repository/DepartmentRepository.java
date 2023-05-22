package br.edu.ifpr.persistproject.repository;

import br.edu.ifpr.persistproject.connection.ConnectionFactory;
import br.edu.ifpr.persistproject.model.Department;
import br.edu.ifpr.persistproject.model.Seller;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentRepository {
    private Connection conn;

    public DepartmentRepository(){
        conn = ConnectionFactory.getConnection();
    }

    public List<Department> getDepartments(){

        List<Department> departments = new ArrayList<Department >();

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM department");

            while (resultSet.next()){
                Department dp = new Department();

                dp.setId(resultSet.getInt("Id"));
                dp.setName(resultSet.getString("Name"));

                departments.add(dp);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.resultSetClose(resultSet);
            ConnectionFactory.statementClose(statement);
        }

        return departments;
    }


    public Department insert(Department dp){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement("INSERT INTO `department` (`Name`) " +
                    "VALUES (?);", PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, dp.getName());

            Integer rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                Integer id = keys.getInt(1);

                dp.setId(id);

                System.out.println(rowsAffected + " linhas alteradas");
                System.out.println("Id: " + id);
            }



        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            ConnectionFactory.statementClose(statement);
        }

        return dp;
    }


    public void delete(Integer id) throws SQLException{

        PreparedStatement pst = null;

        try{
            pst = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            pst.setInt(1, id);

            Integer linhasafetadas = pst.executeUpdate();
            System.out.println(linhasafetadas + " linhas modificadas");

        } catch (SQLException e){
            throw new SQLException();
        } finally {
            ConnectionFactory.statementClose(pst);
        }

    }


    public Department findById(Integer id) {

        Department dp = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        try{
            pst = conn.prepareStatement("SELECT * from department WHERE Id = ?;");

            pst.setInt(1, id);

            rs = pst.executeQuery();

            while (rs.next()){
                dp = instantiateDepartment(rs);
            }

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
        } finally {
            ConnectionFactory.statementClose(pst);
            ConnectionFactory.resultSetClose(rs);
        }

        return dp;
    }

    public Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department;
        department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

}
