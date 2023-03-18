package org.example.springdatacassandraobservability;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

@Table
public class Employee {

	private @Id String id;
	private String name;
	private String role;

	public Employee(String id, String name, String role) {
		this.id = id;
		this.name = name;
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
		return "org.example.springdatacassandraobservability.Employee{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", role='" + role + '\'' + '}';
	}
}
