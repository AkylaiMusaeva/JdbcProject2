package org.example.dao.daoImpl;

import org.example.config.Config;
import org.example.dao.EmployeeDao;
import org.example.model.Employee;
import org.example.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public void createEmployee() {
        String sql = "" +
                "create table if not exists employees(" +
                "id serial primary key," +
                "first_name varchar(50)," +
                "last_name varchar(50)," +
                "age int not null," +
                "email varchar unique," +
                "job_id int references jobs(id)) ";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
            System.out.println("Successfully created table employees!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = "" +
                "insert into employees(" +
                "first_name,last_name,age,email,job_id)" +
                "values(?,   ?,    ?,  ?,     ?)";
        try (
                Connection connection = Config.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.executeUpdate();
            System.out.printf("Employee with name=%s is successfully saved!", employee.getFirstName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = "" +
                "drop table if exists employees";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
            System.out.println("Table employees is successfully deleted!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cleanTable() {
        String sql = "" +
                "truncate table if exists employees";
        try (
                Connection connection = Config.getConnection();
                Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String sql = "" +
                "update employees set first_name=?,last_name=?,age=?,email=?,job_id=? where id=?";
        try (
                Connection connection = Config.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.setLong(6, id);
            preparedStatement.executeUpdate();
            System.out.printf("Employee with id=%d is successfully updated!", id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "" +
                "select * from employees";
        try (
                Connection connection = Config.getConnection();
                Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        Employee employee = new Employee();
        String sql = "" +
                "select * from employees where email=?";
        try (
                Connection connection = Config.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employee = (new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employee;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee,Job>map=new HashMap<>();
        Employee employee=new Employee();
        Job job=new Job();

        String sql="" +
                "select e.*,j.* from employees e join jobs j on e.job_id=j.id where e.id=?";
        try(Connection connection = Config.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1,employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                employee = new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id"));
                job = new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")
                );
                map.put(employee, job);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return map;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        String job_position = null;
        String sql = "" +
                "select j.position ,employees.* from employees join jobs as j on employees.job_id=j.id where j.position=?";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }
}



