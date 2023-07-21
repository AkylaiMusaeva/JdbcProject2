package org.example.dao.daoImpl;

import org.example.config.Config;
import org.example.dao.JobDao;
import org.example.model.Employee;
import org.example.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    @Override
    public void createJobTable() {
        String sql = "" +
                "create table jobs(" +
                "id serial primary key," +
                "position varchar(50)," +
                "profession varchar(50)," +
                "description varchar(50)," +
                "experience int not null)";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
            System.out.println("Table jobs is successfully created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void addJob(Job job) {
        String sql = "" +
                "insert into jobs(" +
                "position,profession,description,experience)" +
                "values(?,?,?,?)";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
            preparedStatement.executeUpdate();
            System.out.println(job.getPosition() + " is successfully saved!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Job getJobById(Long jobId) {
        Job job = new Job();
        String sql = "" +
                "select * from jobs where id=?";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                job = (new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return job;
    }
    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select * from jobs order by experience ");
        if (ascOrDesc.equalsIgnoreCase("asc")) sql.append(ascOrDesc);
        else if (ascOrDesc.equalsIgnoreCase("desc")) sql.append(ascOrDesc);
        else throw new RuntimeException("Error");
        try (
                Connection connection = Config.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql.toString());
            while (resultSet.next()) {
                jobs.add(new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        Employee employee=new Employee();
        String sql = "" +
                "select jobs.*,e.* from jobs join employees as e on jobs.id=e.job_id where e.id=?";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                job = new Job(resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience"));
                employee=new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id"));
                System.out.println(employee);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        String sql = "" +
                "alter table jobs drop column description";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate(sql);
            System.out.println("Description column is successfully deleted!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
