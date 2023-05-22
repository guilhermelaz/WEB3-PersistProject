package br.edu.ifpr.persistproject.connection;

import br.edu.ifpr.persistproject.model.Department;
import br.edu.ifpr.persistproject.model.Seller;
import br.edu.ifpr.persistproject.repository.SellerRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionTest {

    public static void main(String[] args) {

        SellerRepository repository = new SellerRepository();
//        List<Seller> sellers = repository.getSellers();
//
//        sellers.forEach( seller -> System.out.println(seller));
//
//        Seller seller = new Seller();
//        seller.setNome("Guilerme");
//        seller.setEmail("guilherme_laz@hotmail.com");
//        seller.setBaseSalary(3500.33);
//
//
//        repository.insert(seller);

//        repository.updateSalary(625.40, 2);

//        Seller bruh = repository.findById(1);
//        System.out.println("Seller: " + bruh.getNome());
//        System.out.println("Salary: " + bruh.getBaseSalary());
//        System.out.println("Bday: " + bruh.getBirthDate());
//        System.out.println("Department: " + bruh.getDepartment().getName());


        Department dp = new Department();
        dp.setId(1);
        List<Seller> sellers = repository.findByDepartment(dp);

        for (Seller s : sellers){
            System.out.println(s.getNome() + " - ");
            System.out.println(s.getDepartment().getName() + "\n");
        }

//        for (Seller seller : sellers){
//            System.out.println(seller);
//        }
    }


}
