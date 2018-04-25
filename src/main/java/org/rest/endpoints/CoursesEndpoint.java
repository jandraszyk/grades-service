package org.rest.endpoints;

import jersey.repackaged.com.google.common.collect.Lists;
import org.rest.model.Course;
import org.rest.service.CourseService;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/courses")
public class CoursesEndpoint {
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllCourses() {
        CourseService courseService = new CourseService();
        List<Course> courses = courseService.getAllCourses();

        // checking if courses list is empty
        if (courses == null || courses.size() == 0)
            return Response.status(Response.Status.NOT_FOUND).entity("No courses").build();

        GenericEntity<List<Course>> entity = new GenericEntity<List<Course>>(Lists.newArrayList(courses)) {
        };
        // creating xml response
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCourseById(@PathParam("id") int id) {
        // getting course by id
        CourseService courseService = new CourseService();
        Course course = courseService.getCourse(id);

        // checking if course exists
        if (course == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Not found").build();

        // creating xml response
        return Response.status(Response.Status.OK).entity(course).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addCourse(Course course) {
        if (course != null) {
            // TODO - czy potrzebne?
            Course newCourse = new Course(course);

            CourseService courseService = new CourseService();
            newCourse = courseService.addCourse(newCourse);

            String result = "Course " + newCourse + " added!\n";

            // creating response
            return Response.status(Response.Status.CREATED).header("Location", "/courses/" + newCourse.getId()).entity(result).build();
        } else
            return Response.status(Response.Status.NO_CONTENT).entity("Courses cannot be null!").build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCourse(Course course, @PathParam("id") int id) {
        // getting course by it's index
        CourseService courseService = new CourseService();
        Course searchedCourse = courseService.getCourse(id);

        // checking if course exists
        if (searchedCourse == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Not found").build();

        course.setId(id);
        // updating course
        boolean status = courseService.updateCourse(course);
        if (status == true) {
            String result = "Course " + course + " updated!";

            // creating response
            return Response.status(Response.Status.OK).entity(result).build();
        } else
            return Response.status(Response.Status.CONFLICT).entity("Error, not updated").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGrade(@PathParam("id") int id) {
        // getting course by it's index
        CourseService courseService = new CourseService();
        Course searchedCourse = courseService.getCourse(id);

        // checking if course exists
        if (searchedCourse == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found").build();
        }

        // updating course
        boolean status = courseService.deleteCourse(id);
        if (status == true) {
            String result = "Course " + searchedCourse + " deleted!";

            // creating response
            return Response.status(Response.Status.OK).entity(result).build();
        } else
            return Response.status(Response.Status.CONFLICT).entity("Error, not deleted").build();
    }
}
