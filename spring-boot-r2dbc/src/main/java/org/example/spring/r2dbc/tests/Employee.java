package org.example.spring.r2dbc.tests;

import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * This class defines our domain’s type, as required in the EmployeeRepository declaration, by 3 class fields:<br>
 * - The primary key of type Long is denoted by Spring Data Commons’ annotation, @Id.
 *  Note that this is NOT JPA’s jakarta.persistence.Idannotation but instead a Spring Data-specific annotation.<br>
 * - name and role
 */
public class Employee {

  private @Id Long id;
  private String name;
  private String role;

  public Employee(String name, String role) {
    this.name = name;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Employee employee = (Employee) o;
    return Objects.equals(id, employee.id) && Objects.equals(name, employee.name)
      && Objects.equals(role, employee.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, role);
  }

  @Override
  public String toString() {
    return "Employee{" + "id=" + id + ", name='" + name + '\'' + ", role='" + role + '\'' + '}';
  }
}
