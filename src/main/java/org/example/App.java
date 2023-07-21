package org.example;

import org.example.config.Config;
import org.example.model.Employee;
import org.example.model.Job;
import org.example.service.EmployeeService;
import org.example.service.JobService;
import org.example.service.serviceImpl.EmployeeServiceImpl;
import org.example.service.serviceImpl.JobServiceImpl;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        Config.getConnection();
        Scanner scannerWord=new Scanner(System.in);
        Scanner scannerNum=new Scanner(System.in);
        EmployeeService employeeService=new EmployeeServiceImpl();
        JobService jobService=new JobServiceImpl();

        int num;
        while(true){
            System.out.println(""" 
                    \n 
                    Employee                                Job
                    1-create table employees                10-create table job
                    2-save employee                         11-save job
                    3-drop table employees                  12-get job by id
                    4-clean table employees                 13-sort by experience
                    5-update employee                       14-get job by employee id
                    6-get all employees                     15-delete description column;
                    7-find employee by email                
                    8-get employee by id                    
                    9-get employee by position              
                    """);
            switch (num=scannerNum.nextInt()){
                case 1->employeeService.createEmployee();           //done
                case 2->{                                               //done
                    System.out.println("Input employee's first name: ");
                    String firstName=scannerWord.nextLine();
                    System.out.println("Input employee's last name: ");
                    String lastName=scannerWord.nextLine();
                    System.out.println("Input employee's age: ");
                    int age= scannerNum.nextInt();
                    System.out.println("Input employee's email: ");
                    String email=scannerWord.nextLine();
                    System.out.println("Input job's id to reference employee to: ");
                    int job_id=scannerNum.nextInt();
                    employeeService.addEmployee(new Employee(firstName,lastName,age,email,job_id));
                }
                case 3->employeeService.dropTable(); //done
                case 4->employeeService.cleanTable(); //done
                case 5->{                                                   //done
                    System.out.println("Input old employee's id you want to update!");
                    int oldEmployeeId=scannerNum.nextInt();
                    System.out.println("Input new employee's first name: ");
                    String firstName=scannerWord.nextLine();
                    System.out.println("Input new employee's last name: ");
                    String lastName=scannerWord.nextLine();
                    System.out.println("Input new employee's age: ");
                    int age= scannerNum.nextInt();
                    System.out.println("Input new employee's email: ");
                    String email=scannerWord.nextLine();
                    System.out.println("Input job's id to reference new employee to: ");
                    int job_id=scannerNum.nextInt();
                    employeeService.updateEmployee((long) oldEmployeeId,new Employee(firstName,lastName,age,email,job_id));
                }
                case 6->employeeService.getAllEmployees().forEach(System.out::println);//done
                case 7->{                                                               //done
                    System.out.println("Input employee's email you want to find: ");
                    String email=scannerWord.nextLine();
                    System.out.println(employeeService.findByEmail(email));
                }
                case 8->{                                               //done
                    System.out.println("Input employee's id you want to find: ");
                    Long id=scannerNum.nextLong();
                    System.out.println(employeeService.getEmployeeById(id));
                }
                case 9->{      //done
                    System.out.println("Input employee's position you want to find: ");
                    String position=scannerWord.nextLine();
                    employeeService.getEmployeeByPosition(position).forEach(System.out::println);
                }

                case 10->jobService.createJobTable();//done
                case 11->{                                  //done
                    System.out.println("Input job's position: ");
                    String position=scannerWord.nextLine();
                    System.out.println("Input job's profession: ");
                    String profession=scannerWord.nextLine();
                    System.out.println("Input job's description: ");
                    String description=scannerWord.nextLine();
                    System.out.println("Input experience year: ");
                    int experience=scannerNum.nextInt();
                    jobService.addJob(new Job(position,profession,description,experience));
                }
                case 12->{                                              //done
                    System.out.println("Input job id you want to find: ");
                    Long id=scannerNum.nextLong();
                    System.out.println(jobService.getJobById(id));
                }
                case 13->{                                              //done
                    System.out.println("Write asc or desc depending on what way you want to sort:");
                    String ascOrDesc=scannerWord.nextLine();
                    jobService.sortByExperience(ascOrDesc).forEach(System.out::println);
                }
                case 14->{                                              //done
                    System.out.println("Input employee's id to find job: ");
                    Long id=scannerNum.nextLong();
                    System.out.println(jobService.getJobByEmployeeId(id));
                }
                case 15->jobService.deleteDescriptionColumn(); //done

            }
        }



    }
}
