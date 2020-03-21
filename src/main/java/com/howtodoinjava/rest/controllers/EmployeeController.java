package com.howtodoinjava.rest.controllers;

import com.howtodoinjava.auth.User;
import com.howtodoinjava.dao.EmployeeDb;
import com.howtodoinjava.model.Employee;
import io.dropwizard.auth.Auth;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {
    private final EmployeeDb db;
    public EmployeeController(EmployeeDb empDb) {
        db = empDb;
    }
    @GET
    public List<Employee> getEmployees(@Auth User user) {
        return db.getAll();
    }

    @GET
    @Path("/test")
    public Response getEmployeess(@Auth User user) {
        return Response.ok(db.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Employee getEmployee(@PathParam("id") int id) {
        return db.getById(id).orElseThrow(() -> new NotFoundException("Employee with id = " + id + " was not found!"));
    }
}