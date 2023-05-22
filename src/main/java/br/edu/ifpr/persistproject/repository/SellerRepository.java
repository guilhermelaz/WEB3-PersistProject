package br.edu.ifpr.persistproject.repository;

import br.edu.ifpr.persistproject.connection.ConnectionFactory;
import br.edu.ifpr.persistproject.model.Department;
import br.edu.ifpr.persistproject.model.Seller;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerRepository {

    private Connection conn;

    public SellerRepository(){
        conn = ConnectionFactory.getConnection();
    }

    public List<Seller> getSellers(){

        List<Seller> sellers = new ArrayList<Seller>();

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM seller");

            while (resultSet.next()){
                Seller seller = new Seller();

                seller.setId(resultSet.getInt("Id"));
                seller.setNome(resultSet.getString("Name"));
                seller.setEmail(resultSet.getString("Email"));
                seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
                seller.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());

                sellers.add(seller);

                }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {

            ConnectionFactory.resultSetClose(resultSet);
            ConnectionFactory.statementClose(statement);

        }

        return sellers;
    }


    public Seller insert(Seller seller){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement("INSERT INTO `seller` (`Name`, `Email`, `BirthDate`, `BaseSalary`, `DepartmentId`) " +
                    "VALUES (?, ?, ?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getNome());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, new Date(dateFormat.parse("14/07/2003").getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, 2);

            Integer rowsAffected = statement.executeUpdate();


            if (rowsAffected > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                Integer id = keys.getInt(1);

                seller.setId(id);

                System.out.println(rowsAffected + " linhas alteradas");
                System.out.println("Id: " + id);
            }



        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.statementClose(statement);
        }

        return seller;
}


    public void updateSalary(Double aumento, Integer departamentoId){

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            preparedStatement = conn.prepareStatement("UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?");
            preparedStatement.setDouble(1, aumento);
            preparedStatement.setInt(2, departamentoId);

            Integer rowsAffected = preparedStatement.executeUpdate();

            System.out.println("Linhas afetadas: " + rowsAffected);

        } catch (SQLException sqlException){
            System.out.println("Erro: " + sqlException.getMessage());
        } finally {
            ConnectionFactory.statementClose(preparedStatement);
        }

    }


    public void delete(Integer id) throws SQLException{

        PreparedStatement pst = null;

        try{
            pst = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
            pst.setInt(1, id);

            Integer linhasafetadas = pst.executeUpdate();
            System.out.println(linhasafetadas + " linhas modificadas");

        } catch (SQLException e){
            throw new SQLException();
        } finally {
            ConnectionFactory.statementClose(pst);
        }

    }


    public Seller findById(Integer id) {

        Seller seller = null;
        Department department = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        try{
            pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");

            pst.setInt(1, id);

            rs = pst.executeQuery();

            while (rs.next()){
                department = instantiateDepartment(rs);
                seller = instantiateSeller(rs, department);
            }

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
        } finally {
            ConnectionFactory.statementClose(pst);
            ConnectionFactory.resultSetClose(rs);
        }

        return seller;
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setNome(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());

        seller.setDepartment(department);

        return seller;
    }

    public Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department;
        department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

    public List<Seller> findByDepartment(Department department) {

        List<Seller> dpSellers = new ArrayList<Seller>();
        PreparedStatement pst = null;
        ResultSet rs = null;

        Seller seller;
        Department dp;

        try{
            pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");

            pst.setInt(1, department.getId());

            rs = pst.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department dep = map.get(rs.getInt(1));
                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(dep.getId(), dep);
                }

                seller = instantiateSeller(rs, dep);
                dpSellers.add(seller);
            }


        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            ConnectionFactory.statementClose(pst);
            ConnectionFactory.resultSetClose(rs);
        }

        return dpSellers;
    }


}
